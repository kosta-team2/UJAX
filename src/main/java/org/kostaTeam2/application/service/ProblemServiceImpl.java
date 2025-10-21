package org.kostaTeam2.application.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.kostaTeam2.domain.problem.ProblemRepository;
import org.kostaTeam2.domain.problem.Problem;
import org.kostaTeam2.global.exception.DBException;

public class ProblemServiceImpl implements ProblemService {
	private final DataSource ds;
	private final ProblemRepository repository;

	public ProblemServiceImpl(DataSource ds, ProblemRepository repository) {
		this.ds = ds;
		this.repository = repository;
	}

	@Override
	public void createProblem(Problem problem) {
		try (Connection con = ds.getConnection()) {
			boolean old = con.getAutoCommit();
			con.setAutoCommit(false);
			try {
				Long problemIdByProblemNum = repository.findProblemIdByProblemNum(con, problem.getProblemNum());
				if (problemIdByProblemNum != null) {
					con.commit();
					return;
				}

				long problemId = repository.saveProblem(con, problem);
				repository.saveSamplesByProblemId(con, problemId, problem.getSamples());
				repository.linkAlgorithmsByProblemId(con, problemId, problem.getTags());
				con.commit();
				con.setAutoCommit(old);
			} catch (SQLException se) {
				con.rollback();
				throw new DBException("문제 생성 처리 중 DB 오류가 발생하였습니다.", se);
			}
		} catch (SQLException e) {
			throw new DBException("로그인 처리 중 db 오류가 발생하였습니다.", e);
		}
	}

}
