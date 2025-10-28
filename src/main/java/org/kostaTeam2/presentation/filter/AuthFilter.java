package org.kostaTeam2.presentation.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

	private static final Set<String> EXEMPT_EXACT = Set.of(
		"/auth",
		"/auth/login.jsp",
		"/auth/register.jsp",
		"/auth/register",
		"/auth/login",
		"/auth/logout",
		"/error"
	);

	private static final List<String> EXEMPT_PREFIX = List.of(
		"/assets/", "/css/", "/js/", "/images/",
		"/webjars/", "/favicon", "/static/",
		"/auth/css/", "/auth/js/",
		"/workspace/css/", "/workspace/js/",
		"/solution/solution.css", "/solution/solution.js",
		"/giftshop/css/"
	);

	private static final Set<String> PUBLIC_MEMBER_METHODS = Set.of(
		"login", "signup"
	);

	private static final Set<String> PUBLIC_AJAX_AUTH_METHODS = Set.of(
		"sendSignupCode", "verifySignupCode", "refresh"
	);

	private static final String EXT_REFRESH_PATH = "/ext/auth/refresh";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		request.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;

		String ctx = req.getContextPath();
		String uri = req.getRequestURI();
		String path = uri.substring(ctx.length());

		if (isExempt(path)) {
			chain.doFilter(request, response);
			return;
		}

		if ("/front".equals(path)) {
			String key = req.getParameter("key");
			String method = req.getParameter("methodName");

			if ("POST".equalsIgnoreCase(req.getMethod())
				&& "member".equals(key)
				&& PUBLIC_MEMBER_METHODS.contains(method)) {
				chain.doFilter(request, response);
				return;
			}
		}

		if ("/ajax".equals(path)) {
			String key = req.getParameter("key");
			String method = req.getParameter("methodName");

			boolean isAllowedAuthAjax =
				"auth".equals(key)
					&& PUBLIC_AJAX_AUTH_METHODS.contains(method)
					&& ("POST".equalsIgnoreCase(req.getMethod()) || "refresh".equals(method));

			if (isAllowedAuthAjax) {
				chain.doFilter(request, response);
				return;
			}
		}

		if (path.startsWith("/ext/")) {
			if (EXT_REFRESH_PATH.equals(path) && "POST".equalsIgnoreCase(req.getMethod())) {
				chain.doFilter(request, response);
				return;
			}

			String authz = req.getHeader("Authorization");
			if (authz != null && authz.startsWith("Bearer ")) {
				chain.doFilter(request, response);
				return;
			}

			writeJson401(res, "Access token required");
			return;
		}

		HttpSession session = req.getSession(false);
		Long memberId = (session == null) ? null : (Long)session.getAttribute("memberId");
		Object sessionUser = (session == null) ? null : session.getAttribute("SessionUser");

		if (memberId != null || sessionUser != null) {
			chain.doFilter(request, response);
			return;
		}

		res.sendRedirect(req.getContextPath() + "/auth/login.jsp");

	}

	private boolean isExempt(String path) {
		if (EXEMPT_EXACT.contains(path))
			return true;
		for (String p : EXEMPT_PREFIX) {
			if (path.startsWith(p))
				return true;
		}
		return false;
	}

	private static void writeJson401(HttpServletResponse res, String msg) throws IOException {
		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		res.setContentType("application/json;charset=UTF-8");
		res.setHeader("Cache-Control", "no-store");
		res.setHeader("Pragma", "no-cache");
		res.setHeader("WWW-Authenticate", "Bearer realm=\"ujax\"");
		res.getWriter().write("{\"error\":\"UNAUTHORIZED\",\"message\":\"" +
			msg.replace("\"", "\\\"") + "\"}");
	}

}
