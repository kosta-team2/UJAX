// notice.js
window.initNotice = async function () {
    const grid = document.getElementById('noticeGrid');
    if (!grid) return;

    try {
        const res = await fetch(`/mock/notice.json`);
        const notices = await res.json();

        grid.innerHTML = notices.map(n => `
      <div class="notice-card">
        <strong>${n.title}</strong>
        <p>${n.content}</p>
      </div>
    `).join('');

        // 카드 클릭 → 모달 열기
        grid.querySelectorAll('.notice-card').forEach((card, idx) => {
            card.addEventListener('click', () => {
                window.openNoticeModal?.(notices[idx]);
            });
        });

    } catch (err) {
        console.error('❌ 공지 불러오기 실패:', err);
        grid.innerHTML = '<p>공지사항을 불러올 수 없습니다.</p>';
    }

    document.querySelector('.register-btn')
        ?.addEventListener('click', () => {
            window.openNoticeEditor?.();
        });

    document.querySelector('.sort-btn')
        ?.addEventListener('click', () => alert('정렬 기능 실행'));

    // 렌더링 함수
    function renderNotices(list) {
        grid.innerHTML = list.map(n => `
      <div class="notice-card">
        <strong>${n.title}</strong>
        <p>${n.content}</p>
      </div>
    `).join('');

        // 카드 클릭 시 모달 열기
        grid.querySelectorAll('.notice-card').forEach((card, idx) => {
            card.addEventListener('click', () => {
                window.openNoticeModal?.(list[idx]);
            });
        });
    }

    // 등록된 공지를 다시 렌더링할 수 있도록 전역 함수 등록
    window.refreshNoticeList = function (newNotice) {
        notices.unshift(newNotice);
        renderNotices(notices);
    };
};

// 모달 열기 유틸
window.openNoticeModal = function (notice) {
    const modal = document.getElementById('noticeModal');
    if (!modal) return;

    modal.querySelector('#modalTitle').textContent = notice.title ?? '';
    modal.querySelector('#modalContent').textContent = notice.content ?? '';
    modal.style.display = 'flex';
};
