package com.gb.apm.profiler.instrument.classloading;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.PinpointException;
import com.gb.apm.profiler.plugin.PluginConfig;

/**
 * @author Woonduk Kang(emeroad)
 * @author jaehong.kim
 */
public class URLClassLoaderHandler implements ClassInjector {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private static final Method ADD_URL;

    static {
        try {
            ADD_URL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ADD_URL.setAccessible(true);
        } catch (Exception e) {
            throw new PinpointException("Cannot access URLClassLoader.addURL(URL)", e);
        }
    }

    private final URL pluginURL;
    private final String pluginURLString;

    public URLClassLoaderHandler(PluginConfig pluginConfig) {
        if (pluginConfig == null) {
            throw new NullPointerException("pluginConfig must not be null");
        }
        this.pluginURL = pluginConfig.getPluginJar();
        this.pluginURLString = pluginURL.toExternalForm();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> injectClass(ClassLoader classLoader, String className) {
        try {
            if (classLoader instanceof URLClassLoader) {
                final URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
                addPluginURLIfAbsent(urlClassLoader);
                return (Class<T>) urlClassLoader.loadClass(className);
            }
        } catch (Exception e) {
            logger.warn("Failed to load plugin class {} with classLoader {}", className, classLoader, e);
            throw new PinpointException("Failed to load plugin class " + className + " with classLoader " + classLoader, e);
        }
        throw new PinpointException("invalid ClassLoader");
    }

    @Override
    public InputStream getResourceAsStream(ClassLoader targetClassLoader, String classPath) {
        try {
            if (targetClassLoader instanceof URLClassLoader) {
                final URLClassLoader urlClassLoader = (URLClassLoader) targetClassLoader;
                addPluginURLIfAbsent(urlClassLoader);
                return targetClassLoader.getResourceAsStream(classPath);
            }
        } catch (Exception e) {
            logger.warn("Failed to load plugin resource as stream {} with classLoader {}", classPath, targetClassLoader, e);
            return null;
        }
        return null;
    }

    private void addPluginURLIfAbsent(URLClassLoader classLoader) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        final URL[] urls = classLoader.getURLs();
        if (urls != null) {
            final boolean hasPluginJar = hasPluginJar(urls);
            if (!hasPluginJar) {
                if (isDebug) {
                    logger.debug("add Jar:{}", pluginURLString);
                }
                ADD_URL.invoke(classLoader, pluginURL);
            }
        }
    }

    private boolean hasPluginJar(URL[] urls) {
        for (URL url : urls) {
            // if (url.equals(pluginJarURL)) { fix very slow
            // http://michaelscharf.blogspot.com/2006/11/javaneturlequals-and-hashcode-make.html
            final String externalForm = url.toExternalForm();
            if (pluginURLString.equals(externalForm)) {
                return true;
            }
        }
        return false;
    }
}
