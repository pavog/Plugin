package com.wolvencraft.yasp.exceptions;

public class RuntimeSQLException extends RuntimeException {
	private static final long serialVersionUID = 5224696788505678598L;

	public RuntimeSQLException() {
		super();
	}

	public RuntimeSQLException(String message) {
		super(message);
	}

	public RuntimeSQLException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeSQLException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		String message = this.getLocalizedMessage();
		return message != null ? message : "RuntimeSQLException thrown";
	}
}
