package org.kostaTeam2.domain.problem;

import java.util.List;

public class Problem {
	private Long problemId;
	private int problemNum;
	private String title;
	private String tier;
	private String timeLimit;
	private String memoryLimit;
	private String problemDesc;
	private String problemInput;
	private String problemOutput;
	private String url;
	private List<Sample> samples;
	private List<AlgorithmTag> tags;

	public Long getProblemId() {
		return problemId;
	}

	public void setProblemId(Long problemId) {
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

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getMemoryLimit() {
		return memoryLimit;
	}

	public void setMemoryLimit(String memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	public String getProblemDesc() {
		return problemDesc;
	}

	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}

	public String getProblemInput() {
		return problemInput;
	}

	public void setProblemInput(String problemInput) {
		this.problemInput = problemInput;
	}

	public String getProblemOutput() {
		return problemOutput;
	}

	public void setProblemOutput(String problemOutput) {
		this.problemOutput = problemOutput;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Sample> getSamples() {
		return samples;
	}

	public void setSamples(List<Sample> samples) {
		this.samples = samples;
	}

	public List<AlgorithmTag> getTags() {
		return tags;
	}

	public void setTags(List<AlgorithmTag> tags) {
		this.tags = tags;
	}
}
