package com.gb.apm.bootstrap.core.interceptor;

/**
 * @author emeroad
 */
public interface AroundInterceptor extends Interceptor {

    void before(Object target, Object[] args);

    void after(Object target, Object[] args, Object result, Throwable throwable);
}
