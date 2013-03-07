package com.wolvencraft.yasp.exceptions;

public class MetricsConnectionException extends Exception {
	private static final long serialVersionUID = 1325478012737090439L;

	public MetricsConnectionException() { 
		super();
	}

	public MetricsConnectionException(String message) {
		super(message);
	}

	public MetricsConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetricsConnectionException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		String message = this.getLocalizedMessage();
		return message != null ? message : "Failed to connect to Plugin Metrics";
	}
}
