package org.kostaTeam2.presentation.controller.api;

import java.util.Map;

import org.kostaTeam2.presentation.view.JsonResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RestController {
	/**
	 * 비동기 AJAX 요청 처리 메서드.
	 * @param methodName 요청으로 전달된 동작 이름
	 * @param request HttpServletRequest (세션 등 접근 용도)
	 * @param response HttpServletResponse
	 * @return JsonResult (결과 데이터 또는 오류 정보를 담은 객체)
	 * @throws Exception 처리 중 예외 발생 시
	 */
	JsonResult handle(String methodName, HttpServletRequest request, HttpServletResponse response)
		throws Exception;
}
