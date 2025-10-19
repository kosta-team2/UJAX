package org.kostaTeam2.infrastructure.dao;

import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceLanguage;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;
import org.kostaTeam2.global.exception.CreatedException;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.NotFoundException;

import java.sql.*;

public class WorkspaceDao implements WorkspaceRepository {

    @Override
    public Long save(Connection conn, Workspace workspace){
        String ws_name = workspace.getWorkspaceName();
        WorkspaceLanguage ws_lang = workspace.getWorkspaceLanguage();
        boolean is_hint_view = workspace.isHintView();

        String sql = "INSERT INTO workspace (ws_name, ws_lang, is_hint_view) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ws_name);
            ps.setString(2, ws_lang.name());
            ps.setBoolean(3, is_hint_view);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }

                throw new CreatedException("workspace GENERATED_KEYS 반환 실패.");
            }
        } catch (SQLException e) {
            throw new DBException("workspace 생성 중 DB 오류 발생", e);
        }
    }

    @Override
    public Workspace findById(Connection conn, Long workspaceId) {
        String sql = "SELECT * FROM workspace WHERE ws_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, workspaceId);

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return new Workspace(
                            rs.getLong("ws_id"),
                            rs.getString("ws_name"),
                            WorkspaceLanguage.fromString(rs.getString("ws_lang")),
                            rs.getBoolean("is_hint_view"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime(),
                            rs.getBoolean("is_deleted")
                            );

                } else {
                    throw new NotFoundException(
                            "[" + workspaceId + "]" + "에 해당하는 workspace를 찾을 수 없습니다."
                    );
                }
            }
        } catch (SQLException e) {
            throw new DBException("워크스페이스 조회 중 DB 오류 발생", e);
        }
    }

}
