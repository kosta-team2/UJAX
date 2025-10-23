(function () {
    function bindGrid() {
        const grid = document.getElementById('noticeGrid');
        if (!grid) return;

        grid.addEventListener('click', (e) => {
            const card = e.target.closest('.notice-card');
            if (!card) return;
            e.preventDefault();

            // ✅ noticeId 우선, 없으면 id, data-id까지 폭넓게 시도
            const nid =
                card.dataset.noticeId ||
                card.dataset.id ||
                card.getAttribute('data-id') ||
                '';

            window.openNoticeModal?.({
                noticeId: nid, // ✅ 반드시 넘김
                title: card.dataset.title || card.querySelector('strong')?.textContent || '',
                content: card.dataset.content || card.querySelector('p')?.textContent || ''
            });
        });
    }

    document.addEventListener('DOMContentLoaded', () => {
        window.initNoticeModal?.();
        bindGrid();

        document.getElementById('openEditorBtn')?.addEventListener('click', () => {
            window.openNoticeEditor?.();
        });
    });
})();
