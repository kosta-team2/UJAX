package org.kostaTeam2.domain.jwt;

import java.sql.Connection;
import java.time.Instant;

public interface TokenRepository {
	void insert(Connection con, long memberId, String refreshToken, Instant expiresAt) throws Exception;

	RefreshTokenRow findByToken(Connection con, String refreshToken) throws Exception;

	void revoke(Connection con, String refreshToken) throws Exception;
}
