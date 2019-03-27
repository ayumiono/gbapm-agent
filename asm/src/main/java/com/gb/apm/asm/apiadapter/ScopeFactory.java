package com.gb.apm.asm.apiadapter;

import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.interceptor.Interceptor;
import com.gb.apm.bootstrap.core.interceptor.annotation.Scope;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ScopeFactory {

    public ScopeFactory() {
    }

    public ScopeInfo newScopeInfo(ClassLoader classLoader, InstrumentContext pluginContext, String interceptorClassName, InterceptorScope scope, ExecutionPolicy policy) {

        if (scope == null) {
            final Class<? extends Interceptor> interceptorClass = pluginContext.injectClass(classLoader, interceptorClassName);
            final Scope scopeAnnotation = interceptorClass.getAnnotation(Scope.class);
            if (scopeAnnotation != null) {
                return newScopeInfoByAnnotation(pluginContext, scopeAnnotation);
            }
        }
        policy = getExecutionPolicy(scope, policy);

        return new ScopeInfo(scope, policy);
    }

    private ScopeInfo newScopeInfoByAnnotation(InstrumentContext pluginContext, Scope scope) {
        final String scopeName = scope.value();
        final InterceptorScope interceptorScope = pluginContext.getInterceptorScope(scopeName);

        final ExecutionPolicy policy = scope.executionPolicy();
        return new ScopeInfo(interceptorScope, policy);
    }

    private ExecutionPolicy getExecutionPolicy(InterceptorScope scope, ExecutionPolicy policy) {
        if (scope == null) {
            policy = null;
        } else if (policy == null) {
            policy = ExecutionPolicy.BOUNDARY;
        }
        return policy;
    }

}
