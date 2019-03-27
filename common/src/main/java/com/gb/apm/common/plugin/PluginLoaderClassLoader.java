package com.gb.apm.common.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @author emeroad
 */
public class PluginLoaderClassLoader extends URLClassLoader {
//      for debug
//    private final String loaderName

    public PluginLoaderClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public PluginLoaderClassLoader(URL[] urls) {
        super(urls);
    }

    public PluginLoaderClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // for debug
        return super.loadClass(name, resolve);
    }
}
