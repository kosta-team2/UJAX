window.initHomeNotice = async function () {
    const grid = document.getElementById('noticeGrid');
    if (!grid) return;

    try {
        const res = await fetch(`/mock/notice.json`);
        const notices = await res.json();
        const recent = notices.slice(0, 3);

        grid.innerHTML = recent.map((n, i) => `
      <div class="notice-card" data-idx="${i}">
        <strong>${n.title}</strong>
        <p>${n.content}</p>
      </div>
    `).join('');

        // ✅ 이벤트 위임
        grid.onclick = async (e) => {
            const card = e.target.closest('.notice-card');
            if (!card) return;
            const idx = Number(card.dataset.idx);

            // ✅ 모달 DOM이 없으면 대기 (비동기 주입 대비)
            let modal = document.getElementById('noticeModal');
            if (!modal) {
                console.warn('⏳ 모달 DOM을 아직 찾을 수 없음. 100ms 후 재시도.');
                await new Promise(res => setTimeout(res, 100));
                modal = document.getElementById('noticeModal');
                if (!modal) {
                    alert('공지 모달이 로드되지 않았습니다.');
                    return;
                }
            }

            // ✅ 모달 함수가 정의되어 있을 때만 실행
            if (typeof window.openNoticeModal === 'function') {
                window.openNoticeModal(recent[idx]);
            } else {
                console.warn('⚠️ openNoticeModal 함수가 아직 정의되지 않음');
            }
        };
    } catch (err) {
        console.error('❌ 홈 공지 불러오기 실패:', err);
        grid.innerHTML = '<p>공지사항을 불러올 수 없습니다.</p>';
    }
};
