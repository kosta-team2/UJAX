package org.kostaTeam2.dto.request;

import org.kostaTeam2.global.exception.BadRequestException;

import jakarta.servlet.http.HttpServletRequest;

public record LoginUserRequest(String email, String password) {

	public static LoginUserRequest from(HttpServletRequest req) {
		return new LoginUserRequest(
			req.getParameter("email"),
			req.getParameter("password")
		);
	}
}
