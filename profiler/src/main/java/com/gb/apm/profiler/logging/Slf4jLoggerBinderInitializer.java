package com.gb.apm.profiler.logging;

import com.gb.apm.bootstrap.core.logging.PLoggerBinder;
import com.gb.apm.bootstrap.core.logging.PLoggerFactory;

/**
 * For unit test to register/unregister loggerBinder.
 *
 * @author emeroad
 */
public class Slf4jLoggerBinderInitializer {

    private static final PLoggerBinder loggerBinder = new Slf4jLoggerBinder();

    public static void beforeClass() {
        PLoggerFactory.initialize(loggerBinder);
    }

    public static void afterClass() {
        PLoggerFactory.unregister(loggerBinder);
    }
}
