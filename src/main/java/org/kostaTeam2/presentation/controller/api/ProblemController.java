package org.kostaTeam2.presentation.controller.api;

import java.util.List;

import org.kostaTeam2.application.service.ProblemService;
import org.kostaTeam2.domain.problem.AlgorithmTag;
import org.kostaTeam2.domain.problem.Problem;
import org.kostaTeam2.domain.problem.Sample;
import org.kostaTeam2.dto.request.ProblemInfoRequest;

public class ProblemController {
	private final ProblemService problemService;

	public ProblemController(ProblemService problemService) {
		this.problemService = problemService;
	}

	public void ingest(ProblemInfoRequest request) {
		Problem p = new Problem();
		p.setProblemNum(request.getProblemNum());
		p.setTitle(request.getTitle());
		p.setTimeLimit(request.getTimeLimit());
		p.setMemoryLimit(request.getMemoryLimit());
		p.setProblemDesc(request.getProblemDesc());
		p.setProblemInput(request.getProblemInput());
		p.setProblemOutput(request.getProblemOutput());
		p.setUrl(request.getUrl());
		p.setSamples(mapSamples(request.getSamples()));
		p.setTags(mapTags(request.getTags()));

		problemService.createProblem(p);
	}

	private List<Sample> mapSamples(List<ProblemInfoRequest.SampleDto> in) {
		if (in == null || in.isEmpty()) return java.util.Collections.emptyList();
		return in.stream().map(s -> {
				Sample ss = new Sample();
				ss.setSampleIndex(s.getSampleIndex());
				ss.setInput((s.getInput()));
				ss.setOutput(s.getOutput());
				return ss;
			}).sorted(java.util.Comparator.comparingInt(Sample::getSampleIndex))
			.collect(java.util.stream.Collectors.toList());
	}

	private List<AlgorithmTag> mapTags(List<ProblemInfoRequest.TagDto> in) {
		if (in == null || in.isEmpty()) return java.util.Collections.emptyList();
		return in.stream().map(t -> {
				AlgorithmTag tag = new AlgorithmTag();
				tag.setName(t.getName());
				return tag;
			}).filter(t -> !t.getName().isBlank())
			.collect(java.util.stream.Collectors.toList());
	}
}
