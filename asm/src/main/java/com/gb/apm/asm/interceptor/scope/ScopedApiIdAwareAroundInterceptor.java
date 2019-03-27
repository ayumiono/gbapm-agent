/*
 * Copyright 2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gb.apm.asm.interceptor.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.interceptor.ApiIdAwareAroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScopeInvocation;

/**
 * @author emeroad
 */
public class ScopedApiIdAwareAroundInterceptor implements ApiIdAwareAroundInterceptor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final boolean debugEnabled = logger.isDebugEnabled();

    private final ApiIdAwareAroundInterceptor delegate;
    private final InterceptorScope scope;
    private final ExecutionPolicy policy;

    public ScopedApiIdAwareAroundInterceptor(ApiIdAwareAroundInterceptor delegate, InterceptorScope scope, ExecutionPolicy policy) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }
        if (scope == null) {
            throw new NullPointerException("scope must not be null");
        }
        if (policy == null) {
            throw new NullPointerException("policy must not be null");
        }
        this.delegate = delegate;
        this.scope = scope;
        this.policy = policy;
    }

    @Override
    public void before(Object target, int apiId, Object[] args) {
        final InterceptorScopeInvocation transaction = scope.getCurrentInvocation();
        
        if (transaction.tryEnter(policy)) {
            this.delegate.before(target, apiId, args);
        } else {
            if (debugEnabled) {
                logger.debug("tryBefore() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, delegate.getClass());
            }
        }
    }

    @Override
    public void after(Object target, int apiId, Object[] args, Object result, Throwable throwable) {
        final InterceptorScopeInvocation transaction = scope.getCurrentInvocation();
        
        if (transaction.canLeave(policy)) {
            this.delegate.after(target, apiId, args, result, throwable);
            transaction.leave(policy);
        } else {
            if (debugEnabled) {
                logger.debug("tryAfter() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, delegate.getClass());
            }
        }
    }
}
