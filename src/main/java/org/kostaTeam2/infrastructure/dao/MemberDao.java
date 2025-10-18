package org.kostaTeam2.infrastructure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.kostaTeam2.domain.model.member.Member;
import org.kostaTeam2.domain.model.member.MemberRepository;

public class MemberDao implements MemberRepository {

	public Optional<Member> findById(Connection con, long id) throws SQLException {
		String sql = "SELECT id, name FROM member WHERE id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			return Optional.empty();
		}
	}
}
