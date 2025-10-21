package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.dto.request.NoticeRequest;

import java.util.List;

public interface NoticeService {
    Notice create(NoticeRequest dto);
    Notice delete(NoticeRequest dto);
    Notice getNoticeById(Long noticeId);
    List<Notice> getPageNotices(Long workspaceId, int page, int size);

}
