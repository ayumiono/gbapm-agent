package com.gb.apm.bootstrap.core.interceptor.scope;


/**
 * @author Jongho Moon
 *
 */
public interface InterceptorScope {
    String getName();
    InterceptorScopeInvocation getCurrentInvocation();
}
