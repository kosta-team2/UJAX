package org.kostaTeam2.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.kostaTeam2.domain.problem.AlgorithmTag;

public class WorkspaceProblemPageResponse {
	long wsProblemId;
	long wsId;
	long problemId;
	int problemNum;
	String title;
	//백준 티어
	String diff;
	//백준 티어 색깔
	String diffClass;
	boolean submitted;
	int submitCount;
	LocalDateTime deadline;
	List<AlgorithmTag> tags;
	int page;
	int totalPages;

	public static WorkspaceProblemPageResponse of(
		long workspaceProblemId,
		long workspaceId,
		long problemId,
		int problemNum,
		String title,
		String diff,
		String diffClass,
		boolean submitted,
		LocalDateTime deadline,
		int submitCount,
		List<AlgorithmTag> tags,
		int page,
		int totalPages
	) {
		List<AlgorithmTag> safeTags = (tags == null) ? List.of() : List.copyOf(tags); // 불변화
		return new WorkspaceProblemPageResponse(
			workspaceProblemId,
			workspaceId,
			problemId,
			problemNum,
			title,
			diff,
			diffClass,
			submitted,
			deadline,
			submitCount,
			safeTags,
			page,
			totalPages
		);
	}

	private WorkspaceProblemPageResponse(long workspaceProblemId, long workspaceId, long problemId, int problemNum,
		String title, String diff, String diffClass, boolean submitted, LocalDateTime deadline, int submitCount,
		List<AlgorithmTag> tags, int page, int totalPages) {
		this.wsProblemId = workspaceProblemId;
		this.wsId = workspaceId;
		this.problemId = problemId;
		this.problemNum = problemNum;
		this.title = title;
		this.diff = diff;
		this.diffClass = diffClass;
		this.submitted = submitted;
		this.deadline = deadline;
		this.submitCount = submitCount;
		this.tags = tags;
		this.page = page;
		this.totalPages = totalPages;
	}

	public long getWsProblemId() {
		return wsProblemId;
	}

	public void setWsProblemId(long wsProblemId) {
		this.wsProblemId = wsProblemId;
	}

	public long getWsId() {
		return wsId;
	}

	public void setWsId(long wsId) {
		this.wsId = wsId;
	}

	public long getProblemId() {
		return problemId;
	}

	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}

	public int getProblemNum() {
		return problemNum;
	}

	public void setProblemNum(int problemNum) {
		this.problemNum = problemNum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public String getDiffClass() {
		return diffClass;
	}

	public void setDiffClass(String diffClass) {
		this.diffClass = diffClass;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public int getSubmitCount() {
		return submitCount;
	}

	public void setSubmitCount(int submitCount) {
		this.submitCount = submitCount;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public List<AlgorithmTag> getTags() {
		return tags;
	}

	public void setTags(List<AlgorithmTag> tags) {
		this.tags = tags;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
