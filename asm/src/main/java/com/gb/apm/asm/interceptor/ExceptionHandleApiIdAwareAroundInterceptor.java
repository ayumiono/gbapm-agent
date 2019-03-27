package com.gb.apm.asm.interceptor;

import com.gb.apm.bootstrap.core.interceptor.ApiIdAwareAroundInterceptor;

/**
 * @author jaehong.kim
 */
public class ExceptionHandleApiIdAwareAroundInterceptor implements ApiIdAwareAroundInterceptor {

    private final ApiIdAwareAroundInterceptor delegate;

    public ExceptionHandleApiIdAwareAroundInterceptor(ApiIdAwareAroundInterceptor delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }

        this.delegate = delegate;
    }

    @Override
    public void before(Object target, int apiId, Object[] args) {
        try {
            this.delegate.before(target, apiId, args);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }

    @Override
    public void after(Object target, int apiId, Object[] args, Object result, Throwable throwable) {
        try {
            this.delegate.after(target, apiId, args, result, throwable);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }
}