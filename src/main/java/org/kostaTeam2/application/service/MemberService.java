package org.kostaTeam2.application.service;

import java.sql.SQLException;
import java.util.Optional;

import org.kostaTeam2.domain.member.Member;

public interface MemberService {
    Optional<Member> login(String email, String password);

    Optional<Member> getInfo(long id);

    void signup(String email, String password, String nickname);

    void softDelete(long memberId);

    void updateMember(long memberId, String password, String newNickname, String newPassword);

    Optional<Member> findByEmail(String email);

    default boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
