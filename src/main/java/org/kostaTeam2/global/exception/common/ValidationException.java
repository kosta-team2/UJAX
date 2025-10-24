package org.kostaTeam2.global.exception.common;

public class ValidationException extends RuntimeException {
	private final String returnTo;

	public ValidationException(String message, String returnTo) {
		super(message);
		this.returnTo = returnTo;
	}

	public String getReturnTo() {
		return returnTo;
	}
}
