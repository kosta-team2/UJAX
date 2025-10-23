package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
			ps.setObject(4, workspaceProblem.getScheduleAt());

			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("saveWorkspaceProblem DB 오류");
		}
	}

	@Override
	public List<WorkspaceProblem> findWorkspaceProblemsByWorkspaceId(Connection con, Long workspaceId, int page,
		int size) {
		List<WorkspaceProblem> workspaceProblems = new ArrayList<>();
		String sql = """
			select ws_problem_id, ws_id , problem_id, deadline
			from workspace_problem
			where ws_id = ? and is_deleted = 0
			order by deadline desc
			limit ? offset ?
			""";

		int offset = (page - 1) * size;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			ps.setInt(2, size);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					workspaceProblems.add(new WorkspaceProblem(
						rs.getLong(1),
						rs.getLong(2),
						rs.getLong(3),
						rs.getTimestamp(4).toLocalDateTime()
					));
				}
			}
		} catch (SQLException e) {
			throw new DBException("findProblemsByWorkspaceId DB 오류", e);
		}
		return workspaceProblems;
	}

	@Override
	public int findSuccessMembersByWorkspaceProblemId(Connection con, Long workspaceProblemId) {
		String sql = """
			SELECT COUNT(DISTINCT ws_member_id)
			FROM solution
			WHERE ws_problem_id = ? AND status = 1 AND is_deleted = 0
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, workspaceProblemId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			throw new DBException("findProblemsByWorkspaceId DB 오류", e);
		}
	}

	@Override
	public boolean findMemberStatusByWorkspaceProblemIdAndWorkspaceMemberId(Connection con, Long workspaceProblemId,
		Long workspaceMemberId) {
		String sql = """
			SELECT 1
			FROM solution
			WHERE ws_problem_id = ? AND ws_member_id = ? AND status = 1 AND is_deleted = 0
			LIMIT 1
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, workspaceProblemId);
			ps.setLong(2, workspaceMemberId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new DBException("findProblemsByWorkspaceId DB 오류", e);
		}
	}

	@Override
	public int countByWorkspaceId(Connection con, Long workspaceId) {
		String sql = """
			SELECT COUNT(*)
			FROM workspace_problem
			WHERE ws_id = ? AND is_deleted = 0
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			throw new DBException("findProblemsByWorkspaceId DB 오류", e);
		}
	}

}
