package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.kostaTeam2.domain.solution.Solution;
import org.kostaTeam2.domain.solution.SolutionRepository;
import org.kostaTeam2.global.exception.DBException;

public class SolutionDao implements SolutionRepository {

	@Override
	public Long saveSolution(Connection con, Solution s) {
		final String sql = """
			INSERT INTO solution
			    (ws_problem_id, ws_member_id, status, time_ms, memory_mb, code)
			VALUES (?, ?, ?, ?, ?, ?)
			""";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setLong(1, s.getWorkspaceProblemId());
			ps.setLong(2, s.getWorkspaceMemberId());
			ps.setInt(3, s.isStatus() ? 1 : 0);
			ps.setObject(4, s.getTimeMs() == 0 ? null : s.getTimeMs(), Types.INTEGER);
			ps.setObject(5, s.getMemoryMb() == 0 ? null : s.getMemoryMb(), Types.INTEGER);
			ps.setString(6, s.getCode());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				}
				throw new DBException("saveSolution 생성 키를 가져오지 못했습니다.");
			}
		} catch (SQLException e) {
			throw new DBException("saveSolution DB 오류", e);
		}
	}
}
