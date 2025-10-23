package org.kostaTeam2.domain.workspace;

import java.time.LocalDateTime;

public class WorkspaceSolution {
	private Long solutionId;
	private Long workspaceProblemId;
	private Long workspaceMemberId;
	private boolean status;
	private int timeMs;
	private int memoryMb;
	private String code;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isDeleted;

	public WorkspaceSolution(boolean status, int timeMs, int memoryMb, String code,
		LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted) {
		this.status = status;
		this.timeMs = timeMs;
		this.memoryMb = memoryMb;
		this.code = code;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.isDeleted = isDeleted;
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
