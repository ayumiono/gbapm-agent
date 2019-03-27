package com.gb.apm.remoting.exception;

public class DBChangeEventException extends Exception {

	private static final long serialVersionUID = 2646672049929758675L;
	private final int responseCode;
    private final String errorMessage;

    public DBChangeEventException(int responseCode, String errorMessage) {
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
