package org.kostaTeam2.global.exception;

import org.kostaTeam2.global.exception.common.AppException;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 결제 조건 부합으로 거부
 */
public class InsufficientBalanceException extends AppException {
	public InsufficientBalanceException(String message) {
		super(HttpServletResponse.SC_CONFLICT, message);
	}
}
