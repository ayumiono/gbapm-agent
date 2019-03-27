package com.gb.apm.plugins.mysql;

import static com.gb.apm.common.utils.VarArgs.va;

import java.security.ProtectionDomain;
import java.util.List;

import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentException;
import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.instrument.Instrumentor;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformCallback;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformTemplate;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformTemplateAware;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.logging.PLogger;
import com.gb.apm.bootstrap.core.logging.PLoggerFactory;
import com.gb.apm.bootstrap.core.plugin.ProfilerPlugin;
import com.gb.apm.bootstrap.core.plugin.ProfilerPluginSetupContext;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;
import com.gb.apm.bootstrap.core.plugin.jdbc.PreparedStatementBindingMethodFilter;
import com.gb.apm.bootstrap.core.util.InstrumentUtils;

/**
 * @author Jongho Moon
 */
public class MySqlPlugin implements ProfilerPlugin, TransformTemplateAware {

    private static final String MYSQL_SCOPE = MySqlConstants.MYSQL_SCOPE;

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private TransformTemplate transformTemplate;
    private final JdbcUrlParserV2 jdbcUrlParser = new MySqlJdbcUrlParser();

    @Override
    public void setup(ProfilerPluginSetupContext context) {
    	
        MySqlConfig config = new MySqlConfig(context.getConfig());

        if (!config.isPluginEnable()) {
            logger.info("Mysql plugin is not executed because plugin enable value is false.");
            return;
        }

        context.addJdbcUrlParser(jdbcUrlParser);

        addConnectionTransformer(config);
        addDriverTransformer();
        addStatementTransformer();
        addPreparedStatementTransformer(config);
        addCallableStatementTransformer(config);

        // From MySQL driver 5.1.x, backward compatibility is broken.
        // Driver returns not com.mysql.jdbc.Connection but com.mysql.jdbc.JDBC4Connection which extends com.mysql.jdbc.ConnectionImpl from 5.1.x
        addJDBC4PreparedStatementTransformer(config);
        addJDBC4CallableStatementTransformer(config);
    }

