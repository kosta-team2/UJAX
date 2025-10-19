package org.kostaTeam2.dto.request;

import org.kostaTeam2.global.exception.BadRequestException;

import jakarta.servlet.http.HttpServletRequest;

public record LoginUserRequest(String email, String password) {
	public LoginUserRequest {
		if (email == null || email.isBlank())
			throw new BadRequestException("email은 필수입니다.");
		if (password == null || password.isBlank())
			throw new BadRequestException("password는 필수입니다.");
	}

	public static LoginUserRequest from(HttpServletRequest req) {
		return new LoginUserRequest(
			req.getParameter("email"),
			req.getParameter("password")
		);
	}
}
