window.initHomeNotice = async function () {
    const grid = document.getElementById('noticeGrid');
    if (!grid) return;

    try {
        const res = await fetch(`/mock/notice.json`);
        const notices = await res.json();

        const recent = notices.slice(0, 3);
        grid.innerHTML = recent.map(n => `
            <div class="notice-card">
                <strong>${n.title}</strong>
                <p>${n.content}</p>
            </div>
        `).join('');

        // ✅ 홈에서도 모달 바로 열기
        grid.querySelectorAll('.notice-card').forEach((card, idx) => {
            card.addEventListener('click', () => {
                window.openNoticeModal?.(notices[idx]);
            });
        });

    } catch (err) {
        console.error('❌ 홈 공지 불러오기 실패:', err);
        grid.innerHTML = '<p>공지사항을 불러올 수 없습니다.</p>';
    }
};
