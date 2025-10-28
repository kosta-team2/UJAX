package org.kostaTeam2.application.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

import javax.sql.DataSource;

import org.kostaTeam2.domain.jwt.TokenRepository;
import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.member.MemberRepository;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.common.ValidationException;
import org.kostaTeam2.infrastructure.jwt.RefreshTokenIssuer;

public class MemberServiceImpl implements MemberService {
	private final DataSource ds;
	private final MemberRepository memberRepository;
	private final TokenRepository tokenRepository;

	public MemberServiceImpl(DataSource ds, MemberRepository memberRepository,
		TokenRepository tokenRepository) {
		this.ds = ds;
		this.memberRepository = memberRepository;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public Optional<Member> login(String email, String password, String raw, Instant exp) {
		try (Connection con = ds.getConnection()) {
			Optional<Member> member = memberRepository.findByEmailAndPassword(con, email, password);
			if (member.isEmpty())
				return Optional.empty();

			Member m = member.get();
			tokenRepository.insert(con, m.getMemberId(), raw, exp);

			return Optional.of(m);
		} catch (SQLException e) {
			throw new DBException("로그인 처리 중 db 오류가 발생하였습니다.", e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<Member> getInfo(long id) {
		try (Connection con = ds.getConnection()) {
			return memberRepository.findById(con, id);
		} catch (SQLException e) {
			throw new DBException("개인 정보 조회 중 db 오류가 발생하였습니다.", e);
		}
	}

	@Override
	public void signup(String email, String password, String nickname) {
		try (Connection con = ds.getConnection()) {
			if (memberRepository.findByEmail(con, email).isPresent())
				throw new BadRequestException("이미 사용 중인 이메일입니다.");
			if (memberRepository.findByNickname(con, nickname).isPresent())
				throw new BadRequestException("이미 사용 중인 닉네임입니다.");

			Member member = new Member(email, password, nickname);
			memberRepository.saveMember(con, member);
		} catch (SQLException e) {
			throw new DBException("회원가입 처리 중 DB 오류가 발생했습니다.", e);
		}
	}

	@Override
	public void softDelete(long memberId) {
		try (Connection con = ds.getConnection()) {
			memberRepository.softDeleteById(con, memberId);
		} catch (SQLException e) {
			throw new DBException("회원 탈퇴 처리 중 DB 오류가 발생했습니다.", e);
		}
	}

	@Override
	public void updateMember(long memberId, String password, String newNickname, String newPassword) {
		try (Connection con = ds.getConnection()) {
			Member existing = memberRepository.findById(con, memberId)
				.orElseThrow(() -> new BadRequestException("회원 정보를 찾을 수 없습니다."));

			if (!existing.getPassword().equals(password)) {
				throw new ValidationException("비밀번호가 틀려서 변경하지 못했습니다.",
					"/workspace/personal-info.jsp");
			}

			if (memberRepository.findByNickname(con, newNickname).isPresent()) {
				throw new ValidationException("이미 사용 중인 닉네임입니다 다른 닉네임을 골라주세요.",
					"/workspace/personal-info.jsp");
			}

			Member update = new Member(memberId, newNickname, newPassword);
			memberRepository.updateMember(con, update);

		} catch (SQLException e) {
			throw new DBException("회원 정보 수정 처리 중 DB 오류가 발생했습니다.", e);
		}
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		try (Connection con = ds.getConnection()) {
			return memberRepository.findByEmail(con, email);
		} catch (SQLException e) {
			throw new DBException("이메일 확인 중 DB 오류가 발생했습니다.", e);
		}
	}
}
