package org.kostaTeam2.dto.request;

import org.kostaTeam2.domain.workspace.WorkspaceLanguage;

public class WorkspaceCreateDto {
    private Long leaderId;
    private String workspaceName;
    private WorkspaceLanguage workspaceLanguage;
    private Boolean isHintView;

    public WorkspaceCreateDto(Long leaderId, String workspaceName, WorkspaceLanguage workspaceLanguage, Boolean isHintView) {
        this.leaderId = leaderId;
        this.workspaceName = workspaceName;
        this.workspaceLanguage = workspaceLanguage;
        this.isHintView = isHintView;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public WorkspaceLanguage getWorkspaceLanguage() {
        return workspaceLanguage;
    }

    public Boolean getHintView() {
        return isHintView;
    }

}
