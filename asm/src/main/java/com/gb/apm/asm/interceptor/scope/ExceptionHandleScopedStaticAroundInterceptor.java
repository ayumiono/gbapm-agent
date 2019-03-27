package com.gb.apm.asm.interceptor.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.interceptor.InterceptorInvokerHelper;
import com.gb.apm.bootstrap.core.interceptor.StaticAroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScopeInvocation;

/**
 * @author emeroad
 * @author jaehong.kim
 */
public class ExceptionHandleScopedStaticAroundInterceptor implements StaticAroundInterceptor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final boolean debugEnabled = logger.isDebugEnabled();

    private final StaticAroundInterceptor delegate;
    private final InterceptorScope scope;
    private final ExecutionPolicy policy;

    public ExceptionHandleScopedStaticAroundInterceptor(StaticAroundInterceptor delegate, InterceptorScope scope, ExecutionPolicy policy) {
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
    public void before(Object target, String className, String methodName, String parameterDescription, Object[] args) {
        final InterceptorScopeInvocation transaction = scope.getCurrentInvocation();

        if (transaction.tryEnter(policy)) {
            try {
                this.delegate.before(target, className, methodName, parameterDescription, args);
            } catch (Throwable t) {
                InterceptorInvokerHelper.handleException(t);
            }
        } else {
            if (debugEnabled) {
                logger.debug("tryBefore() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, delegate.getClass());
            }
        }
    }

    @Override
    public void after(Object target, String className, String methodName, String parameterDescription, Object[] args, Object result, Throwable throwable) {
        final InterceptorScopeInvocation transaction = scope.getCurrentInvocation();

        if (transaction.canLeave(policy)) {
            try {
                this.delegate.after(target, className, methodName, parameterDescription, args, result, throwable);
            } catch (Throwable t) {
                InterceptorInvokerHelper.handleException(t);
            } finally {
                transaction.leave(policy);
            }
        } else {
            if (debugEnabled) {
                logger.debug("tryAfter() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, delegate.getClass());
            }
        }
    }
}