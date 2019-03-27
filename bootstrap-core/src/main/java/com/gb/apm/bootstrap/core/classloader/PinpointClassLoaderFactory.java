package com.gb.apm.bootstrap.core.classloader;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.gb.apm.common.utils.jdk.JvmUtils;
import com.gb.apm.common.utils.jdk.JvmVersion;

/**
 * @author Taejin Koo
 */
public final class PinpointClassLoaderFactory {

    private static final InnerPinpointClassLoaderFactory CLASS_LOADER_FACTORY = createClassLoaderFactory();

    // Jdk 7+
    private static final String PARALLEL_CAPABLE_CLASS_LOADER_FACTORY = "com.navercorp.pinpoint.bootstrap.classloader.ParallelCapablePinpointClassLoaderFactory";

    private PinpointClassLoaderFactory() {
        throw new IllegalAccessError();
    }

    private static InnerPinpointClassLoaderFactory createClassLoaderFactory() {
        final JvmVersion jvmVersion = JvmUtils.getVersion();
        if (jvmVersion == JvmVersion.JAVA_6) {
            return new DefaultPinpointClassLoaderFactory();
        } else if (jvmVersion.onOrAfter(JvmVersion.JAVA_7)) {
            boolean hasRegisterAsParallelCapableMethod = hasRegisterAsParallelCapableMethod();
            if (hasRegisterAsParallelCapableMethod) {
                try {
                    ClassLoader classLoader = getClassLoader(PinpointClassLoaderFactory.class.getClassLoader());
                    final Class<? extends InnerPinpointClassLoaderFactory> parallelCapableClassLoaderFactoryClass =
                            (Class<? extends InnerPinpointClassLoaderFactory>) Class.forName(PARALLEL_CAPABLE_CLASS_LOADER_FACTORY, true, classLoader);
                    return parallelCapableClassLoaderFactoryClass.newInstance();
                } catch (ClassNotFoundException e) {
                    logError(e);
                } catch (InstantiationException e) {
                    logError(e);
                } catch (IllegalAccessException e) {
                    logError(e);
                }
                return new DefaultPinpointClassLoaderFactory();
            } else {
                return new DefaultPinpointClassLoaderFactory();
            }
        } else {
            throw new RuntimeException("Unsupported jvm version " + jvmVersion);
        }
    }

    private static boolean hasRegisterAsParallelCapableMethod() {
        Method[] methods = ClassLoader.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("registerAsParallelCapable")) {
                return true;
            }
        }

        return false;
    }

    private static ClassLoader getClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }

    private static void logError(Exception e) {
    	//TODO
    }

    public static URLClassLoader createClassLoader(URL[] urls, ClassLoader parent) {
        return CLASS_LOADER_FACTORY.createURLClassLoader(urls, parent);
    }

    public static URLClassLoader createClassLoader(URL[] urls, ClassLoader parent, LibClass libClass) {
        return CLASS_LOADER_FACTORY.createURLClassLoader(urls, parent, libClass);
    }

}
