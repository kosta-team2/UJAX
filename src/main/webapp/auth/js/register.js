// 회원가입 폼 UX + 제출 후 메인으로 이동
(function () {
  // ===== Password confirm check =====
  const pwd = document.getElementById('password');
  const confirm = document.getElementById('passwordConfirm');
  const icon = document.getElementById('pw-check-icon');

  function showCheck(show){ icon.classList.toggle('show', !!show); confirm.classList.toggle('has-adornment', !!show); }
  function resetPH(){ confirm.classList.remove('error-ph'); confirm.placeholder=''; }
  function update(){ const p = pwd.value || '', c = confirm.value || ''; showCheck(c && p && c===p); }

  if (pwd && confirm && icon){
    confirm.addEventListener('input', function(){ resetPH(); update(); });
    pwd.addEventListener('input', update);
    confirm.addEventListener('blur', function(){
      const p = pwd.value || '', c = confirm.value || '';
      if(c && p && c!==p){ confirm.value=''; confirm.placeholder='비밀번호가 불일치합니다. 다시 입력해주세요'; confirm.classList.add('error-ph'); showCheck(false); }
    });
    confirm.addEventListener('focus', resetPH);
  }

  // ===== Email code flow =====
  const sendBtn = document.getElementById('sendCodeBtn');
  const codeWrap = document.getElementById('emailCodeWrap');
  const codeInput = document.getElementById('emailCode');
  const btnConfirmEmail = document.getElementById('btnConfirmEmail');

  function openCode(){ codeWrap && codeWrap.classList.add('open'); }
  function closeCode(){ codeWrap && codeWrap.classList.remove('open'); }

  sendBtn && sendBtn.addEventListener('click', function(){ openCode(); codeInput && codeInput.focus(); });
  btnConfirmEmail && btnConfirmEmail.addEventListener('click', function(){
    if(codeInput && !codeInput.value.trim()){ codeInput.focus(); return; }
    location.hash = 'email-verified'; // CSS로 배지 표시
    closeCode();
  });
  function syncVerified(){ if(location.hash === '#email-verified'){ closeCode(); } }
  window.addEventListener('hashchange', syncVerified); syncVerified();

  // ===== Submit → 메인 페이지로 이동 =====
  const form = document.getElementById('registerForm');
  const email = document.getElementById('email');
  const nickname = document.getElementById('nickname');

  form.addEventListener('submit', function(e){
    e.preventDefault();
    // 간단한 검증 파이프라인
    if(!email.checkValidity()){ email.reportValidity(); return; }
    if(location.hash !== '#email-verified'){ openCode(); codeInput && codeInput.focus(); return; }
    if(!pwd.checkValidity()){ pwd.reportValidity(); return; }
    if(!confirm.value || confirm.value !== pwd.value){ confirm.focus(); return; }
    if(!nickname.checkValidity()){ nickname.reportValidity(); return; }

    // 실제 가입 API 연결 위치
    // auth/ → workspace/ 상대경로 사용
    window.location.href = '../workspace/create.jsp';
  });
})();
