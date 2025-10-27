package org.kostaTeam2.application.service.workspace;

import java.util.List;

import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.dto.response.WorkspaceProblemPageResponse;

public interface WorkspaceHomeService {
	List<Notice> getNoticeList(Long userId, Long workspaceId, int page, int size);

	List<WorkspaceProblemPageResponse> getWorkspaceProblemList(Long workspaceId, Long userId, int page, int size);

	// teamchart

	/**
	 * 워크스페이스의 레벨이 높은 5명의 멤버를 불러온다
	 */
	List<Member> getWorkspaceMemberRanking(Long workspaceId, int limit);
}
