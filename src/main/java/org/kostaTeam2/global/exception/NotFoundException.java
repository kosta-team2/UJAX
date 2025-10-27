package org.kostaTeam2.global.exception;

import org.kostaTeam2.global.exception.common.AppException;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 찾을 수 없음 예외 (해당 페이지가 존재하지 않거나 id에 해당하는 것이 존재 하지 않는 경우 등 )
 * */
public class NotFoundException extends AppException {

	public NotFoundException(String message) {
		super(HttpServletResponse.SC_NOT_FOUND, message);
	}
}
