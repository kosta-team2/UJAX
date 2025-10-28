package org.kostaTeam2.application.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.kostaTeam2.domain.member.MemberRepository;
import org.kostaTeam2.domain.problem.ProblemRepository;
import org.kostaTeam2.domain.solution.Solution;
import org.kostaTeam2.domain.solution.SolutionRepository;
import org.kostaTeam2.domain.workspace.WorkspaceMember;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.WorkspaceProblemRepository;
import org.kostaTeam2.dto.request.SubmissionIngestRequest;
import org.kostaTeam2.global.exception.DBException;

public class SubmissionServiceImpl implements SubmissionService {
	private final DataSource ds;
	private final WorkspaceMemberRepository workspaceMemberRepository;
	private final ProblemRepository problemRepository;
	private final WorkspaceProblemRepository workspaceProblemRepository;
	private final SolutionRepository solutionRepository;

	public SubmissionServiceImpl(DataSource ds, WorkspaceMemberRepository workspaceMemberRepository,
		ProblemRepository problemRepository, WorkspaceProblemRepository workspaceProblemRepository,
		SolutionRepository solutionRepository) {
		this.ds = ds;
		this.workspaceMemberRepository = workspaceMemberRepository;
		this.problemRepository = problemRepository;
		this.workspaceProblemRepository = workspaceProblemRepository;
		this.solutionRepository = solutionRepository;
	}

	@Override
	public void ingest(Long memberId, SubmissionIngestRequest dto) {
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			try {
				List<WorkspaceMember> members = workspaceMemberRepository.findByMemberId(con, memberId);
				Long problemId = problemRepository.findProblemIdByProblemNum(con,
					dto.getProblem().getProblemNum());

				if (problemId == null) {
					con.commit();
					return;
				}

				boolean success = "맞았습니다!!".equals(dto.getSubmission().getVerdict());
				int timeMs;
				int memoryMb;

				if (dto.getSubmission().getTime().isEmpty()) {
					timeMs = 0;
				} else {
					timeMs = Integer.parseInt(dto.getSubmission().getTime());
				}

				if (dto.getSubmission().getMemory().isEmpty()) {
					memoryMb = 0;
				} else {
					memoryMb = Integer.parseInt(dto.getSubmission().getMemory());
				}

				for (WorkspaceMember wm : members) {
					long wsId = wm.getWorkspaceId();
					Long wpId = workspaceProblemRepository.findWorkspaceProblemIdByWsIdANDProblemId(con, wsId,
						problemId);
					if (wpId == null)
						continue;

					Solution sol = new Solution(
						wpId,
						wm.getWorkspaceMemberId(),
						success,
						timeMs,
						memoryMb,
						dto.getSubmission().getCode()
					);

					solutionRepository.saveSolution(con, sol);

				}

				con.commit();
			} catch (Exception e) {
				con.rollback();
				throw e;
			} finally {
				con.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new DBException("제출 코드를 저장하는 과정에서 DB 에러가 발생하였습니다.", e);
		}
	}
}
