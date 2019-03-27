package com.gb.apm.asm.interceptor;

import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor1;

/**
 * @author jaehong.kim
 */
public class ExceptionHandleAroundInterceptor1 implements AroundInterceptor1 {

    private final AroundInterceptor1 delegate;

    public ExceptionHandleAroundInterceptor1(AroundInterceptor1 delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }

        this.delegate = delegate;
    }

    @Override
    public void before(Object target, Object arg0) {
        try {
            this.delegate.before(target, arg0);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }

    @Override
    public void after(Object target, Object arg0, Object result, Throwable throwable) {
        try {
            this.delegate.after(target, arg0, result, throwable);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }
}