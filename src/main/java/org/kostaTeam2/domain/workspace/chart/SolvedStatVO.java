package org.kostaTeam2.domain.workspace.chart;

public class SolvedStatVO {
	private final String nickname;
	private final int solvedCount;

	public SolvedStatVO(String nickname, int solvedCount) {
		this.nickname = nickname;
		this.solvedCount = solvedCount;
	}

	public String getNickname() {
		return nickname;
	}

	public int getSolvedCount() {
		return solvedCount;
	}
}
