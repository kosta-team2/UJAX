package org.kostaTeam2.application.service;

import org.kostaTeam2.dto.response.CommentPageResponse;
import org.kostaTeam2.dto.response.SolutionDetailResponse;
import org.kostaTeam2.dto.response.SubmitterPageResponse;

public interface SolutionService {
	SubmitterPageResponse getSubmitters(Long wsProblemId, int page, int size);

	SolutionDetailResponse getSolutionDetail(Long solutionId, Long wsMemberId);

	SolutionDetailResponse.Like toggleLike(Long solutionId, Long wsMemberId);

	CommentPageResponse getComments(Long solutionId, int page, int size, Long viewerWsMemberId);

	long addComment(Long solutionId, Long wsMemberId, String content);

	void deleteComment(Long commentId, Long wsMemberId);

	void delete(long wsProblemId, long wsMemberId);
}
