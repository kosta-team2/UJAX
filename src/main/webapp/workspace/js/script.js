// 모든 js의 활성을 다룬다
function reload(page) {
    const scriptMap = {
        home: ['js/notice.js', 'js/noticeModal.js', 'js/teamChart.js', 'js/problem.js'],
        notice: ['js/notice.js', 'js/noticeModal.js'],
        problem: ['js/problem.js'],
        info: ['js/info.js'],
        mypage: ['js/mypage.js']
    };

    // DOM 렌더링 시간
    if (scriptMap[page]) {
        requestAnimationFrame(() => {
            scriptMap[page].forEach(src => loadScript(src, true));
        });
    }

    bindCommonEvents();
}

function loadScript(src, force = false) {
    // 기존 js 삭제후 다시 로드
    const existing = document.querySelector(`script[src$="${src}"]`);
    if (existing && !force) return;
    if (existing) existing.remove();

    const contextPath = window.location.pathname.split('/')[1] || '';
    const fullSrc = `/${contextPath ? contextPath + '/' : ''}${src}`;

    if (document.querySelector(`script[src="${fullSrc}"]`)) return;

    const script = document.createElement('script');
    script.src = fullSrc;
    script.defer = false;
    document.body.appendChild(script);
}

// --- 공통 버튼 / 모달 이벤트 바인딩 ---
function bindCommonEvents() {
    // 예: 모달 닫기 / 공통 툴팁 / 다크모드 등
    document.querySelector('.modal-close')?.addEventListener('click', () => {
        document.getElementById('noticeModal').style.display = 'none';
    });
}

// header.js
// logo 클릭시?
// todo 로그아웃 버튼 누를때