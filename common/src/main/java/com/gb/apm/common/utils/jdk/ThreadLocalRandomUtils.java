package com.gb.apm.common.utils.jdk;


import java.util.Random;

/**
 * @author HyunGil Jeong
 */
public class ThreadLocalRandomUtils {

    private static final ThreadLocalRandomFactory THREAD_LOCAL_RANDOM_FACTORY = createThreadLocalRandomFactory();

    // Jdk 7+
    private static final String DEFAULT_THREAD_LOCAL_RANDOM_FACTORY = "com.navercorp.pinpoint.bootstrap.util.jdk.JdkThreadLocalRandomFactory";

    private ThreadLocalRandomUtils() {
        throw new IllegalAccessError();
    }

    private static ThreadLocalRandomFactory createThreadLocalRandomFactory() {
        final JvmVersion jvmVersion = JvmUtils.getVersion();
        if (jvmVersion == JvmVersion.JAVA_6) {
            return new PinpointThreadLocalRandomFactory();
        } else if (jvmVersion.onOrAfter(JvmVersion.JAVA_7)) {
            try {
                ClassLoader classLoader = getClassLoader(ThreadLocalRandomUtils.class.getClassLoader());

                final Class<? extends ThreadLocalRandomFactory> threadLocalRandomFactoryClass =
                        (Class<? extends ThreadLocalRandomFactory>) Class.forName(DEFAULT_THREAD_LOCAL_RANDOM_FACTORY, true, classLoader);
                return threadLocalRandomFactoryClass.newInstance();
            } catch (ClassNotFoundException e) {
                logError(e);
            } catch (InstantiationException e) {
                logError(e);
            } catch (IllegalAccessException e) {
                logError(e);
            }
            return new PinpointThreadLocalRandomFactory();
        } else {
            throw new RuntimeException("Unsupported jvm version " + jvmVersion);
        }

    }

    private static ClassLoader getClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }

    private static void logError(Exception e) {
//        LOGGER.info("JdkThreadLocalRandomFactory not found."); TODO
    }

    public static Random current() {
        return THREAD_LOCAL_RANDOM_FACTORY.current();
    }
}
