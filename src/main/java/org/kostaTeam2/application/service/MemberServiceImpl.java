package org.kostaTeam2.application.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.member.MemberRepository;
import org.kostaTeam2.global.exception.BadRequestException;
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

    @Override
    public Optional<Member> getInfo(long id) {
        try (Connection con = ds.getConnection()) {
            return repository.findById(con, id);
        } catch (SQLException e) {
            throw new DBException("개인 정보 조회 중 db 오류가 발생하였습니다.", e);
        }
    }

    @Override
    public void signup(String email, String password, String nickname) {
        try (Connection con = ds.getConnection()) {
            if (repository.findByEmail(con, email).isPresent())
                throw new BadRequestException("이미 사용 중인 이메일입니다.");
            if (repository.findByNickname(con, nickname).isPresent())
                throw new BadRequestException("이미 사용 중인 닉네임입니다.");

            Member member = new Member(email, password, nickname);
            repository.saveMember(con, member);
        } catch (SQLException e) {
            throw new DBException("회원가입 처리 중 DB 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void softDelete(long memberId) {
        try (Connection con = ds.getConnection()) {
            repository.softDeleteById(con, memberId);
        } catch (SQLException e) {
            throw new DBException("회원 탈퇴 처리 중 DB 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void updateMember(long memberId, String password, String newNickname, String newPassword) {
        try (Connection con = ds.getConnection()) {
            Member existing = repository.findById(con, memberId)
                                        .orElseThrow(() -> new BadRequestException("회원 정보를 찾을 수 없습니다."));

            if (!existing.getPassword().equals(password)) {
                throw new BadRequestException("현재 비밀번호가 일치하지 않습니다.");
            }

            if (repository.findByNickname(con, newNickname).isPresent()) {
                throw new BadRequestException("이미 사용 중인 닉네임입니다.");
            }

            Member update = new Member(memberId, newNickname, newPassword);
            repository.updateMember(con, update);

        } catch (SQLException e) {
            throw new DBException("회원 정보 수정 처리 중 DB 오류가 발생했습니다.", e);
        }
    }
}
