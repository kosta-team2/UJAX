package org.kostaTeam2.presentation.view;

public class JsonResult {
	private Object data;
	private String errorMessage;

	public JsonResult(Object data) {
		this.data = data;
		this.errorMessage = null;
	}

	public JsonResult(String errorMessage) {
		this.data = null;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return errorMessage == null;
	}

	public Object getData() {
		return data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
