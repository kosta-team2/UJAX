package org.kostaTeam2.presentation.controller.page;

import org.kostaTeam2.application.service.workspace.NoticeService;
import org.kostaTeam2.dto.request.NoticeRequest;
import org.kostaTeam2.dto.response.NoticePage;
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
		Long workspaceId = Long.valueOf(request.getParameter("wsId"));
		String title = request.getParameter("noticeTitle");
		String content = request.getParameter("noticeContent");

		noticeService.create(NoticeRequest
			.fromCreate(
				sessionUser.memberId(),
				workspaceId,
				title,
				content
			));

		String target = request.getContextPath() + "/workspace";
		return new ModelAndView(target, true);
	}

	private ModelAndView getList(HttpServletRequest request, HttpServletResponse response) {
		Long workspaceId = Long.valueOf(request.getParameter("wsId"));
		String sort = request.getParameter("sort");
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer limit = Integer.valueOf(request.getParameter("size"));

		NoticePage noticePage = noticeService.getPaged(NoticeRequest
			.fromReadList(
				workspaceId,
				sort,
				page,
				limit
			));
		request.setAttribute("wsId", workspaceId);
		request.setAttribute("notices", noticePage.getNotices());
		request.setAttribute("page", noticePage.getPage());
		request.setAttribute("size", noticePage.getSize());
		request.setAttribute("sort", sort == null ? "latest" : sort);
		request.setAttribute("totalPages", noticePage.getTotalPages());
		request.setAttribute("hasPrev", noticePage.isHasPrev());
		request.setAttribute("hasNext", noticePage.isHasNext());
		request.setAttribute("prevPage", noticePage.getPrevPage());
		request.setAttribute("nextPage", noticePage.getNextPage());
		request.setAttribute("startPage", noticePage.getStartPage());
		request.setAttribute("endPage", noticePage.getEndPage());

		String target = request.getContextPath() + "/workspace/notice.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		Long workspaceId = Long.valueOf(request.getParameter("wsId"));
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
