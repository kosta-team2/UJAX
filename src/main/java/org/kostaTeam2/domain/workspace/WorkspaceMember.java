package org.kostaTeam2.domain.workspace;

import java.time.LocalDateTime;

public class WorkspaceMember {
    private Long workspaceMemberId;
    private Long workspaceId;
    private Long memberId;
    private boolean isLeader;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;

    public WorkspaceMember(Long workspaceId, Long memberId) {
        this.workspaceId = workspaceId;
        this.memberId = memberId;
    }

    public WorkspaceMember(Long workspaceId, Long memberId, Boolean isLeader) {
        this.workspaceId = workspaceId;
        this.memberId = memberId;
        this.isLeader = Boolean.TRUE.equals(isLeader);
    }

    public Long getWorkspaceMemberId() {
        return workspaceMemberId;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Boolean isLeader() {
        return isLeader;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }
}
