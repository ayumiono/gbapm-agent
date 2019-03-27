package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.context.ParsingResult;
import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.SpanEventSimpleAroundInterceptorForPlugin;
import com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.ParsingResultAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.UnKnownDatabaseInfo;
import com.gb.apm.common.utils.InterceptorUtils;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * @author emeroad
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetMethods({
//        @TargetMethod(name="prepareStatement", paramTypes={ "java.lang.String" }),
//        @TargetMethod(name="prepareStatement", paramTypes={ "java.lang.String", "int" }),
//        @TargetMethod(name="prepareStatement", paramTypes={ "java.lang.String", "int[]" }),
//        @TargetMethod(name="prepareStatement", paramTypes={ "java.lang.String", "java.lang.String[]" }),
//        @TargetMethod(name="prepareStatement", paramTypes={ "java.lang.String", "int", "int" }),
//        @TargetMethod(name="prepareStatement", paramTypes={ "java.lang.String", "int", "int", "int" }),
//        @TargetMethod(name="prepareCall", paramTypes={ "java.lang.String" }),
//        @TargetMethod(name="prepareCall", paramTypes={ "java.lang.String", "int", "int" }),
//        @TargetMethod(name="prepareCall", paramTypes={ "java.lang.String", "int", "int", "int" })
//})
public class PreparedStatementCreateInterceptor extends SpanEventSimpleAroundInterceptorForPlugin {

    public PreparedStatementCreateInterceptor(TraceContext context, MethodDescriptor descriptor) {
        super(context, descriptor);
    }

    @Override
    public void doInBeforeTrace(SpanEventRecorder recorder, Object target, Object[] args)  {
        DatabaseInfo databaseInfo = (target instanceof DatabaseInfoAccessor) ? ((DatabaseInfoAccessor)target)._$PINPOINT$_getDatabaseInfo() : null;
        
        if (databaseInfo == null) {
            databaseInfo = UnKnownDatabaseInfo.INSTANCE;
        }
        
        recorder.recordServiceType(databaseInfo.getType());
        recorder.recordEndPoint(databaseInfo.getMultipleHost());
        recorder.recordDestinationId(databaseInfo.getDatabaseId());
    }

    @Override
    protected void prepareAfterTrace(Object target, Object[] args, Object result, Throwable throwable) {
        final boolean success = InterceptorUtils.isSuccess(throwable);
        if (success) {
            if (target instanceof DatabaseInfoAccessor) {
                // set databaseInfo to PreparedStatement only when preparedStatement is generated successfully.
                DatabaseInfo databaseInfo = ((DatabaseInfoAccessor)target)._$PINPOINT$_getDatabaseInfo();
                if (databaseInfo != null) {
                    if (result instanceof DatabaseInfoAccessor) {
                        ((DatabaseInfoAccessor)result)._$PINPOINT$_setDatabaseInfo(databaseInfo);
                    }
                }
            }
            if (result instanceof ParsingResultAccessor) {
                // 1. Don't check traceContext. preparedStatement can be created in other thread.
                // 2. While sampling is active, the thread which creates preparedStatement could not be a sampling target. So record sql anyway. 
                String sql = (String) args[0];
                ParsingResult parsingResult = traceContext.parseSql(sql);
                if (parsingResult != null) {
                    ((ParsingResultAccessor)result)._$PINPOINT$_setParsingResult(parsingResult);
                } else {
                    if (logger.isErrorEnabled()) {
                        logger.error("sqlParsing fail. parsingResult is null sql:{}", sql);
                    }
                }
            }
        }
    }

    @Override
    public void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        if (result instanceof ParsingResultAccessor) {
            ParsingResult parsingResult = ((ParsingResultAccessor)result)._$PINPOINT$_getParsingResult();
            recorder.recordSqlParsingResult(parsingResult);
        }
        recorder.recordException(throwable);
        recorder.recordApi(methodDescriptor);
    }
}
