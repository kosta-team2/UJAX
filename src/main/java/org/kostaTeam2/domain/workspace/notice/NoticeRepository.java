package org.kostaTeam2.domain.workspace.notice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface NoticeRepository {
	int save(Connection conn, Notice notice);

	int countByWorkspace(Connection conn, Long workspaceId) throws SQLException;

	List<Notice> findPageByWorkspace(Connection conn, Long workspaceId, int offset, int limit);

	int delete(Connection conn, Long noticeId);

}
