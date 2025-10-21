package org.kostaTeam2.domain.workspace;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface
WorkspaceRepository {
    /**
     * 워크스페이스 생성 후 워크스페이스 아이디 반환
     */
    Long save(Connection conn, Workspace workspace) throws SQLException;

    /**
     * ID로 워크스페이스 찾기
     */
    Optional<Workspace> findById(Connection conn, Long workspaceId) throws SQLException;

    /**
     * 워크스페이스 수정
     */
    int update(Connection conn, Workspace workspace) throws SQLException;

    /**
     * 워크스페이스 삭제
     */
    int delete(Connection conn, Long workspaceId) throws SQLException;

}
