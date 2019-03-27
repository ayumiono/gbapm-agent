package com.gb.apm.server;

public class GBMongodbException extends Exception {
	private static final long serialVersionUID = 9058987505334242128L;
	
	public GBMongodbException(String msg) {
		super(msg);
	}
	
	public GBMongodbException(String msg,Throwable cause) {
		super(msg, cause);
	}
}
