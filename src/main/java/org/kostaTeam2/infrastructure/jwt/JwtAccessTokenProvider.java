package org.kostaTeam2.infrastructure.jwt;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public final class JwtAccessTokenProvider {
	//임시 Base64 (환경변수 처리 필요)
	private static final String SECRET_B64 =
		"m4V2k4b7zYl2rQw9tZ0yPp6d3s1k8f3u6x9c2b5n7m4v2k4b7zYl2rQw9tZ0yPp6";
	private static final long ACCESS_TTL_SEC = 30 * 60 * 6; // 6시간
	private static final long CLOCK_SKEW_SEC = 60;

	private static final SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_B64));

	public JwtAccessTokenProvider() {
	}

	public static String generateAccess(long memberId) {
		Instant now = Instant.now();
		return Jwts.builder()
			.subject(Long.toString(memberId))
			.issuedAt(Date.from(now))
			.expiration(Date.from(now.plusSeconds(ACCESS_TTL_SEC)))
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public static Jws<Claims> parse(String jwt) throws JwtException {
		return Jwts.parser()
			.clockSkewSeconds(CLOCK_SKEW_SEC)
			.verifyWith(key)
			.build()
			.parseSignedClaims(jwt);
	}

	public static long ttlSec() {
		return ACCESS_TTL_SEC;
	}

}
