package com.gb.apm.profiler.instrument.classloading;

import java.io.InputStream;

import com.gb.apm.asm.apiadapter.BootstrapPackage;
import com.gb.apm.bootstrap.core.PinpointException;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DebugTransformerClassInjector implements ClassInjector {

    private final BootstrapPackage bootstrapPackage = new BootstrapPackage();

    public DebugTransformerClassInjector() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> injectClass(ClassLoader classLoader, String className) {

        ClassLoader targetClassLoader = getClassLoader(classLoader);

        targetClassLoader = filterBootstrapPackage(targetClassLoader, className);

        try {
            return (Class<? extends T>) targetClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new PinpointException("ClassNo class " + className + " with classLoader " + classLoader, e);
        }
    }


    @Override
    public InputStream getResourceAsStream(ClassLoader classLoader, String classPath) {
        ClassLoader targetClassLoader = getClassLoader(classLoader);

        targetClassLoader = filterBootstrapPackage(targetClassLoader, classPath);

        return targetClassLoader.getResourceAsStream(classPath);
    }

    private static ClassLoader getClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }


    private ClassLoader filterBootstrapPackage(ClassLoader classLoader, String classPath) {
        if (bootstrapPackage.isBootstrapPackage(classPath)) {
            return ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }
}
