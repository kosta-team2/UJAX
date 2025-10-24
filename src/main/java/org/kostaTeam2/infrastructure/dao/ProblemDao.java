package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kostaTeam2.domain.problem.AlgorithmTag;
import org.kostaTeam2.domain.problem.Problem;
import org.kostaTeam2.domain.problem.ProblemRepository;
import org.kostaTeam2.domain.problem.Sample;
import org.kostaTeam2.domain.workspace.WorkspaceProblem;
import org.kostaTeam2.global.exception.DBException;

public class ProblemDao implements ProblemRepository {

	@Override
	public Long findProblemIdByProblemNum(Connection con, int problemNum) {
		String sql = "SELECT problem_id FROM problem where problem_num = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, problemNum);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("problem_id");
				}
				return null;
			}
		} catch (SQLException e) {
			throw new DBException("findIdByProblemNum DB 오류", e);
		}
	}

	@Override
	public Optional<Problem> findProblemByProblemId(Connection con, Long problemId) {
		String sql = "SELECT * FROM problem where problem_id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, problemId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(new Problem(
						rs.getLong(1),
						rs.getInt(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9),
						rs.getString(10)
					));
				}
				return Optional.empty();
			}
		} catch (SQLException e) {
			throw new DBException("findIdByProblemNum DB 오류", e);
		}
	}

	@Override
	public List<AlgorithmTag> findAlgorithmTagsByProblemId(Connection con, Long problemId) {
		String sql = """
			SELECT a.algorithm_name
			FROM problem_algorithm pa
			JOIN algorithm a ON a.algorithm_id = pa.algorithm_id
			WHERE pa.problem_id = ?
			ORDER BY pa.algorithm_id desc
			LIMIT 2
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, problemId);

			try (ResultSet rs = ps.executeQuery()) {
				List<AlgorithmTag> list = new ArrayList<>();
				while (rs.next()) {
					list.add(new AlgorithmTag(
						rs.getString(1)
					));
				}
				return list;
			}
		} catch (SQLException e) {
			throw new DBException("findAlgorithmTagsByProblemId DB 에러", e);
		}
	}

	@Override
	public List<Sample> findSamplesByProblemId(Connection con, Long problemId) {
		String sql = """
			SELECT sample_index, sample_input, sample_output
			FROM sample
			WHERE sample.problem_id = ?
			ORDER BY sample.sample_input
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, problemId);

			try (ResultSet rs = ps.executeQuery()) {
				List<Sample> list = new ArrayList<>();
				while (rs.next()) {
					list.add(new Sample(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3)
					));
				}
				return list;
			}
		} catch (SQLException e) {
			throw new DBException("findAlgorithmTagsByProblemId DB 에러", e);
		}
	}

	@Override
	public long saveProblem(Connection con, Problem problem) {
		String sql = """
			INSERT INTO problem(problem_num, title, tier, time_limit_raw, memory_limit_raw, problem_desc, problem_input, problem_output, url)
			values (?, ?, ?, ?, ?, ?, ?, ?, ?)
			""";

		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, problem.getProblemNum());
			ps.setString(2, problem.getTitle());
			ps.setString(3, problem.getTier());
			ps.setString(4, problem.getTimeLimit());
			ps.setString(5, problem.getMemoryLimit());
			ps.setString(6, problem.getProblemDesc());
			ps.setString(7, problem.getProblemInput());
			ps.setString(8, problem.getProblemOutput());
			ps.setString(9, problem.getUrl());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					return rs.getLong(1);
			}
			throw new DBException("이미 존재하는 id 번호입니다.");
		} catch (SQLException e) {
			throw new DBException("findIdByProblemNum DB 오류", e);
		}
	}

	@Override
	public void saveSamplesByProblemId(Connection con, long problemId, List<Sample> samples) {
		if (samples == null || samples.isEmpty())
			return;

		final String sql = """
			INSERT INTO sample (problem_id, sample_index, sample_input, sample_output)
			VALUES (?, ?, ?, ?)
			ON DUPLICATE KEY UPDATE
			  sample_input = VALUES(sample_input),
			  sample_output = VALUES(sample_output)
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			for (Sample s : samples) {
				ps.setLong(1, problemId);
				ps.setInt(2, s.getSampleIndex());
				ps.setString(3, s.getInput());
				ps.setString(4, s.getOutput());
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			throw new DBException("saveSamplesByProblemId DB 오류", e);
		}
	}

	@Override
	public void linkAlgorithmsByProblemId(Connection con, long problemId, List<AlgorithmTag> tags) {
		final String upsertAlgoSql = """
			INSERT INTO algorithm (algorithm_name)
			VALUES (?)
			ON DUPLICATE KEY UPDATE algorithm_id = LAST_INSERT_ID(algorithm_id)
			""";
		final String linkSql = "INSERT IGNORE INTO problem_algorithm (algorithm_id, problem_id) VALUES (?, ?)";

		try (PreparedStatement psAlgo = con.prepareStatement(upsertAlgoSql, Statement.RETURN_GENERATED_KEYS);
			 PreparedStatement psLink = con.prepareStatement(linkSql)) {

			for (AlgorithmTag tag : tags) {
				Integer algoId = tag.getId();
				if (algoId == null) {
					psAlgo.setString(1, tag.getName());
					psAlgo.executeUpdate();
					try (ResultSet k = psAlgo.getGeneratedKeys()) {
						if (k.next())
							algoId = k.getInt(1);
					}
					if (algoId == null) {
						algoId = selectAlgorithmIdByName(con, tag.getName());
						if (algoId == null)
							throw new DBException("algorithm id 조회 실패: " + tag.getName());
					}
				}

				psLink.setInt(1, algoId);
				psLink.setLong(2, problemId);
				psLink.addBatch();
			}
			psLink.executeBatch();
		} catch (SQLException e) {
			throw new DBException("linkAlgorithmsByProblemId DB 오류", e);
		}
	}

	private Integer selectAlgorithmIdByName(Connection con, String name) throws SQLException {
		final String sql = "SELECT algorithm_id FROM algorithm WHERE algorithm_name=? LIMIT 1";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt(1) : null;
			}
		}
	}
}
