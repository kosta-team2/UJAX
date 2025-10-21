package org.kostaTeam2.global.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.kostaTeam2.global.exception.common.AppException;

/**
 * 인증은 되었지만 권한이 없는 사용자가 접근할 때 발생하는 예외.
 * (예: 일반 사용자가 관리자 전용 기능 수행 시)
 */
public class UnauthorizedException extends AppException {
    public UnauthorizedException(String message) {
        super(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
