package org.kostaTeam2.presentation.controller.page;

import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {

	/**
	 * 동기 페이지 요청 처리 메서드.
	 * @param methodName 요청 파라미터로 전달된 동작 이름
	 * @param request HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @return ModelAndView (뷰 이름과 이동 방식)
	 * @throws Exception 비즈니스 로직 처리 중 예외 발생 시
	 */
	ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response)
		throws Exception;
}
