package com.gb.apm.asm.interceptor.registry;

import com.gb.apm.bootstrap.core.interceptor.registry.InterceptorRegistryAdaptor;

/**
 * @author emeroad
 */
public interface InterceptorRegistryBinder {

    void bind();

    void unbind();

    InterceptorRegistryAdaptor getInterceptorRegistryAdaptor();

    String getInterceptorRegistryClassName();
}
