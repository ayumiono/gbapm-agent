package com.gb.apm.bootstrap;

import com.gb.apm.bootstrap.core.PinpointException;

public class BootStrapException extends PinpointException {

	private static final long serialVersionUID = -1247846457351576365L;

	public BootStrapException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public BootStrapException(String message) {
		super(message);
	}
}
