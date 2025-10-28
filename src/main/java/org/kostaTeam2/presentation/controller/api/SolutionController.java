package org.kostaTeam2.presentation.controller.api;

import java.io.IOException;
import java.util.Map;

import org.kostaTeam2.application.service.SolutionService;
import org.kostaTeam2.dto.response.SolutionDetailResponse;
import org.kostaTeam2.dto.response.SubmitterPageResponse;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.view.JsonResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SolutionController implements RestController {
	private final SolutionService solutionService;

	public SolutionController(SolutionService solutionService) {
		this.solutionService = solutionService;
	}

	@Override
	public JsonResult handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "getSubmitters" -> getSubmitters(request, response);
			case "getSolutionDetail" -> getSolutionDetail(request, response);
			case "toggleLike" -> toggleLike(request, response);
			case "getComments" -> getComments(request, response);
			case "addComment" -> addComment(request, response);
			case "deleteComment" -> deleteComment(request, response);
			case "delete" -> delete(request, response);
			default -> throw new BadRequestException("methodName이 올바르지 않습니다.");
		};
	}

	private JsonResult getSubmitters(HttpServletRequest req, HttpServletResponse res) throws IOException {
		long wsProblemId = Long.parseLong(req.getParameter("wsProblemId"));
		int page = Integer.parseInt(req.getParameter("page"));
		int size = Integer.parseInt(req.getParameter("size"));

		SubmitterPageResponse submitters = solutionService.getSubmitters(wsProblemId, page, size);

		return new JsonResult(submitters);
	}

	private JsonResult getSolutionDetail(HttpServletRequest req, HttpServletResponse res) throws IOException {
		long solutionId = Long.parseLong(req.getParameter("solutionId"));
		String wsMemberIdStr = req.getParameter("wsMemberId");
		Long wsMemberId = (wsMemberIdStr == null || wsMemberIdStr.isBlank())
			? null : Long.parseLong(wsMemberIdStr);

		SolutionDetailResponse detail = solutionService.getSolutionDetail(solutionId, wsMemberId);
		return new JsonResult(detail);
	}

	private JsonResult toggleLike(HttpServletRequest req, HttpServletResponse res) {
		long solutionId = Long.parseLong(req.getParameter("solutionId"));
		long wsMemberId = Long.parseLong(req.getParameter("wsMemberId"));
		var like = solutionService.toggleLike(solutionId, wsMemberId);
		return new JsonResult(like);
	}

	private JsonResult getComments(HttpServletRequest req, HttpServletResponse res) {
		long solutionId = Long.parseLong(req.getParameter("solutionId"));
		int page = Integer.parseInt(req.getParameter("page"));
		int size = Integer.parseInt(req.getParameter("size"));
		String viewer = req.getParameter("wsMemberId");
		Long viewerWsMemberId = (viewer == null || viewer.isBlank()) ? null : Long.parseLong(viewer);

		var data = solutionService.getComments(solutionId, page, size, viewerWsMemberId);
		return new JsonResult(data);
	}

	private JsonResult addComment(HttpServletRequest req, HttpServletResponse res) {
		long solutionId = Long.parseLong(req.getParameter("solutionId"));
		long wsMemberId = Long.parseLong(req.getParameter("wsMemberId"));
		String content = req.getParameter("content");

		long id = solutionService.addComment(solutionId, wsMemberId, content);
		return new JsonResult(Map.of("id", id));
	}

	private JsonResult deleteComment(HttpServletRequest req, HttpServletResponse res) {
		long commentId = Long.parseLong(req.getParameter("commentId"));
		long wsMemberId = Long.parseLong(req.getParameter("wsMemberId"));
		solutionService.deleteComment(commentId, wsMemberId);
		return new JsonResult(Map.of("ok", true));
	}

	private JsonResult delete(HttpServletRequest req, HttpServletResponse res) {
		long wsProblemId = Long.parseLong(req.getParameter("wsProblemId"));
		long wsMemberId = Long.parseLong(req.getParameter("wsMemberId"));

		solutionService.delete(wsProblemId, wsMemberId);
		return new JsonResult(Map.of("ok", true));
	}

}
