package org.kostaTeam2.global.exception;

import org.kostaTeam2.global.exception.common.AppException;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 상품 판매시 품절된 상품일 경우
 */
public class SoldOutException extends AppException {
	public SoldOutException(String message) {
		super(HttpServletResponse.SC_CONFLICT, message);
	}
}
