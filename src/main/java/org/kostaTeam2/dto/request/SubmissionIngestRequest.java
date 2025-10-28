package org.kostaTeam2.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionIngestRequest {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Problem {
		private int problemNum;

		public Problem() {
		}

		public int getProblemNum() {
			return problemNum;
		}

		public void setProblemNum(int problemNum) {
			this.problemNum = problemNum;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Submission {
		private int problemId;
		private String verdict;
		private String time;
		private String memory;
		private String language;
		private String code;

		public Submission() {
		}

		public int getProblemId() {
			return problemId;
		}

		public void setProblemId(int problemId) {
			this.problemId = problemId;
		}

		public String getVerdict() {
			return verdict;
		}

		public void setVerdict(String verdict) {
			this.verdict = verdict;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getMemory() {
			return memory;
		}

		public void setMemory(String memory) {
			this.memory = memory;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}

	private Problem problem;
	private Submission submission;

	public SubmissionIngestRequest() {
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

}
