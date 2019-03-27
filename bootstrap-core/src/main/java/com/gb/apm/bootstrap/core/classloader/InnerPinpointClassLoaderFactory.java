package com.gb.apm.bootstrap.core.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Taejin Koo
 */
interface InnerPinpointClassLoaderFactory {

    URLClassLoader createURLClassLoader(URL[] urls, ClassLoader parent);

    URLClassLoader createURLClassLoader(URL[] urls, ClassLoader parent, LibClass libClass);

}
