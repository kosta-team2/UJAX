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
import org.kostaTeam2.dto.response.NoticePage;
import org.kostaTeam2.global.exception.DBException;
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
			//            if (!workspaceMemberRepository.isLeader(conn, workspaceMember)) {
			//                throw new ForbiddenException("리더가 아닙니다.");
			//            }

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
			//            if (!workspaceMemberRepository.isLeader(conn, workspaceMember)) {
			//                throw new ForbiddenException("리더가 아닙니다.");
			//            }

			if (noticeRepository.delete(conn, dto.noticeId()) == 0)
				throw new NotFoundException("삭제할 공지가 없습니다.");
		} catch (SQLException e) {
			throw new DBException("공지 삭제 실패");
		}
	}

	@Override
	public NoticePage getPaged(NoticeRequest dto) {
		Long workspaceId = dto.workspaceId();
		//        String sort = dto.sort(); todo 정렬기능 미구현
		int page = dto.page();
		int size = dto.size();

		try (Connection conn = ds.getConnection()) {
			int total = noticeRepository.countByWorkspace(conn, workspaceId);
			int totalPages = (int)Math.ceil(total / (double)size);
			if (totalPages == 0) {
				totalPages = 1;
			}

			page = Math.max(1, Math.min(totalPages, page));
			int offset = (page - 1) * size;

			List<Notice> list = noticeRepository.findPageByWorkspace(conn, workspaceId, offset, size);

			int window = 5;
			int startPage = Math.max(1, page - window / 2);
			int endPage = Math.min(totalPages, startPage + window - 1);
			startPage = Math.max(1, endPage - window + 1);

			boolean hasPrev = page > 1;
			boolean hasNext = page < totalPages;

			return new NoticePage(
				list, page, size, totalPages,
				hasPrev, hasNext,
				Math.max(1, page - 1),
				Math.min(totalPages, page + 1),
				startPage, endPage
			);
		} catch (SQLException e) {
			throw new DBException("공지 불러오기 실패");
		}
	}
}
