package com.gb.apm.asm.interceptor;

import java.lang.reflect.Method;

import com.gb.apm.bootstrap.core.interceptor.Interceptor;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultInterceptorDefinition implements InterceptorDefinition {
    private final Class<? extends Interceptor> baseInterceptorClazz;
    private final Class<? extends Interceptor> interceptorClazz;
    private final InterceptorType interceptorType;
    private final CaptureType captureType;
    private final Method beforeMethod;
    private final Method afterMethod;

    public DefaultInterceptorDefinition(Class<? extends Interceptor> baseInterceptorClazz, Class<? extends Interceptor> interceptorClazz, InterceptorType interceptorType, CaptureType captureType, Method beforeMethod, Method afterMethod) {
        if (baseInterceptorClazz == null) {
            throw new NullPointerException("baseInterceptorClazz must not be null");
        }
        if (interceptorClazz == null) {
            throw new NullPointerException("interceptorClazz must not be null");
        }
        if (interceptorType == null) {
            throw new NullPointerException("interceptorType must not be null");
        }
        if (captureType == null) {
            throw new NullPointerException("captureType must not be null");
        }
        this.baseInterceptorClazz = baseInterceptorClazz;
        this.interceptorClazz = interceptorClazz;
        this.interceptorType = interceptorType;
        this.captureType = captureType;
        this.beforeMethod = beforeMethod;
        this.afterMethod = afterMethod;
    }

    @Override
    public Class<? extends Interceptor> getInterceptorBaseClass() {
        return baseInterceptorClazz;
    }


    @Override
    public Class<? extends Interceptor> getInterceptorClass() {
        return interceptorClazz;
    }

    @Override
    public InterceptorType getInterceptorType() {
        return interceptorType;
    }


    @Override
    public CaptureType getCaptureType() {
        return captureType;
    }

    @Override
    public Method getBeforeMethod() {
        return beforeMethod;
    }

    @Override
    public Method getAfterMethod() {
        return afterMethod;
    }


}
