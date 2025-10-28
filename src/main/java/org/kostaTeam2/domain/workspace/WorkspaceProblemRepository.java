package org.kostaTeam2.domain.workspace;

import java.sql.Connection;
import java.util.List;

import org.kostaTeam2.domain.problem.Problem;

public interface WorkspaceProblemRepository {
	Integer saveWorkspaceProblem(Connection con, WorkspaceProblem workspaceProblem);

	List<WorkspaceProblem> findWorkspaceProblemsByWorkspaceId(Connection con, Long workspaceId, int page, int size);

	int findSuccessMembersByWorkspaceProblemId(Connection con, Long workspaceProblemId);

	boolean findMemberStatusByWorkspaceProblemIdAndWorkspaceMemberId(Connection con, Long workspaceProblemId, Long workspaceMemberId);

	int countByWorkspaceId(Connection con, Long workspaceId);

	Long findWorkspaceProblemIdByWsIdANDProblemId(Connection con, Long workspaceId, Long problemId);
}
