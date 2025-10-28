package org.kostaTeam2.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.kostaTeam2.presentation.controller.dto.SessionUser;

public record AcceptInviteRequest(String email, Long workspaceId, Long memberId) {
    public static AcceptInviteRequest from(HttpServletRequest req) {
        HttpSession session = req.getSession();
        SessionUser user = (SessionUser) session.getAttribute("SessionUser");

        Long memberId = user.memberId();
        Long wsId = Long.valueOf(req.getParameter("workspaceId"));
        String email = req.getParameter("email");
        return new AcceptInviteRequest(email, wsId, memberId);
    }
}
