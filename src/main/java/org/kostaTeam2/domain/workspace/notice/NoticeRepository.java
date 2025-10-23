package org.kostaTeam2.domain.workspace.notice;

import java.sql.Connection;
import java.util.List;

public interface NoticeRepository {

    int save(Connection conn, Notice notice);
    List<Notice> findNoticePageByWorkspaceId(Connection conn, Long workspaceId, int offset, int limit);
    int delete(Connection conn, Long noticeId);

}
