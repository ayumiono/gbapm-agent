package com.gb.apm.common.hbase;

/**
 * @author emeroad
 */
public interface ValueMapper<T> {
    byte[] mapValue(T value);
}
