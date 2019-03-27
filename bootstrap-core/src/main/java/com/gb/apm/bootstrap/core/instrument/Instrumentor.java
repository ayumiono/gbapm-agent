package com.gb.apm.bootstrap.core.instrument;

import com.gb.apm.bootstrap.core.instrument.transfomer.TransformCallback;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;

/**
 * @author Jongho Moon
 *
 */
public interface Instrumentor {

//    ProfilerConfig getProfilerConfig();//将这个功能移到InstrumentorDelegate中,去耦合。但TransformCallback.doTransform中的参数是Instrumentor,如果去掉则在拦截器中取不到ProfileConfig
    
    InstrumentClass getInstrumentClass(ClassLoader classLoader, String className, byte[] classfileBuffer);
    
    boolean exist(ClassLoader classLoader, String className);
    
    InterceptorScope getInterceptorScope(String scopeName);
        
    <T> Class<? extends T> injectClass(ClassLoader targetClassLoader, String className);
    
    void transform(ClassLoader classLoader, String targetClassName, TransformCallback transformCallback);
    
    void retransform(Class<?> target, TransformCallback transformCallback);
}
