package org.kostaTeam2.application.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.member.MemberRepository;
import org.kostaTeam2.global.exception.DBException;

public class MemberServiceImpl implements MemberService {
	private final DataSource ds;
	private final MemberRepository repository;

	public MemberServiceImpl(DataSource ds, MemberRepository repository) {
		this.ds = ds;
		this.repository = repository;
	}

	@Override
	public Optional<Member> login(String email, String password) {
		try (Connection con = ds.getConnection()) {
			return repository.findByEmailAndPassword(con, email, password);
		} catch (SQLException e) {
			throw new DBException("로그인 처리 중 db 오류가 발생하였습니다.", e);
		}
	}
}
