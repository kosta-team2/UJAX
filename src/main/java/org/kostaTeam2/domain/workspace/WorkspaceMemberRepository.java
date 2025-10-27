package org.kostaTeam2.domain.workspace;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.kostaTeam2.domain.member.Member;

public interface WorkspaceMemberRepository {
	/**
	 * 워크스페이스 멤버 생성
	 */
	int save(Connection conn, WorkspaceMember workspaceMember) throws SQLException;

	/**
	 * 워크스페이스의 리더인지 확인
	 */
	boolean isLeader(Connection conn, WorkspaceMember workspaceMember) throws SQLException;

	/**
	 * 워크스페이스의 멤버인지 확인
	 */
	boolean isMember(Connection conn, WorkspaceMember workspaceMember) throws SQLException;

	/**
	 * 워크스페이스 멤버 불러오기
	 */
	List<WorkspaceMember> getAllMembers(Connection conn, long workspaceId) throws SQLException;

	/**
	 * 리더 위임하기
	 */
	boolean delegateLeader(Connection con, long workspaceId, long currentLeaderId, long newLeaderId) throws
		SQLException;

	/**
	 * 멤버 방출하기
	 */
	int kickUser(Connection con, long workspaceId, long wsMemberId) throws SQLException;

	/**
	 * 워크스페이스 나가기(session 의 memberId 기반이라 dao에서 wsMemberId 로 변환해야함
	 */
	int exitWorkspace(Connection con, long workspaceId, long memberId) throws SQLException;

	/**
	 * 워크스페이스에 1명만 남아있는지 확인
	 */
	boolean amIOnlyPerson(Connection con, long workspaceId) throws SQLException;

	/**
	 * workspace id, member id 기반 workspace_member_id 가져오기
	 * session 통해 dto 로 받아올때는 workspace_member_id 못 가져와서 만듬
	 */
	Optional<Long> findWsMemberIdByWsIdAndMemberId(Connection conn, long wsId, long memberId) throws SQLException;

	List<WorkspaceMember> findByMemberId(Connection conn, Long memberId);

	/**
	 * 워크스페이스별 레벨 top5를 불러온다
	 */
	List<Member> findTopNByLevel(Connection conn, long workspaceId, int limit) throws SQLException;
}
