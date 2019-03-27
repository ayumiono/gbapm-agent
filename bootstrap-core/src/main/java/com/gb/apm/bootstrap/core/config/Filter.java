package com.gb.apm.bootstrap.core.config;

/**
 * @author emeroad
 */
public interface Filter<T> {
    boolean FILTERED = true;
    boolean NOT_FILTERED = !FILTERED;

    boolean filter(T value);
}
