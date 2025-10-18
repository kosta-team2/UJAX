package org.kostaTeam2.presentation.controller.page;

import org.kostaTeam2.application.service.MemberService;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberPageController implements Controller {
	private final MemberService memberService;

	public MemberPageController(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return null;
	}
}
