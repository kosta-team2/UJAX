package org.kostaTeam2.global.exception;

import org.kostaTeam2.global.exception.common.AppException;

import jakarta.servlet.http.HttpServletResponse;
/**
 * 연결 실패 (HTTP 500)
 * */
public class DBException extends AppException {
	public DBException(String message, Throwable cause) {
		super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message ,cause);
	}
}
