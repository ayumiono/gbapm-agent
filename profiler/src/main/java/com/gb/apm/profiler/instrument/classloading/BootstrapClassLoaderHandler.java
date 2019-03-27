package com.gb.apm.profiler.instrument.classloading;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.PinpointException;
import com.gb.apm.profiler.plugin.PluginConfig;

/**
 * @author Woonduk Kang(emeroad)
 * @author jaehong.kim
 */
public class BootstrapClassLoaderHandler implements ClassInjector {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PluginConfig pluginConfig;

    private final Object lock = new Object();
    private boolean injectedToRoot = false;

    private final InstrumentEngine instrumentEngine;


    public BootstrapClassLoaderHandler(PluginConfig pluginConfig, InstrumentEngine instrumentEngine) {
        if (pluginConfig == null) {
            throw new NullPointerException("pluginConfig must not be null");
        }
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }
        this.pluginConfig = pluginConfig;
        this.instrumentEngine = instrumentEngine;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> injectClass(ClassLoader classLoader, String className) {
        try {
            if (classLoader == null) {
                return (Class<T>) injectClass0(className);
            }
        } catch (Exception e) {
            logger.warn("Failed to load plugin class {} with classLoader {}", className, classLoader, e);
            throw new PinpointException("Failed to load plugin class " + className + " with classLoader " + classLoader, e);
        }
        throw new PinpointException("invalid ClassLoader");
    }

    private Class<?> injectClass0(String className) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        appendToBootstrapClassLoaderSearch();
        return Class.forName(className, false, null);
    }

    private void appendToBootstrapClassLoaderSearch() {
        synchronized (lock) {
            if (this.injectedToRoot == false) {
                this.injectedToRoot = true;
                instrumentEngine.appendToBootstrapClassPath(pluginConfig.getPluginJarFile());
            }
        }
    }

    @Override
    public InputStream getResourceAsStream(ClassLoader targetClassLoader, String classPath) {
        try {
            if (targetClassLoader == null) {
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                if (classLoader == null) {
                    return null;
                }
                appendToBootstrapClassLoaderSearch();
                return classLoader.getResourceAsStream(classPath);
            }
        } catch (Exception e) {
            logger.warn("Failed to load plugin resource as stream {} with classLoader {}", classPath, targetClassLoader, e);
            return null;
        }
        logger.warn("Invalid bootstrap class loader. cl={}", targetClassLoader);
        return null;
    }
}