package org.kostaTeam2.domain.workspace;

import java.sql.Connection;

import org.kostaTeam2.domain.problem.Problem;

public interface WorkspaceProblemRepository {
	Integer saveWorkspaceProblem(Connection con, WorkspaceProblem workspaceProblem);
}
