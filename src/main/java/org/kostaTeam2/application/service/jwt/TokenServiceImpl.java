package org.kostaTeam2.application.service.jwt;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.kostaTeam2.domain.jwt.RefreshTokenRow;
import org.kostaTeam2.domain.jwt.TokenRepository;
import org.kostaTeam2.global.exception.DBException;

public class TokenServiceImpl implements TokenService {
	private final DataSource ds;
	private final TokenRepository tokenRepository;

	public TokenServiceImpl(DataSource ds, TokenRepository tokenRepository) {
		this.ds = ds;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public RefreshTokenRow findByToken(String refreshToken) {
		try (Connection con = ds.getConnection()) {
			return tokenRepository.findByToken(con, refreshToken);
		} catch (SQLException e) {
			throw new DBException("token을 불러오는 과정에서 DB 오류가 발생했습니다. ",e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
