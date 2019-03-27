package com.gb.apm.profiler.instrument.classpool;

import javassist.ClassPath;
import javassist.LoaderClassPath;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author emeroad
 */
public class SingleClassPool implements MultipleClassPool {

    private static final Object EXIST = new Object();

    private final NamedClassPool classPool;


    private final Map<ClassLoader, Object> checker = new WeakHashMap<ClassLoader, Object>();


    public SingleClassPool() {
        this.classPool = new NamedClassPool("singlePool");
        this.classPool.appendSystemPath();
    }

    @Override
    public NamedClassPool getClassPool(ClassLoader classLoader) {

        synchronized (classPool) {
            final Object hit = this.checker.get(classLoader);
            if (hit != null) {
                return classPool;
            }

            this.checker.put(classLoader, EXIST);

            final ClassPath classPath = new LoaderClassPath(classLoader);
            this.classPool.appendClassPath(classPath);
            return classPool;
        }

    }




}
