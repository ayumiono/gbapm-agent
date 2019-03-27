package com.gb.apm.asm.interceptor;

import com.gb.apm.bootstrap.core.interceptor.StaticAroundInterceptor;

/**
 * @author jaehong.kim
 */
public class ExceptionHandleStaticAroundInterceptor implements StaticAroundInterceptor {

    private final StaticAroundInterceptor delegate;

    public ExceptionHandleStaticAroundInterceptor(StaticAroundInterceptor delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }

        this.delegate = delegate;
    }

    @Override
    public void before(Object target, String className, String methodName, String parameterDescription, Object[] args) {
        try {
            this.delegate.before(target, className, methodName, parameterDescription, args);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }

    @Override
    public void after(Object target, String className, String methodName, String parameterDescription, Object[] args, Object result, Throwable throwable) {
        try {
            this.delegate.after(target, className, methodName, parameterDescription, args, result, throwable);
        } catch (Throwable t) {
            InterceptorInvokerHelper.handleException(t);
        }
    }
}