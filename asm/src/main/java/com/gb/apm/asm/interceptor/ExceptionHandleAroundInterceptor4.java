package com.gb.apm.asm.interceptor;

import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor4;

/**
 * @author jaehong.kim
 */
public class ExceptionHandleAroundInterceptor4 implements AroundInterceptor4 {

    private final AroundInterceptor4 delegate;

    public ExceptionHandleAroundInterceptor4(AroundInterceptor4 delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }

        this.delegate = delegate;
    }

    @Override
    public void before(Object target, Object arg0, Object arg1, Object arg2, Object arg3) {
        try {
            this.delegate.before(target, arg0, arg1, arg2, arg3);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }

    @Override
    public void after(Object target, Object arg0, Object arg1, Object arg2, Object arg3, Object result, Throwable throwable) {
        try {
            this.delegate.after(target, arg0, arg1, arg2, arg3, result, throwable);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }
}