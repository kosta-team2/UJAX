package org.kostaTeam2.dto.request;

import org.kostaTeam2.global.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;

public record SignupUserRequest(String email, String password, String nickname) {
    public SignupUserRequest {
        if (email == null || email.isBlank())
            throw new BadRequestException("이메일은 필수입니다.");
        if (password == null || password.isBlank())
            throw new BadRequestException("비밀번호는 필수입니다.");
        if (nickname == null || nickname.isBlank())
            throw new BadRequestException("닉네임은 필수입니다.");
    }

    public static SignupUserRequest from(HttpServletRequest req) {
        return new SignupUserRequest(
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("nickname")
        );
    }
}