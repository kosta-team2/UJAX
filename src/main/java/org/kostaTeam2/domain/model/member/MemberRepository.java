package org.kostaTeam2.domain.model.member;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface MemberRepository {
	Optional<Member> findById(Connection con, long id) throws SQLException;
}
