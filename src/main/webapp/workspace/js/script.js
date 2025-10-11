// 모든 js의 활성을 다룬다
(function () {
    // 전역 네임스페이스
    window.App = window.App || {};
    const App = window.App;

    // 페이지별 초기화 묶음
    App.init = {
        home() {
            window.initNoticeModal?.();
            window.initHomeNotice?.();
            window.initTeamChart?.();
            window.initHomeProblem?.();
        },
        notice() {
            window.initNotice?.();
            window.initNoticeModal?.();
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

    function bindCommonEvents() {
        const modal = document.getElementById('noticeModal');
        if (!modal || modal.dataset.bound === '1') return; // ✅ 중복 방지
        modal.dataset.bound = '1';

        const closeBtn = modal.querySelector('.modal-close');
        closeBtn && closeBtn.addEventListener('click', () => {
            modal.style.display = 'none';
        });

        modal.addEventListener('click', (e) => {
            if (e.target === modal) modal.style.display = 'none';
        });
    }

})();

// header.js
// logo 클릭시?
// todo 로그아웃 버튼 누를때