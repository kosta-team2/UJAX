package org.kostaTeam2.presentation.controller.api;

import java.io.IOException;
import java.util.Map;

import org.kostaTeam2.application.service.SubmissionService;
import org.kostaTeam2.dto.request.SubmissionIngestRequest;
import org.kostaTeam2.infrastructure.jwt.JwtAccessTokenProvider;
import org.kostaTeam2.presentation.view.JsonResult;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SubmissionsApiController implements RestController {
	private static final ObjectMapper M = new ObjectMapper()
		.findAndRegisterModules()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private final SubmissionService submissionService;

	public SubmissionsApiController(SubmissionService submissionService) {
		this.submissionService = submissionService;
	}

	@Override
	public JsonResult handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "ingest" -> ingest(request, response);
			default -> new JsonResult("unknown method: " + methodName);
		};
	}

	private JsonResult ingest(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String auth = req.getHeader("Authorization");
		String jwt = auth.substring(7).trim();
		final long memberId;
		try {
			Jws<Claims> jws = JwtAccessTokenProvider.parse(jwt);
			String sub = jws.getPayload().getSubject();
			memberId = Long.parseLong(sub);
		} catch (JwtException | NumberFormatException e) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return new JsonResult("invalid token");
		}

		SubmissionIngestRequest dto = M.readValue(req.getInputStream(), SubmissionIngestRequest.class);
		if (dto == null) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new JsonResult("invalid payload");
		}

		submissionService.ingest(memberId, dto);

		res.setStatus(HttpServletResponse.SC_OK);
		return new JsonResult(Map.of("stored", true));
	}
}
