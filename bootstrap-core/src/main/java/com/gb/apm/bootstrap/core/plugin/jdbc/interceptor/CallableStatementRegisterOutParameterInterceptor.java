package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.SpanEventSimpleAroundInterceptorForPlugin;
import com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.UnKnownDatabaseInfo;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * @author HyunGil Jeong
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetMethods({
//        @TargetMethod(name = "registerOutParameter", paramTypes = {"int", "int"}),
//        @TargetMethod(name = "registerOutParameter", paramTypes = {"int", "int", "int"}),
//        @TargetMethod(name = "registerOutParameter", paramTypes = {"int", "int", "java.lang.String"})
//})
public class CallableStatementRegisterOutParameterInterceptor extends SpanEventSimpleAroundInterceptorForPlugin {

    public CallableStatementRegisterOutParameterInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        super(traceContext, descriptor);
    }

    @Override
    protected void doInBeforeTrace(SpanEventRecorder recorder, Object target, Object[] args) {
    }

    @Override
    protected void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        DatabaseInfo databaseInfo = (target instanceof DatabaseInfoAccessor) ? ((DatabaseInfoAccessor)target)._$PINPOINT$_getDatabaseInfo() : null;

        if (databaseInfo == null) {
            databaseInfo = UnKnownDatabaseInfo.INSTANCE;
        }

        recorder.recordServiceType(databaseInfo.getType());
        recorder.recordEndPoint(databaseInfo.getMultipleHost());
        recorder.recordDestinationId(databaseInfo.getDatabaseId());

        recorder.recordApi(methodDescriptor, args);
        recorder.recordException(throwable);
    }
}
