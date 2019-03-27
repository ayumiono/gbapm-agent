package com.gb.apm.bootstrap.core.interceptor;

/**
 * @author Jongho Moon
 *
 */
public interface ApiIdAwareAroundInterceptor extends Interceptor {
    void before(Object target, int apiId, Object[] args);
    void after(Object target, int apiId, Object[] args, Object result, Throwable throwable);
}
