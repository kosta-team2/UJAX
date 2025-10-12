(function(){
  if (window.mountPersonalInfo) return;

  const ctx = (p) => (window.FE_CTX || '') + p;

  function qs(sel, scope=document){ return scope.querySelector(sel); }

  function openModal(m){ m.hidden = false; document.body.style.overflow = 'hidden'; }
  function closeModal(m){ m.hidden = true; document.body.style.overflow = ''; }

  // 외부에서 호출되는 엔트리
  window.mountPersonalInfo = async function mountPersonalInfo(container = document.getElementById('mainContent')) {
    if (!container) return;

    try {
      container.innerHTML = '<div style="padding:24px;color:#A8B3CF">로딩 중...</div>';
      const res = await fetch('personal-info.jsp', { cache: 'no-store' });
      const html = await res.text();
      container.innerHTML = html;

      // 엘리먼트 캐시
      const root = qs('#personalInfoRoot', container);
      const currentPw = qs('#pi-currentPassword', root);
      const newPw = qs('#pi-newPassword', root);
      const newPwC = qs('#pi-newPasswordConfirm', root);
      const nick = qs('#pi-nickname', root);
      const checkIcon = qs('#pi-pwCheckIcon', root);
      const form = qs('#pi-form', root);
      const backBtn = qs('#pi-backBtn', root);
      const modal = qs('#pi-successModal', root);
      const closeBtn = qs('#pi-closeModal', root);

      // 닉네임 초기값: mock/user.json
      try {
        const u = await fetch(ctx('/mock/user.json'), { cache: 'no-store' }).then(r=>r.json());
        if (u?.nickname) nick.value = u.nickname;
      } catch(e){ /* mock이 없으면 무시 */ }
	  
	  // ── 커스텀 에러 초기화 ─────────────────────────────
	  function clearAllValidity(){
	    [currentPw, newPw, newPwC, nick].forEach(el => el.setCustomValidity(''));
	  }
	  // 타이핑하면 해당 인풋의 커스텀 에러 제거
	  [currentPw, newPw, newPwC, nick].forEach(el => {
	    el.addEventListener('input', () => el.setCustomValidity(''));
	  });

      // 비밀번호 일치 체크
      function showCheck(show){
        checkIcon.classList.toggle('pi-show', !!show);
        newPwC.classList.toggle('pi-hasAdornment', !!show);
      }
      function resetPh(){
        newPwC.classList.remove('pi-errorPh');
        newPwC.placeholder = '';
      }
      function updateMatch(){
        const p = newPw.value || '';
        const c = newPwC.value || '';
        showCheck(p && c && p === c);
      }
      newPw.addEventListener('input', updateMatch);
      newPwC.addEventListener('input', () => { resetPh(); updateMatch(); });
      newPwC.addEventListener('blur', () => {
        if (newPwC.value && newPw.value !== newPwC.value){
          newPwC.value = '';
          newPwC.placeholder = '비밀번호가 불일치합니다. 다시 입력해주세요';
          newPwC.classList.add('pi-errorPh');
          showCheck(false);
        }
      });
      newPwC.addEventListener('focus', resetPh);
	  
	  // 복귀: 마이페이지로 이동 + 데이터 재바인딩(=초기화 reload)
	  async function goMypage() {
	    const main = document.getElementById('mainContent');
	    if (window.mountMypage) {
	      await window.mountMypage(main);  // JSP 주입
	      window.reload && window.reload('mypage'); // ✅ init 실행 → user.json 재로딩
	    } else {
	      history.back();
	    }
	  }

	  // 제출
	  form.addEventListener('submit', async (e)=>{
	    e.preventDefault();

	    // 이전 에러 초기화
	    clearAllValidity();

	    // 1) 현재 비밀번호
	    if (!currentPw.value) {
	      currentPw.setCustomValidity('현재 비밀번호를 입력하세요.');
	      form.reportValidity();
	      currentPw.focus();
	      return;
	    }

	    // 2) 새 비밀번호 길이
	    if (!newPw.value || newPw.value.length < 8) {
	      newPw.setCustomValidity('새 비밀번호는 8자 이상이어야 합니다.');
	      form.reportValidity();
	      newPw.focus();
	      return;
	    }

	    // 3) 새 비밀번호 확인 일치
	    if (newPw.value !== newPwC.value) {
	      newPwC.setCustomValidity('비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
	      form.reportValidity();
	      newPwC.focus();
	      return;
	    }

	    // 4) 닉네임
	    if (!nick.value) {
	      nick.setCustomValidity('닉네임을 입력하세요.');
	      form.reportValidity();
	      nick.focus();
	      return;
	    }

	    // 모든 검증 통과 → 성공 모달 오픈
	    openModal(modal);
	  });


	  // 뒤로가기: 마이페이지로
	  backBtn.addEventListener('click', async ()=>{
	    await goMypage();
	  });

	  // 모달 확인(닫기) 시 마이페이지로
	  closeBtn.addEventListener('click', async ()=>{
	    closeModal(modal);
	    await goMypage();
	  });

	  // ESC로 모달 닫힐 때도 마이페이지로
	  window.addEventListener('keydown', async (e)=>{
	    if (e.key === 'Escape' && !modal.hidden) {
	      closeModal(modal);
	      await goMypage();
	    }
	  });

	  console.log('✅ 개인정보 변경 페이지 로드 완료');
    } catch (err) {
      console.error(err);
      container.innerHTML = '<p style="color:#f87171;padding:24px">개인정보 변경 페이지를 불러오지 못했습니다.</p>';
    }
  };
})();
