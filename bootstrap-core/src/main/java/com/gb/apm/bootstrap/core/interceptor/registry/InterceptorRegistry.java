package com.gb.apm.bootstrap.core.interceptor.registry;

import com.gb.apm.bootstrap.core.interceptor.Interceptor;


/**
 * @author emeroad
 */
public final class InterceptorRegistry {

    private static final Locker LOCK = new DefaultLocker();

    private static InterceptorRegistryAdaptor REGISTRY = EmptyRegistryAdaptor.EMPTY;

    public static void bind(final InterceptorRegistryAdaptor interceptorRegistryAdaptor, final Object lock) {
        if (interceptorRegistryAdaptor == null) {
            throw new NullPointerException("interceptorRegistryAdaptor must not be null");
        }
        
        if (LOCK.lock(lock)) {
            REGISTRY = interceptorRegistryAdaptor;
        } else {
            throw new IllegalStateException("bind failed. lock=" + lock + " current=" + LOCK.getLock());
        }
    }

    public static void unbind(final Object lock) {
        if (LOCK.unlock(lock)) {
            REGISTRY = EmptyRegistryAdaptor.EMPTY;
        } else {
            throw new IllegalStateException("unbind failed. lock=" + lock + " current=" + LOCK.getLock());
        }
    }

    public static Interceptor getInterceptor(int key) {
        return REGISTRY.getInterceptor(key);
    }
}
