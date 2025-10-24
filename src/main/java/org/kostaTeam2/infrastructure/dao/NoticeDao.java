package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.domain.workspace.notice.NoticeContent;
import org.kostaTeam2.domain.workspace.notice.NoticeRepository;
import org.kostaTeam2.domain.workspace.notice.NoticeTitle;
import org.kostaTeam2.global.exception.DBException;

public class NoticeDao implements NoticeRepository {

	@Override
	public int save(Connection conn, Notice notice) {
		Long workspaceId = notice.getWorkspaceId();
		NoticeTitle title = notice.getTitle();
		NoticeContent content = notice.getContent();

		final String sql = "INSERT INTO notice(ws_id, n_title, n_content) VALUES (?,?,?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			ps.setString(2, title.value());
			ps.setString(3, content.value());

			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("공지 생성중 DB에러", e);
		}
	}

	@Override
	public int countByWorkspace(Connection conn, Long workspaceId) throws SQLException {
		final String sql = """
			SELECT count(*) FROM notice WHERE ws_id = ?;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}

	}

	@Override
	public List<Notice> findPageByWorkspace(Connection conn, Long workspaceId, int offset, int size) {
		final String sql = """
			SELECT
			    n_id,
			    n_title,
			    n_content
			FROM notice
			WHERE ws_id = ?
			ORDER BY created_at DESC, n_id DESC
			LIMIT ? OFFSET ?;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, workspaceId);
			ps.setInt(2, size);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				List<Notice> list = new ArrayList<>();
				while (rs.next()) {
					Notice notice = Notice.read(rs.getLong("n_id"), workspaceId,
						new NoticeTitle(rs.getString("n_title")),
						new NoticeContent(rs.getString("n_content")));
					list.add(notice);
				}

				return list;
			}
		} catch (SQLException e) {
			throw new DBException("공지 페이지 조회 DB 오류", e);
		}
	}

	@Override
	public int delete(Connection conn, Long noticeId) {
		final String sql = "DELETE FROM notice WHERE n_id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, noticeId);

			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("공지 삭제중 DB에러", e);
		}
	}
}
