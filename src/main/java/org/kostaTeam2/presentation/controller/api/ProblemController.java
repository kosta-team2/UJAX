package org.kostaTeam2.presentation.controller.api;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
		if (request.getTitle().isEmpty()) {
			return;
		}
		List<Sample> samples =
			(request.getSamples() == null ? Collections.<ProblemInfoRequest.SampleDto>emptyList() :
				request.getSamples())
				.stream()
				.map(s -> new Sample(
					s.getSampleIndex(),
					s.getInput(),
					s.getOutput()
				))
				.sorted(Comparator.comparingInt(Sample::getSampleIndex))
				.collect(Collectors.toList());

		List<AlgorithmTag> tags =
			(request.getTags() == null ? Collections.<ProblemInfoRequest.TagDto>emptyList() : request.getTags())
				.stream()
				.map(t -> new AlgorithmTag(t.getName()))
				.filter(t -> !t.getName().isBlank())
				.collect(Collectors.toList());

		Problem p = new Problem(
			request.getProblemNum(),
			request.getTitle(),
			request.getTier(),
			request.getTimeLimit(),
			request.getMemoryLimit(),
			request.getProblemDesc(),
			request.getProblemInput(),
			request.getProblemOutput(),
			request.getUrl(),
			samples,
			tags
		);

		problemService.createProblem(p);
	}
}