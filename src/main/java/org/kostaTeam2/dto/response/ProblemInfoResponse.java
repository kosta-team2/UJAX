package org.kostaTeam2.dto.response;

import java.util.List;

import org.kostaTeam2.domain.problem.Sample;

public class ProblemInfoResponse {
	int problemNum;
	String title;
	String description;
	String input;
	String output;
	List<Sample> samples;
	String url;
	String timeLimit;
	String memoryLimit;

	public static ProblemInfoResponse of(
		int problemNum,
		String title,
		String description,
		String input,
		String output,
		List<Sample> samples,
		String url,
		String timeLimit,
		String memoryLimit
	) {
		return new ProblemInfoResponse(
			problemNum,
			title,
			description,
			input,
			output,
			samples,
			url,
			timeLimit,
			memoryLimit
		);
	}

	public ProblemInfoResponse(
		int problemNum, String title, String description, String input, String output,
		List<Sample> samples, String url, String timeLimit, String memoryLimit
	) {
		this.problemNum = problemNum;
		this.title = title;
		this.description = description;
		this.input = input;
		this.output = output;
		this.samples = samples;
		this.url = url;
		this.timeLimit = timeLimit;
		this.memoryLimit = memoryLimit;
	}

	public int getProblemNum() {
		return problemNum;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getInput() {
		return input;
	}

	public String getOutput() {
		return output;
	}

	public List<Sample> getSamples() {
		return samples;
	}

	public String getUrl() {
		return url;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public String getMemoryLimit() {
		return memoryLimit;
	}
}
