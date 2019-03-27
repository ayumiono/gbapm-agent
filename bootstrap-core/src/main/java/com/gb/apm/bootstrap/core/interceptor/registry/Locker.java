package com.gb.apm.bootstrap.core.interceptor.registry;


/**
 * @author emeroad
 */
public interface Locker {

    boolean lock(Object lock);

    boolean unlock(Object lock);

    Object getLock();
}
