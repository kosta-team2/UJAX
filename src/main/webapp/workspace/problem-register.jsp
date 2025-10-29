<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:if test="${not empty sessionScope.flashMessageJs}">
    <script>
        alert('${sessionScope.flashMessageJs}');
    </script>
    <c:remove var="flashMessageJs" scope="session"/>
</c:if>

<link rel="stylesheet" id="theme-style" href="../common/css/darkmode.css">
<link rel="stylesheet" href="css/problem-register.css">

<section class="register-problem-section" id="register-problem-fragment">
    <div class="card">
        <div class="card-header">
            <a class="back-link" id="backBtn"
               onclick="history.back()">←
                돌아가기</a>
            <h1>문제 등록</h1>
        </div>

        <div class="promo" id="recommendBox">
      <span id="recommendText">
        추천: <b class="problem-link">맞춤 추천을 준비 중이에요.</b>
      </span>
            <button id="refreshRecommend" class="refresh" aria-label="추천 새로고침">↻</button>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/front" class="grid-form"
              id="registerForm">
            <input type="hidden" name="key" value="problem"/>
            <input type="hidden" name="methodName" value="create"/>
            <input type="hidden" name="workspaceId" value="${param.workspaceId}"/>
            <input type="hidden" name="workspaceMemberId" value="${param.workspaceMemberId}"/>
            <input type="hidden" name="isLeader" value="${param.isLeader}"/>

            <div class="form-col">
                <div class="field">
                    <label for="problemNum">문제 번호</label>
                    <input
                            id="problemNum"
                            name="problemNum"
                            type="text"
                            placeholder="문제 번호를 입력하세요"
                            required
                    />
                </div>

                <div class="field">
                    <label for="deadline">제출 기한</label>
                    <input
                            id="deadline"
                            name="deadline"
                            type="datetime-local"
                            step="1800"
                            required
                    />
                    <p class="hint">현재 시각 기준 <b>2시간 이후</b>만 설정할 수 있습니다.</p>
                </div>
            </div>

            <div class="form-col">
                <div class="field">
                    <label>알람 설정</label>
                    <div class="alarm-pill" role="radiogroup" aria-label="알림 설정">
                        <label class="radio-item">
                            <input type="radio" name="alarm" value="on" id="alarmOn"/>
                            <span class="dot"></span><span class="txt">On</span>
                        </label>
                        <label class="radio-item">
                            <input type="radio" name="alarm" value="off" id="alarmOff" checked/>
                            <span class="dot"></span><span class="txt">Off</span>
                        </label>
                    </div>
                </div>

                <div id="alarmOptions" class="alarm-options" hidden>
                    <div class="opt">
                        <label for="alarmAt">마감 몇 시간 전부터</label>
                        <div class="opt-row">
                            <input id="alarmAt" name="alarmAt" type="number" min="1" max="24" step="1" value="1"
                                   inputmode="numeric"/>
                            <span class="unit">시간 전</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="actions">
                <button type="submit" class="btn primary">등록</button>
            </div>
        </form>
    </div>

</section>

<%--TODO: 알람 시간 받아오는 내용을 def로 받아오지 못해 일단 생으로 삽입--%>
<script>
    (() => {
        console.info('[problem-register] loaded');

        function syncAlarmUI(form) {
            if (!form) return;
            const options = form.querySelector('#alarmOptions');
            const alarmAt = form.querySelector('#alarmAt');
            const checked = form.querySelector('input[name="alarm"]:checked');
            const isOn = !!checked && checked.value === 'on';

            if (options) {
                options.hidden = !isOn;             // 속성 기반
                options.classList.toggle('show', isOn); // 클래스 기반 (CSS 충돌 대비)
            }
            if (alarmAt) {
                alarmAt.disabled = !isOn;  // off면 전송 안 됨
                alarmAt.required = isOn;
            }
        }

        function bindForm(form) {
            if (!form || form.dataset._alarmBound === '1') return;
            form.dataset._alarmBound = '1';

            syncAlarmUI(form);

            const onAny = () => syncAlarmUI(form);
            form.addEventListener('change', (e) => {
                const t = e.target;
                if (t && t.name === 'alarm') onAny();
            });
            form.addEventListener('input', (e) => {
                const t = e.target;
                if (t && t.name === 'alarm') onAny();
            });
            form.addEventListener('click', (e) => {
                // 라벨 클릭이 들어와도 반응하도록 보강
                const label = e.target.closest?.('.radio-item');
                if (label && label.querySelector('input[name="alarm"]')) {
                    // 브라우저 토글 직후에 동기화
                    setTimeout(onAny, 0);
                }
            });

            console.info('[problem-register] bound form');
        }

        function bindNow() {
            const forms = document.querySelectorAll('#registerForm');
            forms.forEach(bindForm);
        }

        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', bindNow, {once: true});
        } else {
            bindNow();
        }

        const mo = new MutationObserver(() => bindNow());
        mo.observe(document.documentElement, {childList: true, subtree: true});

        let kicks = 0;
        const kick = setInterval(() => {
            bindNow();
            document.querySelectorAll('#registerForm').forEach(syncAlarmUI);
            if (++kicks > 20) clearInterval(kick); // 2초 정도만 보강
        }, 100);

        window.__problemRegisterSync = bindNow;
    })();
</script>
