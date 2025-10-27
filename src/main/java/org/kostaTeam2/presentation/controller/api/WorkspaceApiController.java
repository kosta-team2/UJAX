package org.kostaTeam2.presentation.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kostaTeam2.application.service.MemberService;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.view.JsonResult;
import org.kostaTeam2.infrastructure.mail.MailService;

public class WorkspaceApiController implements RestController {

    private final MemberService memberService;

    public WorkspaceApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public JsonResult handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return switch (methodName) {
            case "invite" -> invite(request, response);
            default -> throw new BadRequestException("methodName이 올바르지 않습니다.");
        };
    }

    private JsonResult invite(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String wsId = req.getParameter("workspaceId");
            String email = req.getParameter("email");

            if (email == null || email.isBlank()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                return new JsonResult("이메일을 입력하세요.");
            }

            String trimmed = email.trim();
            if (!trimmed.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                return new JsonResult("올바른 이메일 형식이 아닙니다.");
            }

            if (wsId == null || wsId.isBlank()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                return new JsonResult("워크스페이스 ID가 없습니다.");
            }

            if (!memberService.existsByEmail(trimmed)) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                return new JsonResult("해당 이메일의 회원이 없습니다.");
            }

            MailService mail = new MailService(req.getServletContext());
            mail.sendMail(
                    trimmed,
                    "[ujax] 워크스페이스 초대",
                    wsId + " 워크스페이스 로부터 초대장이 날라왔습니다."
            );

            resp.setStatus(HttpServletResponse.SC_OK);
            return new JsonResult((Object) "초대 메일을 보냈습니다.");
        } catch (Exception e) {
            // 실패 시: errorMessage 생성자 사용
            return new JsonResult("메일 전송 실패: " + e.getMessage());
        }
    }
}
