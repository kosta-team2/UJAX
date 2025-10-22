package org.kostaTeam2.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.kostaTeam2.presentation.controller.dto.SessionUser;

public record UpdateUserRequest(long memberId, String newNickname, String newPassword, String password) {
    public static UpdateUserRequest from(HttpServletRequest req) {
        HttpSession session = req.getSession();
        SessionUser user = (SessionUser) session.getAttribute("SessionUser");

        return new UpdateUserRequest(
                user.memberId(),
                req.getParameter("newNickname"),
                req.getParameter("newPassword"),
                req.getParameter("password")
        );
    }
}
