package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.kostaTeam2.domain.gift.BarcodeRepository;

public class BarcodeDao implements BarcodeRepository {
	@Override
	public boolean existByProductId(Connection conn, Long productId) throws SQLException {
		String sql = """
			SELECT 1
			FROM barcode
			WHERE product_id = ? AND status = 0
			LIMIT 1;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, productId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	@Override
	public String consumeAvailableBarcodeAndGetImage(Connection con, Long productId) throws SQLException {
		String sql = """
			UPDATE barcode
			SET status = 1, updated_at = NOW()
			WHERE product_id = ? AND status = 0
			ORDER BY barcode_id
			LIMIT 1
			RETURNING barcode_image;
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, productId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("barcode_image");
				}
				return null;
			}
		}
	}
}
