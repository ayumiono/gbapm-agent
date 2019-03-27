package com.gb.apm.profiler.instrument.classloading;

import java.io.InputStream;

/**
 * @author Jongho Moon
 * @author jaehong.kim
 */
public interface ClassInjector  {

    <T> Class<? extends T> injectClass(ClassLoader classLoader, String className);

    InputStream getResourceAsStream(ClassLoader classLoader, String classPath);

}