package org.kostaTeam2.global.exception.common;

public class AppException extends RuntimeException {
	private int status;

	public AppException(int status, String message) {
		super(message);
		this.status = status;
	}

	public AppException(int status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
