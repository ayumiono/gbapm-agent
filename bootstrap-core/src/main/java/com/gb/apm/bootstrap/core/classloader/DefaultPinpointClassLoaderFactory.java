package com.gb.apm.bootstrap.core.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Taejin Koo
 */
class DefaultPinpointClassLoaderFactory implements InnerPinpointClassLoaderFactory {

    @Override
    public URLClassLoader createURLClassLoader(URL[] urls, ClassLoader parent) {
        return new PinpointURLClassLoader(urls, parent);
    }

    @Override
    public URLClassLoader createURLClassLoader(URL[] urls, ClassLoader parent, LibClass libClass) {
        return new PinpointURLClassLoader(urls, parent, libClass);
    }

}
