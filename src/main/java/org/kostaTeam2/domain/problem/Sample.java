package org.kostaTeam2.domain.problem;

public class Sample {
	private Long sampleId;
	private int sampleIndex;
	private String input;
	private String output;

	public Sample(int sampleIndex, String input, String output) {
		this.sampleIndex = sampleIndex;
		this.input = input;
		this.output = output;
	}

	public Long getSampleId() {
		return sampleId;
	}

	public int getSampleIndex() {
		return sampleIndex;
	}

	public String getInput() {
		return input;
	}

	public String getOutput() {
		return output;
	}
}
