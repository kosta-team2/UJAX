<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>


<!-- ✅ mock 데이터 (추후 Controller 세션/리퀘스트 연동 예정) -->
<c:set var="currentUser" value="${sessionScope.user != null ? sessionScope.user : { 'id': 1, 'name': 'testuser123', 'leader': true } }" />
<c:set var="workspace" value="${requestScope.workspace != null ? requestScope.workspace : {
    'id': 101,
    'name': 'kosta-2조',
    'lang': 'python',
    'members': [
        {'id':1,'name':'testuser123','email':'aaa@example.com','leader':true},
        {'id':2,'name':'최홍만','email':'bbb@example.com','leader':false},
        {'id':3,'name':'정찬성','email':'ccc@example.com','leader':false}
    ]
} }" />

<section id="ws-settings-root"
         class="ws-settings"
         data-ws-id="${workspace.id}"
         data-current-user-id="${currentUser.id}"
         data-is-leader="${currentUser.leader}">

    <section class="card">
        <div class="card-header">
            <h1>워크스페이스 설정</h1>
        </div>

        <form id="ws-settingsForm" class="stack" novalidate>
            <!-- 기본 정보 -->
            <section class="block">
                <div class="block-title">기본 정보</div>
                <div class="grid-2">
                    <div class="field">
                        <label for="ws-wsName">워크스페이스 이름</label>
                        <input id="ws-wsName"
                               type="text"
                               value="${workspace.name}"
                               placeholder="예: KOSTA Study"
                               <c:if test="${!currentUser.leader}">disabled</c:if> />
                    </div>
                    <div class="field">
                        <label for="ws-wsLang">사용 언어 (프로그래밍)</label>
                        <select id="ws-wsLang" <c:if test="${!currentUser.leader}">disabled</c:if>>
                            <c:forEach var="lang" items="${['cpp','java','python','javascript','go','rust']}">
                                <option value="${lang}" <c:if test="${lang eq workspace.lang}">selected</c:if>>
                                        ${lang == 'cpp' ? 'C++' : fn:toUpperCase(lang)}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <c:if test="${currentUser.leader}">
                    <div class="actions left" id="ws-basicActions">
                        <button type="submit" class="btn primary" id="ws-applyBtn">변경</button>
                        <button type="button" class="btn" id="ws-resetBtn">취소</button>
                    </div>
                </c:if>
            </section>

            <!-- 위험 구역 배너 -->
            <c:if test="${currentUser.leader}">
                <section class="danger-banner" id="ws-dangerBanner">
                    <div class="danger-title">위험 구역</div>
                    <p>여기에는 <b>추방, 리더 위임, 워크스페이스 삭제</b> 등 되돌릴 수 없는 작업이 포함됩니다.</p>
                </section>
            </c:if>

            <!-- 멤버 관리 -->
            <section class="block">
                <div class="block-title-row">
                    <div class="block-title">멤버</div>
                    <div class="member-toolbar">
                        <input id="ws-memberSearch" class="search" placeholder="이름/이메일 검색" />
                        <c:if test="${currentUser.leader}">
                            <button type="button" class="btn primary" id="ws-openInvite">멤버 추가</button>
                        </c:if>
                    </div>
                </div>

                <table class="table">
                    <thead>
                    <tr><th>정보</th><th>역할</th><th></th></tr>
                    </thead>
                    <tbody id="ws-memberTbody">
                    <c:forEach var="m" items="${workspace.members}">
                        <tr>
                            <td>
                                <div style="font-weight:600">
                                        ${m.name}<c:if test="${m.id == currentUser.id}"> (나)</c:if>
                                </div>
                                <div style="color:#9aa6bf">${m.email}</div>
                            </td>
                            <td>
                                    <span class="badge ${m.leader ? 'leader' : ''}">
                                            ${m.leader ? '리더' : '멤버'}
                                    </span>
                            </td>
                            <td>
                                <c:if test="${currentUser.leader and !m.leader}">
                                    <div class="row-actions">
                                        <button type="button" class="btn ghost" data-delegate="${m.id}">리더 위임</button>
                                        <button type="button" class="btn warn" data-kick="${m.id}">추방</button>
                                    </div>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${empty workspace.members}">
                    <div class="table-foot" id="ws-emptyState">멤버가 없습니다.</div>
                </c:if>

                <div class="leave-zone">
                    <div>
                        <div class="leave-zone-title">워크스페이스 나가기</div>
                        <p class="leave-zone-desc">
                            이 워크스페이스에서 본인 계정을 탈퇴합니다.
                            리더가 다른 멤버가 남아있는 상태에서 나가려면 먼저 리더를 위임해야 합니다.
                        </p>
                    </div>
                    <button type="button" id="ws-leaveWorkspaceBtn" class="btn danger-outline">워크스페이스 나가기</button>
                </div>

                <c:if test="${currentUser.leader}">
                    <div class="danger-zone" id="ws-dangerZone">
                        <div>
                            <div class="danger-zone-title">워크스페이스 삭제</div>
                            <p class="danger-zone-desc">
                                이 작업은 되돌릴 수 없습니다. 모든 데이터와 설정이 영구적으로 삭제됩니다.
                            </p>
                        </div>
                        <button type="button" id="ws-deleteWorkspaceBtn" class="btn warn">워크스페이스 삭제하기</button>
                    </div>
                </c:if>
            </section>
        </form>
    </section>

    <!-- ✅ 모달 섹션 직접 포함 -->
    <div class="modal" id="ws-inviteModal" hidden>
        <div class="modal-card">
            <div class="modal-title">멤버 추가하기</div>
            <div class="field">
                <label for="ws-inviteEmail">이메일</label>
                <input id="ws-inviteEmail" type="email" placeholder="example@domain.com" />
            </div>
            <div class="modal-actions">
                <button class="btn" id="ws-inviteCancel" type="button">취소</button>
                <button class="btn primary" id="ws-inviteOk" type="button">초대</button>
            </div>
        </div>
    </div>

    <div class="modal" id="ws-confirmModal" hidden>
        <div class="modal-card">
            <div class="modal-title">확인</div>
            <p id="ws-confirmMessage" style="margin:6px 0 0 2px; color: var(--muted)"></p>
            <div class="modal-actions">
                <button class="btn" id="ws-confirmNo" type="button">아니오</button>
                <button class="btn primary" id="ws-confirmYes" type="button">예</button>
            </div>
        </div>
    </div>
</section>

<link rel="stylesheet" href="css/info.css" />
