package org.kostaTeam2.domain.workspace;

import java.time.LocalDateTime;

public class WorkspaceMember {
    private Long workspaceMemberId;
    private Long workspaceId;
    private Long memberId;
    private boolean leader;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private String nickname;
    private String email;

    public WorkspaceMember(Long workspaceId, Long memberId) {
        this.workspaceId = workspaceId;
        this.memberId = memberId;
    }

    public WorkspaceMember(Long workspaceId, Long memberId, Boolean isLeader) {
        this.workspaceId = workspaceId;
        this.memberId = memberId;
        this.leader = Boolean.TRUE.equals(isLeader);
    }

    public WorkspaceMember(Long workspaceId, Long memberId, Boolean isLeader, String nickname, String email) {
        this.workspaceId = workspaceId;
        this.memberId = memberId;
        this.leader = Boolean.TRUE.equals(isLeader);
        this.nickname = nickname;
        this.email = email;
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

    public boolean isLeader() {
        return leader;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setWorkspaceMemberId(Long workspaceMemberId) {
        this.workspaceMemberId = workspaceMemberId;
    }
}
