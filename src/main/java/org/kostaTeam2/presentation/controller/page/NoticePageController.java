package org.kostaTeam2.presentation.controller.page;

import java.util.List;

import org.kostaTeam2.application.service.workspace.NoticeService;
import org.kostaTeam2.domain.workspace.notice.Notice;
import org.kostaTeam2.dto.request.NoticeRequest;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NoticePageController implements Controller {
	private final NoticeService noticeService;

	public NoticePageController(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	@Override
	public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "createNotice" -> create(request, response);
			case "getNotices" -> getList(request, response);
			case "deleteNotice" -> delete(request, response);
			default -> throw new BadRequestException("workspace methodName이 올바르지 않습니다.");
		};
	}

	private ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		Long workspaceId = Long.valueOf(request.getParameter("workspaceId"));
		String title = request.getParameter("noticeTitle");
		String content = request.getParameter("noticeContent");

		noticeService.create(NoticeRequest
			.fromCreate(
				sessionUser.memberId(),
				workspaceId,
				title,
				content
			));

		//        request.setAttribute("wsId", workspaceId);
		// todo 과연 어떤 워크스페이스의 notice.jsp로?
		String target = request.getContextPath() + "/workspace/";
		return new ModelAndView(target, true);
	}

	private ModelAndView getList(HttpServletRequest request, HttpServletResponse response) {
		Long workspaceId = Long.valueOf(request.getParameter("workspaceId"));
		String sort = request.getParameter("sort");
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer limit = Integer.valueOf(request.getParameter("limit"));

		List<Notice> noticeList = noticeService.getPageNotices(NoticeRequest
			.fromReadList(
				workspaceId,
				sort,
				page,
				limit
			));

		request.setAttribute("noticeList", noticeList);
		String target = request.getContextPath() + "/workspace/notice.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		Long workspaceId = Long.valueOf(request.getParameter("workspaceId"));
		Long noticeId = Long.valueOf(request.getParameter("noticeId"));

		noticeService.delete(NoticeRequest
			.fromDelete(
				sessionUser.memberId(),
				workspaceId,
				noticeId
			));

		String target = request.getContextPath() + "/workspace/notice.jsp";
		return new ModelAndView(target);
	}

}
