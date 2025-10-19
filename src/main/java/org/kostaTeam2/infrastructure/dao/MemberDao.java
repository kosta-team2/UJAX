package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.member.MemberRepository;

public class MemberDao implements MemberRepository {

	public Optional<Member> findByEmailAndPassword(Connection con, String email, String password) throws SQLException {
		String sql = """
			SELECT member_id, email, nickname
			FROM member
			WHERE email = ? and password = ? and is_deleted = 0
			LIMIT 1
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return Optional.empty();
				}
				return Optional.of(new Member(
					rs.getLong("member_id"),
					rs.getString("email"),
					rs.getString("nickname")
				));
			}
		}
	}
}
