package org.kostaTeam2.dto.request;

import org.kostaTeam2.domain.workspace.WorkspaceLanguage;
import org.kostaTeam2.presentation.controller.dto.SessionUser;

import jakarta.servlet.http.HttpServletRequest;

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
		boolean isHintView = (req.getParameter("isHintView") != null);

		WorkspaceLanguage language = languageStr != null
			? WorkspaceLanguage.valueOf(languageStr.toUpperCase())
			: null;

		return new WorkspaceRequest(userId, null, workspaceName, language, isHintView);
	}

	public static WorkspaceRequest showDto(HttpServletRequest req, SessionUser user) {
		Long userId = user.memberId();
		Long workspaceId = Long.valueOf(req.getParameter("workspaceId"));

		return new WorkspaceRequest(userId, workspaceId, null, null, null);
	}

	public static WorkspaceRequest updateDto(HttpServletRequest req, SessionUser user) {
		Long userId = user.memberId();
		Long workspaceId = Long.valueOf(req.getParameter("workspaceId"));
		String workspaceName = req.getParameter("workspaceName");
		String languageStr = req.getParameter("workspaceLanguage");
        boolean hintView = Boolean.parseBoolean(req.getParameter("isHintView"));
// 또는 Optional.ofNullable(...).map(Boolean::parseBoolean).orElse(false)


        WorkspaceLanguage language = languageStr != null
			? WorkspaceLanguage.valueOf(languageStr.toUpperCase())
			: null;

		return new WorkspaceRequest(userId, workspaceId, workspaceName, language, hintView);
	}

	public static WorkspaceRequest deleteDto(HttpServletRequest req, SessionUser user) {
		Long userId = user.memberId();
		Long workspaceId = Long.valueOf(req.getParameter("workspaceId"));

		return new WorkspaceRequest(userId, workspaceId, null, null, null);
	}
}
