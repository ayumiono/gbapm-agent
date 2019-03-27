package com.gb.apm.common.utils.logger;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

/**
 * @author Woonduk Kang(emeroad)
 */
public class StdoutCommonLogger implements CommonLogger {

//    private final String loggerName;
    private final String messagePattern;
    private final PrintStream out;
    private final PrintStream err;


    public StdoutCommonLogger(String loggerName) {
        this(loggerName, System.out, System.err);
    }

    // for test
    StdoutCommonLogger(String loggerName, PrintStream out, PrintStream err) {
        if (loggerName == null) {
            throw new NullPointerException("loggerName must not be null");
        }
        if (out == null) {
            throw new NullPointerException("out must not be null");
        }
        if (err == null) {
            throw new NullPointerException("err must not be null");
        }

//        this.loggerName = loggerName;
        this.messagePattern = "{0,date,yyyy-MM-dd HH:mm:ss} [{1}](" + loggerName + ") {2}{3}";
        this.out = out;
        this.err = err;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String msg) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void info(String msg) {
        String message = format("INFO ", msg, "");
        this.out.println(message);
    }

    private String format(String logLevel, String msg, String exceptionMessage) {
        exceptionMessage = defaultString(exceptionMessage, "");

        MessageFormat messageFormat = new MessageFormat(messagePattern);
        
        final long date = System.currentTimeMillis();
        Object[] parameter = {date, logLevel, msg, exceptionMessage};
        return messageFormat.format(parameter);
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String msg) {
        warn(msg, null);
    }

    @Override
    public void warn(String msg, Throwable throwable) {
        String exceptionMessage = toString(throwable);
        String message = format("WARN ", msg, exceptionMessage);
        this.err.println(message);
    }

    private String toString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        throwable.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    private String defaultString(String exceptionMessage, String defaultValue) {
        if (exceptionMessage == null) {
            return defaultValue;
        }
        return exceptionMessage;
    }
}
