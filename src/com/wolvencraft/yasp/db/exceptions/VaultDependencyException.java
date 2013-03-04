package com.wolvencraft.yasp.db.exceptions;

public class VaultDependencyException extends Exception {
	private static final long serialVersionUID = 4959781479993255261L;

	public VaultDependencyException() { 
		super();
	}

	public VaultDependencyException(String message) {
		super(message);
	}

	public VaultDependencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public VaultDependencyException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		String message = this.getLocalizedMessage();
		return message != null ? message : "Error occurred while setting up Vault hooks";
	}
}
