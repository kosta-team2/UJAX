package org.kostaTeam2.application.service.jwt;

import java.sql.Connection;

import org.kostaTeam2.domain.jwt.RefreshTokenRow;

public interface TokenService {

	RefreshTokenRow findByToken(String refreshToken);
}
