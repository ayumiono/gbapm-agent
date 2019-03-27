package com.gb.apm.bootstrap.core.plugin.jdbc.interceptor;

import java.util.HashMap;
import java.util.Map;

import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.StaticAroundInterceptor;
import com.gb.apm.bootstrap.core.logging.PLogger;
import com.gb.apm.bootstrap.core.logging.PLoggerFactory;
import com.gb.apm.bootstrap.core.plugin.jdbc.BindValueAccessor;
import com.gb.apm.bootstrap.core.plugin.jdbc.bindvalue.BindValueConverter;
import com.gb.apm.common.utils.NumberUtils;

/**
 * @author emeroad
 * @author jaehong.kim
 */
// #1375 Workaround java level Deadlock
// https://oss.navercorp.com/pinpoint/pinpoint-naver/issues/1375
//@TargetFilter(type = "com.navercorp.pinpoint.bootstrap.plugin.jdbc.PreparedStatementBindingMethodFilter", singleton = true)
public class PreparedStatementBindVariableInterceptor implements StaticAroundInterceptor {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private final TraceContext traceContext;

    public PreparedStatementBindVariableInterceptor(TraceContext traceContext) {
        this.traceContext = traceContext;
    }

    @Override
    public void before(Object target, String className, String methodName, String parameterDescription, Object[] args) {
    }


    @Override
    public void after(Object target, String className, String methodName, String parameterDescription, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, className, methodName, parameterDescription, args, result, throwable);
        }

        final Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        if (!(target instanceof BindValueAccessor)) {
            return;
        }

        final Integer index = NumberUtils.toInteger(args[0]);
        if (index == null) {
            // something is wrong
            return;
        }

        Map<Integer, String> bindList = ((BindValueAccessor) target)._$PINPOINT$_getBindValue();
        if (bindList == null) {
            bindList = new HashMap<Integer, String>();
            ((BindValueAccessor) target)._$PINPOINT$_setBindValue(bindList);
        }

        final String value = BindValueConverter.convert(methodName, args);
        bindList.put(index, value);
    }
}