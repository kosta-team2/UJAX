package org.kostaTeam2.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import org.kostaTeam2.domain.workspace.WorkspaceLanguage;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;

public record WorkspaceCreateRequest(
        Long userId,
        String workspaceName,
        WorkspaceLanguage workspaceLanguage,
        Boolean isHintView
) {
    public WorkspaceCreateRequest {
        if (workspaceName() == null) {
            throw new BadRequestException("workspaceName cannot be null");
        }
        if (workspaceLanguage == null) {
            throw new BadRequestException("workspaceLanguage cannot be null");
        }
    }


    public static WorkspaceCreateRequest from(HttpServletRequest req, SessionUser user) {
        Long userId = user.memberId();
        String workspaceName = req.getParameter("workspaceName");
        String languageStr = req.getParameter("workspaceLanguage");
        String hintViewStr = req.getParameter("isHintView");

        WorkspaceLanguage language = languageStr != null
                ? WorkspaceLanguage.valueOf(languageStr.toUpperCase())
                : null;
        Boolean isHintView = Boolean.parseBoolean(hintViewStr);

        return new WorkspaceCreateRequest(userId, workspaceName, language, isHintView);
    }
}
