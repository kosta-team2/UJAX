package org.kostaTeam2.domain.gift;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GiftRepository {
	Gift findById(Connection conn, Long giftId) throws SQLException;

	int countProducts(Connection conn) throws SQLException;

	List<Gift> paginationGift(Connection conn, int offset, int limit) throws SQLException;

}
