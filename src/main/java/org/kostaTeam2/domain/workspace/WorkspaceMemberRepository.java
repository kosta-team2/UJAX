package org.kostaTeam2.domain.workspace;

import org.kostaTeam2.dto.request.WorkspaceUserRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    /**
     * 워크스페이스 멤버 불러오기
     */
    List<WorkspaceMember> getAllMembers(Connection conn, long workspaceId) throws SQLException;

    /**
     * 리더 위임하기
     */
    boolean delegateLeader(Connection con, long workspaceId, long currentLeaderId, long newLeaderId) throws SQLException;

    /**
     * 멤버 방출하기
     */
    int kickUser(Connection con, long workspaceId, long wsMemberId) throws SQLException;

    /**
     * 워크스페이스 나가기
     */
    int exitWorkspace(Connection con, long workspaceId, long wsMemberId) throws SQLException;

    /**
     * 워크스페이스에 1명만 남아있는지 확인
     */
    boolean amIOnlyPerson(Connection con, long workspaceId) throws SQLException;
}
