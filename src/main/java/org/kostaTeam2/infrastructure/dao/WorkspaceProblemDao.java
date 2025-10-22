package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.kostaTeam2.domain.workspace.WorkspaceProblem;
import org.kostaTeam2.domain.workspace.WorkspaceProblemRepository;
import org.kostaTeam2.global.exception.DBException;

public class WorkspaceProblemDao implements WorkspaceProblemRepository {

	@Override
	public Integer saveWorkspaceProblem(Connection con, WorkspaceProblem workspaceProblem) {
		String sql = "INSERT INTO workspace_problem(ws_id, problem_id, deadline, scheduled_at) VALUES (?, ?, ?, ?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, workspaceProblem.getWsId());
			ps.setLong(2, workspaceProblem.getProblemId());
			ps.setTimestamp(3, Timestamp.valueOf(workspaceProblem.getDeadLine()));
			ps.setTimestamp(4, Timestamp.valueOf(workspaceProblem.getScheduleAt()));

			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("saveWorkspaceProblem DB 오류");
		}
	}
}
