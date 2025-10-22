(function () {
    function bindModal() {
        const modal = document.getElementById('noticeModal');
        if (!modal || modal.dataset.boundInit === '1') return;
        modal.dataset.boundInit = '1';

        const viewSection = modal.querySelector('#viewSection');
        const editSection = modal.querySelector('#editSection');
        const titleInput = modal.querySelector('#noticeTitleInput');
        const contentInput = modal.querySelector('#noticeContentInput');
        const saveBtn = modal.querySelector('#saveNoticeBtn');
        const closeBtn = modal.querySelector('#modalClose');

        function close() {
            modal.style.display = 'none';
            document.body.style.overflow = '';
        }

        closeBtn?.addEventListener('click', close);
        modal.addEventListener('click', (e) => {
            if (e.target === modal) close();
        });
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') close();
        });

        // 전역 오픈 (읽기)
        window.openNoticeModal = function (notice) {
            modal.querySelector('#modalTitle').textContent = notice?.title ?? '';
            modal.querySelector('#modalContent').textContent = notice?.content ?? '';
            viewSection.style.display = 'block';
            modal.style.display = 'flex';
            document.body.style.overflow = 'hidden';
        };

        window.openNoticeEditor = function () {
            modal.querySelector('#modalTitle').textContent = '공지 등록';
            titleInput.value = '';
            contentInput.value = '';
            viewSection.style.display = 'none';
            editSection.style.display = 'block';
            modal.style.display = 'flex';
            document.body.style.overflow = 'hidden';
        };

        saveBtn?.addEventListener('click', () => {
            const title = titleInput.value.trim();
            const content = contentInput.value.trim();
            if (!title || !content) {
                alert('제목과 내용을 모두 입력하세요.');
                return;
            }
            const newNotice = {title, content, date: new Date().toISOString().slice(0, 10)};
            window.refreshNoticeList?.(newNotice);
            close();
        });
    }

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

    document.getElementById('openEditorBtn')?.addEventListener('click', () => {
        window.openNoticeEditor?.();
    });

    document.addEventListener('DOMContentLoaded', () => {
        bindModal();
        bindGrid();
    });

    bindModal();
    bindGrid();
})();