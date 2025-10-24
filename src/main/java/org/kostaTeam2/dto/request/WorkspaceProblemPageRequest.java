package org.kostaTeam2.dto.request;

import jakarta.servlet.http.HttpServletRequest;

public record WorkspaceProblemPageRequest(
	Long workspaceId,
	Long workspaceMemberId,
	int page,
	int size
) {
	public static WorkspaceProblemPageRequest from(HttpServletRequest req) {
		Long workspaceId = Long.valueOf(req.getParameter("workspaceId"));
		Long workspaceMemberId = Long.valueOf(req.getParameter("workspaceMemberId"));
		int page = Integer.parseInt((req.getParameter("page")));
		int size = Integer.parseInt((req.getParameter("size")));

		return new WorkspaceProblemPageRequest(
			workspaceId,
			workspaceMemberId,
			page,
			size
		);
	}
}
