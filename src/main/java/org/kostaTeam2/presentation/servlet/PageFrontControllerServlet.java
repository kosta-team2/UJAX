package org.kostaTeam2.presentation.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kostaTeam2.presentation.controller.page.Controller;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/front", loadOnStartup = 1)
public class PageFrontControllerServlet extends HttpServlet {
	private Map<String, Controller> controllerMap = new HashMap<>();

	@Override
	public void init() throws ServletException {
		Object attr = getServletContext().getAttribute("controllerMap");
		if (!(attr instanceof Map))
			throw new ServletException("controllerMap의 형식이 map이 아닙니다.");
		controllerMap = (Map<String, Controller>)attr;
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String key = request.getParameter("key");
		String method = request.getParameter("methodName");

		if (key == null || method == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		Controller controller = controllerMap.get(key);
		if (controller == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		ModelAndView mv;
		try {
			mv = controller.handle(method, request, response);
		} catch (Exception e) {
			throw new ServletException("Controller error", e);
		}

		if (mv.isRedirect()) {
			response.sendRedirect(mv.getViewName());
		} else {
			request.getRequestDispatcher(mv.getViewName()).forward(request, response);
		}

	}
}
