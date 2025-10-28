package org.kostaTeam2.application.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.kostaTeam2.domain.problem.ProblemRepository;
import org.kostaTeam2.domain.solution.Solution;
import org.kostaTeam2.domain.solution.SolutionRepository;
import org.kostaTeam2.dto.response.CommentPageResponse;
import org.kostaTeam2.dto.response.SolutionDetailResponse;
import org.kostaTeam2.dto.response.SubmitterPageResponse;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.ForbiddenException;
import org.kostaTeam2.global.exception.NotFoundException;

public class SolutionServiceImpl implements SolutionService {
	private final DataSource ds;
	private final SolutionRepository solutionRepository;

	public SolutionServiceImpl(DataSource ds, SolutionRepository solutionRepository) {
		this.ds = ds;
		this.solutionRepository = solutionRepository;
	}

	@Override
	public SubmitterPageResponse getSubmitters(Long wsProblemId, int page, int size) {
		try (Connection con = ds.getConnection()) {
			if (page < 1) page = 1;
			if (size < 1) size = 5;
			int offset = (page - 1) * size;

			var rows = solutionRepository.findSubmitters(con, wsProblemId, offset, size);
			int total = solutionRepository.countMembersForWsProblem(con, wsProblemId);

			List<SubmitterPageResponse.MemberTab> members = new ArrayList<>(rows.size());
			Long primeWsMemberId = null;
			Long primeSolutionId = null;

			for (var r : rows) {
				boolean has = (r.latestSolutionId != null);
				members.add(new SubmitterPageResponse.MemberTab(
					r.wsMemberId,
					r.nickname,
					has,
					r.latestSolutionId
				));
				if (primeSolutionId == null && has) {
					primeWsMemberId = r.wsMemberId;
					primeSolutionId = r.latestSolutionId;
				}
			}

			SubmitterPageResponse.Prime prime = (primeSolutionId == null)
				? null
				: new SubmitterPageResponse.Prime(primeWsMemberId, primeSolutionId);

			return new SubmitterPageResponse(members, page, size, total, prime);

		} catch (SQLException e) {
			throw new DBException("제출자 명단을 찾는 중 db 오류가 발생하였습니다.", e);
		}
	}

	@Override
	public SolutionDetailResponse getSolutionDetail(Long solutionId, Long viewerWsMemberId) {
		try (Connection con = ds.getConnection()) {
			Solution s = solutionRepository.findSolutionById(con, solutionId)
				.orElseThrow(() -> new NotFoundException("해당 제출을 찾을 수 없습니다."));

			String status = s.isStatus() ? "success" : "fail";
			Integer timeMs = s.getTimeMs() == 0 ? null : s.getTimeMs();
			Integer memoryMb = s.getMemoryMb() == 0 ? null : s.getMemoryMb();

			int likeCount = solutionRepository.countLikes(con, solutionId);
			boolean likedByMe = (viewerWsMemberId != null)
				&& solutionRepository.existsLike(con, solutionId, viewerWsMemberId);

			var like = new SolutionDetailResponse.Like(likedByMe, likeCount);
			int commentCount = solutionRepository.countComments(con, solutionId);;

			return new SolutionDetailResponse(
				s.getSolutionId(),
				status,
				timeMs,
				memoryMb,
				s.getCode(),
				like,
				commentCount
			);

		} catch (SQLException e) {
			throw new DBException("상세 제출 코드를 찾는 중 db 오류가 발생하였습니다.", e);
		}
	}

	@Override
	public SolutionDetailResponse.Like toggleLike(Long solutionId, Long wsMemberId) {
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			boolean exists = solutionRepository.existsLike(con, solutionId, wsMemberId);
			if (exists) {
				solutionRepository.removeLike(con, solutionId, wsMemberId);
			} else {
				solutionRepository.addLike(con, solutionId, wsMemberId);
			}
			int cnt = solutionRepository.countLikes(con, solutionId);
			con.commit();
			return new SolutionDetailResponse.Like(!exists, cnt);
		} catch (SQLException e) {
			throw new DBException("좋아요 토글 중 DB 오류", e);
		}
	}

	@Override
	public CommentPageResponse getComments(Long solutionId, int page, int size, Long viewerWsMemberId) {
		try (Connection con = ds.getConnection()) {
			if (page < 1) page = 1;
			if (size < 1) size = 5;
			int offset = (page - 1) * size;

			var rows = solutionRepository.findComments(con, solutionId, offset, size);
			int total = solutionRepository.countComments(con, solutionId);

			List<CommentPageResponse.Item> items = new ArrayList<>(rows.size());
			for (var r : rows) {
				boolean isOwner = (viewerWsMemberId != null && viewerWsMemberId == r.wsMemberId);
				String ts = r.createdAt.toString().replace('T', ' ').substring(0, 19); // yyyy-MM-dd HH:mm:ss
				items.add(new CommentPageResponse.Item(
					r.commentId,
					r.content,
					ts,
					isOwner,
					new CommentPageResponse.User(r.wsMemberId, r.nickname)
				));
			}
			return new CommentPageResponse(items, page, size, total);
		} catch (SQLException e) {
			throw new DBException("댓글 조회 중 DB 오류", e);
		}
	}

	@Override
	public long addComment(Long solutionId, Long wsMemberId, String content) {
		if (wsMemberId == null) throw new BadRequestException("로그인이 필요합니다.");
		if (content == null || content.trim().isEmpty()) throw new BadRequestException("댓글 내용을 입력해 주세요.");

		try (Connection con = ds.getConnection()) {
			return solutionRepository.insertComment(con, solutionId, wsMemberId, content.trim());
		} catch (SQLException e) {
			throw new DBException("댓글 등록 중 DB 오류", e);
		}
	}

	@Override
	public void deleteComment(Long commentId, Long wsMemberId) {
		if (wsMemberId == null) throw new BadRequestException("권한이 없습니다.");
		try (Connection con = ds.getConnection()) {
			int affected = solutionRepository.deleteComment(con, commentId, wsMemberId);
			if (affected == 0) throw new BadRequestException("삭제할 수 없습니다.");
		} catch (SQLException e) {
			throw new DBException("댓글 삭제 중 DB 오류", e);
		}
	}

	@Override
	public void delete(long wsProblemId, long wsMemberId) {
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);

			boolean allowed = solutionRepository.isLeaderOfWsProblem(con, wsProblemId, wsMemberId);
			if (!allowed) throw new ForbiddenException("삭제 권한이 없습니다.");

			solutionRepository.deleteWorkspaceProblem(con, wsProblemId);
			solutionRepository.deleteSolutionsByWsProblem(con, wsProblemId);

			con.commit();
		} catch (SQLException e) {
			throw new DBException("워크스페이스 문제 삭제 중 DB 오류", e);
		}
	}
}
