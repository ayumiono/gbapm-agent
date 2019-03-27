package com.gb.apm.bootstrap.core.instrument;

// TODO Separate this class when hierarchical layers are needed
/**
 * @author emeroad
 */
public class InstrumentException extends Exception {

    private static final long serialVersionUID = 7594176009977030312L;

    public InstrumentException() {
    }

    public InstrumentException(String message) {
        super(message);
    }

    public InstrumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstrumentException(Throwable cause) {
        super(cause);
    }
}
