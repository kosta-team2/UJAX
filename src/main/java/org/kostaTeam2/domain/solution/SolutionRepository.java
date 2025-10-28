package org.kostaTeam2.domain.solution;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface SolutionRepository {
	Long saveSolution(Connection con, Solution solution);

	List<SubmitterVo> findSubmitters(Connection con, long wsProblemId, int offset, int limit);

	int countMembersForWsProblem(Connection con, long wsProblemId);

	Optional<Solution> findSolutionById(Connection con, long solutionId);

	boolean existsLike(Connection con, long solutionId, long wsMemberId);

	void addLike(Connection con, long solutionId, long wsMemberId);

	void removeLike(Connection con, long solutionId, long wsMemberId);

	int countLikes(Connection con, long solutionId);

	List<CommentVo> findComments(Connection con, long solutionId, int offset, int limit);

	int countComments(Connection con, long solutionId);

	long insertComment(Connection con, long solutionId, long wsMemberId, String content);

	int deleteComment(Connection con, long commentId, long wsMemberId);

	boolean isLeaderOfWsProblem(Connection con, long wsProblemId, long wsMemberId);

	int deleteWorkspaceProblem(Connection con, long wsProblemId);

	int deleteSolutionsByWsProblem(Connection con, long wsProblemId);
}
