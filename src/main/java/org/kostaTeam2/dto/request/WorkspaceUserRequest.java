package org.kostaTeam2.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import org.kostaTeam2.presentation.controller.dto.SessionUser;

import java.io.Serializable;

public record WorkspaceUserRequest(Long wsId, Long whoAmI, Long wsMemberId) implements Serializable {
    public static WorkspaceUserRequest needAuthDto(HttpServletRequest req, SessionUser user) {
        long wsId = Long.parseLong(req.getParameter("wsId"));
        long whoAmI = user.memberId();
        long wsMemberId = Long.parseLong(req.getParameter("wsMemberId"));
        return new WorkspaceUserRequest(wsId, whoAmI, wsMemberId);
    }

    public static WorkspaceUserRequest exitDto(HttpServletRequest req, SessionUser user) {
        long wsId = Long.parseLong(req.getParameter("wsId"));
        long whoAmI = user.memberId();
        return new WorkspaceUserRequest(wsId, whoAmI, null);
    }
}
