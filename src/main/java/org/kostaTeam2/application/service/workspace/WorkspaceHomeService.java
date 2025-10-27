package org.kostaTeam2.application.service.workspace;

import java.util.List;

import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.dto.response.WorkspaceProblemPageResponse;

public interface WorkspaceHomeService {
	List<Notice> getNoticeList(Long userId, Long workspaceId, int page, int size);

	List<WorkspaceProblemPageResponse> getWorkspaceProblemList(Long workspaceId, Long userId, int page, int size);

	// todo teamchart
}
