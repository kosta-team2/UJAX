package org.kostaTeam2.presentation.filter;

import java.io.IOException;

import org.kostaTeam2.global.exception.common.AppException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TODO: 리팩터링 필요
 * */
@WebFilter("/*")
public class GlobalExceptionFilter implements Filter {
	private static final String ERROR_JSP = "/WEB-INF/views/error.jsp";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;

		// 정적 리소스는 우회(최소 규칙: 확장자만 체크)
		String uri = req.getRequestURI();
		if (uri.matches(".*\\.(css|js|png|jpg|jpeg|gif|svg|ico|woff2?|ttf|eot)$")) {
			chain.doFilter(request, response);
			return;
		}

		try {
			chain.doFilter(request, response);

		} catch (AppException ae) {
			ae.printStackTrace();
			int status = ae.getStatus() > 0 ? ae.getStatus() : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			if (res.isCommitted())
				return;

			if (isJsonRequest(req)) {
				res.reset();
				res.setStatus(status);
				res.setContentType("application/json; charset=UTF-8");
				res.getWriter().write("{\"error\":\"" + escape(ae.getMessage()) + "\"}");
			} else {
				res.reset();
				res.setStatus(status);
				req.setAttribute("errorMessage", safe(ae.getMessage(), "요청 처리 중 오류가 발생했습니다."));
				req.getRequestDispatcher(ERROR_JSP).forward(req, res);
			}

		} catch (Throwable t) { // 예기치 못한 예외
			//TODO: 로깅으로 변경
			t.printStackTrace();
			if (res.isCommitted())
				return;

			if (isJsonRequest(req)) {
				res.reset();
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				res.setContentType("application/json; charset=UTF-8");
				res.getWriter().write("{\"error\":\"Internal Server Error\"}");
			} else {
				res.reset();
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				req.setAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
				req.getRequestDispatcher(ERROR_JSP).forward(req, res);
			}
		}
	}

	// 최소 규칙: /ajax 로 시작 OR Accept: application/json OR X-Requested-With: XMLHttpRequest
	private boolean isJsonRequest(HttpServletRequest req) {
		String ctx = req.getContextPath();
		String uri = req.getRequestURI();
		if (uri.startsWith(ctx + "/ajax"))
			return true;

		String accept = req.getHeader("Accept");
		if (accept != null && accept.contains("application/json"))
			return true;

		String xrw = req.getHeader("X-Requested-With");
		return xrw != null && xrw.equalsIgnoreCase("XMLHttpRequest");
	}

	private static String safe(String s, String fallback) {
		return (s == null || s.isBlank()) ? fallback : s;
	}

	private static String escape(String s) {
		if (s == null)
			return "";
		return s.replace("\\", "\\\\").replace("\"", "\\\"")
			.replace("\n", "\\n").replace("\r", "\\r");
	}
}