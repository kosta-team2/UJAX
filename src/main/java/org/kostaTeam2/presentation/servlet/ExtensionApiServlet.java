package org.kostaTeam2.presentation.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kostaTeam2.application.service.ProblemService;
import org.kostaTeam2.application.service.SubmissionService;
import org.kostaTeam2.application.service.jwt.TokenService;
import org.kostaTeam2.domain.jwt.TokenRepository;
import org.kostaTeam2.infrastructure.jwt.JwtAccessTokenProvider;
import org.kostaTeam2.infrastructure.jwt.TokenDao;
import org.kostaTeam2.presentation.controller.api.AuthController;
import org.kostaTeam2.presentation.controller.api.ProblemController;
import org.kostaTeam2.presentation.controller.api.RestController;
import org.kostaTeam2.presentation.controller.api.SubmissionsApiController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/ext/*", loadOnStartup = 1)
public class ExtensionApiServlet extends HttpServlet {
	private static final ObjectMapper MAPPER = new ObjectMapper()
		.findAndRegisterModules()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private final Map<String, RestController> controllerMap = new HashMap<>();

	@Override
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();

		ProblemService problemService = (ProblemService)ctx.getAttribute("problemService");
		TokenService tokenService = (TokenService)ctx.getAttribute("tokenService");
		SubmissionService submissionService = (SubmissionService) ctx.getAttribute("submissionService");

		controllerMap.put("auth", new AuthController(tokenService));
		controllerMap.put("submissions", new SubmissionsApiController(submissionService));
		controllerMap.put("problems", new ProblemController(problemService));
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws
		ServletException,
		IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		String pathInfo = request.getPathInfo();
		String key = null, method = null;

		//url 패턴 매핑
		if (pathInfo != null && pathInfo.length() > 1) {
			String[] seg = pathInfo.substring(1).split("/");
			if (seg.length >= 2) {
				key = seg[0];
				method = seg[1];
			}
		}

		if (key == null)
			key = request.getParameter("key");
		if (method == null)
			method = request.getParameter("methodName");

		if (key == null || method == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "key/method 누락");
			return;
		}

		RestController controller = controllerMap.get(key);
		if (controller == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "controller not found: " + key);
			return;
		}

		try {
			var result = controller.handle(method, request, response);
			if (response.isCommitted())
				return;

			if (result == null) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				return;
			}
			if (result.isSuccess()) {
				if (response.getStatus() < 400)
					response.setStatus(HttpServletResponse.SC_OK);
				MAPPER.writeValue(response.getWriter(), result.getData());
			} else {
				if (response.getStatus() < 400)
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				MAPPER.writeValue(response.getWriter(), java.util.Map.of("error", result.getErrorMessage()));
			}
		} catch (Exception e) {
			throw new ServletException("Extension API error", e);
		}
	}
}
