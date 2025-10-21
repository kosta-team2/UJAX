package org.kostaTeam2.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProblemInfoRequest {
	private int problemNum;
	private String title;
	private String tier;
	private String timeLimit;
	private String memoryLimit;
	private String problemDesc;
	private String problemInput;
	private String problemOutput;
	private String url;
	private List<SampleDto> samples;
	private List<TagDto> tags;

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

	public List<SampleDto> getSamples() {
		return samples;
	}

	public List<TagDto> getTags() {
		return tags;
	}

	public static class SampleDto {
		private int sampleIndex;
		private String input;
		private String output;

		public int getSampleIndex() {
			return sampleIndex;
		}

		public void setSampleIndex(int sampleIndex) {
			this.sampleIndex = sampleIndex;
		}

		public String getInput() {
			return input;
		}

		public void setInput(String input) {
			this.input = input;
		}

		public String getOutput() {
			return output;
		}

		public void setOutput(String output) {
			this.output = output;
		}
	}

	public static class TagDto {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
