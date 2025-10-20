package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.dto.request.WorkspaceRequest;

import java.util.Optional;

public interface WorkspaceService {
    /**
     * 워크스페이스 생성후 워크스페이스ID를 통해 워크스페이스 멤버에 리더를 추가하는 트랜잭션을 실행한다.
     * @param dto
     * @return
     */
    Optional<Workspace> createWorkspace(WorkspaceRequest dto);

    /**
     * 워크스페이스 아이디로 찾아 반환
     * @param dto
     * @return
     */
    Optional<Workspace> getWorkspaceById(WorkspaceRequest dto);

    /**
     * 리더인지 확인하고, 워크스페이스 삭제
     * @param dto
     * @return
     */
    void deleteWorkspace(WorkspaceRequest dto);

    /**
     * 리더인지 확인하고, 워크스페이스 수정
     * @param dto
     * @return
     */
    Optional<Workspace> updateWorkspace(WorkspaceRequest dto);
}
