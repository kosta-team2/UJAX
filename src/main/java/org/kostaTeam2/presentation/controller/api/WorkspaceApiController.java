package org.kostaTeam2.presentation.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.view.JsonResult;
import org.kostaTeam2.infrastructure.mail.MailService;

public class WorkspaceApiController implements RestController {

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
                return new JsonResult("이메일을 입력하세요.");
            }

            if (!email.contains("@")) {
                return new JsonResult("유효하지 않은 이메일 형식입니다.");
            }

            MailService mail = new MailService(req.getServletContext());
            mail.sendMail(
                    email,
                    "[Workspace 초대] 안녕하세요!",
                    wsId + " 로 부터 초대장이 날라왔습니다."
            );

            // 성공 시: data에 결과 메시지 저장
            return new JsonResult("초대 메일을 보냈습니다.");
        } catch (Exception e) {
            // 실패 시: errorMessage 생성자 사용
            return new JsonResult("메일 전송 실패: " + e.getMessage());
        }
    }
}
