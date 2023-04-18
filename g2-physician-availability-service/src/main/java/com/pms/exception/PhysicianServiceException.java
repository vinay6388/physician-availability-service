package com.pms.exception;

public class PhysicianServiceException extends Exception {
	private static final long serialVersionUID = 526706541554094375L;

	public PhysicianServiceException(String message) {
		super(message);
	}

	public PhysicianServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PhysicianServiceException(Throwable cause) {
		super(cause);
	}

}
