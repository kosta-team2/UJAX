package org.kostaTeam2.dto.request;

import org.kostaTeam2.global.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;

public record SignupUserRequest(String email, String password, String nickname) {
    public static SignupUserRequest from(HttpServletRequest req) {
        return new SignupUserRequest(
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("nickname")
        );
    }
}