package org.kostaTeam2.domain.member;

/**
 * TODO: password hash
 * */
public class Member {
	private Long memberId;
	private String email;
	private String password;
	private String nickname;
	private int reward;
	private int xp;

	public Member(Long memberId, String email, String password, String nickname, int reward, int xp) {
		this.memberId = memberId;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.reward = reward;
		this.xp = xp;
	}

	public Member(String email, String password, String nickname) {
		this.memberId = memberId;
		this.email = email;
		this.nickname = nickname;
	}

    public Member(Long memberId, String nickname, String password) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.password = password;
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

	public int getReward() {
		return reward;
	}

	public int getXp() {
		return xp;
	}
}
