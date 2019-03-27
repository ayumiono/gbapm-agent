package com.gb.apm.profiler.context.scope;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface Pool<K, V> {

    V get(K key);

}
