// 로그인 폼 동작: 검증 후 메인으로, 회원가입 버튼은 register.jsp로
(function () {
  const form = document.getElementById('loginForm');
  const email = document.getElementById('email');
  const pwd = document.getElementById('password');

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (!email.checkValidity() || !pwd.checkValidity()) {
      email.reportValidity();
      pwd.reportValidity();
      return;
    }
    // 실제 인증 로직 연결 위치
    // auth/ → workspace/ 상대경로로 안전 이동 (contextPath 필요 없음)
    window.location.href = '../workspace/create.jsp';
  });

  document.getElementById('signupBtn').addEventListener('click', function () {
    window.location.href = 'register.jsp';
  });
})();
