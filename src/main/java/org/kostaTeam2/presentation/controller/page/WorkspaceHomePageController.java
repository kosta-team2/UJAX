package org.kostaTeam2.presentation.controller.page;

import java.util.List;

import org.kostaTeam2.application.service.workspace.WorkspaceHomeService;
import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.workspace.chart.CommentStatVO;
import org.kostaTeam2.domain.workspace.chart.SolvedStatVO;
import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.dto.response.WorkspaceProblemPageResponse;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WorkspaceHomePageController implements Controller {
	private final WorkspaceHomeService workspaceHomeService;

	public WorkspaceHomePageController(WorkspaceHomeService workspaceHomeService) {
		this.workspaceHomeService = workspaceHomeService;
	}

	@Override
	public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "getWorkspaceHome" -> getHomeInfo(request, response);
			default -> throw new BadRequestException("workspaceHome methodName이 올바르지 않습니다.");
		};
	}

	private ModelAndView getHomeInfo(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		Long userId = sessionUser.memberId();
		Long workspaceId = Long.valueOf(request.getParameter("workspaceId"));
		Integer size = Integer.valueOf(request.getParameter("size"));
		Integer page = Integer.valueOf(request.getParameter("page"));

		List<Notice> notices = workspaceHomeService.getNoticeList(userId, workspaceId, page, size);
		List<WorkspaceProblemPageResponse> problems = workspaceHomeService.getWorkspaceProblemList(workspaceId, userId,
			page, size);

		// 팀 차트 서비스
		List<Member> topLevel = workspaceHomeService.getWorkspaceMemberRanking(workspaceId, 5);
		List<SolvedStatVO> topSolved = workspaceHomeService.getWorkspaceMemberSolvedRanking(workspaceId, 5);
		List<CommentStatVO> topComment = workspaceHomeService.getWorkspaceMemberCommentCountRanking(workspaceId, 5);

		request.setAttribute("notices", notices);
		request.setAttribute("problems", problems);
		request.setAttribute("topLevel", topLevel);
		request.setAttribute("topSolved", topSolved);
		request.setAttribute("topComment", topComment);

		String target = request.getContextPath() + "/workspace/home.jsp";
		return new ModelAndView(target);
	}

}
