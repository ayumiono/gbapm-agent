package com.gb.apm.asm.interceptor;

import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor3;

/**
 * @author jaehong.kim
 */
public class ExceptionHandleAroundInterceptor3 implements AroundInterceptor3 {

    private final AroundInterceptor3 delegate;

    public ExceptionHandleAroundInterceptor3(AroundInterceptor3 delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }

        this.delegate = delegate;
    }

    @Override
    public void before(Object target, Object arg0, Object arg1, Object arg2) {
        try {
            this.delegate.before(target, arg0, arg1, arg2);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }

    @Override
    public void after(Object target, Object arg0, Object arg1, Object arg2, Object result, Throwable throwable) {
        try {
            this.delegate.after(target, arg0, arg1, arg2, result, throwable);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }
}