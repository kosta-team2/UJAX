package org.kostaTeam2.application.service.workspace;

import java.util.List;

import org.kostaTeam2.domain.workspace.WorkspaceProblem;
import org.kostaTeam2.dto.response.ProblemInfoResponse;
import org.kostaTeam2.dto.response.WorkspaceProblemPageResponse;

public interface WorkspaceProblemService {
	Integer createWorkSpaceProblem(WorkspaceProblem workspaceProblem, int setProblemNum);

	List<WorkspaceProblemPageResponse> getWorkspaceProblemList(Long workspaceId, Long workspaceMemberId, int page, int size);

	ProblemInfoResponse getProblemDetail(Long problemId);
}
