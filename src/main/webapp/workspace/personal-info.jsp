<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 개인정보 변경 (partial JSP: #mainContent에 주입됨) -->
<section id="personalInfoRoot" class="pi page">
  <div class="pi-top">
    <button class="pi-back" id="pi-backBtn" aria-label="뒤로 가기">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
           stroke-linecap="round" stroke-linejoin="round" width="20" height="20">
        <path d="M15 18l-6-6 6-6"/>
      </svg>
    </button>
  </div>

  <h1 class="pi-title">개인정보 변경</h1>

  <section class="pi-panel card">
    <form id="pi-form" class="pi-form" novalidate>
      <!-- 현재 비밀번호 -->
      <div class="pi-field">
        <label for="pi-currentPassword">현재 비밀번호</label>
        <input id="pi-currentPassword" class="pi-input" type="password"
               placeholder="현재 비밀번호를 입력하세요" required />
        <p class="pi-hint">본인 확인을 위해 현재 사용 중인 비밀번호를 입력해주세요.</p>
      </div>

      <div class="pi-divider" aria-hidden="true"></div>

      <!-- 새 비밀번호 -->
      <div class="pi-field">
        <label for="pi-newPassword">새 비밀번호</label>
        <input id="pi-newPassword" class="pi-input" type="password"
               placeholder="8자 이상, 영문/숫자 조합" minlength="8" required />
      </div>

      <!-- 새 비밀번호 확인 -->
      <div class="pi-field">
        <label for="pi-newPasswordConfirm">새 비밀번호 확인</label>
        <div class="pi-input-wrap">
          <input id="pi-newPasswordConfirm" class="pi-input" type="password" required />
          <svg id="pi-pwCheckIcon" class="pi-input-check" viewBox="0 0 24 24" fill="none"
               stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"
               aria-hidden="true"><path d="M20 6L9 17l-5-5"/></svg>
        </div>
      </div>

      <!-- 닉네임 -->
      <div class="pi-field">
        <label for="pi-nickname">닉네임</label>
        <input id="pi-nickname" class="pi-input" type="text"
               placeholder="변경할 닉네임을 입력하세요" minlength="2" maxlength="20" required />
      </div>

      <div class="pi-actions">
        <button type="submit" id="pi-submitBtn" class="pi-btn">변경하기</button>
      </div>
      <p class="pi-hint">보안을 위해 중요한 변경은 다시 로그인할 수 있습니다.</p>
    </form>
  </section>

  <!-- 성공 모달 -->
  <div class="pi-modal" id="pi-successModal" role="dialog" aria-modal="true" aria-labelledby="pi-successTitle" hidden>
    <div class="pi-dialog">
      <div class="pi-dialog-head">
        <svg class="pi-check" viewBox="0 0 24 24" fill="none" stroke="currentColor"
             stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M20 6L9 17l-5-5"/>
        </svg>
        <h2 id="pi-successTitle" class="pi-dialog-title">개인정보가 변경되었습니다</h2>
      </div>
      <div class="pi-dialog-body">변경 사항이 저장되었어요. 일부 항목은 재로그인이 필요할 수 있습니다.</div>
      <div class="pi-dialog-actions">
        <button type="button" id="pi-closeModal" class="pi-btn pi-btn-secondary">확인</button>
      </div>
    </div>
  </div>
</section>