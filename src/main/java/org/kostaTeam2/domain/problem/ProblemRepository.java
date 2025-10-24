package org.kostaTeam2.domain.problem;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ProblemRepository {
	Long findProblemIdByProblemNum(Connection con, int problemNum);

	Optional<Problem> findProblemByProblemId(Connection con, Long problemId);

	List<AlgorithmTag> findAlgorithmTagsByProblemId(Connection con, Long problemId);

	List<Sample> findSamplesByProblemId(Connection con, Long problemId);

	long saveProblem(Connection con, Problem problem);

	void saveSamplesByProblemId(Connection con, long problemId, List<Sample> samples);

	void linkAlgorithmsByProblemId(Connection con, long problemId, List<AlgorithmTag> tags);
}
