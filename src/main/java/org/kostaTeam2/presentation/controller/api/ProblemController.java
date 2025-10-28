package org.kostaTeam2.presentation.controller.api;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.kostaTeam2.application.service.ProblemService;
import org.kostaTeam2.domain.problem.AlgorithmTag;
import org.kostaTeam2.domain.problem.Problem;
import org.kostaTeam2.domain.problem.Sample;
import org.kostaTeam2.dto.request.ProblemInfoRequest;
import org.kostaTeam2.presentation.view.JsonResult;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProblemController implements RestController {
	private static final ObjectMapper MAPPER = new ObjectMapper()
		.findAndRegisterModules()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private final ProblemService problemService;

	public ProblemController(ProblemService problemService) {
		this.problemService = problemService;
	}

	@Override
	public JsonResult handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "ingest" -> ingest(request, response);
			default -> new JsonResult("unknown method: " + methodName);
		};
	}

	public JsonResult ingest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ProblemInfoRequest dto = MAPPER.readValue(request.getInputStream(), ProblemInfoRequest.class);

		if (dto.getTitle().isEmpty()) {
			return new JsonResult(java.util.Map.of("skipped", true));
		}
		List<Sample> samples =
			(dto.getSamples() == null ? Collections.<ProblemInfoRequest.SampleDto>emptyList() :
				dto.getSamples())
				.stream()
				.map(s -> new Sample(
					s.getSampleIndex(),
					s.getInput(),
					s.getOutput()
				))
				.sorted(Comparator.comparingInt(Sample::getSampleIndex))
				.collect(Collectors.toList());

		List<AlgorithmTag> tags =
			(dto.getTags() == null ? Collections.<ProblemInfoRequest.TagDto>emptyList() : dto.getTags())
				.stream()
				.map(t -> new AlgorithmTag(t.getName()))
				.filter(t -> !t.getName().isBlank())
				.collect(Collectors.toList());

		//TODO: service로 이전
		String s = dto.getTitle();
		int tabIdx = s.indexOf('\t');
		if (tabIdx >= 0)
			s = s.substring(0, tabIdx);
		s = s.replace('\r', '\n').replace('\n', ' ');
		s = s.replaceAll("\\s+", " ").trim();

		Problem p = new Problem(
			dto.getProblemNum(),
			s,
			dto.getTier(),
			dto.getTimeLimit(),
			dto.getMemoryLimit(),
			dto.getProblemDesc(),
			dto.getProblemInput(),
			dto.getProblemOutput(),
			dto.getUrl(),
			samples,
			tags
		);

		problemService.createProblem(p);
		response.setStatus(HttpServletResponse.SC_OK);
		return new JsonResult(java.util.Map.of("created", true));
	}
}