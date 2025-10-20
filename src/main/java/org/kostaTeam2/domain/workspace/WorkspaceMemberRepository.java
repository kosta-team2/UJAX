package org.kostaTeam2.domain.workspace;

import java.sql.Connection;
import java.sql.SQLException;

public interface WorkspaceMemberRepository {
    /**
     * 워크스페이스 멤버 생성
     *
     * @param conn
     * @param workspaceMember
     */
    int save(Connection conn, WorkspaceMember workspaceMember) throws SQLException;
}
