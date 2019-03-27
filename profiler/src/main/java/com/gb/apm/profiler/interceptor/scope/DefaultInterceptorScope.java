package com.gb.apm.profiler.interceptor.scope;

import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScopeInvocation;

/**
 * @author Jongho Moon
 *
 */
public class DefaultInterceptorScope implements InterceptorScope {
    private final String name;
    private final ThreadLocal<InterceptorScopeInvocation> threadLocal;
    
    public DefaultInterceptorScope(final String name) {
        this.name = name;
        this.threadLocal = new ThreadLocal<InterceptorScopeInvocation>() {

            @Override
            protected InterceptorScopeInvocation initialValue() {
                return new DefaultInterceptorScopeInvocation(name);
            }
            
        };
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InterceptorScopeInvocation getCurrentInvocation() {
        return threadLocal.get();
    }
}
