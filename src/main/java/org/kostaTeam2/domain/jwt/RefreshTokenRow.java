package org.kostaTeam2.domain.jwt;

import java.time.Instant;

public class RefreshTokenRow {
	public long memberId;
	public Instant expiresAt;
	public Instant revokedAt;
}
