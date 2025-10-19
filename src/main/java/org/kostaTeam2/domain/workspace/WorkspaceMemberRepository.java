package org.kostaTeam2.domain.workspace;

import java.sql.Connection;

public interface WorkspaceMemberRepository {
    /**
     * 워크스페이스 멤버 생성
     *
     * @param conn
     * @param workspaceMember
     */
    void save(Connection conn, WorkspaceMember workspaceMember);
}
