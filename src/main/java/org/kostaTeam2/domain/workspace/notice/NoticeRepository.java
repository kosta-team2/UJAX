package org.kostaTeam2.domain.workspace.notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository {

    Notice save(Notice notice);
    Optional<Notice> findById(Long noticeId);
    List<Notice> findAll(Long workspaceId, int offset, int limit);
    void delete(Long noticeId, Long workspaceId);

}
