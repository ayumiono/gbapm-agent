package com.gb.apm.asm.interceptor;

import java.lang.reflect.Method;

import com.gb.apm.bootstrap.core.interceptor.Interceptor;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface InterceptorDefinition {
    Class<? extends Interceptor> getInterceptorBaseClass();

    Class<? extends Interceptor> getInterceptorClass();

    InterceptorType getInterceptorType();

    CaptureType getCaptureType();

    Method getBeforeMethod();

    Method getAfterMethod();
}
