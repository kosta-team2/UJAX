package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;
import org.kostaTeam2.dto.request.WorkspaceCreateRequest;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.NotFoundException;
import org.kostaTeam2.global.exception.common.AppException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

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
    public Optional<Workspace> createWorkspace(WorkspaceCreateRequest dto) {
        Connection conn = null;

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            Long workspaceId = workspaceRepository.save(conn, new Workspace(
                    dto.workspaceName(),
                    dto.workspaceLanguage(),
                    dto.isHintView()
            ));

            WorkspaceMember workspaceMember = new WorkspaceMember(workspaceId , dto.userId(), true);
            workspaceMemberRepository.save(conn, workspaceMember);

            conn.commit();
            return getWorkspaceById(workspaceId);
        } catch (AppException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            throw e;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            throw new DBException("워크스페이스 생성 중 error 발생", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    public Optional<Workspace> getWorkspaceById(Long workspaceId) {
        try (Connection conn = ds.getConnection()){
            Optional<Workspace> workspace = workspaceRepository.findById(conn, workspaceId);
            if (workspace.isEmpty()) {
                throw new NotFoundException("워크스페이스를 찾지 못했습니다.");
            }

            return workspace;
        } catch (SQLException e) {
            throw new DBException("워크스페이스 조회 중 error 발생", e);
        }
    }

    @Override
    public void deleteWorkspace(Long workspaceId, Long memberId) {
        try (Connection conn = ds.getConnection()){

        } catch (SQLException e) {
            throw new DBException("워크스페이스 삭제 중 error 발생", e);
        }
    }
}
