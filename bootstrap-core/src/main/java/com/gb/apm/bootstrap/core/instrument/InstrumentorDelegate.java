package com.gb.apm.bootstrap.core.instrument;


import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformCallback;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;

/**
 * @author emeroad
 */
public class InstrumentorDelegate implements Instrumentor {
    private final ProfilerConfig profilerConfig;
    private final InstrumentContext instrumentContext;

    public InstrumentorDelegate(ProfilerConfig profilerConfig, InstrumentContext instrumentContext) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (instrumentContext == null) {
            throw new NullPointerException("instrumentContext must not be null");
        }
        this.profilerConfig = profilerConfig;
        this.instrumentContext = instrumentContext;
    }

    public ProfilerConfig getProfilerConfig() {
        return profilerConfig;
    }

    @Override
    public InstrumentClass getInstrumentClass(ClassLoader classLoader, String className, byte[] classfileBuffer) {
        return instrumentContext.getInstrumentClass(classLoader, className, classfileBuffer);
    }

    @Override
    public boolean exist(ClassLoader classLoader, String className) {
        return instrumentContext.exist(classLoader, className);
    }

    @Override
    public InterceptorScope getInterceptorScope(String scopeName) {
        return instrumentContext.getInterceptorScope(scopeName);
    }

    @Override
    public <T> Class<? extends T> injectClass(ClassLoader targetClassLoader, String className) {
        return instrumentContext.injectClass(targetClassLoader, className);
    }

    @Override
    public void transform(ClassLoader classLoader, String targetClassName, TransformCallback transformCallback) {
        instrumentContext.addClassFileTransformer(classLoader, targetClassName, transformCallback);
    }

    @Override
    public void retransform(Class<?> target, TransformCallback transformCallback) {
        instrumentContext.retransform(target, transformCallback);
    }

}
