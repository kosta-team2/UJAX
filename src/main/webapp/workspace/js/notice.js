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

    // 버튼들(존재 시)
    document.querySelector('.register-btn')
        ?.addEventListener('click', () => alert('공지 등록 모달이 열릴 예정입니다.'));
    document.querySelector('.sort-btn')
        ?.addEventListener('click', () => alert('정렬 기능 실행'));
};

// 모달 열기 유틸(다른 모듈에서도 쓰게 전역 공개)
window.openNoticeModal = function (notice) {
    const modal = document.getElementById('noticeModal');
    if (!modal) return;

    modal.querySelector('#modalTitle').textContent = notice.title ?? '';
    modal.querySelector('#modalContent').textContent = notice.content ?? '';
    modal.style.display = 'flex';
};
