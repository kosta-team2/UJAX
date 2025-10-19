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

}
