package com.gb.apm.common.utils.logger;

/**
 * @author Woonduk Kang(emeroad)
 */
public class StdoutCommonLoggerFactory implements CommonLoggerFactory {

    public static final CommonLoggerFactory INSTANCE = new StdoutCommonLoggerFactory();

    static {
        setup();
    }

    private static void setup() {
        // TODO setup stdout logger
    }

    public CommonLogger getLogger(String loggerName) {
        return new StdoutCommonLogger(loggerName);
    }
}
