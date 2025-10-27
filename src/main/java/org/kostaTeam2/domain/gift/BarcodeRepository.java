package org.kostaTeam2.domain.gift;

import java.sql.Connection;
import java.sql.SQLException;

public interface BarcodeRepository {
	/**
	 * 상품의 바코드가 존재하는지 확인
	 */
	boolean existByProductId(Connection conn, Long productId) throws SQLException;

	/**
	 * 상품 구매시 바코드의 상태를 변경하고 바코드 이미지 반환
	 */
	String consumeAvailableBarcodeAndGetImage(Connection con, Long productId) throws SQLException;
}
