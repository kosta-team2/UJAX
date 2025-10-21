package org.kostaTeam2.domain.problem;

import java.sql.Connection;
import java.util.List;

public interface ProblemRepository {
	Long findProblemIdByProblemNum(Connection con, int problemNum);

	long saveProblem(Connection con, Problem problem);

	void saveSamplesByProblemId(Connection con, long problemId, List<Sample> samples);

	void linkAlgorithmsByProblemId(Connection con, long problemId, List<AlgorithmTag> tags);
}
