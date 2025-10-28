package org.kostaTeam2.application.service.workspace;

import java.util.List;

import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.workspace.chart.CommentStatVO;
import org.kostaTeam2.domain.workspace.chart.SolvedStatVO;
import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.dto.response.WorkspaceProblemPageResponse;

public interface WorkspaceHomeService {
	List<Notice> getNoticeList(Long userId, Long workspaceId, int page, int size);

	List<WorkspaceProblemPageResponse> getWorkspaceProblemList(Long workspaceId, Long userId, int page, int size);

	// teamchart

	/**
	 * 워크스페이스의 레벨이 높은 멤버 n명을 불러온다
	 */
	List<Member> getWorkspaceMemberRanking(Long workspaceId, int limit);

	/**
	 * 워크스페이스 별로 풀이가 많은 멤버 n명을 불러온다
	 */
	List<SolvedStatVO> getWorkspaceMemberSolvedRanking(Long workspaceId, int limit);

	/**
	 * 워크스페이스 별로 댓글을 많이 작성한 멤버 n명을 불러온다
	 */
	List<CommentStatVO> getWorkspaceMemberCommentCountRanking(Long workspaceId, int limit);

}
