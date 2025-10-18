package org.kostaTeam2.application.service;

import java.util.Optional;

import javax.sql.DataSource;

import org.kostaTeam2.domain.model.member.Member;
import org.kostaTeam2.domain.model.member.MemberRepository;

public class MemberServiceImpl implements MemberService {
	private final DataSource ds;
	private final MemberRepository repository;

	public MemberServiceImpl(DataSource ds, MemberRepository repository) {
		this.ds = ds;
		this.repository = repository;
	}

	@Override
	public Optional<Member> get(long id) {
		return Optional.empty();
	}
}
