package org.kostaTeam2.infrastructure.jwt;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

public final class RefreshTokenIssuer {
	// TTL: 90일
	private static final long REFRESH_TTL_SEC = 24 * 60 * 60 * 90;
	private static final SecureRandom random = new SecureRandom();

	/**
	 * URL-safe 256비트 난수 토큰 발급
	 * */
	public static String issueRaw() {
		byte[] buf = new byte[32];
		random.nextBytes(buf);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
	}

	/**
	 * 만료시각 계산
	 * */
	public static Instant calcExpiry() {
		return Instant.now().plusSeconds(REFRESH_TTL_SEC);
	}
}

