package com.gb.apm.asm.interceptor.registry;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.interceptor.registry.DefaultInterceptorRegistryAdaptor;
import com.gb.apm.bootstrap.core.interceptor.registry.InterceptorRegistry;
import com.gb.apm.bootstrap.core.interceptor.registry.InterceptorRegistryAdaptor;

/**
 * @author emeroad
 */
public class DefaultInterceptorRegistryBinder implements InterceptorRegistryBinder {

    public final static int DEFAULT_MAX = 8192;

    private static final AtomicInteger LOCK_NUMBER = new AtomicInteger();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String lock = "DefaultRegistry-" + LOCK_NUMBER.getAndIncrement();
    private final InterceptorRegistryAdaptor interceptorRegistryAdaptor;

    public DefaultInterceptorRegistryBinder() {
        this(DEFAULT_MAX);
    }

    public DefaultInterceptorRegistryBinder(int maxRegistrySize) {
        this.interceptorRegistryAdaptor = new DefaultInterceptorRegistryAdaptor(maxRegistrySize);
    }

    @Override
    public void bind() {
        logger.info("bind:{}", lock);
        InterceptorRegistry.bind(interceptorRegistryAdaptor, lock);
    }

    @Override
    public void unbind() {
        logger.info("unbind:{}", lock);
        InterceptorRegistry.unbind(lock);
    }

    public InterceptorRegistryAdaptor getInterceptorRegistryAdaptor() {
        return interceptorRegistryAdaptor;
    }

    @Override
    public String getInterceptorRegistryClassName() {
        return InterceptorRegistry.class.getName();
    }
}
