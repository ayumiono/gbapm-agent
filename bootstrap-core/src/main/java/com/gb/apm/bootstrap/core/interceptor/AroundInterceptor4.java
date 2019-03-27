package com.gb.apm.bootstrap.core.interceptor;

/**
 * @author Jongho Moon
 *
 */
public interface AroundInterceptor4 extends Interceptor {

    void before(Object target, Object arg0, Object arg1, Object arg2, Object arg3);

    void after(Object target, Object arg0, Object arg1, Object arg2, Object arg3, Object result, Throwable throwable);
}
