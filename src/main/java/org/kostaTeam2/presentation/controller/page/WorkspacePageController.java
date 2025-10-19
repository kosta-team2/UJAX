package org.kostaTeam2.presentation.controller.page;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.kostaTeam2.application.service.workspace.WorkspaceService;
import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.domain.workspace.WorkspaceLanguage;
import org.kostaTeam2.dto.request.WorkspaceCreateDto;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

public class WorkspacePageController implements Controller {
    private WorkspaceService workspaceService;

    public WorkspacePageController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

	@Override
	public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
        return switch (methodName) {
            case "createWorkspace" -> createWorkspace(request, response);
            default -> throw new BadRequestException("workspace methodName이 올바르지 않습니다.");
        };
	}

    /**
     * 워크스페이스 생성
     */
    private ModelAndView createWorkspace(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("SessionUser") == null) {
            throw new BadRequestException("로그인이 필요합니다.");
        }

        SessionUser loginUser = (SessionUser) session.getAttribute("SessionUser");
        Long leaderId = loginUser.memberId();

        // validation
        String workspaceName = request.getParameter("workspaceName");
        if (workspaceName == null || workspaceName.isBlank()) {
            throw new BadRequestException("워크스페이스 이름은 필수입니다.");
        }
        WorkspaceLanguage workspaceLanguage =
                WorkspaceLanguage.fromString(request.getParameter("workspaceLanguage"));
        Boolean isHintView = Boolean.valueOf(request.getParameter("isHintView"));

        Workspace workspace = workspaceService.createWorkspace(
                new WorkspaceCreateDto(
                        leaderId,
                        workspaceName,
                        workspaceLanguage,
                        isHintView
                ));

        String target = request.getContextPath() + "/workspaces/" + workspace.getWorkspaceId();
        return new ModelAndView(target, true);
    }
}
