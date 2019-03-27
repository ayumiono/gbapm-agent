package com.gb.apm.asm.interceptor;

import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor0;

/**
 * @author jaehong.kim
 */
public class ExceptionHandleAroundInterceptor0 implements AroundInterceptor0 {

    private final AroundInterceptor0 delegate;

    public ExceptionHandleAroundInterceptor0(AroundInterceptor0 delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }

        this.delegate = delegate;
    }

    @Override
    public void before(Object target) {
        try {
            this.delegate.before(target);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }

    }

    @Override
    public void after(Object target, Object result, Throwable throwable) {
        try {
            this.delegate.after(target, result, throwable);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }
}