package org.kostaTeam2.presentation.controller.page;

import java.util.Optional;

import org.kostaTeam2.application.service.MemberService;
import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.dto.request.LoginUserRequest;
import org.kostaTeam2.dto.request.SignupUserRequest;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MemberPageController implements Controller {
	private final MemberService memberService;

	public MemberPageController(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "login" -> login(request, response);
            case "logout" -> logout(request, response);
            case "signup" -> signup(request, response);
            case "delete" -> delete(request, response);
			default -> throw new BadRequestException("login methodName이 올바르지 않습니다.");
		};
	}

	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		var dto = LoginUserRequest.from(request);
		Optional<Member> member = memberService.login(dto.email(), dto.password());

		if (member.isEmpty()) {
			request.setAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
			request.setAttribute("email", dto.email());
			return new ModelAndView("/auth/login.jsp");
		}

		Member m = member.get();
		HttpSession old = request.getSession(false);
		if (old != null) old.invalidate();
		HttpSession session = request.getSession(true);

		session.setAttribute("SessionUser", new SessionUser(
			m.getMemberId(),
			m.getEmail(),
			m.getNickname()
		));

		String target = request.getContextPath() + "/workspace";
		return new ModelAndView(target, true);
	}

    private ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return new ModelAndView(request.getContextPath() + "/auth/login.jsp", true);
    }

    private ModelAndView signup(HttpServletRequest request, HttpServletResponse response) {
        var dto = SignupUserRequest.from(request);
        try {
            memberService.signup(dto.email(), dto.password(), dto.nickname());
            return new ModelAndView(request.getContextPath() + "/auth/login.jsp", true);
        } catch (BadRequestException e) {
            request.setAttribute("error", e.getMessage());
            return new ModelAndView("/auth/signup.jsp");
        }
    }

    private ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        // 세션이 만료된 상태에서 접근 시 로그인 페이지로 리다이렉트
        if (session == null || session.getAttribute("SessionUser") == null) {
            request.setAttribute("error", "로그인 세션이 만료되었습니다. 다시 로그인해주세요.");
            return new ModelAndView("/auth/login.jsp");
        }

        SessionUser user = (SessionUser) session.getAttribute("SessionUser");

        try {
            memberService.softDelete(user.memberId());
            session.invalidate();

            request.setAttribute("message", "회원 탈퇴가 정상적으로 처리되었습니다. 이용해주셔서 감사합니다.");
            return new ModelAndView("/auth/login.jsp");

        } catch (BadRequestException e) {
            request.setAttribute("error", e.getMessage());
            return new ModelAndView("/workspace.jsp");

        } catch (Exception e) {
            // 이건 나중에 오류 생기면 잡으려고 해놓음
            request.setAttribute("error", "회원 탈퇴 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return new ModelAndView("/workspace.jsp");
        }
    }

}
