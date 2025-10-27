package org.kostaTeam2.domain.member;

/**
 * TODO: password hash
 *
 */
public class Member {
	private Long memberId;
	private String email;
	private String password;
	private String nickname;
	private Long reward;
	private int xp;

	// dto
	public Member(Long memberId, String email, String password, String nickname, Long reward, int xp) {
		this.memberId = memberId;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.reward = reward;
		this.xp = xp;
	}

	// 로그인
	public Member(String email, String password, String nickname) {
		this.memberId = memberId;
		this.email = email;
		this.nickname = nickname;
	}

	// 회원 정보 수정
	public Member(Long memberId, String nickname, String password) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.password = password;
	}

	// 결제
	public Member(Long memberId, Long reward) {
		this.memberId = memberId;
		this.reward = reward;
	}

	public Long getMemberId() {
		return memberId;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getNickname() {
		return nickname;
	}

	public Long getReward() {
		return reward;
	}

	public int getXp() {
		return xp;
	}
}
