package com.gb.apm.bootstrap.core.interceptor;

/**
 * @author Jongho Moon
 *
 */
public interface AroundInterceptor1 extends Interceptor {

    void before(Object target, Object arg0);

    void after(Object target, Object arg0, Object result, Throwable throwable);
}
