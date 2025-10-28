package org.kostaTeam2.domain.solution;

import java.time.LocalDateTime;

public class Solution {
	Long solutionId;
	Long workspaceProblemId;
	Long workspaceMemberId;
	boolean status;
	int timeMs;
	int memoryMb;
	String code;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isDeleted;

	public Solution(Long workspaceProblemId, Long workspaceMemberId, boolean status, int timeMs,
		int memoryMb, String code) {
		this.workspaceProblemId = workspaceProblemId;
		this.workspaceMemberId = workspaceMemberId;
		this.status = status;
		this.timeMs = timeMs;
		this.memoryMb = memoryMb;
		this.code = code;
	}

	public Long getSolutionId() {
		return solutionId;
	}

	public Long getWorkspaceProblemId() {
		return workspaceProblemId;
	}

	public Long getWorkspaceMemberId() {
		return workspaceMemberId;
	}

	public boolean isStatus() {
		return status;
	}

	public int getTimeMs() {
		return timeMs;
	}

	public int getMemoryMb() {
		return memoryMb;
	}

	public String getCode() {
		return code;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public boolean isDeleted() {
		return isDeleted;
	}
}
