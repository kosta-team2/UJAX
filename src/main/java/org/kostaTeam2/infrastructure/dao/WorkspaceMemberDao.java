package org.kostaTeam2.infrastructure.dao;

import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkspaceMemberDao implements WorkspaceMemberRepository {

    @Override
    public int save(Connection conn, WorkspaceMember workspaceMember) throws SQLException {
        Long ws_id = workspaceMember.getWorkspaceId();
        Long member_id = workspaceMember.getMemberId();
        boolean is_leader = workspaceMember.isLeader();

        Map<String, String> infoMap = getUserEmailAndNickName(conn, member_id);
        String nickname = infoMap.get("nickname");
        String email = infoMap.get("email");

        String sql = "INSERT INTO workspace_member(ws_id, member_id, is_leader, nickname, email) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ws_id);
            ps.setLong(2, member_id);
            ps.setBoolean(3, is_leader);
            ps.setString(4, nickname);
            ps.setString(5, email);

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

    @Override
    public List<WorkspaceMember> getAllMembers(Connection conn, long workspaceId) throws SQLException {
        List<WorkspaceMember> workspaceMembers = new ArrayList<>();
        String sql = """
                SELECT ws_member_id, member_id, is_leader, email, nickname
                FROM workspace_member
                WHERE ws_id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, workspaceId);

            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    workspaceMembers.add(new WorkspaceMember(rs.getLong(1), rs.getLong(2), rs.getBoolean(3), rs.getString(4), rs.getString(5)));
                }
            }
        }
        return workspaceMembers;
    }

    private Map<String, String> getUserEmailAndNickName(Connection conn, long memberId) throws SQLException {
        Map<String, String> infoMap = new HashMap();
        String sql = """
                SELECT email, nickname
                FROM member
                WHERE member_id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, memberId);

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    infoMap.put("email", rs.getString(1));
                    infoMap.put("nickname", rs.getString(2));
                }
            }
        }
        return infoMap;
    }
}
