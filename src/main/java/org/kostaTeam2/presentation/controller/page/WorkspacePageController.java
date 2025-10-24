package org.kostaTeam2.presentation.controller.page;

import org.kostaTeam2.application.service.workspace.WorkspaceService;
import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.dto.request.WorkspaceRequest;
import org.kostaTeam2.dto.request.WorkspaceUserRequest;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.global.exception.common.AppException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WorkspacePageController implements Controller {
	private final WorkspaceService workspaceService;

	public WorkspacePageController(WorkspaceService workspaceService) {
		this.workspaceService = workspaceService;
	}

    @Override
    public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
            Exception {
        return switch (methodName) {
            case "create" -> createWorkspace(request, response);
            case "show" -> showWorkspace(request, response);
            case "update" -> updateWorkspace(request, response);
            case "delete" -> deleteWorkspace(request, response);
            case "updateRole" -> delegateLeader(request, response);
            case "kickUser" -> kickUser(request, response);
            case "exit" -> exitWorkspace(request, response);
            default -> throw new BadRequestException("workspace methodName이 올바르지 않습니다.");
        };
    }

	private ModelAndView createWorkspace(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		var dto = WorkspaceRequest.createDto(request, sessionUser);

        Workspace workspace = workspaceService.createWorkspace(dto)
                                              .orElseThrow(() -> new AppException(500, "워크스페이스 생성에 실패 했습니다. 다시 시도해 주십시오."));

        request.setAttribute("workspace", workspace);

        String target = request.getContextPath()
                + "/front?key=workspace&methodName=show&workspaceId=" + workspace.getWorkspaceId();
        return new ModelAndView(target, true);
    }

		// todo 새로 생성한 워크스페이스 홈으로 이동시켜주기?
		String target = request.getContextPath() + "/workspace/create.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView showWorkspace(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		var dto = WorkspaceRequest.showDto(request, sessionUser);
        Workspace workspace = workspaceService.getWorkspaceInfo(dto)
                                              .orElseThrow(() -> new AppException(500, "워크스페이스 조회에 실패 했습니다. 다시 시도해 주십시오."));

		Workspace workspace = workspaceService.getWorkspaceInfo(dto)
			.orElseThrow(() -> new AppException(500, "워크스페이스 조회에 실패 했습니다. 다시 시도해 주십시오."));

		request.setAttribute("workspace", workspace);

        // session 유효성 검증은 filter로...
        request.setAttribute("currentUserId", sessionUser.memberId());      // long
        request.setAttribute("currentUserEmail", sessionUser.email());      // String
        request.setAttribute("currentUserNickname", sessionUser.nickname());// String


        //리더 여부는 서버에서 계산
        boolean isLeader = workspace.getWorkspaceMemberList() != null &&
                sessionUser != null &&
                workspace.getWorkspaceMemberList().stream()
                         .anyMatch(m -> m.getMemberId() == sessionUser.memberId() && m.isLeader());
        request.setAttribute("isLeader", isLeader);

        String target = request.getContextPath() + "/workspace/info.jsp";
        return new ModelAndView(target);
    }

		String target = request.getContextPath() + "/workspace";
		return new ModelAndView(target);
	}

	private ModelAndView updateWorkspace(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		var dto = WorkspaceRequest.updateDto(request, sessionUser);

		Workspace workspace = workspaceService.updateWorkspace(dto)
			.orElseThrow(() -> new AppException(500, "워크스페이스 수정에 실패 했습니다. 다시 시도해 주십시오."));

        String target = request.getContextPath()
                + "/front?key=workspace&methodName=show&workspaceId=" + workspace.getWorkspaceId();
        return new ModelAndView(target, true);
    }

		request.setAttribute("workspace", workspace);
		String target = request.getContextPath() + "/workspace/info.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView deleteWorkspace(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		var dto = WorkspaceRequest.deleteDto(request, sessionUser);

        request.setAttribute("target", request.getContextPath() + "/workspace");
        return new ModelAndView("common/top-redirect.jsp");
    }

    private ModelAndView delegateLeader(HttpServletRequest request, HttpServletResponse response) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("SessionUser");
        var dto = WorkspaceUserRequest.needAuthDto(request, sessionUser);

        workspaceService.delegateLeader(dto);

        // 리더 위임 성공했으면 성공 메세지 담아서 forward
        String target = request.getContextPath()
                + "/front?key=workspace&methodName=show&workspaceId=" + dto.wsId();
        return new ModelAndView(target, true);
    }

    private ModelAndView kickUser(HttpServletRequest request, HttpServletResponse response) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("SessionUser");
        var dto = WorkspaceUserRequest.needAuthDto(request, sessionUser);

        workspaceService.kickUser(dto);

        String target = request.getContextPath()
                + "/front?key=workspace&methodName=show&workspaceId=" + dto.wsId();
        return new ModelAndView(target, true);
    }

    private ModelAndView exitWorkspace(HttpServletRequest request, HttpServletResponse response) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("SessionUser");
        var dto = WorkspaceUserRequest.exitDto(request, sessionUser);

        workspaceService.exitWorkspace(dto);

        request.setAttribute("target", request.getContextPath() + "/workspace");
        return new ModelAndView("common/top-redirect.jsp");
    }
}
