package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.domain.workspace.notice.NoticeRepository;
import org.kostaTeam2.dto.request.NoticeRequest;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.ForbiddenException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class NoticeServiceImpl implements NoticeService {
    private final DataSource ds;
    private final NoticeRepository noticeRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    public NoticeServiceImpl(DataSource ds, NoticeRepository noticeRepository, WorkspaceMemberRepository workspaceMemberRepository) {
        this.ds = ds;
        this.noticeRepository = noticeRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    @Override
    public Notice create(NoticeRequest dto) {
        try (Connection conn = ds.getConnection()){
            if(!workspaceMemberRepository.isLeader(dto.memberId(), dto.workspaceId)) {
                throw new ForbiddenException("리더가 아닙니다.");
            }
            Notice notice = new Notice(dto.workspcaeId(), dto.title, dto.content);

            if(noticeRepository.save(notice) != 1) throw new DBException();
        } catch (SQLException e) {
            throw new DBException("공지 생성에 실패했습니다.");
        }
    }

    @Override
    public Notice delete(NoticeRequest dto) {
        return null;
    }

    @Override
    public Notice getNoticeById(Long noticeId) {
        return null;
    }

    @Override
    public List<Notice> getPageNotices(Long workspaceId, int page, int size) {
        return List.of();
    }
}
