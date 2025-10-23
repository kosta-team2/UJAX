(function () {
    function bindGrid() {
        const grid = document.getElementById('noticeGrid');
        if (!grid) return;

        grid.addEventListener('click', (e) => {
            const card = e.target.closest('.notice-card');
            if (!card) return;
            e.preventDefault();

            window.openNoticeModal?.({
                title: card.dataset.title || card.querySelector('strong')?.textContent || '',
                content: card.dataset.content || card.querySelector('p')?.textContent || ''
            });
        });
    }

    document.addEventListener('DOMContentLoaded', () => {
        // ✅ 모달은 오직 여기서 한 번만 초기화
        window.initNoticeModal?.();
        bindGrid();

        // "공지 등록" 버튼으로 에디터 열기
        document.getElementById('openEditorBtn')?.addEventListener('click', () => {
            window.openNoticeEditor?.();
        });
    });
})();
