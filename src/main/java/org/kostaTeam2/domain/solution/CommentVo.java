package org.kostaTeam2.domain.solution;

import java.time.LocalDateTime;

public class CommentVo {
	public final long commentId;
	public final long wsMemberId;
	public final String nickname;
	public final String content;
	public final LocalDateTime createdAt;

	public CommentVo(long commentId, long wsMemberId, String nickname, String content, LocalDateTime createdAt) {
		this.commentId = commentId;
		this.wsMemberId = wsMemberId;
		this.nickname = nickname;
		this.content = content;
		this.createdAt = createdAt;
	}
}
