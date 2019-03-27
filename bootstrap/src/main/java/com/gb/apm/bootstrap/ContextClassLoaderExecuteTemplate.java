package com.gb.apm.bootstrap;

import java.util.concurrent.Callable;

public class ContextClassLoaderExecuteTemplate<V> {
    private final ClassLoader classLoader;

    public ContextClassLoaderExecuteTemplate(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new NullPointerException("classLoader must not be null");
        }
        this.classLoader = classLoader;
    }

    public V execute(Callable<V> callable) throws BootStrapException {
        try {
            final Thread currentThread = Thread.currentThread();
            final ClassLoader before = currentThread.getContextClassLoader();
            currentThread.setContextClassLoader(ContextClassLoaderExecuteTemplate.this.classLoader);
            try {
                return callable.call();
            } finally {
                currentThread.setContextClassLoader(before);
            }
        } catch (BootStrapException ex){
            throw ex;
        } catch (Exception ex) {
            throw new BootStrapException("execute fail. Error:" + ex.getMessage(), ex);
        }
    }
}
