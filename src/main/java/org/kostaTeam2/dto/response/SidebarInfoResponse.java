package org.kostaTeam2.dto.response;

public class SidebarInfoResponse {
	long workspaceId;
	long workspaceMemberId;
	boolean leader;
	String wsName;

	public static SidebarInfoResponse of(
		long workspaceId,
		long workspaceMemberId,
		boolean leader,
		String wsName
	){
		return new SidebarInfoResponse(
			workspaceId,
			workspaceMemberId,
			leader,
			wsName
		);
	}

	public SidebarInfoResponse(long workspaceId, long workspaceMemberId, boolean leader, String wsName) {
		this.workspaceId = workspaceId;
		this.workspaceMemberId = workspaceMemberId;
		this.leader = leader;
		this.wsName = wsName;
	}

	public long getWorkspaceId() {
		return workspaceId;
	}

	public long getWorkspaceMemberId() {
		return workspaceMemberId;
	}

	public boolean isLeader() {
		return leader;
	}

	public String getWsName() {
		return wsName;
	}
}
