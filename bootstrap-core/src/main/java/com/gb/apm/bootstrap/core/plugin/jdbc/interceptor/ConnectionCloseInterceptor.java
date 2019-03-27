package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor;
import com.gb.apm.bootstrap.core.logging.PLogger;
import com.gb.apm.bootstrap.core.logging.PLoggerFactory;
import com.gb.apm.bootstrap.core.plugin.jdbc.DatabaseInfoAccessor;

/**
 * @author emeroad
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetMethod(name="close")
public class ConnectionCloseInterceptor implements AroundInterceptor {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }
        // In case of close, we have to delete data even if the invocation failed.
        if (target instanceof DatabaseInfoAccessor) {
            ((DatabaseInfoAccessor) target)._$PINPOINT$_setDatabaseInfo(null);
        }
    }

// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//    @IgnoreMethod
    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {

    }
}
