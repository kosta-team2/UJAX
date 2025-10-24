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
import org.kostaTeam2.domain.problem.Sample;
import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceProblem;
import org.kostaTeam2.domain.workspace.WorkspaceProblemRepository;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;
import org.kostaTeam2.dto.response.ProblemInfoResponse;
import org.kostaTeam2.dto.response.WorkspaceProblemPageResponse;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.NotFoundException;

public class WorkspaceProblemServiceImpl implements WorkspaceProblemService {
	private final DataSource ds;
	private final WorkspaceRepository workspaceRepository;
	private final WorkspaceProblemRepository workspaceProblemRepository;
	private final ProblemRepository problemRepository;

	public WorkspaceProblemServiceImpl(DataSource ds, WorkspaceRepository workspaceRepository,
		WorkspaceProblemRepository workspaceProblemRepo, ProblemRepository problemRepository) {
		this.ds = ds;
		this.workspaceRepository = workspaceRepository;
		this.workspaceProblemRepository = workspaceProblemRepo;
		this.problemRepository = problemRepository;
	}

	@Override
	public Integer createWorkSpaceProblem(WorkspaceProblem workspaceProblem, int setProblemNum) {
		try (Connection con = ds.getConnection()) {
			boolean oldAuto = con.getAutoCommit();
			try {
				con.setAutoCommit(false);
				Long problemIdByProblemNum = problemRepository.findProblemIdByProblemNum(con, setProblemNum);
				if (problemIdByProblemNum == null) {
					return null;
				}

				Integer newId = workspaceProblemRepository.saveWorkspaceProblem(con, new WorkspaceProblem(
					workspaceProblem.getWsId(),
					problemIdByProblemNum,
					workspaceProblem.getDeadLine(),
					workspaceProblem.getScheduleAt()
				));

				con.commit();
				return newId;
			} catch (Exception e) {
				con.rollback();
				throw new DBException("워크스페이스 문제 생성 중 db 오류가 발생하였습니다.", e);
			} finally {
				con.setAutoCommit(oldAuto);
			}
		} catch (SQLException e) {
			throw new DBException("워크스페이스 문제 생성 중 db 오류가 발생하였습니다.", e);
		}
	}

	@Override
	public List<WorkspaceProblemPageResponse> getWorkspaceProblemList(Long workspaceId, Long workspaceMemberId,
		int page, int size) {
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
						con, wp.getWsProblemId(), workspaceMemberId);

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

	@Override
	public ProblemInfoResponse getProblemDetail(Long problemId) {
		try (Connection con = ds.getConnection()) {

			Optional<Problem> problem = problemRepository.findProblemByProblemId(con, problemId);
			List<Sample> samples = problemRepository.findSamplesByProblemId(con, problemId);

			if (problem.isPresent()) {
				Problem p = problem.get();
				return ProblemInfoResponse.of(
					p.getProblemNum(),
					p.getTitle(),
					p.getProblemDesc(),
					p.getProblemInput(),
					p.getProblemOutput(),
					samples,
					p.getUrl(),
					p.getTimeLimit(),
					p.getMemoryLimit()
				);
			} else {
				throw new NotFoundException("해당하는 문제에 대한 정보를 가져오지 못했습니다.");
			}
		} catch (SQLException e) {
			throw new DBException("문제 상세를 불러오는 중 db 오류가 발생하였습니다.", e);
		}
	}

}
