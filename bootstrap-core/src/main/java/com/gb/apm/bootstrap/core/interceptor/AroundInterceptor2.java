package com.gb.apm.bootstrap.core.interceptor;

/**
 * @author Jongho Moon
 *
 */
public interface AroundInterceptor2 extends Interceptor {

    void before(Object target, Object arg0, Object arg1);

    void after(Object target, Object arg0, Object arg1, Object result, Throwable throwable);
}
