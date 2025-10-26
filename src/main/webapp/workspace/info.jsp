<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:if test="${not empty sessionScope.flashMessageJs}">
    <script>
        alert('${sessionScope.flashMessageJs}');
    </script>
    <c:remove var="flashMessageJs" scope="session"/>
</c:if>

<link rel="stylesheet" id="theme-style" href="<c:url value='/common/css/darkmode.css'/>">
<link rel="stylesheet" href="<c:url value='/workspace/css/info.css'/>">

<section id="ws-settings-root"
         class="ws-settings"
         data-ws-id="${workspace.workspaceId}"
         data-current-user-email="${currentUserEmail}"
         data-is-leader="${isLeader}">

    <section class="card">
        <div class="card-header">
            <h1>워크스페이스 설정</h1>
        </div>

        <form id="ws-settingsForm"
              class="stack"
              method="post"
              action="${pageContext.request.contextPath}/front?key=workspace&methodName=update">

            <!-- DTO에 맞게 수정 -->
            <input type="hidden" name="workspaceId" value="${workspace.workspaceId}"/>

            <section class="block">
                <div class="block-title">기본 정보</div>
                <div class="grid-2">
                    <div class="field">
                        <label for="ws-wsName">워크스페이스 이름</label>
                        <input id="ws-wsName"
                               name="workspaceName"
                               type="text"
                               value="${workspace.workspaceName}"
                               placeholder="예: KOSTA Study"
                               <c:if test="${!isLeader}">disabled</c:if> />
                    </div>

                    <div class="field">
                        <label for="ws-wsLang">사용 언어 (프로그래밍)</label>
                        <c:set var="langs" value="${{
                'PYTHON3':'Python 3',
                'PYPY3':'PyPy3',
                'C99':'C99',
                'JAVA11':'Java 11',
                'RUBY':'Ruby',
                'KOTLIN':'Kotlin (JVM)',
                'SWIFT':'Swift',
                'TEXT':'Text',
                'CS':'C#',
                'NODE':'Node.js',
                'GO':'Go',
                'D':'D',
                'RUST2018':'Rust 2018',
                'CPP17':'C++17 (Clang)'
            }}"/>

                        <select id="ws-wsLang" name="workspaceLanguage" <c:if test="${!isLeader}">disabled</c:if>>
                            <c:forEach var="entry" items="${langs}">
                                <option value="${fn:toLowerCase(entry.key)}"
                                        <c:if test="${workspace.workspaceLanguage.name() == entry.key}">selected</c:if>>
                                        ${entry.value}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <!-- 🔄 체크박스 → 라디오 세그먼트 UI -->
                <div class="grid-2" style="margin-top:10px">
                    <div class="field">
                        <label>힌트보기 설정 <span class="field-desc">문제 풀이 시 알고리즘 유형 표시 여부</span></label>

                        <div class="hint-radio-group">
                            <input type="radio"
                                   id="hint-on"
                                   name="isHintView"
                                   value="true"
                                   <c:if test="${workspace.hintView}">checked</c:if>
                                   <c:if test="${!isLeader}">disabled</c:if> />
                            <label for="hint-on">On</label>

                            <input type="radio"
                                   id="hint-off"
                                   name="isHintView"
                                   value="false"
                                   <c:if test="${!workspace.hintView}">checked</c:if>
                                   <c:if test="${!isLeader}">disabled</c:if> />
                            <label for="hint-off">Off</label>
                        </div>
                    </div>
                </div>

                <c:if test="${isLeader}">
                    <div class="actions left" id="ws-basicActions">
                        <button type="submit" class="btn primary" id="ws-applyBtn">변경</button>
                        <button type="reset" class="btn" id="ws-resetBtn">취소</button>
                    </div>
                </c:if>
            </section>
        </form>

        <!-- ✅ 아래 두 섹션을 동일 패딩/규격으로 보이도록 stack로 감쌈 -->
        <div class="stack">

            <!-- =========================
                 2) 위험 구역 배너
               ========================= -->
            <c:if test="${isLeader}">
                <section class="danger-banner" id="ws-dangerBanner">
                    <div class="danger-title">위험 구역</div>
                    <p>여기에는 <b>추방, 리더 위임, 워크스페이스 삭제</b> 등 되돌릴 수 없는 작업이 포함됩니다.</p>
                </section>
            </c:if>

            <!-- =========================
                 3) 멤버 관리
               ========================= -->
            <section class="block">
                <div class="block-title-row">
                    <div class="block-title">멤버</div>
                    <div class="member-toolbar">
                        <%--<input id="ws-memberSearch" class="search" placeholder="이름/이메일 검색"/>--%>
                        <c:if test="${isLeader}">
                            <button type="button" class="btn primary" id="ws-openInvite">멤버 추가</button>
                        </c:if>
                    </div>
                </div>

                <table class="table">
                    <thead>
                    <tr>
                        <th class="th-user">정보</th>
                        <th class="th-role">역할</th>
                        <th class="th-actions"></th>
                    </tr>
                    </thead>
                    <tbody id="ws-memberTbody">
                    <c:choose>
                        <c:when test="${not empty workspace.workspaceMemberList}">
                            <c:forEach var="m" items="${workspace.workspaceMemberList}">
                                <tr>
                                    <td>
                                        <div style="font-weight:600">
                                                ${m.nickname}
                                            <c:if test="${not empty currentUserEmail and m.email == currentUserEmail}"> (나)</c:if>
                                        </div>
                                        <div style="color:#9aa6bf">${m.email}</div>
                                    </td>
                                    <td>
                      <span class="badge ${m.leader ? 'leader' : ''}">
                              ${m.leader ? '리더' : '멤버'}
                      </span>
                                    </td>
                                    <td class="actions-cell">
                                        <c:if test="${isLeader and !m.leader}">
                                            <div class="row-actions">
                                                <!-- 리더 위임 -->
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/front?key=workspace&methodName=updateRole">
                                                    <input type="hidden" name="workspaceId"
                                                           value="${workspace.workspaceId}"/>
                                                    <input type="hidden" name="wsMemberId"
                                                           value="${m.workspaceMemberId}"/>
                                                    <button type="submit" class="btn ghost">리더 위임</button>
                                                </form>

                                                <!-- 추방 -->
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/front?key=workspace&methodName=kickUser">
                                                    <input type="hidden" name="workspaceId"
                                                           value="${workspace.workspaceId}"/>
                                                    <input type="hidden" name="wsMemberId"
                                                           value="${m.workspaceMemberId}"/>
                                                    <button type="submit" class="btn warn">추방</button>
                                                </form>
                                            </div>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3" class="table-foot" id="ws-emptyState">멤버가 없습니다.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>

                <!-- 나가기 -->
                <div class="leave-zone">
                    <div>
                        <div class="leave-zone-title">워크스페이스 나가기</div>
                        <p class="leave-zone-desc">
                            이 워크스페이스에서 본인 계정을 탈퇴합니다.
                            리더가 다른 멤버가 남아있는 상태에서 나가려면 먼저 리더를 위임해야 합니다.
                        </p>
                    </div>
                    <form method="post"
                          action="${pageContext.request.contextPath}/front?key=workspace&methodName=exit">
                        <input type="hidden" name="workspaceId" value="${workspace.workspaceId}"/>
                        <button type="submit" id="ws-leaveWorkspaceBtn" class="btn danger-outline">워크스페이스 나가기</button>
                    </form>
                </div>

                <!-- 삭제 -->
                <c:if test="${isLeader}">
                    <div class="danger-zone" id="ws-dangerZone">
                        <div>
                            <div class="danger-zone-title">워크스페이스 삭제</div>
                            <p class="danger-zone-desc">
                                이 작업은 되돌릴 수 없습니다. 모든 데이터와 설정이 영구적으로 삭제됩니다.
                            </p>
                        </div>
                        <form method="post"
                              action="${pageContext.request.contextPath}/front?key=workspace&methodName=delete">
                            <input type="hidden" name="workspaceId" value="${workspace.workspaceId}"/>
                            <button type="submit" id="ws-deleteWorkspaceBtn" class="btn warn">워크스페이스 삭제하기</button>
                        </form>
                    </div>
                </c:if>
            </section>
        </div>
    </section>

    <!-- 모달 -->
    <div class="modal" id="ws-inviteModal" hidden>
        <div class="modal-card">
            <div class="modal-title">멤버 추가하기</div>
            <div class="field">
                <label for="ws-inviteEmail">이메일</label>
                <input id="ws-inviteEmail" type="email" placeholder="example@domain.com"/>
            </div>
            <div class="modal-actions">
                <button class="btn" id="ws-inviteCancel" type="button">취소</button>
                <button class="btn primary" id="ws-inviteOk" type="button" disabled>초대</button>
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

<script src="<c:url value='/workspace/js/info.js'/>?v=${System.currentTimeMillis()}" defer></script>
