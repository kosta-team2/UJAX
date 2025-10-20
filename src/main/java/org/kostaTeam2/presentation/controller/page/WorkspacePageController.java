package org.kostaTeam2.presentation.controller.page;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.kostaTeam2.application.service.workspace.WorkspaceService;
import org.kostaTeam2.domain.workspace.Workspace;
import org.kostaTeam2.dto.request.WorkspaceCreateRequest;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.global.exception.common.AppException;
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
        try {
            return switch (methodName) {
                case "createWorkspace" -> create(request, response);
                default -> throw new BadRequestException("workspace methodName이 올바르지 않습니다.");
            };
        } catch (AppException e) {
            request.setAttribute("errorMessage", e.getMessage());
            return new ModelAndView("/workspace");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "시스템 오류가 발생했습니다.");
            return new ModelAndView("/workspace");
        }
	}

    /**
     * 워크스페이스 생성
     */
    private ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("SessionUser") == null) {
            throw new AppException(401, "로그인이 필요합니다.");
        }

        SessionUser sessionUser = (SessionUser) session.getAttribute("SessionUser");
        var dto =  WorkspaceCreateRequest.from(request, sessionUser);

        Workspace workspace = workspaceService.createWorkspace(dto)
                .orElseThrow(() -> new AppException(500, "워크스페이스 생성에 실패 했습니다. 다시 시도해 주십시오."));

        request.setAttribute("workspace", workspace);
        
        String target = request.getContextPath() + "/workspace";
        return new ModelAndView(target);
    }
}
