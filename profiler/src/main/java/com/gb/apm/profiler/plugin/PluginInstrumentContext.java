package com.gb.apm.profiler.plugin;

import java.io.InputStream;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.NotFoundInstrumentException;
import com.gb.apm.bootstrap.core.instrument.matcher.Matcher;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformCallback;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.profiler.context.scope.ConcurrentPool;
import com.gb.apm.profiler.context.scope.Pool;
import com.gb.apm.profiler.instrument.classloading.ClassInjector;
import com.gb.apm.profiler.instrument.classloading.PluginClassInjector;
import com.gb.apm.profiler.interceptor.scope.InterceptorScopeFactory;

/**
 * @author Woonduk Kang(emeroad)
 */
public class PluginInstrumentContext implements InstrumentContext {

    private final ProfilerConfig profilerConfig;
    private final InstrumentEngine instrumentEngine;
    private final DynamicTransformTrigger dynamicTransformTrigger;
    private final ClassInjector classInjector;

    private final Pool<String, InterceptorScope> interceptorScopePool = new ConcurrentPool<String, InterceptorScope>(new InterceptorScopeFactory());

    private final ClassFileTransformerLoader transformerRegistry;

    public PluginInstrumentContext(ProfilerConfig profilerConfig, InstrumentEngine instrumentEngine, DynamicTransformTrigger dynamicTransformTrigger, ClassInjector classInjector, ClassFileTransformerLoader transformerRegistry) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }
        if (dynamicTransformTrigger == null) {
            throw new NullPointerException("dynamicTransformTrigger must not be null");
        }
        if (classInjector == null) {
            throw new NullPointerException("classInjector must not be null");
        }
        if (transformerRegistry == null) {
            throw new NullPointerException("transformerRegistry must not be null");
        }
        this.profilerConfig = profilerConfig;
        this.instrumentEngine = instrumentEngine;
        this.dynamicTransformTrigger = dynamicTransformTrigger;
        this.classInjector = classInjector;
        this.transformerRegistry = transformerRegistry;
    }



    public PluginConfig getPluginConfig() {
        if (classInjector instanceof PluginClassInjector) {
            return ((PluginClassInjector) classInjector).getPluginConfig();
        }
        return null;
    }



    @Override
    public InstrumentClass getInstrumentClass(ClassLoader classLoader, String className, byte[] classFileBuffer) {
        if (className == null) {
            throw new NullPointerException("className must not be null");
        }
        try {
            final InstrumentEngine instrumentEngine = getInstrumentEngine();
            return instrumentEngine.getClass(this, classLoader, className, classFileBuffer);
        } catch (NotFoundInstrumentException e) {
            return null;
        }
    }

    @Override
    public boolean exist(ClassLoader classLoader, String className) {
        if (className == null) {
            throw new NullPointerException("className must not be null");
        }
        final InstrumentEngine instrumentEngine = getInstrumentEngine();
        return instrumentEngine.hasClass(classLoader, className);
    }

    private InstrumentEngine getInstrumentEngine() {
        return this.instrumentEngine;
    }

    @Override
    public void addClassFileTransformer(final String targetClassName, final TransformCallback transformCallback) {
        if (targetClassName == null) {
            throw new NullPointerException("targetClassName must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }

        transformerRegistry.addClassFileTransformer(this, targetClassName, transformCallback);
    }

    @Override
    public void addClassFileTransformer(ClassLoader classLoader, String targetClassName, final TransformCallback transformCallback) {
        if (targetClassName == null) {
            throw new NullPointerException("targetClassName must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }

        this.transformerRegistry.addClassFileTransformer(this, classLoader, targetClassName, transformCallback);
    }


    @Override
    public void retransform(Class<?> target, final TransformCallback transformCallback) {
        if (target == null) {
            throw new NullPointerException("target must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }

        final ClassFileTransformerGuardDelegate classFileTransformerGuardDelegate = new ClassFileTransformerGuardDelegate(profilerConfig, this, transformCallback);

        this.dynamicTransformTrigger.retransform(target, classFileTransformerGuardDelegate);
    }


    @Override
    public <T> Class<? extends T> injectClass(ClassLoader targetClassLoader, String className) {
        if (className == null) {
            throw new NullPointerException("className must not be null");
        }

        return classInjector.injectClass(targetClassLoader, className);
    }

    @Override
    public InputStream getResourceAsStream(ClassLoader targetClassLoader, String classPath) {
        if (classPath == null) {
            return null;
        }

        return classInjector.getResourceAsStream(targetClassLoader, classPath);
    }


    @Override
    public InterceptorScope getInterceptorScope(String name) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }

        return interceptorScopePool.get(name);
    }



	@Override
	public void addClassFileTransformer(Matcher matcher, TransformCallback transformCallback) {
		if (matcher == null) {
            throw new NullPointerException("matcher must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }

        transformerRegistry.addClassFileTransformer(this, matcher, transformCallback);
	}
}
