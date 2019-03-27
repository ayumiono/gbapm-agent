package com.gb.apm.bootstrap.core.instrument;

import java.io.InputStream;

import com.gb.apm.bootstrap.core.instrument.matcher.Matcher;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformCallback;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;

/**
 * @author Woonduk Kang(emeroad)
 * @author jaehong.kim
 */
public interface InstrumentContext {

    InstrumentClass getInstrumentClass(ClassLoader classLoader, String className, byte[] classfileBuffer);

    boolean exist(ClassLoader classLoader, String className);

    InterceptorScope getInterceptorScope(String name);

    <T> Class<? extends T> injectClass(ClassLoader targetClassLoader, String className);

    InputStream getResourceAsStream(ClassLoader targetClassLoader, String classPath);

    void addClassFileTransformer(ClassLoader classLoader, String targetClassName, TransformCallback transformCallback);

    void addClassFileTransformer(String targetClassName, TransformCallback transformCallback);
    
    void addClassFileTransformer(Matcher matcher, TransformCallback transformCallback);

    void retransform(Class<?> target, TransformCallback transformCallback);

}
