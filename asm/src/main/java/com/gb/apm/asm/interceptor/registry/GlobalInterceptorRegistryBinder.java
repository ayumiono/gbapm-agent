package com.gb.apm.asm.interceptor.registry;

import com.gb.apm.bootstrap.core.interceptor.registry.GlobalInterceptorRegistry;
import com.gb.apm.bootstrap.core.interceptor.registry.InterceptorRegistryAdaptor;

/**
 * for test
 * @author emeroad
 */
@Deprecated
public class GlobalInterceptorRegistryBinder implements InterceptorRegistryBinder {

    public GlobalInterceptorRegistryBinder() {
    }


    @Override
    public void bind() {
    }

    @Override
    public void unbind() {
    }

    public InterceptorRegistryAdaptor getInterceptorRegistryAdaptor() {
        return GlobalInterceptorRegistry.REGISTRY;
    }

    @Override
    public String getInterceptorRegistryClassName() {
        return GlobalInterceptorRegistry.class.getName();
    }
}
