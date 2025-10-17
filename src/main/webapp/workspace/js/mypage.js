/* ------------------------------------------------------------------
   My Page
   - 사이드바 프로필 박스 클릭 시 mypage.jsp를 #mainContent에 주입
   - 주입 후 mock/user.json으로 데이터 바인딩
------------------------------------------------------------------ */

// 외부에서도 호출 가능하도록 export
window.mountMypage = async function mountMypage(container = document.getElementById('mainContent')) {
  if (!container) return;

  try {
    container.innerHTML = '<div style="padding:24px;color:#A8B3CF">로딩 중...</div>';
	// 같은 디렉터리(또는 실제 위치)에 맞게 상대경로로 요청
	const res = await fetch('mypage.jsp', { cache: 'no-store' });
    const html = await res.text();
    container.innerHTML = html;
	
	// ADD: 개인정보 변경 페이지로 이동
	const editBtn = (container || document).querySelector('#editBtn');
	editBtn && editBtn.addEventListener('click', () => window.mountPersonalInfo && window.mountPersonalInfo());

	// 초기화는 rightSidebar.js의 reload('mypage')에서만 수행
    console.log('✅ 마이페이지 로드 완료');
  } catch (err) {
    container.innerHTML = '<p style="color:#f87171;padding:16px">❌ 마이페이지를 불러올 수 없습니다.</p>';
    console.error(err);
  }
};

// 페이지 내부 초기화(데이터 렌더링, 버튼/모달 바인딩)
window.initMypage = async function initMypage() {
  const root = document.getElementById('mypage-root');
  if (!root) return;

  try {
	// mock 폴더가 index.jsp와 같은 루트에 있을 때
	const res = await fetch('../mock/user.json', { cache: 'no-store' });
    const user = await res.json();
    paintUser(user);
    bindHint(user);
    bindModals();
  } catch (e) {
    console.error('❌ user.json 로드 실패', e);
  }
};

function paintUser(u) {
  // 상단 정보
  qs('#levelPill').textContent = `LV.${u.level ?? '-'}`;
  qs('#nickname').innerHTML = `<strong>${safe(u.nickname)}</strong>`;
  qs('#email').textContent = safe(u.email);

  // 리워드 (KRW)
  qs('#reward').textContent = (u.rewardWon ?? 0).toLocaleString('ko-KR') + '원';

  // EXP
  const cur = u.exp?.current ?? 0;
  const max = u.exp?.max ?? 0;
  const pct = max > 0 ? Math.round((cur / max) * 100) : 0;
  qs('#exp').textContent = cur.toLocaleString('ko-KR');
  qs('#exp-max').textContent = max.toLocaleString('ko-KR');
  qs('#expLegend').textContent = `${cur.toLocaleString('ko-KR')} / ${max.toLocaleString('ko-KR')}`;
  qs('#expPercent').textContent = `${pct}%`;
  qs('#expProgress').style.setProperty('--value', `${pct}%`);

  // 정답률
  const acc = Math.max(0, Math.min(100, Number(u.accuracy) || 0));
  qs('#accRing').style.setProperty('--p', String(acc));
  qs('#accText').textContent = `${acc}%`;
  qs('#accuracy').textContent = `${acc}%`;

  // Streak
  renderStreak(u.streak);
}

function renderStreak(streak) {
  const grid = qs('#streakGrid');
  grid.innerHTML = '';
  const cols = streak?.cols || 26;
  const rows = streak?.rows || 7;
  const total = cols * rows;
  const values = Array.from({ length: total }, (_, i) => streak?.values?.[i] ?? 0);

  values.forEach((v) => {
    const dot = document.createElement('div');
    dot.className = 'dot' + (v > 0 ? ` lv${Math.min(4, Math.max(1, v))}` : '');
    grid.appendChild(dot);
  });
}

function bindHint(u) {
  const on = qs('#hintOn'), off = qs('#hintOff');
  const saved = localStorage.getItem('mypage:showHints');
  const showHints = saved == null ? !!u?.settings?.showHints : saved === 'true';
  (showHints ? on : off).checked = true;

  on.addEventListener('change', () => localStorage.setItem('mypage:showHints', 'true'));
  off.addEventListener('change', () => localStorage.setItem('mypage:showHints', 'false'));
}

function bindModals() {
  const deleteBtn = qs('#deleteBtn');
  const confirmModal = qs('#confirmModal');
  const okBtn = qs('#okConfirm');
  const cancelBtn = qs('#cancelConfirm');

  const resultModal = qs('#resultModal');
  const closeResult = qs('#closeResult');

  const pageAlert = qs('#pageAlert');
  const closeAlert = qs('#closeAlert');

  const open = (modal) => { modal.classList.add('open'); modal.removeAttribute('aria-hidden'); document.body.style.overflow = 'hidden'; };
  const close = (modal) => { modal.classList.remove('open'); modal.setAttribute('aria-hidden', 'true'); document.body.style.overflow = ''; };

  const bindBackdropClose = (modal) => {
    modal.addEventListener('click', (e) => { if (e.target === modal) close(modal); });
  };

  const showPageAlert = (msg) => {
    if (msg) pageAlert.querySelector('.msg').textContent = msg;
    pageAlert.classList.add('show');
  };
  const hidePageAlert = () => pageAlert.classList.remove('show');

  deleteBtn?.addEventListener('click', () => { open(confirmModal); setTimeout(() => qs('#okConfirm').focus(), 0); });
  okBtn?.addEventListener('click', () => {
    close(confirmModal);
    open(resultModal);
    setTimeout(() => closeResult.focus(), 0);
    // 대안: 상단 배너만 쓰고 싶다면 아래 두 줄 사용
    // close(confirmModal);
    // showPageAlert('정상적으로 탈퇴 처리되었습니다.');
  });

  cancelBtn?.addEventListener('click', () => close(confirmModal));
  closeResult?.addEventListener('click', () => close(resultModal));
  closeAlert?.addEventListener('click', hidePageAlert);

  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      if (confirmModal.classList.contains('open')) close(confirmModal);
      if (resultModal.classList.contains('open')) close(resultModal);
      if (pageAlert.classList.contains('show')) hidePageAlert();
    }
  });

  bindBackdropClose(confirmModal);
  bindBackdropClose(resultModal);
}

// utils
function qs(sel, scope = document) { return scope.querySelector(sel); }
function safe(s) { return (s ?? '').toString(); }
