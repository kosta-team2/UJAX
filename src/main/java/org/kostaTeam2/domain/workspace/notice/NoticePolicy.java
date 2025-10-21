package org.kostaTeam2.domain.workspace.notice;

public interface NoticePolicy {
    // 리더인지 검사, 아니면 예외
    void assertCanManage(long workspaceId, Long memberId);

}
