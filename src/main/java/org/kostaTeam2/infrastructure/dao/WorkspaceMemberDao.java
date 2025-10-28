package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.chart.CommentStatVO;
import org.kostaTeam2.domain.workspace.chart.SolvedStatVO;

public class WorkspaceMemberDao implements WorkspaceMemberRepository {

	@Override
	public int save(Connection conn, WorkspaceMember workspaceMember) throws SQLException {
		Long ws_id = workspaceMember.getWorkspaceId();
		Long member_id = workspaceMember.getMemberId();
		boolean is_leader = workspaceMember.isLeader();

		Map<String, String> infoMap = getUserEmailAndNickName(conn, member_id);
		String nickname = infoMap.get("nickname");
		String email = infoMap.get("email");

		String sql = "INSERT INTO workspace_member(ws_id, member_id, is_leader, nickname, email) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, ws_id);
			ps.setLong(2, member_id);
			ps.setBoolean(3, is_leader);
			ps.setString(4, nickname);
			ps.setString(5, email);

			return ps.executeUpdate();
		}
	}

	@Override
	public Optional<Long> findWsMemberIdByWsIdAndMemberId(Connection conn, long wsId, long memberId) throws
		SQLException {
		String sql = """
			    SELECT ws_member_id
			    FROM workspace_member
			    WHERE ws_id = ? AND member_id = ? AND is_deleted = 0
			    LIMIT 1
			""";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, wsId);
			ps.setLong(2, memberId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(rs.getLong(1));
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean isLeader(Connection conn, WorkspaceMember workspaceMember) throws SQLException {
		String sql = "SELECT is_leader FROM workspace_member WHERE ws_member_id = ? AND ws_id = ? AND is_deleted = 0";

		Optional<Long> wsMemberId = findWsMemberIdByWsIdAndMemberId(conn, workspaceMember.getWorkspaceId(),
			workspaceMember.getMemberId());
		if (wsMemberId.isEmpty()) {
			return false;
		}

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, wsMemberId.get());
			ps.setLong(2, workspaceMember.getWorkspaceId());

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getBoolean("is_leader");
				}

				return false;
			}
		}
	}

	@Override
	public boolean isMember(Connection conn, WorkspaceMember workspaceMember) throws SQLException {
		String sql = "SELECT EXISTS (SELECT 1 FROM workspace_member WHERE member_id = ? AND ws_id = ? AND is_deleted = 0) AS exist";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, workspaceMember.getMemberId());
			ps.setLong(2, workspaceMember.getWorkspaceId());

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getBoolean("exist");
				}
				return false;
			}
		}
	}

	@Override
	public List<WorkspaceMember> getAllMembers(Connection conn, long workspaceId) throws SQLException {
		List<WorkspaceMember> list = new ArrayList<>();
		String sql = """
			    SELECT ws_member_id, ws_id, member_id, is_leader, email, nickname
			    FROM workspace_member
			    WHERE ws_id = ?
			      AND is_deleted = 0
			""";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long wsMemberId = rs.getLong("ws_member_id");
					long wsId = rs.getLong("ws_id");
					long memberId = rs.getLong("member_id");
					boolean leader = rs.getBoolean("is_leader");
					String email = rs.getString("email");
					String nickname = rs.getString("nickname");
					WorkspaceMember m = new WorkspaceMember(wsId, memberId, leader, nickname, email);
					m.setWorkspaceMemberId(wsMemberId); //
					list.add(m);
				}
			}
		}
		return list;
	}

	@Override
	public boolean delegateLeader(Connection con, long workspaceId, long currentLeaderId, long newLeaderId) throws
		SQLException {
		String sql1 = """
			UPDATE workspace_member
			SET is_leader = 0
			WHERE ws_member_id = ? AND ws_id = ?;
			""";
		String sql2 = """
			UPDATE workspace_member
			SET is_leader = 1
			WHERE ws_member_id = ? AND ws_id = ?;
			""";

		int result = 0;
		Optional<Long> currentLeaderWsId = findWsMemberIdByWsIdAndMemberId(con, workspaceId, currentLeaderId);
		if (currentLeaderWsId.isEmpty()) {
			return false;
		}

		try (
			PreparedStatement ps1 = con.prepareStatement(sql1);
			PreparedStatement ps2 = con.prepareStatement(sql2)
		) {
			con.setAutoCommit(false); // 트랜잭션 시작

			// 1. 기존 리더 해제
			ps1.setLong(1, currentLeaderWsId.get());
			ps1.setLong(2, workspaceId);
			result += ps1.executeUpdate();

			// 2. 새 리더 지정
			ps2.setLong(1, newLeaderId);
			ps2.setLong(2, workspaceId);
			result += ps2.executeUpdate();

			if (result == 2) {
				con.commit();
				return true;
			} else {
				con.rollback();
				return false;
			}
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(true);
		}
	}

	@Override
	public boolean amIOnlyPerson(Connection con, long workspaceId) throws SQLException {
		String sql = """
			SELECT COUNT(*) AS cnt
			FROM workspace_member
			WHERE ws_id = ?
			AND is_deleted = 0
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("cnt");
					return count == 1;
				}
			}
		}
		return false;
	}

	@Override
	public int kickUser(Connection con, long workspaceId, long wsMemberId) throws SQLException {
		String sql = """
			UPDATE workspace_member
			SET is_deleted = 1
			WHERE ws_id = ? AND ws_member_id = ?;
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			ps.setLong(2, wsMemberId);
			return ps.executeUpdate();
		}
	}

	@Override
	public int exitWorkspace(Connection con, long workspaceId, long memberId) throws SQLException {
		String sql = """
			UPDATE workspace_member
			SET is_deleted = 1
			WHERE ws_id = ? AND ws_member_id = ?;
			""";
		Optional<Long> wsMemberId = findWsMemberIdByWsIdAndMemberId(con, workspaceId, memberId);
		if (wsMemberId.isEmpty()) {
			return 0;
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			ps.setLong(2, wsMemberId.get());
			return ps.executeUpdate();
		}
	}

	private Map<String, String> getUserEmailAndNickName(Connection conn, long memberId) throws SQLException {
		Map<String, String> infoMap = new HashMap();
		String sql = """
			SELECT email, nickname
			FROM member
			WHERE member_id = ?;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, memberId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					infoMap.put("email", rs.getString(1));
					infoMap.put("nickname", rs.getString(2));
				}
			}
		}
		return infoMap;
	}

	@Override
	public List<WorkspaceMember> findByMemberId(Connection conn, Long memberId) {
		List<WorkspaceMember> list = new ArrayList<>();
		String sql = """
			SELECT ws_member_id, is_leader, ws_id
			FROM workspace_member
			WHERE member_id = ?;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, memberId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new WorkspaceMember(
						rs.getLong("ws_member_id"),
						rs.getBoolean("is_leader"),
						rs.getLong("ws_id")
					));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	@Override
	public List<Member> findTopNByLevel(Connection conn, long workspaceId, int limit) throws SQLException {
		List<Member> list = new ArrayList<>();
		String sql = """
			SELECT m.nickname, m.xp
			FROM workspace_member wm
			JOIN member m ON m.member_id = wm.member_id
			WHERE wm.ws_id = ?
			ORDER BY m.xp DESC, m.member_id ASC
			LIMIT ?;
			""";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			ps.setInt(2, limit);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String nickname = rs.getString("nickname");
					int xp = rs.getInt("xp");
					list.add(new Member(nickname, xp));
				}

				return list;
			}
		}
	}

	@Override
	public List<SolvedStatVO> findTopNBySolved(Connection conn, long workspaceId, int limit) throws SQLException {
		List<SolvedStatVO> list = new ArrayList<>();
		String sql = """
			SELECT
			  wm.nickname,
			  COALESCE(COUNT(DISTINCT CASE WHEN s.status = 1 THEN s.ws_problem_id END), 0) AS solved_count
			FROM workspace_member wm
			LEFT JOIN solution s
			  ON s.ws_member_id = wm.ws_member_id
			 AND s.is_deleted = 0
			LEFT JOIN workspace_problem wp
			  ON wp.ws_problem_id = s.ws_problem_id
			 AND wp.ws_id = wm.ws_id
			WHERE wm.ws_id = ?
			GROUP BY wm.ws_member_id, wm.nickname
			ORDER BY solved_count DESC, wm.nickname ASC
			LIMIT ?;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			ps.setInt(2, limit);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new SolvedStatVO(
						rs.getString("nickname"),
						rs.getInt("solved_count")
					));
				}
			}
		}
		return list;
	}

	@Override
	public List<CommentStatVO> findTopByComment(Connection conn, long workspaceId, int limit) throws
		SQLException {
		List<CommentStatVO> list = new ArrayList<>();
		String sql = """
			SELECT
			    wm.nickname,
			    COUNT(c.comment_id) AS comment_count
			FROM workspace_member wm
			LEFT JOIN comment c
			    ON wm.ws_member_id = c.ws_member_id
			WHERE wm.ws_id = ?
			GROUP BY wm.nickname
			ORDER BY comment_count DESC
			LIMIT ?;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			ps.setInt(2, limit);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new CommentStatVO(
						rs.getString("nickname"),
						rs.getInt("comment_count")
					));
				}
			}
		}
		return list;
	}

}
