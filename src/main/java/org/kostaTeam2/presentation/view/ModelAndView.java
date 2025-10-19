package org.kostaTeam2.presentation.view;

public class ModelAndView {
	private String viewName;
	private boolean isRedirect;

	public ModelAndView() {
	}

	public ModelAndView(String viewName) {
		this.viewName = viewName;
	}

	public ModelAndView(String viewName, boolean isRedirect) {
		this(viewName);
		this.isRedirect = isRedirect;
	}

	public String getViewName() {
		return viewName;
	}

	public boolean isRedirect() {
		return isRedirect;
	}
}
