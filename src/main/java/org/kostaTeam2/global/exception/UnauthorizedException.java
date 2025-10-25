package org.kostaTeam2.global.exception;

import org.kostaTeam2.global.exception.common.AppException;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 로그인(세션)이 없는 사람이 요청시 거부 todo (Auth 이외의 요청의 filter에서 사용)
 */
public class UnauthorizedException extends AppException {
	public UnauthorizedException(String message) {
		super(HttpServletResponse.SC_UNAUTHORIZED, message);
	}
}
