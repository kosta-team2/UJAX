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
		String updateSql = """
			UPDATE barcode
			       	SET status = 1, updated_at = NOW(), barcode_id = LAST_INSERT_ID(barcode_id)
			        	WHERE product_id = ? AND status = 0
			        	ORDER BY barcode_id
			       	LIMIT 1
			""";

		try (PreparedStatement ps = con.prepareStatement(updateSql)) {
			ps.setLong(1, productId);

			if (ps.execute()) {
				try (ResultSet rs = ps.getResultSet()) {
					if (!rs.next()) {
						if (ps.getUpdateCount() == 0) {
							return null;
						}
					}
				}
			}
		}

		long barcodeId;
		try (PreparedStatement ps = con.prepareStatement("SELECT LAST_INSERT_ID()")) {
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				barcodeId = rs.getLong(1);
			}
		}

		try (PreparedStatement ps = con.prepareStatement("SELECT barcode_image FROM barcode WHERE barcode_id = ?")) {
			ps.setLong(1, barcodeId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getString("barcode_image") : null;
			}
		}
	}
}
