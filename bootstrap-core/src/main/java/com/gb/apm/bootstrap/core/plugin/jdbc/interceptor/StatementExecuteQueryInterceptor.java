package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.SpanEventSimpleAroundInterceptorForPlugin;
import com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.UnKnownDatabaseInfo;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * @author netspider
 * @author emeroad
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetMethod(name="executeQuery", paramTypes={ "java.lang.String" })
public class StatementExecuteQueryInterceptor extends SpanEventSimpleAroundInterceptorForPlugin {
    public StatementExecuteQueryInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        super(traceContext, descriptor);
    }


    @Override
    public void doInBeforeTrace(SpanEventRecorder recorder, final Object target, Object[] args) {
        /*
         * If method was not called by request handler, we skip tagging.
         */
        DatabaseInfo databaseInfo = (target instanceof DatabaseInfoAccessor) ? ((DatabaseInfoAccessor)target)._$PINPOINT$_getDatabaseInfo() : null;
        
        if (databaseInfo == null) {
            databaseInfo = UnKnownDatabaseInfo.INSTANCE;
        }

        recorder.recordServiceType(databaseInfo.getExecuteQueryType());
        recorder.recordEndPoint(databaseInfo.getMultipleHost());
        recorder.recordDestinationId(databaseInfo.getDatabaseId());

    }


    @Override
    public void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        recorder.recordApi(methodDescriptor);
        if (args.length > 0) {
            Object arg = args[0];
            if (arg instanceof String) {
                recorder.recordSqlInfo((String) arg);
                // TODO more parsing result processing
            }
        }
        recorder.recordException(throwable);
    }
}
