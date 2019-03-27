package com.gb.apm.profiler.context;

import com.gb.apm.common.utils.NamedThreadLocal;

/**
 * @author emeroad
 */
public class ThreadLocalBinder<T> implements Binder<T> {

    private final ThreadLocal<T> threadLocal = new NamedThreadLocal<T>("ThreadLocalBinder");

    @Override
    public T get() {
        return threadLocal.get();
    }

    @Override
    public void set(T t) {
        threadLocal.set(t);
    }

    @Override
    public T remove() {
        final T value = threadLocal.get();
        threadLocal.remove();
        return value;
    }
}
