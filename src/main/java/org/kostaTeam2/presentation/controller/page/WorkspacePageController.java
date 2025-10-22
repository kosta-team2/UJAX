package org.kostaTeam2.presentation.controller.page;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kostaTeam2.application.service.workspace.WorkspaceService;
import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.dto.request.WorkspaceRequest;
import org.kostaTeam2.dto.request.DelegateLeaderRequest;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.global.exception.common.AppException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

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
            case "update"  -> updateWorkspace(request, response);
            case "delete" -> deleteWorkspace(request, response);
            case "updateRole" -> delegateLeader(request, response);
            default -> throw new BadRequestException("workspace methodName이 올바르지 않습니다.");
        };
	}

    private ModelAndView createWorkspace(HttpServletRequest request, HttpServletResponse response) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("SessionUser");
        var dto = WorkspaceRequest.createDto(request, sessionUser);

        Workspace workspace = workspaceService.createWorkspace(dto)
                .orElseThrow(() -> new AppException(500, "워크스페이스 생성에 실패 했습니다. 다시 시도해 주십시오."));

        request.setAttribute("workspace", workspace);
        
        String target = request.getContextPath() + "/workspace";
        return new ModelAndView(target);
    }

    private ModelAndView showWorkspace(HttpServletRequest request, HttpServletResponse response) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("SessionUser");
        var dto = WorkspaceRequest.showDto(request, sessionUser);

        Workspace workspace = workspaceService.getWorkspaceInfo(dto)
                .orElseThrow(() -> new AppException(500, "워크스페이스 조회에 실패 했습니다. 다시 시도해 주십시오."));

        request.setAttribute("workspace", workspace);

        String target = request.getContextPath() + "/workspace";
        return new ModelAndView(target);
    }

    private ModelAndView updateWorkspace(HttpServletRequest request, HttpServletResponse response) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("SessionUser");
        var dto = WorkspaceRequest.updateDto(request, sessionUser);

        Workspace workspace = workspaceService.updateWorkspace(dto)
                .orElseThrow(() -> new AppException(500, "워크스페이스 수정에 실패 했습니다. 다시 시도해 주십시오."));

        request.setAttribute("workspace", workspace);
        String target = request.getContextPath() + "/workspace/info.jsp";
        return new ModelAndView(target);
    }

    private ModelAndView deleteWorkspace(HttpServletRequest request, HttpServletResponse response) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("SessionUser");
        var dto = WorkspaceRequest.deleteDto(request, sessionUser);

        workspaceService.deleteWorkspace(dto);

        String target = request.getContextPath() + "/workspace";
        return new ModelAndView(target, true);
    }

    private ModelAndView delegateLeader(HttpServletRequest request, HttpServletResponse response) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("SessionUser");
        var dto = DelegateLeaderRequest.delegateDto(request, sessionUser);

        workspaceService.delegateLeader(dto);

        String target = request.getContextPath() + "/workspace/info.jsp";
        return new ModelAndView(target, true);
    }
}
