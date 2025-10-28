package org.kostaTeam2.infrastructure.jwt;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletResponse;

public final class CookieUtil {
	public static final String REFRESH_COOKIE = "refresh";

	public static void addRefreshCookie(HttpServletResponse res, String rawToken, long maxAgeSec,
		String domain, boolean isDev) {
		String v = URLEncoder.encode(rawToken, StandardCharsets.UTF_8);
		StringBuilder sb = new StringBuilder()
			.append(REFRESH_COOKIE).append("=").append(v)
			.append("; Path=/ext/auth/refresh")
			.append("; HttpOnly")
			.append("; Max-Age=").append(maxAgeSec);
		if (!isDev) {
			sb.append("; Secure").append("; SameSite=None");
			if (domain != null && !domain.isBlank())
				sb.append("; Domain=").append(domain);
		}
		res.addHeader("Set-Cookie", sb.toString());
	}

	public static void clearRefreshCookie(HttpServletResponse res, String domain, boolean isDev) {
		StringBuilder sb = new StringBuilder()
			.append(REFRESH_COOKIE).append("=; Path=").append("/ext/auth/refresh")
			.append("; Max-Age=0; HttpOnly");
		if (!isDev) {
			sb.append("; Secure").append("; SameSite=None");
			if (domain != null && !domain.isBlank())
				sb.append("; Domain=").append(domain);
		}
		res.addHeader("Set-Cookie", sb.toString());
	}
}
