package org.kostaTeam2.infrastructure.dao;

import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.domain.workspace.notice.NoticeContent;
import org.kostaTeam2.domain.workspace.notice.NoticeRepository;
import org.kostaTeam2.domain.workspace.notice.NoticeTitle;
import org.kostaTeam2.global.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    public List<Notice> findNoticePageByWorkspaceId(Connection conn, Long workspaceId, int offset, int limit) {
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
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                List<Notice> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    Long noticeId = rs.getLong("n_id");
                    String title = rs.getString("n_title");
                    String content = rs.getString("n_content");

                    list.add(
                            Notice.read(noticeId, workspaceId, new NoticeTitle(title), new NoticeContent(content))
                    );
                }

                return list;
            }
        } catch (SQLException e) {
            throw new DBException("공지 목록 조회 중 DB 오류", e);
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
