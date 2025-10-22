package org.kostaTeam2.domain.workspace;

import java.time.LocalDateTime;
import java.util.List;

public class Workspace {
    private Long workspaceId;
    private String workspaceName;
    private WorkspaceLanguage workspaceLanguage;
    private Boolean isHintView;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private List<WorkspaceMember> workspaceMemberList;

    public Workspace(String workspaceName, WorkspaceLanguage workspaceLanguage, Boolean isHintView) {
        this.workspaceName = workspaceName;
        this.workspaceLanguage = workspaceLanguage;
        this.isHintView = Boolean.TRUE.equals(isHintView);
    }

    public Workspace(Long workspaceId, String workspaceName, WorkspaceLanguage workspaceLanguage, Boolean isHintView) {
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.workspaceLanguage = workspaceLanguage;
        this.isHintView = isHintView;
    }

    public Workspace(Long workspaceId, String workspaceName, WorkspaceLanguage workspaceLanguage, Boolean isHintView, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.workspaceLanguage = workspaceLanguage;
        this.isHintView = Boolean.TRUE.equals(isHintView);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = Boolean.TRUE.equals(isDeleted);
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public WorkspaceLanguage getWorkspaceLanguage() {
        return workspaceLanguage;
    }

    public Boolean isHintView() {
        return isHintView;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setWorkspaceMemberList(List<WorkspaceMember> workspaceMemberList) {
        this.workspaceMemberList = workspaceMemberList;
    }
}
