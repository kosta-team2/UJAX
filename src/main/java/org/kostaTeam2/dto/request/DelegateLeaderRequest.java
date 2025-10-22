package org.kostaTeam2.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import org.kostaTeam2.presentation.controller.dto.SessionUser;

public record DelegateLeaderRequest(Long wsId, Long currentLeaderId, Long newLeaderId) {
    public static DelegateLeaderRequest delegateDto(HttpServletRequest req, SessionUser user) {
        long wsId = Long.parseLong(req.getParameter("wsId"));
        long currentLeaderId = user.memberId();
        long newLeaderId = Long.parseLong(req.getParameter("wsMemberId"));
        return new DelegateLeaderRequest(wsId, newLeaderId, currentLeaderId);
    }
}
