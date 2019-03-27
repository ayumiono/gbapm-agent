package com.gb.apm.collector;

/**
 * @author emeroad
 */
public interface FutureListener<T> {
    void onComplete(Future<T> future);
}
