package com.gb.apm.bootstrap.core.interceptor.registry;

import com.gb.apm.bootstrap.core.interceptor.Interceptor;


/**
 * for test
 * @author emeroad
 */
public class GlobalInterceptorRegistry {

    public static final InterceptorRegistryAdaptor REGISTRY = new DefaultInterceptorRegistryAdaptor();

    public static void bind(final InterceptorRegistryAdaptor interceptorRegistryAdaptor, final Object lock) {

    }

    public static void unbind(final Object lock) {

    }

    public static Interceptor getInterceptor(int key) {
        return REGISTRY.getInterceptor(key);
    }
}
