package com.gb.apm.profiler.objectfactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.objectfactory.ArgumentProvider;
import com.gb.apm.asm.objectfactory.ArgumentsResolver;
import com.gb.apm.asm.objectfactory.ConstructorResolver;
import com.gb.apm.asm.objectfactory.ObjectFactory;
import com.gb.apm.asm.objectfactory.ObjectFactory.ByConstructor;
import com.gb.apm.asm.objectfactory.ObjectFactory.ByStaticFactoryMethod;
import com.gb.apm.asm.objectfactory.OrderedValueProvider;
import com.gb.apm.asm.objectfactory.StaticMethodResolver;
import com.gb.apm.bootstrap.core.PinpointException;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;

/**
 * @author Jongho Moon
 *
 */
public class AutoBindingObjectFactory implements com.gb.apm.asm.objectfactory.AutoBindingObjectFactory{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private final InstrumentContext pluginContext;
    private final ClassLoader classLoader;
    private final List<ArgumentProvider> commonProviders;

    public AutoBindingObjectFactory(ProfilerConfig profilerConfig, TraceContext traceContext, InstrumentContext pluginContext, ClassLoader classLoader, ArgumentProvider... argumentProviders) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (traceContext == null) {
            throw new NullPointerException("traceContext must not be null");
        }
        if (pluginContext == null) {
            throw new NullPointerException("pluginContext must not be null");
        }
        this.pluginContext = pluginContext;
        this.classLoader = classLoader;
        this.commonProviders = newArgumentProvider(profilerConfig, traceContext, pluginContext, argumentProviders);
    }

    private List<ArgumentProvider> newArgumentProvider(ProfilerConfig profilerConfig, TraceContext traceContext, InstrumentContext pluginContext, ArgumentProvider[] argumentProviders) {
        final List<ArgumentProvider> commonProviders = new ArrayList<ArgumentProvider>();
        for (ArgumentProvider argumentProvider : argumentProviders) {
            commonProviders.add(argumentProvider);
        }
        ProfilerPluginArgumentProvider profilerPluginArgumentProvider = new ProfilerPluginArgumentProvider(profilerConfig, traceContext, pluginContext);
        commonProviders.add(profilerPluginArgumentProvider);
        return commonProviders;
    }

    @Override
    public Object createInstance(ObjectFactory objectFactory, ArgumentProvider... providers) {
        final Class<?> type = pluginContext.injectClass(classLoader, objectFactory.getClassName());
        final ArgumentsResolver argumentsResolver = getArgumentResolver(objectFactory, providers);
        
        if (objectFactory instanceof ByConstructor) {
            return byConstructor(type, (ByConstructor) objectFactory, argumentsResolver);
        } else if (objectFactory instanceof ByStaticFactoryMethod) {
            return byStaticFactoryMethod(type, (ByStaticFactoryMethod) objectFactory, argumentsResolver);
        }
        
        throw new IllegalArgumentException("Unknown objectFactory type: " + objectFactory);
    }
    
    private Object byConstructor(Class<?> type, ByConstructor byConstructor, ArgumentsResolver argumentsResolver) {
        final ConstructorResolver resolver = new ConstructorResolver(type, argumentsResolver);
        
        if (!resolver.resolve()) {
            throw new PinpointException("Cannot find suitable constructor for " + type.getName());
        }
        
        final Constructor<?> constructor = resolver.getResolvedConstructor();
        final Object[] resolvedArguments = resolver.getResolvedArguments();
        
        if (isDebug) {
            logger.debug("Create instance by constructor {}, with arguments {}", constructor, Arrays.toString(resolvedArguments));
        }
        
        try {
            return constructor.newInstance(resolvedArguments);
        } catch (Exception e) {
            throw new PinpointException("Fail to invoke constructor: " + constructor + ", arguments: " + Arrays.toString(resolvedArguments), e);
        }
    }

    private Object byStaticFactoryMethod(Class<?> type, ByStaticFactoryMethod staticFactoryMethod, ArgumentsResolver argumentsResolver) {
        StaticMethodResolver resolver = new StaticMethodResolver(type, staticFactoryMethod.getFactoryMethodName(), argumentsResolver);
        
        if (!resolver.resolve()) {
            throw new PinpointException("Cannot find suitable factory method " + type.getName() + "." + staticFactoryMethod.getFactoryMethodName());
        }
        
        final Method method = resolver.getResolvedMethod();
        final Object[] resolvedArguments = resolver.getResolvedArguments();

        if (isDebug) {
            logger.debug("Create instance by static factory method {}, with arguments {}", method, Arrays.toString(resolvedArguments));
        }

        try {
            return method.invoke(null, resolvedArguments);
        } catch (Exception e) {
            throw new PinpointException("Fail to invoke factory method: " + type.getName() + "." + staticFactoryMethod.getFactoryMethodName() + ", arguments: " + Arrays.toString(resolvedArguments), e);
        }

    }
    
    private ArgumentsResolver getArgumentResolver(ObjectFactory objectFactory, ArgumentProvider[] providers) {
        final List<ArgumentProvider> merged = new ArrayList<ArgumentProvider>(commonProviders);
        merged.addAll(Arrays.asList(providers));
        
        if (objectFactory.getArguments() != null) {
            merged.add(new OrderedValueProvider(this, objectFactory.getArguments()));
        }
        
        return new ArgumentsResolver(merged);
    }
}
