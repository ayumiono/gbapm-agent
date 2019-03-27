package com.gb.apm.asm.interceptor;

import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor2;

/**
 * @author jaehong.kim
 */
public class ExceptionHandleAroundInterceptor2 implements AroundInterceptor2 {

    private final AroundInterceptor2 delegate;

    public ExceptionHandleAroundInterceptor2(AroundInterceptor2 delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }

        this.delegate = delegate;
    }

    @Override
    public void before(Object target, Object arg0, Object arg1) {
        try {
            this.delegate.before(target, arg0, arg1);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }

    @Override
    public void after(Object target, Object arg0, Object arg1, Object result, Throwable throwable) {
        try {
            this.delegate.after(target, arg0, arg1, result, throwable);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }
}