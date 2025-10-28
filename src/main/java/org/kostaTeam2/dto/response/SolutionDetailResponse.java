package org.kostaTeam2.dto.response;

public class SolutionDetailResponse {
	private final long solutionId;
	private final String status;
	private final Integer timeMs;
	private final Integer memoryMb;
	private final String code;
	private final Like like;
	private final Integer commentCount;

	public SolutionDetailResponse(long solutionId, String status, Integer timeMs, Integer memoryMb,
		String code, Like like, Integer commentCount) {
		this.solutionId = solutionId;
		this.status = status;
		this.timeMs = timeMs;
		this.memoryMb = memoryMb;
		this.code = code;
		this.like = like;
		this.commentCount = commentCount;
	}

	public long getSolutionId() {
		return solutionId;
	}

	public String getStatus() {
		return status;
	}

	public Integer getTimeMs() {
		return timeMs;
	}

	public Integer getMemoryMb() {
		return memoryMb;
	}

	public String getCode() {
		return code;
	}

	public Like getLike() {
		return like;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public static class Like {
		private final boolean me;
		private final int count;

		public Like(boolean me, int count) {
			this.me = me;
			this.count = count;
		}

		public boolean isMe() {
			return me;
		}

		public int getCount() {
			return count;
		}
	}
}
