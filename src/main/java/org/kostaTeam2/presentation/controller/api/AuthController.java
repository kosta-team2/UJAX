package org.kostaTeam2.presentation.controller.api;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.kostaTeam2.application.service.jwt.TokenService;
import org.kostaTeam2.infrastructure.jwt.CookieUtil;
import org.kostaTeam2.infrastructure.jwt.JwtAccessTokenProvider;
import org.kostaTeam2.presentation.view.JsonResult;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthController implements RestController {
	private final TokenService tokenService;

	public AuthController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public JsonResult handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "refresh" -> refresh(request, response);
			default -> new JsonResult("unknown method: " + methodName);
		};
	}

	private JsonResult refresh(HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.setHeader("Cache-Control", "no-store");
		res.setHeader("Pragma", "no-cache");

		String raw = findCookie(req, CookieUtil.REFRESH_COOKIE);
		if (raw == null || raw.isBlank()) {
			res.setStatus(401);
			return new JsonResult("no refresh");
		}

		raw = URLDecoder.decode(raw, StandardCharsets.UTF_8);
		var row = tokenService.findByToken(raw);

		String access = JwtAccessTokenProvider.generateAccess(row.memberId);
		long ttlSec = JwtAccessTokenProvider.ttlSec();

		res.setStatus(HttpServletResponse.SC_OK);
		return new JsonResult(java.util.Map.of("accessToken", access, "expiresIn", ttlSec));
	}

	private static String findCookie(HttpServletRequest req, String name) {
		Cookie[] cs = req.getCookies();
		if (cs == null)
			return null;
		for (Cookie c : cs)
			if (name.equals(c.getName()))
				return c.getValue();
		return null;
	}
}
