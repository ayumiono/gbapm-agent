package com.gb.apm.asm.interceptor.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.interceptor.InterceptorInvokerHelper;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor0;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScopeInvocation;

/**
 * @author emeroad
 * @author jaehong.kim
 */
public class ExceptionHandleScopedInterceptor0 implements AroundInterceptor0 {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final boolean debugEnabled = logger.isDebugEnabled();

    private final AroundInterceptor0 interceptor;
    private final InterceptorScope scope;
    private final ExecutionPolicy policy;

    public ExceptionHandleScopedInterceptor0(AroundInterceptor0 interceptor, InterceptorScope scope, ExecutionPolicy policy) {
        if (interceptor == null) {
            throw new NullPointerException("interceptor must not be null");
        }
        if (scope == null) {
            throw new NullPointerException("scope must not be null");
        }
        if (policy == null) {
            throw new NullPointerException("policy must not be null");
        }
        this.interceptor = interceptor;
        this.scope = scope;
        this.policy = policy;
    }

    @Override
    public void before(Object target) {
        final InterceptorScopeInvocation transaction = scope.getCurrentInvocation();

        if (transaction.tryEnter(policy)) {
            try {
                this.interceptor.before(target);
            } catch (Throwable t) {
                InterceptorInvokerHelper.handleException(t);
            }
        } else {
            if (debugEnabled) {
                logger.debug("tryBefore() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, interceptor.getClass());
            }
        }
    }

    @Override
    public void after(Object target, Object result, Throwable throwable) {
        final InterceptorScopeInvocation transaction = scope.getCurrentInvocation();

        if (transaction.canLeave(policy)) {
            try {
                this.interceptor.after(target, result, throwable);
            } catch (Throwable t) {
                InterceptorInvokerHelper.handleException(t);
            } finally {
                transaction.leave(policy);
            }
        } else {
            if (debugEnabled) {
                logger.debug("tryAfter() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, interceptor.getClass());
            }
        }
    }
}