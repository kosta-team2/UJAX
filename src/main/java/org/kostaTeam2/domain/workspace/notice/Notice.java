package org.kostaTeam2.domain.workspace.notice;

import java.time.LocalDateTime;

public class Notice {
    private final Long noticeId;
    private final Long workspaceId;
    private final NoticeTitle title;
    private final NoticeContent content;
    private final LocalDateTime createdAt;

    public Notice(Long noticeId, Long workspaceId, NoticeTitle title, NoticeContent content, LocalDateTime createdAt) {
        this.noticeId = noticeId;
        this.workspaceId = workspaceId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Notice create(Long workspaceId, NoticeTitle title, NoticeContent content) {
        return new Notice(null, workspaceId, title, content, LocalDateTime.now());
    }

    public static Notice read(Long noticeId, Long workspaceId, NoticeTitle title, NoticeContent content) {
        return new Notice(noticeId, workspaceId, title, content, LocalDateTime.now());
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public NoticeTitle getTitle() {
        return title;
    }

    public NoticeContent getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
