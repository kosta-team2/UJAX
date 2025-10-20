package org.kostaTeam2.domain.workspace;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface
WorkspaceRepository {
    /**
     * 워크스페이스 생성 후 워크스페이스 아이디 반환
     * @param conn
     * @param workspace
     * @return workspaceId
     * @throws SQLException
     */
    Long save(Connection conn, Workspace workspace) throws SQLException;

    /**
     * ID로 워크스페이스 찾기
     * @param conn
     * @param id
     * @return
     */
    Optional<Workspace> findById(Connection conn, Long id) throws SQLException;
}
