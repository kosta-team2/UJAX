package org.kostaTeam2.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import org.kostaTeam2.domain.workspace.WorkspaceLanguage;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;

public record WorkspaceRequest(
        Long userId,
        Long workspaceId,
        String workspaceName,
        WorkspaceLanguage workspaceLanguage,
        Boolean isHintView
) {

    public static WorkspaceRequest createDto(HttpServletRequest req, SessionUser user) {
        Long userId = user.memberId();
        String workspaceName = req.getParameter("workspaceName");
        String languageStr = req.getParameter("workspaceLanguage");
        String hintViewStr = req.getParameter("isHintView");

        WorkspaceLanguage language = languageStr != null
                ? WorkspaceLanguage.valueOf(languageStr.toUpperCase())
                : null;
        Boolean isHintView = Boolean.parseBoolean(hintViewStr);

        return new WorkspaceRequest(userId, null, workspaceName, language, isHintView);
    }

    public static WorkspaceRequest deleteDto(HttpServletRequest req, SessionUser user) {
        Long userId = user.memberId();
        Long workspaceId = Long.valueOf(req.getParameter("workspaceId"));

        return new WorkspaceRequest(userId, workspaceId, null, null, null);
    }
}
