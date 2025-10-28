<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>회원가입</title>
  <link rel="stylesheet" href="css/register.css">
</head>
<body>
<c:if test="${not empty sessionScope.flashMessageJs}">
    <script>
        alert('${sessionScope.flashMessageJs}');
    </script>
    <c:remove var="flashMessageJs" scope="session"/>
</c:if>
  <div class="page">
    <div class="top">
      <a class="back" href="login.jsp" aria-label="뒤로 가기">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="20" height="20">
          <path d="M15 18l-6-6 6-6"/>
        </svg>
      </a>
    </div>

    <h1 class="title">회원가입</h1>
    <br>

    <div class="panel">
      <form id="registerForm" action="#" method="post" novalidate>
        <!-- 이메일 -->
        <div class="field email-field">
          <label for="email">이메일</label>
          <div class="inline">
            <input id="email" class="input" name="email" type="email" placeholder="name@example.com" required />
            <div class="right-stack">
              <span id="email-verified" class="verified-badge" aria-live="polite"><span class="verified-text">verified</span>
                <svg class="verified-check" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                  <path d="M20 6L9 17l-5-5"/>
                </svg>
              </span>
              <button type="button" class="btn small send-btn" id="sendCodeBtn" aria-label="인증코드 전송">인증코드 전송</button>
            </div>
          </div>
          <p class="hint">이메일 인증 후에만 가입이 가능합니다.</p>
          <div id="emailCodeWrap" class="code-wrap">
            <input id="emailCode" class="input" type="text" inputmode="numeric" pattern="[0-9]*" placeholder="인증코드 6자리" autocomplete="one-time-code" />
            <button type="button" class="btn small" id="btnConfirmEmail">인증완료</button>
          </div>
          <p class="code-hint" aria-live="polite">인증코드가 전송되었습니다. 10분 이내에 입력해 주세요.</p>
        </div>

        <div class="divider"></div>

        <!-- 비밀번호 -->
        <div class="field">
          <label for="password">비밀번호</label>
          <input id="password" class="input" name="password" type="password" placeholder="비밀번호는 8자 이상 입력해주세요" minlength="8" required />
        </div>

        <!-- 비밀번호 확인 -->
        <div class="field pw-confirm-field">
          <label for="passwordConfirm">비밀번호 확인</label>
          <div class="input-wrap">
            <input id="passwordConfirm" class="input" name="passwordConfirm" type="password" required />
            <svg id="pw-check-icon" class="input-check" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="M20 6L9 17l-5-5"/>
            </svg>
          </div>
        </div>

        <div class="field">
          <label for="nickname">닉네임 설정</label>
          <input id="nickname" class="input" name="nickname" type="text" placeholder="예: 코딩너구리(닉네임은 최소 2글자 최대 20글자로 설정 가능합니다.)" minlength="2" maxlength="20" required />
        </div>

        <div class="actions">
          <button class="btn" type="submit">가입하기</button>
        </div>
        <div class="foot">가입 후에도 이메일을 제외한 프로필 정보는 언제든 변경할 수 있습니다.</div>
      </form>
    </div>
  </div>
  <script defer src="js/register.js?v=${System.currentTimeMillis()}"></script>
</body>
</html>
