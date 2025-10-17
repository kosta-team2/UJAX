<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page" id="mypage-root">
  <div class="page-head">
    <h1 class="title">내 프로필 (My Page)</h1>
    <div class="actions">
      <button class="btn btn-ghost" id="editBtn">개인정보 변경</button>
      <button class="btn btn-danger-outline" id="deleteBtn">회원 탈퇴</button>
    </div>
  </div>

  <div class="stack-16">
    <!-- 내 정보 상세 -->
    <section class="card" aria-labelledby="infoTitle">
      <div class="card-header">
        <div class="card-title" id="infoTitle">내 정보 상세</div>
        <span class="pill" id="levelPill">LV.-</span>
      </div>
      <div class="kv">
        <div class="k">닉네임</div>
        <div id="nickname"><strong>-</strong></div>

        <div class="k">이메일</div>
        <div id="email">-</div>

        <div class="k">리워드</div>
        <div><strong id="reward">0원</strong></div>

        <div class="k">경험치</div>
        <div><span id="exp">0</span> / <span id="exp-max">0</span></div>

        <div class="k">정답률</div>
        <div><span id="accuracy">0%</span></div>
      </div>
    </section>

    <!-- 활동 지표 -->
    <section class="card">
      <div class="card-header">
        <div class="card-title">활동 지표</div>
        <span class="small">EXP & 정답률</span>
      </div>
      <div class="metrics">
        <div class="progress" id="expProgress" style="--value:0%">
          <div style="font-weight:700;margin-bottom:8px">경험치 진행률</div>
          <div class="bar"><span aria-hidden="true"></span></div>
          <div class="legend">
            <span id="expLegend">0 / 0</span>
            <span id="expPercent">0%</span>
          </div>
        </div>

        <div style="display:flex;gap:16px;align-items:center;justify-content:center">
          <div class="ring" id="accRing" style="--p:0"></div>
          <div>
            <div style="font-size:28px;font-weight:800" id="accText">0%</div>
            <div class="small">평균 정답률</div>
          </div>
        </div>
      </div>
    </section>

    <!-- Daily Streak -->
    <section class="card">
      <div class="card-header">
        <div class="card-title">Daily Streak</div>
      </div>
      <div class="streak" id="streakGrid" aria-hidden="true"></div>
    </section>

    <!-- 힌트보기 설정 -->
    <section class="card">
      <div class="card-header">
        <div class="card-title">힌트보기 설정</div>
        <span class="small">문제 풀이 시 힌트 표시 여부</span>
      </div>
      <div class="radio-wrap" role="radiogroup" aria-label="힌트보기 설정">
        <label class="radio">
          <input type="radio" name="hint" value="on" id="hintOn">
          <span class="label">On</span>
        </label>
        <label class="radio off">
          <input type="radio" name="hint" value="off" id="hintOff">
          <span class="label">Off</span>
        </label>
      </div>
    </section>
  </div>
</div>

<!-- Confirm Modal -->
<div class="modal" id="confirmModal" aria-hidden="true" role="dialog" aria-modal="true" aria-labelledby="confirmTitle">
  <div class="dialog">
    <h3 class="card-title" id="confirmTitle">정말로 탈퇴하시겠습니까?</h3>
    <p class="desc">회원탈퇴 시 모든 데이터가 삭제될 수 있습니다.</p>
    <div class="actions" style="justify-content:flex-end">
      <button class="btn btn-danger-outline" id="okConfirm">확인</button>
      <button class="btn btn-ghost" id="cancelConfirm">취소</button>
    </div>
  </div>
</div>

<!-- Result Modal -->
<div class="modal" id="resultModal" aria-hidden="true" role="dialog" aria-modal="true" aria-labelledby="resultTitle">
  <div class="dialog">
    <h3 class="card-title" id="resultTitle">정상적으로 탈퇴 처리되었습니다.</h3>
    <div class="actions" style="justify-content:flex-end;margin-top:8px">
      <button class="btn btn-ghost" id="closeResult">확인</button>
    </div>
  </div>
</div>

<!-- Page Alert -->
<div class="page-alert" id="pageAlert" role="alert">
  <span class="msg">정상적으로 탈퇴 처리되었습니다.</span>
  <button class="close" id="closeAlert" aria-label="알림 닫기">×</button>
</div>
