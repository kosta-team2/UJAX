window.initNoticeModal = function () {
    const modal = document.getElementById('noticeModal');
    if (!modal || modal.dataset.boundInit === '1') return;
    modal.dataset.boundInit = '1';

    const viewSection  = modal.querySelector('#viewSection');
    const editSection  = modal.querySelector('#editSection');
    const titleInput   = modal.querySelector('#noticeTitleInput');
    const contentInput = modal.querySelector('#noticeContentInput');
    const saveBtn      = modal.querySelector('#saveNoticeBtn');
    const closeBtn     = modal.querySelector('.modal-close');

    // 닫기
    closeBtn?.addEventListener('click', () => (modal.style.display = 'none'));
    modal.addEventListener('click', (e) => {
        if (e.target === modal) modal.style.display = 'none';
    });

    // ✅ 전역에서 항상 모달을 안전하게 찾아서 씀 (1개만 유지)
    window.openNoticeModal = function (notice) {
        const m = document.getElementById('noticeModal');
        if (!m) {
            console.warn('⚠️ 모달 DOM 없음');
            return;
        }
        const view = m.querySelector('#viewSection');
        const edit = m.querySelector('#editSection');

        m.querySelector('#modalTitle').textContent = notice?.title ?? '';
        m.querySelector('#modalContent').textContent = notice?.content ?? '';

        view.style.display = 'block';
        edit.style.display = 'none';
        m.style.display = 'flex';
    };

    // ✅ 등록 모드
    window.openNoticeEditor = function () {
        modal.querySelector('#modalTitle').textContent = '공지 등록';
        viewSection.style.display = 'none';
        editSection.style.display = 'block';
        modal.style.display = 'flex';
    };

    // ✅ 저장 (등록 완료)
    saveBtn?.addEventListener('click', () => {
        const title = titleInput.value.trim();
        const content = contentInput.value.trim();
        if (!title || !content) {
            alert('제목과 내용을 모두 입력하세요.');
            return;
        }

        const newNotice = {
            title,
            content,
            date: new Date().toISOString().split('T')[0]
        };

        // 공지 리스트에 반영
        window.refreshNoticeList?.(newNotice);

        alert('공지 등록 완료!');
        modal.style.display = 'none';
        titleInput.value = '';
        contentInput.value = '';
    });
};
