package org.kostaTeam2.domain.workspace;

import java.time.LocalDateTime;

public class WorkspaceProblem {
	private Long wsProblemId;
	private Long wsId;
	private Long problemId;
	private LocalDateTime deadLine;
	private LocalDateTime scheduleAt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isDeleted;

	public WorkspaceProblem(Long wsId,
		LocalDateTime deadLine, LocalDateTime scheduleAt) {
		this.wsId = wsId;
		this.deadLine = deadLine;
		this.scheduleAt = scheduleAt;
	}

	public WorkspaceProblem(Long wsId, Long problemId,
		LocalDateTime deadLine, LocalDateTime scheduleAt) {
		this.wsId = wsId;
		this.problemId = problemId;
		this.deadLine = deadLine;
		this.scheduleAt = scheduleAt;
	}

	public WorkspaceProblem(Long wsProblemId, Long wsId, Long problemId, LocalDateTime deadLine) {
		this.wsProblemId = wsProblemId;
		this.wsId = wsId;
		this.problemId = problemId;
		this.deadLine = deadLine;
	}

	public Long getWsProblemId() {
		return wsProblemId;
	}

	public Long getWsId() {
		return wsId;
	}

	public Long getProblemId() {
		return problemId;
	}

	public LocalDateTime getDeadLine() {
		return deadLine;
	}

	public LocalDateTime getScheduleAt() {
		return scheduleAt;
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
