package org.kostaTeam2.presentation.servelt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kostaTeam2.presentation.controller.api.RestController;
import org.kostaTeam2.presentation.controller.api.WorkspaceApiController;
import org.kostaTeam2.presentation.view.JsonResult;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/ajax", loadOnStartup = 1)
public class ApiFrontControllerServlet extends HttpServlet {
	private Map<String, RestController> controllerMap = new HashMap<>();

	@Override
	public void init() throws ServletException {
		controllerMap.put("workspace", new WorkspaceApiController());
		//TODO: 컨트롤러 매핑 추가
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
		} catch (Exception e) {
			throw new ServletException("API Controller error", e);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if (result.isSuccess()) {
			String jsonOutput = new Gson().toJson(result.getData());
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(jsonOutput);
		} else {
			String errorMsg = result.getErrorMessage();
			String errorJson = "{\"error\":\"" + errorMsg + "\"}";
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(errorJson);
		}

	}
}
