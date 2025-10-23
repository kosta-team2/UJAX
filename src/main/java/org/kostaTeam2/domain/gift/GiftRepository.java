package org.kostaTeam2.domain.gift;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GiftRepository {
	Gift findById(Connection conn, Long giftId) throws SQLException;

	int countByWorkspace(Connection conn) throws SQLException;

	List<Gift> findPages(Connection conn, int offset, int limit) throws SQLException;

}
