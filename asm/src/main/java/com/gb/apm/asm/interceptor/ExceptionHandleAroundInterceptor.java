package com.gb.apm.asm.interceptor;

import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor;

/**
 * @author jaehong.kim
 */
public class ExceptionHandleAroundInterceptor implements AroundInterceptor {

    private final AroundInterceptor delegate;

    public ExceptionHandleAroundInterceptor(AroundInterceptor delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }

        this.delegate = delegate;
    }

    @Override
    public void before(Object target, Object[] args) {
        try {
            this.delegate.before(target, args);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        try {
            this.delegate.after(target, args, result, throwable);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }
}