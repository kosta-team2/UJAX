// 워크스페이스 생성 페이지 스크립트
(function () {
  'use strict';

  const form = document.getElementById('createForm');
  const wsName = document.getElementById('wsName');
  const langSelect = document.getElementById('langSelect');
  const modal = document.getElementById('createdModal');
  const modalOk = document.getElementById('modalOk');

  // 언어 목록 로드 (mock/language.json)
  const LANG_URL = '../mock/language.json';
  const fallback = [
    { id: 'python3', label: 'Python 3' },
    { id: 'pypy3', label: 'PyPy3' },
    { id: 'c99', label: 'C99' },
    { id: 'java11', label: 'Java 11' },
    { id: 'ruby', label: 'Ruby' },
    { id: 'kotlin', label: 'Kotlin (JVM)' },
    { id: 'swift', label: 'Swift' },
    { id: 'text', label: 'Text' },
    { id: 'cs', label: 'C#' },
    { id: 'node', label: 'node.js' },
    { id: 'go', label: 'Go' },
    { id: 'd', label: 'D' },
    { id: 'rust2018', label: 'Rust 2018' },
    { id: 'cpp17', label: 'C++17 (Clang)' }
  ];

  function injectOptions(list) {
    // 기존 placeholder 유지, 이후 옵션 주입
    list.forEach(({ id, label }) => {
      const opt = document.createElement('option');
      opt.value = id;
      opt.textContent = label;
      langSelect.appendChild(opt);
    });
  }

  fetch(LANG_URL, { cache: 'no-store' })
    .then(r => r.ok ? r.json() : Promise.reject(r.status))
    .then(json => Array.isArray(json?.langs) ? json.langs : fallback)
    .catch(() => fallback)
    .then(injectOptions);

  // 폼 제출 → 생성 후 메인으로 이동
  form.addEventListener('submit', (e) => {
    e.preventDefault();

    if (!wsName.checkValidity()) { wsName.reportValidity(); return; }
    if (!langSelect.value) { langSelect.focus(); return; }

    // (선택) mock 저장
    try {
      const ws = { name: wsName.value.trim(), lang: langSelect.value, createdAt: Date.now() };
      localStorage.setItem('FE_WS_LAST', JSON.stringify(ws));
    } catch {}

    // 모달 열기
    modal.classList.add('show');

    // 확인 클릭 또는 900ms 뒤 이동
    const go = () => { window.location.href = '../workspace/'; };
    modalOk.addEventListener('click', go, { once: true });
    setTimeout(go, 900);
  });
})();
