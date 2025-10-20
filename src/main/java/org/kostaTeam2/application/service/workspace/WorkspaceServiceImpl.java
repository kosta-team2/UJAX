package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;
import org.kostaTeam2.dto.request.WorkspaceRequest;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.ForbiddenException;
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
    public Optional<Workspace> createWorkspace(WorkspaceRequest dto) {
        Connection conn = null;

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            Long workspaceId = workspaceRepository.save(conn, new Workspace(
                    dto.workspaceName(),
                    dto.workspaceLanguage(),
                    dto.isHintView()
            ));

            if (workspaceId == null) {throw new SQLException();}

            WorkspaceMember workspaceMember = new WorkspaceMember(workspaceId , dto.userId(), true);
            int res = workspaceMemberRepository.save(conn, workspaceMember);
            if (res == 0) {throw new SQLException();}

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
                throw new NotFoundException("워크스페이스가 존재하지 않습니다.");
            }

            return workspace;
        } catch (SQLException e) {
            throw new DBException("워크스페이스 조회 중 error 발생", e);
        }
    }

    @Override
    public void deleteWorkspace(WorkspaceRequest dto) {

        try (Connection conn = ds.getConnection()){
            if (workspaceMemberRepository.isLeader(conn, dto.userId(), dto.workspaceId()) != 1) {
                throw new ForbiddenException("워크스페이스 삭제 권한이 없습니다.");
            }

            int res = workspaceRepository.delete(conn, dto.workspaceId());

        } catch (SQLException e) {
            throw new DBException("워크스페이스 삭제 중 error 발생", e);
        }
    }
}
