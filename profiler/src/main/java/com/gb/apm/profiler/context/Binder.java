package com.gb.apm.profiler.context;

/**
 * @author emeroad
 */
public interface Binder<T> {

    T get();

    void set(T value);

    T remove();
}
