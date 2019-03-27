package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.context.ParsingResult;
import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor;
import com.gb.apm.bootstrap.core.logging.PLogger;
import com.gb.apm.bootstrap.core.logging.PLoggerFactory;
import com.gb.apm.bootstrap.core.plugin.jdbc.BindValueAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.ParsingResultAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.UnKnownDatabaseInfo;
import com.gb.apm.bootstrap.core.plugin.jdbc.bindvalue.BindValueUtils;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * @author emeroad
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetMethods({
//        @TargetMethod(name="execute"),
//        @TargetMethod(name="executeQuery"),
//        @TargetMethod(name="executeUpdate")
//})
public class PreparedStatementExecuteQueryInterceptor implements AroundInterceptor {

    private static final int DEFAULT_BIND_VALUE_LENGTH = 1024;

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;
    private final int maxSqlBindValueLength;
    
    
    public PreparedStatementExecuteQueryInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        this(traceContext, descriptor, DEFAULT_BIND_VALUE_LENGTH);
    }
    
    public PreparedStatementExecuteQueryInterceptor(TraceContext traceContext, MethodDescriptor descriptor, int maxSqlBindValueLength) {
        this.traceContext = traceContext;
        this.descriptor = descriptor;
        this.maxSqlBindValueLength = maxSqlBindValueLength;
    }

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        SpanEventRecorder recorder = trace.traceBlockBegin();
        try {
            DatabaseInfo databaseInfo = (target instanceof DatabaseInfoAccessor) ? ((DatabaseInfoAccessor)target)._$PINPOINT$_getDatabaseInfo() : null;
            
            if (databaseInfo == null) {
                databaseInfo = UnKnownDatabaseInfo.INSTANCE;
            }
            
            recorder.recordServiceType(databaseInfo.getExecuteQueryType());
            recorder.recordEndPoint(databaseInfo.getMultipleHost());
            recorder.recordDestinationId(databaseInfo.getDatabaseId());

            ParsingResult parsingResult = null;
            if (target instanceof ParsingResultAccessor) {
                parsingResult = ((ParsingResultAccessor)target)._$PINPOINT$_getParsingResult();
            }
            Map<Integer, String> bindValue = null;
            if (target instanceof BindValueAccessor) {
                bindValue = ((BindValueAccessor)target)._$PINPOINT$_getBindValue();
            }
            if (bindValue != null) {
                String bindString = toBindVariable(bindValue);
                recorder.recordSqlParsingResult(parsingResult, bindString);
            } else {
                recorder.recordSqlParsingResult(parsingResult);
            }

            recorder.recordApi(descriptor);
//            trace.recordApi(apiId);
            
            // Need to change where to invoke clean().
            // There is cleanParameters method but it's not necessary to intercept that method.
            // iBatis intentionally does not invoke it in most cases. 
            clean(target);


        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage(), e);
            }
        }

    }

    private void clean(Object target) {
        if (target instanceof BindValueAccessor) {
            ((BindValueAccessor)target)._$PINPOINT$_setBindValue(new HashMap<Integer, String>());
        }
    }

    private String toBindVariable(Map<Integer, String> bindValue) {
        return BindValueUtils.bindValueToString(bindValue, maxSqlBindValueLength);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }

        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        
        try {
            SpanEventRecorder recorder = trace.currentSpanEventRecorder();
            // TODO Test if it's success. if failed terminate. else calculate resultset fetch too. we'd better make resultset fetch optional.
            recorder.recordException(throwable);
        } finally {
            trace.traceBlockEnd();
        }
    }
}
