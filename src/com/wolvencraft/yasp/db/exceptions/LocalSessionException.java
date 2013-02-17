package com.wolvencraft.yasp.db.exceptions;

public class LocalSessionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6255006170131809940L;
	
	public LocalSessionException() {
		super();
	}

	public LocalSessionException(String message) {
		super(message);
	}

	public LocalSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public LocalSessionException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		String message = this.getLocalizedMessage();
		return message != null ? message : "RuntimeSQLException thrown";
	}
	
}
