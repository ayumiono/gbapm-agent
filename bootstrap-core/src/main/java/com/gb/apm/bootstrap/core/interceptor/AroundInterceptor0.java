package com.gb.apm.bootstrap.core.interceptor;

/**
 * @author Jongho Moon
 *
 */
public interface AroundInterceptor0 extends Interceptor {

    void before(Object target);

    void after(Object target, Object result, Throwable throwable);
}
