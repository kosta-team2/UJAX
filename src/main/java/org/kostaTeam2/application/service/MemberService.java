package org.kostaTeam2.application.service;

import java.util.Optional;

import org.kostaTeam2.domain.model.member.Member;

public interface MemberService {
	Optional<Member> get(long id);
}
