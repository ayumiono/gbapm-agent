package com.gb.apm.bootstrap.core.logging;


import com.gb.apm.common.utils.logger.CommonLogger;
import com.gb.apm.common.utils.logger.StdoutCommonLoggerFactory;

/**
 * @author emeroad
 */
public final class PLoggerFactory {

    private static PLoggerBinder loggerBinder;

    public static void initialize(PLoggerBinder loggerBinder) {
        if (PLoggerFactory.loggerBinder == null) {
            PLoggerFactory.loggerBinder = loggerBinder;
        } else {
            final CommonLogger logger = StdoutCommonLoggerFactory.INSTANCE.getLogger(PLoggerFactory.class.getName());
            logger.warn("loggerBinder is not null");
        }
    }

    public static void unregister(PLoggerBinder loggerBinder) {
        // Limited to remove only the ones already registered
        // when writing a test case, logger register/unregister logic must be located in beforeClass and afterClass
        if (loggerBinder == PLoggerFactory.loggerBinder) {
            PLoggerFactory.loggerBinder = null;
        }
    }

    public static PLogger getLogger(String name) {
        if (loggerBinder == null) {
            // this prevents null exception: need to return Dummy until a Binder is assigned
            return DummyPLogger.INSTANCE;
        }
        return loggerBinder.getLogger(name);
    }

    public static PLogger getLogger(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException("class must not be null");
        }
        return getLogger(clazz.getName());
    }
}
