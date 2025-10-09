<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<section id="ws-settings-root" class="ws-settings">
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
                        <input id="ws-wsName" type="text" placeholder="예: KOSTA Study" required />
                    </div>
                    <div class="field">
                        <label for="ws-wsLang">사용 언어 (프로그래밍)</label>
                        <select id="ws-wsLang">
                            <option value="cpp">C++</option>
                            <option value="java">Java</option>
                            <option value="python" selected>Python</option>
                            <option value="javascript">JavaScript</option>
                            <option value="go">Go</option>
                            <option value="rust">Rust</option>
                        </select>
                    </div>
                </div>
                <div class="actions left" id="ws-basicActions">
                    <button type="submit" class="btn primary" id="ws-applyBtn">변경</button>
                    <button type="button" class="btn" id="ws-resetBtn">취소</button>
                </div>
            </section>

            <!-- 위험 구역 배너 -->
            <section class="danger-banner" id="ws-dangerBanner">
                <div class="danger-title">위험 구역</div>
                <p>여기에는 <b>추방, 리더 위임, 워크스페이스 삭제</b> 등 되돌릴 수 없는 작업이 포함됩니다.</p>
            </section>

            <!-- 멤버 관리 -->
            <section class="block">
                <div class="block-title-row">
                    <div class="block-title">멤버</div>
                    <div class="member-toolbar">
                        <input id="ws-memberSearch" class="search" placeholder="이름/이메일 검색" />
                        <button type="button" class="btn primary" id="ws-openInvite">멤버 추가</button>
                    </div>
                </div>

                <table class="table">
                    <colgroup>
                        <col class="col-user" />
                        <col class="col-role" />
                        <col class="col-actions" />
                    </colgroup>
                    <thead>
                    <tr><th class="th-user">정보</th><th class="th-role">역할</th><th class="th-actions"></th></tr>
                    </thead>
                    <tbody id="ws-memberTbody"></tbody>
                </table>
                <div class="table-foot" id="ws-emptyState" hidden>멤버가 없습니다.</div>

                <!-- 워크스페이스 나가기 -->
                <div class="leave-zone">
                    <div>
                        <div class="leave-zone-title">워크스페이스 나가기</div>
                        <p class="leave-zone-desc">이 워크스페이스에서 본인 계정을 탈퇴합니다. 리더가 다른 멤버가 남아있는 상태에서 나가려면 먼저 리더를 위임해야 합니다.</p>
                    </div>
                    <button type="button" id="ws-leaveWorkspaceBtn" class="btn danger-outline">워크스페이스 나가기</button>
                </div>

                <!-- 워크스페이스 삭제 -->
                <div class="danger-zone" id="ws-dangerZone">
                    <div>
                        <div class="danger-zone-title">워크스페이스 삭제</div>
                        <p class="danger-zone-desc">이 작업은 되돌릴 수 없습니다. 모든 데이터와 설정이 영구적으로 삭제됩니다.</p>
                    </div>
                    <button type="button" id="ws-deleteWorkspaceBtn" class="btn warn">워크스페이스 삭제하기</button>
                </div>
            </section>
        </form>
    </section>

    <!-- 멤버 초대 모달 -->
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

    <!-- 공용 확인 모달 -->
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

<!-- CSS는 link가 innerHTML로도 적용되므로 포함해도 OK -->
<link rel="stylesheet" href="css/workspaceInfo.css" />
<!-- JS는 innerHTML로 실행되지 않으니, 선택지 A/B/C 중 하나로 실행 필요 -->
