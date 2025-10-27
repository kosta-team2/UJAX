package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.kostaTeam2.domain.gift.Gift;
import org.kostaTeam2.domain.gift.GiftRepository;
import org.kostaTeam2.global.exception.NotFoundException;

public class GiftDao implements GiftRepository {

	@Override
	public Gift findById(Connection conn, Long giftId) throws SQLException {
		String sql = """
			SELECT g.product_name, g.product_price, g.product_image
			FROM gift g
			WHERE g.product_id = ?
			AND EXISTS (
			    SELECT 1
			    FROM barcode b
			    WHERE b.product_id = g.product_id
			    AND b.status = 0
			);
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, giftId);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					throw new NotFoundException("상품 정보가 없습니다.");

				return new Gift(
					rs.getString("product_name"),
					rs.getLong("product_price"),
					rs.getString("product_image")
				);
			}
		}
	}

	@Override
	public int countProducts(Connection conn) throws SQLException {
		String sql = """
			SELECT count(*)
			FROM gift;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	@Override
	public List<Gift> paginationGift(Connection conn, int offset, int size) throws SQLException {
		String sql = """
			SELECT g.product_id, g.product_name, g.product_price, g.product_image
			FROM gift g
			WHERE EXISTS (
			    SELECT 1
			    FROM  barcode b
			    WHERE b.product_id = g.product_id
			    AND b.status = 0
			)
			ORDER BY g.product_price
			LIMIT ? OFFSET ?;
			""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, size);
			ps.setInt(2, offset);

			try (ResultSet rs = ps.executeQuery()) {
				List<Gift> list = new ArrayList<>();

				while (rs.next()) {
					Gift gift = new Gift(
						rs.getLong("product_id"),
						rs.getString("product_name"),
						rs.getLong("product_price"),
						rs.getString("product_image")
					);

					list.add(gift);
				}

				return list;
			}
		}
	}
}
