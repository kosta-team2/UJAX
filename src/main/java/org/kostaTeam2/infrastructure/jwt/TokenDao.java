package org.kostaTeam2.infrastructure.jwt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;

import org.kostaTeam2.domain.jwt.RefreshTokenRow;
import org.kostaTeam2.domain.jwt.TokenRepository;

public class TokenDao implements TokenRepository {

	@Override
	public void insert(Connection con, long memberId, String refreshToken, Instant expiresAt) throws Exception {
		String sql = "INSERT INTO token (member_id, refresh_token, expires_at) VALUES (?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, memberId);
			ps.setString(2, refreshToken);
			ps.setTimestamp(3, Timestamp.from(expiresAt));
			ps.executeUpdate();
		}
	}

	@Override
	public RefreshTokenRow findByToken(Connection con, String refreshToken) throws Exception {
		String sql = "SELECT member_id, expires_at, revoked_at FROM token WHERE refresh_token = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, refreshToken);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return null;
				RefreshTokenRow row = new RefreshTokenRow();
				row.memberId = rs.getLong("member_id");
				row.expiresAt = rs.getTimestamp("expires_at").toInstant();
				Timestamp rev = rs.getTimestamp("revoked_at");
				row.revokedAt = (rev == null ? null : rev.toInstant());
				return row;
			}
		}
	}

	@Override
	public void revoke(Connection con, String refreshToken) throws Exception {
		String sql = "UPDATE token SET revoked_at = CURRENT_TIMESTAMP(3) WHERE refresh_token = ? AND revoked_at IS NULL";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, refreshToken);
			ps.executeUpdate();
		}
	}
}
