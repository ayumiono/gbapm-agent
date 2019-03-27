package com.gb.apm.bootstrap.core.interceptor.registry;

import com.gb.apm.bootstrap.core.interceptor.Interceptor;


/**
 * @author emeroad
 */
public interface InterceptorRegistryAdaptor {
    int addInterceptor(Interceptor interceptor);
    Interceptor getInterceptor(int key);
}
