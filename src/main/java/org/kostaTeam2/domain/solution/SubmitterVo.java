package org.kostaTeam2.domain.solution;

public class SubmitterVo {
	public final long wsMemberId;
	public final String nickname;
	public final Long latestSolutionId;

	public SubmitterVo(long wsMemberId, String nickname, Long latestSolutionId) {
		this.wsMemberId = wsMemberId;
		this.nickname = nickname;
		this.latestSolutionId = latestSolutionId;
	}

	public long getWsMemberId() {
		return wsMemberId;
	}

	public String getNickname() {
		return nickname;
	}

	public Long getLatestSolutionId() {
		return latestSolutionId;
	}
}
