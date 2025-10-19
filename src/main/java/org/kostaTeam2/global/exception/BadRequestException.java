package org.kostaTeam2.global.exception;

import org.kostaTeam2.global.exception.common.AppException;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 잘못된 요청 예외 (HTTP 400)
 * */
public class BadRequestException extends AppException {

	public BadRequestException(String message) {
		super(HttpServletResponse.SC_BAD_REQUEST, message);
	}
}
