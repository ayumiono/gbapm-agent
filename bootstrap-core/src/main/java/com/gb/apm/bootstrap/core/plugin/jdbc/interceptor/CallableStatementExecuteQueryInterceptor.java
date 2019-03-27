package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * @author HyunGil Jeong
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetMethods({
//        @TargetMethod(name = "execute"),
//        @TargetMethod(name = "executeQuery"),
//        @TargetMethod(name = "executeUpdate")
//})
public class CallableStatementExecuteQueryInterceptor extends PreparedStatementExecuteQueryInterceptor {

    public CallableStatementExecuteQueryInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        super(traceContext, descriptor);
    }

    public CallableStatementExecuteQueryInterceptor(TraceContext traceContext, MethodDescriptor descriptor, int maxSqlBindValue) {
        super(traceContext, descriptor, maxSqlBindValue);
    }

}
