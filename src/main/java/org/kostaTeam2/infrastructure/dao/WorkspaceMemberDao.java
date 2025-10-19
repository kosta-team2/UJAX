package org.kostaTeam2.infrastructure.dao;

import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.global.exception.CreatedException;
import org.kostaTeam2.global.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WorkspaceMemberDao implements WorkspaceMemberRepository {

    @Override
    public void save(Connection conn, WorkspaceMember workspaceMember){
        Long ws_id = workspaceMember.getWorkspaceId();
        Long member_id = workspaceMember.getMemberId();
        boolean is_leader = workspaceMember.isLeader();

        String sql = "INSERT INTO workspace_member(ws_id, member_id, is_leader) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ws_id);
            ps.setLong(2, member_id);
            ps.setBoolean(3, is_leader);

            int res = ps.executeUpdate();
            if (res <= 0) throw new CreatedException("워크스페이스 멤버 생성 실패.");
        } catch (SQLException e) {
            throw new DBException("워크스페이스 멤버 저장 중 DB 오류 발생", e);
        }
    }

}
