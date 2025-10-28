package org.kostaTeam2.dto.response;

import java.util.List;

public class SubmitterPageResponse {
	private final List<MemberTab> members;
	private final int page;
	private final int pageSize;
	private final int total;
	private final Prime prime;

	public SubmitterPageResponse(List<MemberTab> members, int page, int pageSize, int total, Prime prime) {
		this.members = members;
		this.page = page;
		this.pageSize = pageSize;
		this.total = total;
		this.prime = prime;
	}

	public List<MemberTab> getMembers() {
		return members;
	}

	public int getPage() {
		return page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotal() {
		return total;
	}

	public Prime getPrime() {
		return prime;
	}

	public static class MemberTab {
		private final long wsMemberId;
		private final String nickname;
		private final boolean hasSubmission;
		private final Long solutionId;

		public MemberTab(long wsMemberId, String nickname, boolean hasSubmission, Long solutionId) {
			this.wsMemberId = wsMemberId;
			this.nickname = nickname;
			this.hasSubmission = hasSubmission;
			this.solutionId = solutionId;
		}

		public long getWsMemberId() {
			return wsMemberId;
		}

		public String getNickname() {
			return nickname;
		}

		public boolean isHasSubmission() {
			return hasSubmission;
		}

		public Long getSolutionId() {
			return solutionId;
		}
	}

	public static class Prime {
		private final Long wsMemberId;
		private final Long solutionId;

		public Prime(Long wsMemberId, Long solutionId) {
			this.wsMemberId = wsMemberId;
			this.solutionId = solutionId;
		}

		public Long getWsMemberId() {
			return wsMemberId;
		}

		public Long getSolutionId() {
			return solutionId;
		}
	}
}