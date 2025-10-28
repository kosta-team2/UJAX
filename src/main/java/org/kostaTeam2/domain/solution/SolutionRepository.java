package org.kostaTeam2.domain.solution;

import java.sql.Connection;

public interface SolutionRepository {
	Long saveSolution(Connection con, Solution solution);
}
