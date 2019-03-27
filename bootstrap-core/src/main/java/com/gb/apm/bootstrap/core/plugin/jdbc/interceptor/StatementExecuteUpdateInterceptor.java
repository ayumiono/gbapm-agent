package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.SpanEventSimpleAroundInterceptorForPlugin;
import com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.UnKnownDatabaseInfo;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * protected int executeUpdate(String sql, boolean isBatch, boolean returnGeneratedKeys)
 *
 * @author netspider
 * @author emeroad
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetMethods({
//        @TargetMethod(name="executeUpdate", paramTypes={ "java.lang.String" }),
//        @TargetMethod(name="executeUpdate", paramTypes={ "java.lang.String", "int" }),
//        @TargetMethod(name="execute", paramTypes={ "java.lang.String" }),
//        @TargetMethod(name="execute", paramTypes={ "java.lang.String", "int" })
//})
public class StatementExecuteUpdateInterceptor extends SpanEventSimpleAroundInterceptorForPlugin {
    
    public StatementExecuteUpdateInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        super(traceContext, descriptor);
    }

    @Override
    public void doInBeforeTrace(SpanEventRecorder recorder, Object target, Object[] args) {
        DatabaseInfo databaseInfo = (target instanceof DatabaseInfoAccessor) ? ((DatabaseInfoAccessor)target)._$PINPOINT$_getDatabaseInfo() : null;
        
        if (databaseInfo == null) {
            databaseInfo = UnKnownDatabaseInfo.INSTANCE;
        }

        recorder.recordServiceType(databaseInfo.getExecuteQueryType());
        recorder.recordEndPoint(databaseInfo.getMultipleHost());
        recorder.recordDestinationId(databaseInfo.getDatabaseId());

        recorder.recordApi(methodDescriptor);
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof String) {
                recorder.recordSqlInfo((String) arg);
            }
        }
    }


    @Override
    public void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        recorder.recordException(throwable);
    }
}