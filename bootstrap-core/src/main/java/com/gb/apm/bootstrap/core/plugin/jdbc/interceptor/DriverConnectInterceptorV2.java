package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.SpanEventSimpleAroundInterceptorForPlugin;
import com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.UnKnownDatabaseInfo;
import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.common.utils.InterceptorUtils;
import com.gb.apm.dapper.context.MethodDescriptor;


/**
 * must be used with ExecutionPolicy.ALWAYS
 * 
 * @author emeroad
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetMethod(name="connect", paramTypes={ "java.lang.String", "java.util.Properties" })
public class DriverConnectInterceptorV2 extends SpanEventSimpleAroundInterceptorForPlugin {

    private final ServiceType serviceType;
    private final boolean recordConnection;

    public DriverConnectInterceptorV2(TraceContext context, MethodDescriptor descriptor, ServiceType serviceType) {
        this(context, descriptor, serviceType, true);
    }

    public DriverConnectInterceptorV2(TraceContext context, MethodDescriptor descriptor, ServiceType serviceType, boolean recordConnection) {
        super(context, descriptor);

        this.serviceType = serviceType;
        // option for mysql loadbalance only. Destination is recorded at lower implementations.
        this.recordConnection = recordConnection;
    }

    @Override
    protected void logBeforeInterceptor(Object target, Object[] args) {
        // Must not log args because it contains a password
        logger.beforeInterceptor(target, null);
    }

    @Override
    protected void doInBeforeTrace(SpanEventRecorder recorder, Object target, Object[] args) {
    }


    @Override
    protected void logAfterInterceptor(Object target, Object[] args, Object result, Throwable throwable) {
        logger.afterInterceptor(target, null, result, throwable);
    }

    @Override
    protected void prepareAfterTrace(Object target, Object[] args, Object result, Throwable throwable) {
        final boolean success = InterceptorUtils.isSuccess(throwable);
        // Must not check if current transaction is trace target or not. Connection can be made by other thread.
        final String driverUrl = (String) args[0];
        DatabaseInfo databaseInfo = traceContext.getJdbcContext().parseJdbcUrl(serviceType, driverUrl);
        if (success) {
            if (recordConnection) {
                if (result instanceof DatabaseInfoAccessor) {
                    ((DatabaseInfoAccessor) result)._$PINPOINT$_setDatabaseInfo(databaseInfo);
                }
            }
        }
    }

    @Override
    protected void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        if (recordConnection) {
            DatabaseInfo databaseInfo;
            if (result instanceof DatabaseInfoAccessor) {
                databaseInfo = ((DatabaseInfoAccessor) result)._$PINPOINT$_getDatabaseInfo();
            } else {
                databaseInfo = null;
            }

            if (databaseInfo == null) {
                databaseInfo = UnKnownDatabaseInfo.INSTANCE;
            }
            
            // Count database connect too because it's very heavy operation
            recorder.recordServiceType(databaseInfo.getType());
            recorder.recordEndPoint(databaseInfo.getMultipleHost());
            recorder.recordDestinationId(databaseInfo.getDatabaseId());
        }
        final String driverUrl = (String) args[0];
        // Invoking databaseInfo.getRealUrl() here is dangerous. It doesn't return real URL if it's a loadbalance connection.  
        recorder.recordApiCachedString(methodDescriptor, driverUrl, 0);

        recorder.recordException(throwable);
    }

}
