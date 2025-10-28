package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kostaTeam2.domain.solution.CommentVo;
import org.kostaTeam2.domain.solution.Solution;
import org.kostaTeam2.domain.solution.SolutionRepository;
import org.kostaTeam2.domain.solution.SubmitterVo;
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

	@Override
	public List<SubmitterVo> findSubmitters(Connection con, long wsProblemId, int offset, int limit) {
		final String sql = """
			SELECT wm.ws_member_id,
			       wm.nickname,
			       last.solution_id AS latest_solution_id
			FROM workspace_member wm
			JOIN workspace_problem wp ON wp.ws_id = wm.ws_id
			LEFT JOIN (
			    SELECT s.ws_member_id, MAX(s.solution_id) AS solution_id
			    FROM solution s
			    WHERE s.ws_problem_id = ?
			    GROUP BY s.ws_member_id
			) last ON last.ws_member_id = wm.ws_member_id
			WHERE wp.ws_problem_id = ?
			ORDER BY wm.ws_member_id
			LIMIT ? OFFSET ?
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, wsProblemId);
			ps.setLong(2, wsProblemId);
			ps.setInt(3, limit);
			ps.setInt(4, offset);
			try (ResultSet rs = ps.executeQuery()) {
				List<SubmitterVo> list = new ArrayList<>();
				while (rs.next()) {
					long wsMemberId = rs.getLong("ws_member_id");
					String nickname = rs.getString("nickname");
					Long latestSid = (Long)rs.getObject("latest_solution_id");
					list.add(new SubmitterVo(wsMemberId, nickname, latestSid));
				}
				return list;
			}
		} catch (SQLException e) {
			throw new DBException("findSubmitters DB 오류", e);
		}
	}

	@Override
	public int countMembersForWsProblem(Connection con, long wsProblemId) {
		final String sql = """
			SELECT COUNT(*) AS cnt
			FROM workspace_member wm
			JOIN workspace_problem wp ON wp.ws_id = wm.ws_id
			WHERE wp.ws_problem_id = ?
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, wsProblemId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt("cnt");
				return 0;
			}
		} catch (SQLException e) {
			throw new DBException("countMembersForWsProblem DB 오류", e);
		}
	}

	@Override
	public Optional<Solution> findSolutionById(Connection con, long solutionId) {
		final String sql = """
			SELECT solution_id, ws_problem_id, ws_member_id, status, time_ms, memory_mb, code
			FROM solution
			WHERE solution_id = ?
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, solutionId);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return Optional.empty();
				Solution s = new Solution(
					rs.getLong("solution_id"),
					rs.getLong("ws_problem_id"),
					rs.getLong("ws_member_id"),
					rs.getInt("status") == 1,
					toIntOrZero(rs.getObject("time_ms")),
					toIntOrZero(rs.getObject("memory_mb")),
					rs.getString("code")
				);
				return Optional.of(s);
			}
		} catch (SQLException e) {
			throw new DBException("findSolutionById DB 오류", e);
		}
	}

	@Override
	public boolean existsLike(Connection con, long solutionId, long wsMemberId) {
		final String sql = "SELECT 1 FROM likes WHERE solution_id=? AND ws_member_id=? LIMIT 1";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, solutionId);
			ps.setLong(2, wsMemberId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new DBException("existsLike DB 오류", e);
		}
	}

	@Override
	public void addLike(Connection con, long solutionId, long wsMemberId) {
		final String sql = "INSERT INTO likes (solution_id, ws_member_id) VALUES (?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, solutionId);
			ps.setLong(2, wsMemberId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("addLike DB 오류", e);
		}
	}

	@Override
	public void removeLike(Connection con, long solutionId, long wsMemberId) {
		final String sql = "DELETE FROM likes WHERE solution_id=? AND ws_member_id=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, solutionId);
			ps.setLong(2, wsMemberId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("removeLike DB 오류", e);
		}
	}

	@Override
	public int countLikes(Connection con, long solutionId) {
		final String sql = "SELECT COUNT(*) AS cnt FROM likes WHERE solution_id=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, solutionId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt("cnt") : 0;
			}
		} catch (SQLException e) {
			throw new DBException("countLikes DB 오류", e);
		}
	}

	@Override
	public List<CommentVo> findComments(Connection con, long solutionId, int offset, int limit) {
		final String sql = """
			SELECT c.comment_id, c.ws_member_id, wm.nickname, c.comment_content, c.created_at
			FROM comment c
			JOIN workspace_member wm ON wm.ws_member_id = c.ws_member_id
			WHERE c.solution_id = ?
			ORDER BY c.created_at DESC
			LIMIT ? OFFSET ?
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, solutionId);
			ps.setInt(2, limit);
			ps.setInt(3, offset);
			try (ResultSet rs = ps.executeQuery()) {
				List<CommentVo> list = new ArrayList<>();
				while (rs.next()) {
					list.add(new CommentVo(
						rs.getLong("comment_id"),
						rs.getLong("ws_member_id"),
						rs.getString("nickname"),
						rs.getString("comment_content"),
						rs.getTimestamp("created_at").toLocalDateTime()
					));
				}
				return list;
			}
		} catch (SQLException e) {
			throw new DBException("findComments DB 오류", e);
		}
	}

	@Override
	public int countComments(Connection con, long solutionId) {
		final String sql = "SELECT COUNT(*) cnt FROM comment WHERE solution_id = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, solutionId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt("cnt") : 0;
			}
		} catch (SQLException e) {
			throw new DBException("countComments DB 오류", e);
		}
	}

	@Override
	public long insertComment(Connection con, long solutionId, long wsMemberId, String content) {
		final String sql = """
			INSERT INTO comment (solution_id, ws_member_id, comment_content)
			VALUES (?, ?, ?)
			""";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setLong(1, solutionId);
			ps.setLong(2, wsMemberId);
			ps.setString(3, content);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					return rs.getLong(1);
			}
			throw new DBException("insertComment: 생성 키를 못가져왔습니다.");
		} catch (SQLException e) {
			throw new DBException("insertComment DB 오류", e);
		}
	}

	@Override
	public int deleteComment(Connection con, long commentId, long wsMemberId) {
		final String sql = "DELETE FROM comment WHERE comment_id = ? AND ws_member_id = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, commentId);
			ps.setLong(2, wsMemberId);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("deleteComment DB 오류", e);
		}
	}

	@Override
	public boolean isLeaderOfWsProblem(Connection con, long wsProblemId, long wsMemberId) {
		final String sql = """
			SELECT 1
			FROM workspace_problem wp
			JOIN workspace_member wm ON wm.ws_id = wp.ws_id
			WHERE wp.ws_problem_id = ?
			  AND wm.ws_member_id = ?
			  AND wm.is_leader = 1
			  AND wp.is_deleted = 0
			  AND wm.is_deleted = 0
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, wsProblemId);
			ps.setLong(2, wsMemberId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new DBException("isLeaderOfWsProblem DB 오류", e);
		}
	}

	@Override
	public int deleteWorkspaceProblem(Connection con, long wsProblemId) {
		final String sql = """
			UPDATE workspace_problem
			   SET is_deleted = 1,
			       updated_at = CURRENT_TIMESTAMP(3)
			 WHERE ws_problem_id = ?
			   AND is_deleted = 0
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, wsProblemId);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("softDeleteWorkspaceProblem DB 오류", e);
		}
	}

	@Override
	public int deleteSolutionsByWsProblem(Connection con, long wsProblemId) {
		final String sql = """
			UPDATE solution
			   SET is_deleted = 1,
			       updated_at = CURRENT_TIMESTAMP(3)
			 WHERE ws_problem_id = ?
			   AND is_deleted = 0
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, wsProblemId);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("softDeleteSolutionsByWsProblem DB 오류", e);
		}
	}

	private int toIntOrZero(Object o) {
		if (o == null)
			return 0;
		if (o instanceof Integer i)
			return i;
		return Integer.parseInt(String.valueOf(o));
	}

}
