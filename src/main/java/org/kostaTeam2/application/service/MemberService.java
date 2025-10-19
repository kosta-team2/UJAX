package org.kostaTeam2.application.service;

import java.util.Optional;

import org.kostaTeam2.domain.member.Member;

public interface MemberService {
	Optional<Member> login(String email, String password);

}
