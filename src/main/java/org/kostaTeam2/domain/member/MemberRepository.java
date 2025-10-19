package org.kostaTeam2.domain.member;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface MemberRepository {
	Optional<Member> findByEmailAndPassword(Connection con, String email, String password)
		throws SQLException;

    Optional<Member> findByEmail(Connection con, String email) throws SQLException;

    Optional<Member> findByNickname(Connection con, String nickname) throws SQLException;
    void saveMember(Connection con, Member member) throws SQLException;
}
