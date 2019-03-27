package com.gb.apm.common.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * TODO Loading all plugins with a single class loader could cause class collisions.
 *      Also, with current implementation, plugins can use dependencies by putting them in the plugin directory too.
 *      But it can lead to dependency collision between plugins because they are loaded by a single class loader.
 *      
 *      How can we prevent this?
 *      A ClassLoader per plugin could do it but then we have to create "N of target class loader" x "N of plugin" class loaders.
 *      It seems too much. For now, Just leave it as it is. 
 * 
 * 
 * @author Jongho Moon <jongho.moon@navercorp.com>
 * @author emeroad
 *
 * @param <T>
 */
public class PluginLoader {
    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();

    public static <T> List<T> load(Class<T> serviceType, URL[] urls) {
//        URLClassLoader classLoader = createPluginClassLoader(urls, ClassLoader.getSystemClassLoader());//FIXME
        URLClassLoader classLoader = createPluginClassLoader(urls, null);
        return load(serviceType, classLoader);
    }

    private static PluginLoaderClassLoader createPluginClassLoader(final URL[] urls, final ClassLoader parent) {
        if (SECURITY_MANAGER != null) {
            return AccessController.doPrivileged(new PrivilegedAction<PluginLoaderClassLoader>() {
                public PluginLoaderClassLoader run() {
                    return new PluginLoaderClassLoader(urls, parent);
                }
            });
        } else {
            return new PluginLoaderClassLoader(urls, parent);
        }
    }
    
    public static <T> List<T> load(Class<T> serviceType, ClassLoader classLoader) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceType, classLoader);
        
        List<T> plugins = new ArrayList<T>();
        for (T plugin : serviceLoader) {
            plugins.add(serviceType.cast(plugin));
        }

        return plugins;
    }
}
