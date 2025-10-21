package org.kostaTeam2.domain.workspace;

import java.sql.Connection;
import java.sql.SQLException;

public interface WorkspaceMemberRepository {
    /**
     * 워크스페이스 멤버 생성
     */
    int save(Connection conn, WorkspaceMember workspaceMember) throws SQLException;

    /**
     * 워크스페이스의 리더인지 확인
     */
    boolean isLeader(Connection conn, WorkspaceMember workspaceMember) throws SQLException;

    /**
     * 워크스페이스의 멤버인지 확인
     */
    boolean isMember(Connection conn, WorkspaceMember workspaceMember) throws SQLException;
}
