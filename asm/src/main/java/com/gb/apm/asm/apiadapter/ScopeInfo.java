package com.gb.apm.asm.apiadapter;

import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ScopeInfo {
    private final InterceptorScope interceptorScope;
    private final ExecutionPolicy executionPolicy;

    public ScopeInfo(InterceptorScope interceptorScope, ExecutionPolicy executionPolicy) {
        this.interceptorScope = interceptorScope;
        this.executionPolicy = executionPolicy;
    }

    public InterceptorScope getInterceptorScope() {
        return interceptorScope;
    }

    public ExecutionPolicy getExecutionPolicy() {
        return executionPolicy;
    }

    @Override
    public String toString() {
        return "ScopeInfo{" +
                "interceptorScope=" + interceptorScope +
                ", executionPolicy=" + executionPolicy +
                '}';
    }
}
