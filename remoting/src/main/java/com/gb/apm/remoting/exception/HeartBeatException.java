package com.gb.apm.remoting.exception;

public class HeartBeatException extends Exception {
    private static final long serialVersionUID = 5975020272601250368L;
    private final int responseCode;
    private final String errorMessage;

    public HeartBeatException(int responseCode, String errorMessage) {
    	super("code:"+responseCode + " errorMessage:"+errorMessage);
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
