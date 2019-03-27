package com.gb.apm.bootstrap.core.logging;

/**
 * @author emeroad
 */
public interface PLoggerBinder {
    PLogger getLogger(String name);

    void shutdown();
}
