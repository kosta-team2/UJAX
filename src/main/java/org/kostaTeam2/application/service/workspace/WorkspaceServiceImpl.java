package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;
import org.kostaTeam2.dto.request.WorkspaceCreateDto;
import org.kostaTeam2.global.exception.DBException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class WorkspaceServiceImpl implements WorkspaceService{
    private final DataSource ds;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    public WorkspaceServiceImpl(DataSource ds, WorkspaceRepository repository, WorkspaceMemberRepository workspaceMemberRepository) {
        this.ds = ds;
        this.workspaceRepository = repository;
        this.workspaceMemberRepository = workspaceMemberRepository;
    }


    @Override
    public Workspace createWorkspace(WorkspaceCreateDto dto) {
        Workspace workspace = new Workspace(
                dto.getWorkspaceName(),
                dto.getWorkspaceLanguage(),
                dto.getHintView()
        );

        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            Long workspaceId = workspaceRepository.save(conn, workspace);

            WorkspaceMember workspaceMember = new WorkspaceMember(workspaceId , dto.getLeaderId(), true);
            workspaceMemberRepository.save(conn, workspaceMember);

            conn.commit();
            return getWorkspaceById(workspaceId);
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            throw new DBException("워크스페이스 생성 중 오류 발생", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    public Workspace getWorkspaceById(Long workspaceId) {
        try (Connection conn = ds.getConnection()){
            return workspaceRepository.findById(conn, workspaceId);
        } catch (SQLException e) {
            throw new DBException("워크스페이스 조회 중 DB 오류 발생", e);
        }
    }
}
