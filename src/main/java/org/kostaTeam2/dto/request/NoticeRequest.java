package org.kostaTeam2.dto.request;

import org.kostaTeam2.global.exception.BadRequestException;

import java.util.Locale;
import java.util.Objects;

public record NoticeRequest(
        Long userId,
        Long workspaceId,
        Long noticeId,
        String title,
        String content,
        String sort,
        Integer page,
        Integer limit
) {
    public static final int DEFAULT_PAGE  = 0;
    public static final int DEFAULT_LIMIT = 3;
    public static final int MAX_LIMIT     = 20;
    public static final String DEFAULT_SORT = "createdAt desc";

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long userId;
        private Long workspaceId;
        private Long noticeId;
        private String title;
        private String content;
        private String sort;
        private Integer page;
        private Integer limit;

        public Builder userId(Long v) { this.userId = v; return this; }
        public Builder workspaceId(Long v) { this.workspaceId = v; return this; }
        public Builder noticeId(Long v) { this.noticeId = v; return this; }
        public Builder title(String v) { this.title = v; return this; }
        public Builder content(String v) { this.content = v; return this; }
        public Builder sort(String v) { this.sort = v; return this; }
        public Builder page(Integer v) { this.page = v; return this; }
        public Builder limit(Integer v) { this.limit = v; return this; }

        public NoticeRequest build() {
            String normSort = sort==null ? DEFAULT_SORT : sort;
            int normPage  = (page == null || page < 0) ? DEFAULT_PAGE : page;
            int normLimit = (limit == null || limit <= 0) ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);
            return new NoticeRequest(userId, workspaceId, noticeId, title, content, normSort, normPage, normLimit);
        }

        public Builder validateForCreate() {
            if (userId == null) throw new BadRequestException("userId is required.");
            if (workspaceId == null) throw new BadRequestException("workspaceId is required.");
            if (title == null) throw new BadRequestException("title is required.");
            if (content == null) throw new BadRequestException("content is required.");
            return this;
        }
        public Builder validateForRead() {
            if (workspaceId == null) throw new BadRequestException("workspaceId is required.");
            return this;
        }
        public Builder validateForDelete() {
            if (userId == null) throw new BadRequestException("userId is required.");
            if (workspaceId == null) throw new BadRequestException("workspaceId is required.");
            if (noticeId == null) throw new BadRequestException("noticeId is required.");
            return this;
        }
    }

    public static NoticeRequest fromCreate(Long userId, Long workspaceId,
                                           String title, String content) {
        return builder()
                .userId(userId).workspaceId(workspaceId)
                .title(title).content(content)
                .validateForCreate()
                .build();
    }

    public static NoticeRequest fromReadList(Long workspaceId, String sort, Integer page, Integer limit) {
        return builder()
                .workspaceId(workspaceId)
                .sort(sort).page(page).limit(limit)
                .validateForRead()
                .build();
    }

    public static NoticeRequest fromDelete(Long userId, Long workspaceId, Long noticeId) {
        return builder()
                .userId(userId).workspaceId(workspaceId).noticeId(noticeId)
                .validateForDelete()
                .build();
    }

}
