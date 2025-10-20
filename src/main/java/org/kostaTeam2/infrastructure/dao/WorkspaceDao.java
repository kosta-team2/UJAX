package org.kostaTeam2.infrastructure.dao;

import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceLanguage;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;

import java.sql.*;
import java.util.Optional;

public class WorkspaceDao implements WorkspaceRepository {

    @Override
    public Long save(Connection conn, Workspace workspace) throws SQLException {
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
                if (!rs.next()) return null;

                return rs.getLong(1);
            }
        }
    }

    @Override
    public Optional<Workspace> findById(Connection conn, Long workspaceId) throws SQLException {
        String sql = "SELECT * " +
                "FROM workspace " +
                "WHERE ws_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, workspaceId);

            try (ResultSet rs = ps.executeQuery()){
                if (!rs.next()) return Optional.empty();

                return Optional.of(new Workspace(
                        rs.getLong("ws_id"),
                        rs.getString("ws_name"),
                        WorkspaceLanguage.fromString(rs.getString("ws_lang")),
                        rs.getBoolean("is_hint_view"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getBoolean("is_deleted")
                ));
            }
        }
    }

    @Override
    public int update(Connection conn, Workspace workspace) throws SQLException {
        String sql = "UPDATE workspace " +
                "SET ws_name = ?, ws_lang = ?, is_hint_view = ? " +
                "WHERE ws_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, workspace.getWorkspaceName());
            ps.setString(2, workspace.getWorkspaceLanguage().name());
            ps.setBoolean(3, workspace.isHintView());
            ps.setLong(4, workspace.getWorkspaceId());

            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Connection conn, Long workspaceId) throws SQLException {
        String sql = "UPDATE workspace " +
                "SET is_deleted = 1 " +
                "WHERE ws_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, workspaceId);
            return ps.executeUpdate();
        }
    }
}
