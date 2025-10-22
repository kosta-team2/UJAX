package org.kostaTeam2.application.service.workspace;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.kostaTeam2.domain.problem.ProblemRepository;
import org.kostaTeam2.domain.workspace.WorkspaceProblem;
import org.kostaTeam2.domain.workspace.WorkspaceProblemRepository;
import org.kostaTeam2.global.exception.DBException;

public class WorkspaceProblemServiceImpl implements WorkspaceProblemService {
	private final DataSource ds;
	private final WorkspaceProblemRepository repository;
	private final ProblemRepository problemRepository;

	public WorkspaceProblemServiceImpl(DataSource ds, WorkspaceProblemRepository workspaceProblemRepo,
		ProblemRepository problemRepository) {
		this.ds = ds;
		this.repository = workspaceProblemRepo;
		this.problemRepository = problemRepository;
	}

	@Override
	public Integer createWorkSpaceProblem(WorkspaceProblem workspaceProblem, int setProblemNum) {
		try (Connection con = ds.getConnection()) {
			Long problemIdByProblemNum = problemRepository.findProblemIdByProblemNum(con, setProblemNum);
			if (problemIdByProblemNum == null) {
				return null;
			}
			return repository.saveWorkspaceProblem(con, new WorkspaceProblem(
				workspaceProblem.getWsId(),
				problemIdByProblemNum,
				workspaceProblem.getDeadLine(),
				workspaceProblem.getScheduleAt()
			));
		} catch (SQLException e) {
			throw new DBException("워크스페이스 문제 생성 중 db 오류가 발생하였습니다.", e);
		}
	}
}
