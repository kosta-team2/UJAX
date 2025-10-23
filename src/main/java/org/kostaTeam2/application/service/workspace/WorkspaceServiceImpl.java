package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;
import org.kostaTeam2.dto.request.WorkspaceUserRequest;
import org.kostaTeam2.dto.request.WorkspaceRequest;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.ForbiddenException;
import org.kostaTeam2.global.exception.common.AppException;
import org.kostaTeam2.global.exception.common.ValidationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class WorkspaceServiceImpl implements WorkspaceService {
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

            if (workspaceId == null) {
                throw new SQLException();
            }

            int res = workspaceMemberRepository
                    .save(conn,
                            new WorkspaceMember(workspaceId,
                                    dto.userId(),
                                    true
                            ));

            if (res == 0) {
                throw new SQLException();
            }
            conn.commit();

            return workspaceRepository.findById(conn, workspaceId);
        } catch (AppException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
            throw e;
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
            throw new DBException("워크스페이스 생성 실패했습니다.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    /**
     * 워크스페이스 기본 정보 가져오고, 해당 워크스페이스의 멤버 정보 담아서 request로 전송
     *
     * @param dto
     * @return workspace
     */
    @Override
    public Optional<Workspace> getWorkspaceInfo(WorkspaceRequest dto) {
        try (Connection conn = ds.getConnection()) {
            if (!workspaceMemberRepository.isMember(conn, new WorkspaceMember(dto.workspaceId(), dto.userId()))) {
                throw new ForbiddenException("워크스페이스 멤버가 아닙니다.");
            }

            Optional<Workspace> workspace = workspaceRepository.findById(conn, dto.workspaceId());
            if (workspace.isEmpty()) {
                throw new SQLException();
            }

            long workspaceId = workspace.get().getWorkspaceId();
            List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.getAllMembers(conn, workspaceId);
            workspace.get().setWorkspaceMemberList(workspaceMembers);

            return workspace;
        } catch (SQLException e) {
            throw new DBException("워크스페이스 조회 실패했습니다.", e);
        }
    }

    @Override
    public Optional<Workspace> updateWorkspace(WorkspaceRequest dto) {
        try (Connection conn = ds.getConnection()) {
            if (!workspaceMemberRepository.isLeader(conn, new WorkspaceMember(dto.workspaceId(), dto.userId()))) {
                throw new ForbiddenException("워크스페이스 수정 권한이 없습니다.");
            }

            int res = workspaceRepository
                    .update(
                            conn, new Workspace(dto.workspaceId(),
                                    dto.workspaceName(),
                                    dto.workspaceLanguage(),
                                    dto.isHintView()
                            ));
            if (res == 0) {
                throw new SQLException();
            }

            return workspaceRepository.findById(conn, dto.workspaceId());
        } catch (SQLException e) {
            throw new DBException("워크스페이스 수정 실패했습니다.", e);
        }
    }

    @Override
    public void delegateLeader(WorkspaceUserRequest dto) {
        try (Connection con = ds.getConnection()) {
            if (!workspaceMemberRepository.isLeader(con, new WorkspaceMember(dto.wsId(), dto.whoAmI()))) {
                throw new ForbiddenException("리더 변경 권한이 없습니다.");
            }

            if (!workspaceMemberRepository.isMember(con, new WorkspaceMember(dto.wsId(), dto.wsMemberId()))) {
                throw new BadRequestException("더 이상 존재하지 않는 멤버입니다.");
            }

            long currentLeaderId = dto.whoAmI();
            long wsId = dto.wsId();
            long newLeaderId = dto.wsMemberId();

            if (!workspaceMemberRepository.delegateLeader(con, wsId, currentLeaderId, newLeaderId)) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            throw new DBException("리더 위임에 실패했습니다.", e);
        }
    }

    @Override
    public void kickUser(WorkspaceUserRequest dto) {
        long wsId = dto.wsId();
        long whoAmI = dto.whoAmI();
        long wsMemberId = dto.wsMemberId();

        try (Connection con = ds.getConnection()) {
            if (!workspaceMemberRepository.isLeader(con, new WorkspaceMember(wsId, whoAmI))) {
                throw new ForbiddenException("유저 방출 권한이 없습니다.");
            }

            if (!workspaceMemberRepository.isMember(con, new WorkspaceMember(wsId, wsMemberId))) {
                throw new BadRequestException("더 이상 존재하지 않는 멤버입니다.");
            }

            int res = workspaceMemberRepository.kickUser(con, wsId, wsMemberId);
            if (res == 0) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            throw new DBException("유저 방출에 실패했습니다.", e);
        }
    }

    @Override
    public void exitWorkspace(WorkspaceUserRequest dto) {
        long wsId = dto.wsId();
        long whoAmI = dto.whoAmI();

        try (Connection con = ds.getConnection()) {
            if (workspaceMemberRepository.isLeader(con, new WorkspaceMember(wsId, whoAmI))) {
                //워크스페이스에 남아 있는 사람이 1명인지 check
                if (!workspaceMemberRepository.amIOnlyPerson(con, wsId)) {
                    throw new ValidationException("리더를 위임하고 탈퇴하세요.",
                            "/front?key=workspace&methodName=show&workspaceId="+dto.wsId());
                }
            }

            int res = workspaceMemberRepository.exitWorkspace(con, wsId, whoAmI);
            if (res == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new DBException("워크스페이스 나가기에 실패했습니다.", e);
        }
    }

    @Override
    public void deleteWorkspace(WorkspaceRequest dto) {

        try (Connection conn = ds.getConnection()) {
            if (!workspaceMemberRepository.isLeader(conn, new WorkspaceMember(dto.workspaceId(), dto.userId()))) {
                throw new ForbiddenException("워크스페이스 삭제 권한이 없습니다.");
            }

            int res = workspaceRepository.delete(conn, dto.workspaceId());
            if (res == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new DBException("워크스페이스 삭제 실패했습니다.", e);
        }
    }
}
