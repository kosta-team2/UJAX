package org.kostaTeam2.presentation.controller.page;

import java.time.LocalDateTime;
import java.util.List;

import org.kostaTeam2.application.service.workspace.WorkspaceProblemService;
import org.kostaTeam2.domain.workspace.WorkspaceProblem;
import org.kostaTeam2.dto.request.WorkspaceProblemPageRequest;
import org.kostaTeam2.dto.request.WorkspaceProblemRequest;
import org.kostaTeam2.dto.response.WorkspaceProblemPageResponse;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.global.exception.common.ValidationException;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WorkSpaceProblemController implements Controller {
	private final WorkspaceProblemService service;

	public WorkSpaceProblemController(WorkspaceProblemService service) {
		this.service = service;
	}

	@Override
	public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "create" -> create(request, response);
			case "getProblems" -> getProblems(request, response);
			default -> throw new BadRequestException("methodName이 올바르지 않습니다.");
		};
	}

	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
		var dto = WorkspaceProblemRequest.from(request);

		LocalDateTime scheduleAt = null;
		if (dto.isAlarm() && dto.alarmAt() != null) {
			scheduleAt = dto.dueDate().minusHours(dto.alarmAt());
		}

		Integer save = service.createWorkSpaceProblem(new WorkspaceProblem(
			dto.workspaceId(),
			dto.dueDate(),
			scheduleAt
		), dto.problemNum());

		if (save == null) {
			throw new ValidationException("해당하는 문제 번호가 존재하지 않습니다.",
				"/workspace/problem-register.jsp");
		}

		return new ModelAndView("/front?key=problem&methodName=getProblems&workspaceId=1&workspaceMemberId=1&page=1&size=6", true);
	}

	public ModelAndView getProblems(HttpServletRequest request, HttpServletResponse response) {
		var dto = WorkspaceProblemPageRequest.from(request);

		List<WorkspaceProblemPageResponse> problems = service.getWorkspaceProblemList(
			dto.workspaceId(),
			dto.workspaceMemberId(),
			dto.page(),
			dto.size()
		);

		if (problems.isEmpty()) {
			request.setAttribute("page", dto.page());
			request.setAttribute("totalPages", 0);
			return new ModelAndView("/workspace/problem.jsp");
		}

		//TODO: 페이징 dto로 변경
		request.setAttribute("problems", problems);
		request.setAttribute("page", problems.get(0).getPage());
		request.setAttribute("totalPages", problems.get(0).getTotalPages());

		return new ModelAndView("/workspace/problem.jsp");
	}
}
