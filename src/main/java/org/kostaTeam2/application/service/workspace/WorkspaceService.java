package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.dto.request.WorkspaceUserRequest;
import org.kostaTeam2.dto.request.WorkspaceRequest;
import org.kostaTeam2.dto.response.SidebarInfoResponse;

import java.util.List;
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
    Optional<Workspace> getWorkspaceInfo(WorkspaceRequest dto);

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

    /**
     * 리더인지 확인하고, 위임할 멤버가 현 워크스페이스에 있는 것 까지 확인하고 리더 위임
     * @param dto
     */
    void delegateLeader(WorkspaceUserRequest dto);

    /**
     * 리더인지 확인하고, 방출할 멤버가 현 워크스페이스에 있는 걸 확인하고 방출
     */
    void kickUser(WorkspaceUserRequest dto);

    /**
     * 리더인지 확인하고, 리더면 본인만 있을경우 나가기 허용.
     * 일반 멤버면 바로 나가기 허용
     */
    void exitWorkspace(WorkspaceUserRequest dto);

    List<SidebarInfoResponse> getSidebarInfo(Long memberId);
}
