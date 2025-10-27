package org.kostaTeam2.application.service.workspace;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.kostaTeam2.domain.problem.AlgorithmTag;
import org.kostaTeam2.domain.problem.Problem;
import org.kostaTeam2.domain.problem.ProblemRepository;
import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.WorkspaceProblem;
import org.kostaTeam2.domain.workspace.WorkspaceProblemRepository;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;
import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.domain.workspace.notice.NoticeRepository;
import org.kostaTeam2.dto.response.WorkspaceProblemPageResponse;
import org.kostaTeam2.global.exception.DBException;

public class WorkspaceHomeServiceImpl implements WorkspaceHomeService {
	private final DataSource ds;
	private final NoticeRepository noticeRepository;
	private final WorkspaceRepository workspaceRepository;
	private final WorkspaceProblemRepository workspaceProblemRepository;
	private final ProblemRepository problemRepository;
	private final WorkspaceMemberRepository workspaceMemberRepo;

	public WorkspaceHomeServiceImpl(DataSource ds, NoticeRepository noticeRepository,
		WorkspaceRepository workspaceRepository,
		WorkspaceProblemRepository workspaceProblemRepository, ProblemRepository problemRepository,
		WorkspaceMemberRepository workspaceMemberRepo) {
		this.ds = ds;
		this.noticeRepository = noticeRepository;
		this.workspaceRepository = workspaceRepository;
		this.workspaceProblemRepository = workspaceProblemRepository;
		this.problemRepository = problemRepository;
		this.workspaceMemberRepo = workspaceMemberRepo;
	}

	@Override
	public List<Notice> getNoticeList(Long userId, Long workspaceId, int page, int size) {
		try (Connection conn = ds.getConnection()) {
			int total = noticeRepository.countByWorkspace(conn, workspaceId);
			int totalPages = (int)Math.ceil(total / (double)size);
			if (totalPages == 0) {
				totalPages = 1;
			}

			page = Math.max(1, Math.min(totalPages, page));
			int offset = (page - 1) * size;

			return noticeRepository.findPageByWorkspace(conn, workspaceId, offset, size);
		} catch (SQLException e) {
			throw new DBException("공지 불러오기 실패");
		}
	}

	@Override
	public List<WorkspaceProblemPageResponse> getWorkspaceProblemList(Long workspaceId, Long userId, int page,
		int size) {
		try (Connection con = ds.getConnection()) {
			boolean oldAuto = con.getAutoCommit();
			try {
				con.setAutoCommit(false);

				List<WorkspaceProblem> list = workspaceProblemRepository.findWorkspaceProblemsByWorkspaceId(
					con, workspaceId, page, size);
				int total = workspaceProblemRepository.countByWorkspaceId(con, workspaceId);
				int totalPages = (int)Math.ceil((double)total / size);

				Optional<Workspace> workspace = workspaceRepository.findById(con, workspaceId);
				boolean hintView = workspace.isPresent() && Boolean.TRUE.equals(workspace.get().isHintView());

				List<WorkspaceProblemPageResponse> responses = new ArrayList<>(list.size());

				for (WorkspaceProblem wp : list) {
					Optional<Problem> problem = problemRepository.findProblemByProblemId(con,
						wp.getProblemId());

					if (problem.isEmpty()) {
						throw new DBException("문제 정보를 찾을 수 없습니다.");
					}

					Problem p = problem.get();

					int submitCount = workspaceProblemRepository.findSuccessMembersByWorkspaceProblemId(
						con, wp.getWsProblemId());
					boolean submitted = workspaceProblemRepository.findMemberStatusByWorkspaceProblemIdAndWorkspaceMemberId(
						con, wp.getWsProblemId(), userId);

					List<AlgorithmTag> tags = hintView
						? problemRepository.findAlgorithmTagsByProblemId(con, wp.getProblemId())
						: List.of();

					String diff = p.getTier();
					String diffClass = diff.split(" ")[0];

					responses.add(WorkspaceProblemPageResponse.of(
						wp.getWsProblemId(),
						workspaceId,
						wp.getProblemId(),
						p.getProblemNum(),
						p.getTitle(),
						diff,
						diffClass,
						submitted,
						wp.getDeadLine(),
						submitCount,
						tags,
						page,
						totalPages
					));
				}
				return responses;

			} catch (Exception e) {
				con.rollback();
				e.printStackTrace();
				throw new DBException("워크스페이스 리스트를 불러오는 중 db 오류가 발생하였습니다.", e);
			} finally {
				con.setAutoCommit(oldAuto);
			}
		} catch (
			SQLException e) {
			throw new DBException("워크스페이스 리스트를 불러오는 중 db 오류가 발생하였습니다.", e);
		}
	}
}
