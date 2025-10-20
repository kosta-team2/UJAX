package org.kostaTeam2.presentation.servlet;

import java.io.IOException;

import org.kostaTeam2.application.service.ProblemService;
import org.kostaTeam2.dto.request.ProblemInfoRequest;
import org.kostaTeam2.presentation.controller.api.ProblemController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/problems/*")
public class ProblemApiServlet extends HttpServlet {
	private static final ObjectMapper MAPPER = new ObjectMapper()
		.findAndRegisterModules()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private ProblemController controller;

	@Override
	public void init() throws ServletException {
		ProblemService svc = (ProblemService) getServletContext().getAttribute("problemService");
		controller = new ProblemController(svc);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
		ServletException,
		IOException {
		response.setContentType("application/json; charset=UTF-8");

		ProblemInfoRequest dto = MAPPER.readValue(request.getInputStream(), ProblemInfoRequest.class);
		controller.ingest(dto);
	}
}