    private void addConnectionTransformer(final MySqlConfig config) {
        TransformCallback transformer = new TransformCallback() {

            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                if (!target.isInterceptable()) {
                    return null;
                }

                target.addField("com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor");

                InstrumentUtils.findConstructor(target, "java.lang.String", "int", "java.util.Properties", "java.lang.String", "java.lang.String")
                        .addInterceptor("com.gb.apm.plugins.mysql.interceptor.MySQLConnectionCreateInterceptor");

                // close
                InstrumentUtils.findMethod(target, "close")
                        .addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.ConnectionCloseInterceptor", MYSQL_SCOPE);

                // createStatement
                final String statementCreate = "com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.StatementCreateInterceptor";
                InstrumentUtils.findMethod(target, "createStatement")
                        .addScopedInterceptor(statementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "createStatement", "int", "int")
                        .addScopedInterceptor(statementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "createStatement", "int", "int", "int")
                        .addScopedInterceptor(statementCreate, MYSQL_SCOPE);

                // preparedStatement
                final String preparedStatementCreate = "com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.PreparedStatementCreateInterceptor";
                InstrumentUtils.findMethod(target, "prepareStatement",  "java.lang.String")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "prepareStatement",  "java.lang.String", "int")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "prepareStatement",  "java.lang.String", "int[]")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "prepareStatement",  "java.lang.String", "java.lang.String[]")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "prepareStatement",  "java.lang.String", "int", "int")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "prepareStatement",  "java.lang.String", "int", "int", "int")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);

                // preparecall
                InstrumentUtils.findMethod(target, "prepareCall",  "java.lang.String")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "prepareCall",  "java.lang.String", "int", "int")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "prepareCall",  "java.lang.String", "int", "int", "int")
                        .addScopedInterceptor(preparedStatementCreate, MYSQL_SCOPE);

                if (config.isProfileSetAutoCommit()) {
                    InstrumentUtils.findMethod(target, "setAutoCommit",  "boolean")
                            .addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.TransactionSetAutoCommitInterceptor", MYSQL_SCOPE);
                }

                if (config.isProfileCommit()) {
                    InstrumentUtils.findMethod(target, "commit")
                            .addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.TransactionCommitInterceptor", MYSQL_SCOPE);
                }

                if (config.isProfileRollback()) {
                    InstrumentUtils.findMethod(target, "rollback")
                            .addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.TransactionRollbackInterceptor", MYSQL_SCOPE);
                }

                return target.toBytecode();
            }
        };

        transformTemplate.transform("com.mysql.jdbc.Connection", transformer);
        transformTemplate.transform("com.mysql.jdbc.ConnectionImpl", transformer);
    }

    private void addDriverTransformer() {
        transformTemplate.transform("com.mysql.jdbc.NonRegisteringDriver", new TransformCallback() {

            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                InstrumentUtils.findMethod(target, "connect",  "java.lang.String", "java.util.Properties")
                        .addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.DriverConnectInterceptorV2", va(MySqlConstants.MYSQL, false), MYSQL_SCOPE, ExecutionPolicy.ALWAYS);

                return target.toBytecode();
            }
        });
    }

    private void addPreparedStatementTransformer(final MySqlConfig config) {
        transformTemplate.transform("com.mysql.jdbc.PreparedStatement", new TransformCallback() {

            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                target.addField("com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor");
                target.addField("com.gb.apm.bootstrap.core.plugin.jdbc.ParsingResultAccessor");
                target.addField("com.gb.apm.bootstrap.core.plugin.jdbc.BindValueAccessor");

                int maxBindValueSize = config.getMaxSqlBindValueSize();

                final String preparedStatementInterceptor = "com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.PreparedStatementExecuteQueryInterceptor";
                InstrumentUtils.findMethod(target, "execute")
                        .addScopedInterceptor(preparedStatementInterceptor, va(maxBindValueSize), MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "executeQuery")
                        .addScopedInterceptor(preparedStatementInterceptor, va(maxBindValueSize), MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "executeUpdate")
                        .addScopedInterceptor(preparedStatementInterceptor, va(maxBindValueSize), MYSQL_SCOPE);

                if (config.isTraceSqlBindValue()) {
                    final PreparedStatementBindingMethodFilter excludes = PreparedStatementBindingMethodFilter.excludes("setRowId", "setNClob", "setSQLXML");
                    final List<InstrumentMethod> declaredMethods = target.getDeclaredMethods(excludes);
                    for (InstrumentMethod method : declaredMethods) {
                        method.addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.PreparedStatementBindVariableInterceptor", MYSQL_SCOPE, ExecutionPolicy.BOUNDARY);
                    }
                }

                return target.toBytecode();
            }
        });
    }

    private void addCallableStatementTransformer(final MySqlConfig config) {
        transformTemplate.transform("com.mysql.jdbc.CallableStatement", new TransformCallback() {

            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                target.addField("com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor");
                target.addField("com.gb.apm.bootstrap.core.plugin.jdbc.ParsingResultAccessor");
                target.addField("com.gb.apm.bootstrap.core.plugin.jdbc.BindValueAccessor");

                int maxBindValueSize = config.getMaxSqlBindValueSize();

                final String callableStatementExecuteQuery = "com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.CallableStatementExecuteQueryInterceptor";
                InstrumentUtils.findMethod(target,"execute")
                        .addScopedInterceptor(callableStatementExecuteQuery, va(maxBindValueSize), MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "executeQuery")
                        .addScopedInterceptor(callableStatementExecuteQuery, va(maxBindValueSize), MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "executeUpdate")
                        .addScopedInterceptor(callableStatementExecuteQuery, va(maxBindValueSize), MYSQL_SCOPE);

                final String registerOutParameterInterceptor = "com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.CallableStatementRegisterOutParameterInterceptor";
                InstrumentUtils.findMethod(target, "registerOutParameter", "int", "int")
                        .addScopedInterceptor(registerOutParameterInterceptor, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "registerOutParameter", "int", "int", "int")
                        .addScopedInterceptor(registerOutParameterInterceptor, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "registerOutParameter", "int", "int", "java.lang.String")
                        .addScopedInterceptor(registerOutParameterInterceptor, MYSQL_SCOPE);

                if (config.isTraceSqlBindValue()) {
                    final PreparedStatementBindingMethodFilter excludes = PreparedStatementBindingMethodFilter.excludes("setRowId", "setNClob", "setSQLXML");
                    final List<InstrumentMethod> declaredMethods = target.getDeclaredMethods(excludes);
                    for (InstrumentMethod method : declaredMethods) {
                        method.addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.CallableStatementBindVariableInterceptor", MYSQL_SCOPE, ExecutionPolicy.BOUNDARY);
                    }
                }

                return target.toBytecode();
            }
        });
    }

    private void addJDBC4PreparedStatementTransformer(final MySqlConfig config) {
        transformTemplate.transform("com.mysql.jdbc.JDBC4PreparedStatement", new TransformCallback() {

            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                if (config.isTraceSqlBindValue()) {
                    final PreparedStatementBindingMethodFilter includes = PreparedStatementBindingMethodFilter.includes("setRowId", "setNClob", "setSQLXML");
                    final List<InstrumentMethod> declaredMethods = target.getDeclaredMethods(includes);
                    for (InstrumentMethod method : declaredMethods) {
                        method.addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.PreparedStatementBindVariableInterceptor", MYSQL_SCOPE, ExecutionPolicy.BOUNDARY);
                    }
                }

                return target.toBytecode();
            }
        });
    }

    private void addJDBC4CallableStatementTransformer(final MySqlConfig config) {
        transformTemplate.transform("com.mysql.jdbc.JDBC4CallableStatement", new TransformCallback() {

            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                if (config.isTraceSqlBindValue()) {
                    final PreparedStatementBindingMethodFilter includes = PreparedStatementBindingMethodFilter.includes("setRowId", "setNClob", "setSQLXML");
                    final List<InstrumentMethod> declaredMethods = target.getDeclaredMethods(includes);
                    for (InstrumentMethod method : declaredMethods) {
                        method.addScopedInterceptor("com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.CallableStatementBindVariableInterceptor", MYSQL_SCOPE, ExecutionPolicy.BOUNDARY);
                    }
                }

                return target.toBytecode();
            }
        });
    }

    private void addStatementTransformer() {
        TransformCallback transformer = new TransformCallback() {

            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                if (!target.isInterceptable()) {
                    return null;
                }

                target.addField("com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor");

                final String executeQueryInterceptor = "com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.StatementExecuteQueryInterceptor";
                InstrumentUtils.findMethod(target, "executeQuery", "java.lang.String")
                        .addScopedInterceptor(executeQueryInterceptor, MYSQL_SCOPE);

                final String executeUpdateInterceptor = "com.gb.apm.bootstrap.core.plugin.jdbc.interceptor.StatementExecuteUpdateInterceptor";
                InstrumentUtils.findMethod(target, "executeUpdate", "java.lang.String")
                        .addScopedInterceptor(executeUpdateInterceptor, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "executeUpdate",  "java.lang.String", "int")
                        .addScopedInterceptor(executeUpdateInterceptor, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "execute",  "java.lang.String")
                        .addScopedInterceptor(executeUpdateInterceptor, MYSQL_SCOPE);
                InstrumentUtils.findMethod(target, "execute",  "java.lang.String", "int")
                        .addScopedInterceptor(executeUpdateInterceptor, MYSQL_SCOPE);

                return target.toBytecode();
            }
        };

        transformTemplate.transform("com.mysql.jdbc.Statement", transformer);
        transformTemplate.transform("com.mysql.jdbc.StatementImpl", transformer);

    }

    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        this.transformTemplate = transformTemplate;
    }
}
