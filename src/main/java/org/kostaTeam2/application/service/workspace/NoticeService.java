package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.dto.request.NoticeRequest;

import java.util.List;

public interface NoticeService {
    void create(NoticeRequest dto);
    void delete(NoticeRequest dto);
    List<Notice> getPageNotices(NoticeRequest dto);
}
