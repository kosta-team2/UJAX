package org.kostaTeam2.presentation.controller.page;

import java.util.Optional;

import org.kostaTeam2.application.service.MemberService;
import org.kostaTeam2.application.service.workspace.WorkspaceService;
import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.dto.request.LoginUserRequest;
import org.kostaTeam2.dto.request.SignupUserRequest;
import org.kostaTeam2.dto.request.UpdateUserRequest;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MemberPageController implements Controller {
    private final MemberService memberService;

    public MemberPageController(MemberService memberService, WorkspaceService workspaceService) {
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
            case "getInfo" -> getInfo(request, response);
            case "updateUser" -> updateUser(request, response);
            case "getSidebar" -> getSidebar(request, response);
            default -> throw new BadRequestException("methodName이 올바르지 않습니다.");
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

        String ctx = request.getContextPath();
        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isBlank()) {
            if (redirect.startsWith(ctx + "/")) {
                return new ModelAndView(redirect, true);
            }
            if (redirect.startsWith("/")) {
                return new ModelAndView(ctx + redirect, true);
            }
        }

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

        SessionUser user = (SessionUser) session.getAttribute("SessionUser");

        try {
            memberService.softDelete(user.memberId());
            session.invalidate();

            request.setAttribute("target", request.getContextPath() + "/auth/login.jsp");
            return new ModelAndView("common/top-redirect.jsp");

        } catch (BadRequestException e) {
            request.setAttribute("error", e.getMessage());
            return new ModelAndView("/workspace.jsp");

        } catch (Exception e) {
            // 이건 나중에 오류 생기면 잡으려고 해놓음
            request.setAttribute("error", "회원 탈퇴 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return new ModelAndView("/workspace.jsp");
        }
    }

    private ModelAndView getInfo(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        SessionUser user = (SessionUser) session.getAttribute("SessionUser");

        try {
            Member userInfo = memberService.getInfo(user.memberId()).orElse(null);
            request.setAttribute("userInfo", userInfo);
        } catch (BadRequestException e) {
            request.setAttribute("error", e.getMessage());
            return new ModelAndView("/workspace.jsp");
        }
        return new ModelAndView("/workspace/mypage.jsp");
    }

    private ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        var dto = UpdateUserRequest.from(request);

        try {
            memberService.updateMember(dto.memberId(), dto.password(), dto.newNickname(), dto.newPassword());

            // update 성공했으면 기존 session user의 nickname, password도 변경 적용
            SessionUser user = (SessionUser) session.getAttribute("SessionUser");
            session.setAttribute("SessionUser", new SessionUser(user.memberId(), user.email(), user.nickname()));

            String target = request.getContextPath() + "/front?key=member&methodName=getInfo";
            return new ModelAndView(target, true);
        } catch (BadRequestException e) {
            request.setAttribute("error", e.getMessage());
            return new ModelAndView("/workspace.jsp");
        }
    }

    private ModelAndView getSidebar(HttpServletRequest request, HttpServletResponse response) {
        SessionUser user = (SessionUser) request.getSession().getAttribute("SessionUser");

        Member userInfo = memberService.getInfo(user.memberId()).orElse(null);

        request.setAttribute("userInfo", userInfo);
        return new ModelAndView("none");
    }
}
