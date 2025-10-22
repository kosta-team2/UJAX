package org.kostaTeam2.dto.request;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;

public record WorkspaceProblemRequest(
	Long workspaceId,
	int problemNum,
	boolean isAlarm,
	Integer alarmAt,
	LocalDateTime dueDate
) {
	public static WorkspaceProblemRequest from(HttpServletRequest req) {
		Long workspaceId = Long.valueOf(req.getParameter("workspaceId"));
		int problemNum = Integer.parseInt((req.getParameter("problemNum")));

		String alarmVal = req.getParameter("alarm");
		boolean isAlarm = "on".equalsIgnoreCase(alarmVal) || "true".equalsIgnoreCase(alarmVal);

		Integer alarmAt = null;
		if (isAlarm) {
			String alarmAtStr = req.getParameter("alarmAt");
			alarmAt = Integer.valueOf(alarmAtStr);
		}

		LocalDateTime dueDate = LocalDateTime.parse(req.getParameter("deadline"));

		return new WorkspaceProblemRequest(
			workspaceId,
			problemNum,
			isAlarm,
			alarmAt,
			dueDate
		);
	}
}
