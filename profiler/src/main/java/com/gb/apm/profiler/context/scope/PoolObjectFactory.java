package com.gb.apm.profiler.context.scope;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface PoolObjectFactory<K, V> {
    V create(K key);
}
