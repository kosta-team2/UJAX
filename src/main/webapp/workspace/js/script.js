// 모든 js의 활성을 다룬다
(function () {
    // 전역 네임스페이스
    window.App = window.App || {};
    const App = window.App;

    // 페이지별 초기화 묶음
    App.init = {
        home() {
            // 홈 구성요소들 초기화 (있을 때만 호출)
            window.initHomeNotice?.();   // 홈 공지 3개
            window.initTeamChart?.();    // 월간 잔디
            window.initHomeProblem?.();  // (필요 시) 홈의 문제 하이라이트
        },
        notice() {
            window.initNotice?.();       // 공지 리스트 + 카드 클릭 핸들링
            window.initNoticeModal?.();  // 모달 닫기/오버레이 등
        },
        problem() {
            window.initProblem?.();
        },
        info() {
            window.initInfo?.();
        },
        mypage() {
            window.initMypage?.();
        }
    };

    // 기존 left/rightSidebar.js에서 호출하는 hook 유지
    window.reload = function (page) {
        // DOM 주입 직후 프레임이 그려지고 나서 초기화
        requestAnimationFrame(() => {
            App.init[page]?.();
            bindCommonEvents();
        });
    };

    // 공통 이벤트(있을 때만)
    function bindCommonEvents() {
        const modal = document.getElementById('noticeModal');
        if (!modal) return;

        const closeBtn = modal.querySelector('.modal-close');
        closeBtn && closeBtn.addEventListener('click', () => {
            modal.style.display = 'none';
        });

        // 오버레이 클릭으로 닫기 (중복 방지: 모달 자체에만 바인딩)
        modal.addEventListener('click', (e) => {
            if (e.target === modal) modal.style.display = 'none';
        }, { once: true });
    }
})();

// header.js
// logo 클릭시?
// todo 로그아웃 버튼 누를때