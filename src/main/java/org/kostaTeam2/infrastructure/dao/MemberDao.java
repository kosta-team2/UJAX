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
			SELECT *
			FROM member
			WHERE email = ? and password = ? and is_deleted = 0
			LIMIT 1
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapRow(rs));
				}
				return Optional.empty();
			}
		}
	}

	@Override
	public Optional<Member> findByEmail(Connection con, String email) throws SQLException {
		String sql = """
			SELECT * FROM member
			WHERE email = ? AND is_deleted = 0
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapRow(rs));
				}
				return Optional.empty();
			}
		}
	}

	@Override
	public Optional<Member> findByNickname(Connection con, String nickname) throws SQLException {
		String sql = """
			SELECT * FROM member
			WHERE nickname = ? AND is_deleted = 0
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nickname);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapRow(rs));
				}
				return Optional.empty();
			}
		}
	}

	@Override
	public Optional<Member> findById(Connection con, long id) throws SQLException {
		String sql = """
			SELECT * FROM member
			WHERE member_id = ? AND is_deleted = 0
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapRow(rs));
				}
			}
			return Optional.empty();
		}
	}

	@Override
	public void updateMember(Connection con, Member member) throws SQLException {
		String sql = """
			UPDATE member
			SET nickname = ?, password = ?
			WHERE member_id = ? AND is_deleted = 0
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, member.getNickname());
			ps.setString(2, member.getPassword());
			ps.setLong(3, member.getMemberId());
			ps.executeUpdate();
		}
	}

	@Override
	public int debitRewardIfEnough(Connection con, Long memberId, Long amount) throws SQLException {
		String sql = """
			UPDATE member
			SET reward = reward - ?
			WHERE member_id = ?
			AND is_deleted = 0
			AND reward >= ?;
			""";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, amount);
			ps.setLong(2, memberId);
			ps.setLong(3, amount);

			return ps.executeUpdate();
		}
	}

	@Override
	public void saveMember(Connection con, Member member) throws SQLException {
		String sql = """
			INSERT INTO member (email, password, nickname)
			VALUES (?, ?, ?)
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, member.getEmail());
			ps.setString(2, member.getPassword());
			ps.setString(3, member.getNickname());
			ps.executeUpdate();
		}
	}

	@Override
	public void softDeleteById(Connection con, long memberId) throws SQLException {
		String sql = """
			UPDATE member
			SET is_deleted = 1
			WHERE member_id = ?
			""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, memberId);
			ps.executeUpdate();
		}
	}

	// rs로부터 Member 생성하는 mapper
	private Member mapRow(ResultSet rs) throws SQLException {
		return new Member(
			rs.getLong("member_id"),
			rs.getString("email"),
			rs.getString("password"),
			rs.getString("nickname"),
			rs.getLong("reward"),
			rs.getInt("xp")
		);
	}
}
