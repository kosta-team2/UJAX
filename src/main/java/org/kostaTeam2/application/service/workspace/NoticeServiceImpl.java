package org.kostaTeam2.application.service.workspace;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.domain.workspace.notice.NoticeContent;
import org.kostaTeam2.domain.workspace.notice.NoticeRepository;
import org.kostaTeam2.domain.workspace.notice.NoticeTitle;
import org.kostaTeam2.dto.request.NoticeRequest;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.ForbiddenException;
import org.kostaTeam2.global.exception.NotFoundException;

public class NoticeServiceImpl implements NoticeService {
	private final DataSource ds;
	private final NoticeRepository noticeRepository;
	private final WorkspaceMemberRepository workspaceMemberRepository;

	public NoticeServiceImpl(DataSource ds, NoticeRepository noticeRepository,
		WorkspaceMemberRepository workspaceMemberRepository) {
		this.ds = ds;
		this.noticeRepository = noticeRepository;
		this.workspaceMemberRepository = workspaceMemberRepository;
	}

	@Override
	public void create(NoticeRequest dto) {
		try (Connection conn = ds.getConnection()) {
			WorkspaceMember workspaceMember = new WorkspaceMember(dto.userId(), dto.workspaceId());
			// todo isLeader 서비스 이동
			if (!workspaceMemberRepository.isLeader(conn, workspaceMember)) {
				throw new ForbiddenException("리더가 아닙니다.");
			}

			Notice notice = Notice
				.create(
					dto.workspaceId(),
					new NoticeTitle(dto.title()),
					new NoticeContent(dto.content())
				);

			noticeRepository.save(conn, notice);
		} catch (SQLException e) {
			throw new DBException("공지 생성 실패");
		}
	}

	@Override
	public void delete(NoticeRequest dto) {
		try (Connection conn = ds.getConnection()) {
			WorkspaceMember workspaceMember = new WorkspaceMember(dto.userId(), dto.workspaceId());
			if (!workspaceMemberRepository.isLeader(conn, workspaceMember)) {
				throw new ForbiddenException("리더가 아닙니다.");
			}

			if (noticeRepository.delete(conn, dto.noticeId()) == 0)
				throw new NotFoundException("삭제할 공지가 없습니다.");
		} catch (SQLException e) {
			throw new DBException("공지 삭제 실패");
		}
	}

	@Override
	public List<Notice> getPageNotices(NoticeRequest dto) {
		Long workspaceId = dto.workspaceId();
		//        String sort = dto.sort(); todo 정렬기능 미구현
		int page = dto.page();
		int limit = dto.limit();
		int offset = page * limit;

		try (Connection conn = ds.getConnection()) {
			return noticeRepository.findNoticePageByWorkspaceId(conn, dto.workspaceId(), offset, limit);
		} catch (SQLException e) {
			throw new DBException("공지 불러오기 실패");
		}
	}
}
