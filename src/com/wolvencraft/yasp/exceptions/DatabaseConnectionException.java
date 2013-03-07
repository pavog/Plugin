package com.wolvencraft.yasp.exceptions;

public class DatabaseConnectionException extends Exception {
	private static final long serialVersionUID = 1325478012737090439L;

	public DatabaseConnectionException() { 
		super();
	}

	public DatabaseConnectionException(String message) {
		super(message);
	}

	public DatabaseConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseConnectionException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		String message = this.getLocalizedMessage();
		return message != null ? message : "Failed to connect to the database";
	}
}
