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

	public Problem(Long problemId, int problemNum, String title, String tier, String timeLimit, String memoryLimit,
		String problemDesc, String problemInput, String problemOutput, String url) {
		this.problemId = problemId;
		this.problemNum = problemNum;
		this.title = title;
		this.tier = tier;
		this.timeLimit = timeLimit;
		this.memoryLimit = memoryLimit;
		this.problemDesc = problemDesc;
		this.problemInput = problemInput;
		this.problemOutput = problemOutput;
		this.url = url;
	}

	public Problem(int problemNum, String title, String tier, String timeLimit, String memoryLimit,
		String problemDesc, String problemInput, String problemOutput, String url, List<Sample> samples,
		List<AlgorithmTag> tags) {
		this.problemNum = problemNum;
		this.title = title;
		this.tier = tier;
		this.timeLimit = timeLimit;
		this.memoryLimit = memoryLimit;
		this.problemDesc = problemDesc;
		this.problemInput = problemInput;
		this.problemOutput = problemOutput;
		this.url = url;
		this.samples = samples;
		this.tags = tags;
	}

	public Problem(int problemNum, String title, String tier) {
		this.problemNum = problemNum;
		this.title = title;
		this.tier = tier;
	}

	public Problem(int problemNum, String title, String tier, List<Sample> samples) {
		this.problemNum = problemNum;
		this.title = title;
		this.tier = tier;
		this.samples = samples;
	}

	public Long getProblemId() {
		return problemId;
	}

	public int getProblemNum() {
		return problemNum;
	}

	public String getTitle() {
		return title;
	}

	public String getTier() {
		return tier;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public String getMemoryLimit() {
		return memoryLimit;
	}

	public String getProblemDesc() {
		return problemDesc;
	}

	public String getProblemInput() {
		return problemInput;
	}

	public String getProblemOutput() {
		return problemOutput;
	}

	public String getUrl() {
		return url;
	}

	public List<Sample> getSamples() {
		return samples;
	}

	public List<AlgorithmTag> getTags() {
		return tags;
	}
}
