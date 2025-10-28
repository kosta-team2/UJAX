package org.kostaTeam2.domain.workspace.chart;

public class CommentStatVO {
	private final String nickname;
	private final int commentCount;

	public CommentStatVO(String nickname, int commentCount) {
		this.nickname = nickname;
		this.commentCount = commentCount;
	}

	public String getNickname() {
		return nickname;
	}

	public int getCommentCount() {
		return commentCount;
	}
}
