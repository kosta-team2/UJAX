package org.kostaTeam2.infrastructure.dao;

import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkspaceMemberDao implements WorkspaceMemberRepository {

    @Override
    public int save(Connection conn, WorkspaceMember workspaceMember) throws SQLException {
        Long ws_id = workspaceMember.getWorkspaceId();
        Long member_id = workspaceMember.getMemberId();
        boolean is_leader = workspaceMember.isLeader();

        String sql = "INSERT INTO workspace_member(ws_id, ws_member_id, is_leader) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ws_id);
            ps.setLong(2, member_id);
            ps.setBoolean(3, is_leader);

            return ps.executeUpdate();
        }
    }

    @Override
    public boolean isLeader(Connection conn, WorkspaceMember workspaceMember) throws SQLException {
        String sql = "SELECT is_leader FROM workspace_member WHERE ws_member_id = ? AND ws_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, workspaceMember.getMemberId());
            ps.setLong(2, workspaceMember.getWorkspaceId());

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return rs.getBoolean("is_leader");
                }

                return false;
            }
        }
    }

    @Override
    public boolean isMember(Connection conn, WorkspaceMember workspaceMember) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM workspace_member WHERE ws_member_id = ? AND ws_id = ?) AS exist";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, workspaceMember.getMemberId());
            ps.setLong(2, workspaceMember.getWorkspaceId());

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return rs.getBoolean("exist");
                }

                return false;
            }
        }
    }

}
