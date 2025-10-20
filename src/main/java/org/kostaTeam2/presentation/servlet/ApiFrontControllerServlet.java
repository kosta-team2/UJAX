package org.kostaTeam2.presentation.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kostaTeam2.global.exception.common.AppException;
import org.kostaTeam2.presentation.controller.api.RestController;
import org.kostaTeam2.presentation.view.JsonResult;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/ajax", loadOnStartup = 1)
public class ApiFrontControllerServlet extends HttpServlet {
	private static final ObjectMapper MAPPER = new ObjectMapper()
		.findAndRegisterModules()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private Map<String, RestController> controllerMap = new HashMap<>();

	@Override
	public void init() throws ServletException {
		Object attr = getServletContext().getAttribute("apiControllerMap");
		if (!(attr instanceof Map))
			throw new ServletException("apiControllerMap의 형식이 map이 아닙니다.");
		controllerMap = (Map<String, RestController>)attr;
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws
		ServletException,
		IOException {
		String key = request.getParameter("key");
		String method = request.getParameter("methodName");

		if (key == null || method == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		RestController controller = controllerMap.get(key);
		if (controller == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		JsonResult result;
		try {
			result = controller.handle(method, request, response);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ServletException("API Controller error", e);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if (result.isSuccess()) {
			response.setStatus(HttpServletResponse.SC_OK);
			MAPPER.writeValue(response.getWriter(), result.getData());
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			MAPPER.writeValue(response.getWriter(),
				Map.of("error", result.getErrorMessage()));
		}

	}
}
