package org.kostaTeam2.presentation.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kostaTeam2.presentation.view.JsonResult;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.infrastructure.mail.MailService;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class AuthApiController implements RestController {

    private static final String KEY_EMAIL   = "SIGNUP_EMAIL";
    private static final String KEY_CODE    = "SIGNUP_CODE";
    private static final String KEY_EXPIRES = "SIGNUP_CODE_EXPIRES";
    private static final long   TTL_MILLIS  = TimeUnit.MINUTES.toMillis(10);

    @Override
    public JsonResult handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return switch (methodName) {
            case "sendSignupCode"   -> sendSignupCode(request, response);
            case "verifySignupCode" -> verifySignupCode(request, response);
            default -> throw new BadRequestException("auth methodName이 올바르지 않습니다.");
        };
    }

    private JsonResult sendSignupCode(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonResult("이메일을 입력하세요.");
        }
        String trimmed = email.trim();
        if (!trimmed.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonResult("올바른 이메일 형식이 아닙니다.");
        }

        // 6자리 코드 생성
        String code = generate6Digits();
        long expiresAt = Instant.now().toEpochMilli() + TTL_MILLIS;

        // 세션에 보관
        var session = req.getSession(true);
        session.setAttribute(KEY_EMAIL, trimmed);
        session.setAttribute(KEY_CODE, code);
        session.setAttribute(KEY_EXPIRES, expiresAt);

        // 메일 발송
        try {
            String body = """
                    [ujax] 회원가입 인증코드

                    아래 6자리 코드를 10분 이내에 입력해 주세요.
                    인증코드: %s
                    """.formatted(code);

            new MailService(req.getServletContext())
                    .sendMail(trimmed, "[ujax] 회원가입 인증코드", body);

            resp.setStatus(HttpServletResponse.SC_OK);
            return new JsonResult("인증코드를 전송했습니다. 10분 이내에 입력해 주세요.");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new JsonResult("인증코드 전송 실패: " + e.getMessage());
        }
    }

    private JsonResult verifySignupCode(HttpServletRequest req, HttpServletResponse resp) {
        var session = req.getSession(false);
        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonResult("세션이 만료되었습니다. 다시 시도해 주세요.");
        }

        String inputEmail = req.getParameter("email");
        String inputCode  = req.getParameter("code");

        if (inputEmail == null || inputCode == null || inputEmail.isBlank() || inputCode.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonResult("이메일/코드를 모두 입력해 주세요.");
        }

        String savedEmail = (String) session.getAttribute(KEY_EMAIL);
        String savedCode  = (String) session.getAttribute(KEY_CODE);
        Long   expiresAt  = (Long)   session.getAttribute(KEY_EXPIRES);

        if (savedEmail == null || savedCode == null || expiresAt == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonResult("인증을 먼저 요청해 주세요.");
        }
        if (!savedEmail.equals(inputEmail.trim())) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonResult("인증을 요청한 이메일과 다릅니다.");
        }
        if (Instant.now().toEpochMilli() > expiresAt) {
            // 만료 시 세션 정리
            session.removeAttribute(KEY_CODE);
            session.removeAttribute(KEY_EXPIRES);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonResult("인증코드가 만료되었습니다. 다시 요청해 주세요.");
        }
        if (!savedCode.equals(inputCode.trim())) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new JsonResult("인증코드가 일치하지 않습니다.");
        }

        // 검증 성공 → 코드 폐기 + 성공 플래그 셋
        session.removeAttribute(KEY_CODE);
        session.removeAttribute(KEY_EXPIRES);
        session.setAttribute("SIGNUP_EMAIL_VERIFIED", Boolean.TRUE);

        resp.setStatus(HttpServletResponse.SC_OK);
        return new JsonResult((Object) "이메일 인증이 완료되었습니다.");
    }

    private static String generate6Digits() {
        SecureRandom r = new SecureRandom();
        int n = r.nextInt(1_000_000);
        return String.format("%06d", n);
    }
}
