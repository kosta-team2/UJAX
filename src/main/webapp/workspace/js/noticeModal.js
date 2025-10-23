function initNoticeModal() {
    if (window.__noticeModalInited) return;
    window.__noticeModalInited = true;

    const modal = document.getElementById('noticeModal');
    const viewSection = document.getElementById('viewSection');
    const editSection = document.getElementById('editSection');
    const closeBtn = modal?.querySelector('.modal-close');
    const saveBtn = document.getElementById('saveNoticeBtn');
    const titleInput = document.getElementById('noticeTitleInput');
    const contentInput = document.getElementById('noticeContentInput');
    const modalTitle = document.getElementById('modalTitle');
    const modalContent = document.getElementById('modalContent');

    // ====== 공통: 열기/닫기 토글 ======
    function showModal() {
        if (modal) modal.style.display = 'flex';
    }

    function hideModal() {
        if (modal) modal.style.display = 'none';
    }

    // ====== 보기 모드 열기 (🔙 복구) ======
    function openNoticeModal({title = '', content = ''} = {}) {
        if (!modal) return;
        // 보기/편집 전환
        viewSection?.removeAttribute('style');     // 보이기
        if (editSection) editSection.style.display = 'none';

        // 내용 채우기
        if (modalTitle) modalTitle.textContent = title;
        if (modalContent) modalContent.textContent = content;

        showModal();
    }

    // ====== 등록(에디터) 모드 열기 (🔙 복구) ======
    function openNoticeEditor() {
        if (!modal) return;
        // 보기/편집 전환
        editSection?.removeAttribute('style');     // 보이기
        if (viewSection) viewSection.style.display = 'none';

        // 입력 초기화/포커스
        if (titleInput) titleInput.value = titleInput.value || '';
        if (contentInput) contentInput.value = contentInput.value || '';
        showModal();
        titleInput?.focus();
    }

    // ====== 닫기 바인딩 (🔙 복구) ======
    closeBtn?.addEventListener('click', hideModal);
    // 바깥(오버레이) 클릭으로 닫기
    modal?.addEventListener('click', (e) => {
        if (e.target === modal) hideModal();
    });

    // ====== 저장 → /front submit (유지) ======
    saveBtn?.addEventListener('click', () => {
        const title = (titleInput?.value || '').trim();
        const content = (contentInput?.value || '').trim();

        if (!title) {
            alert('제목을 입력하세요.');
            titleInput?.focus();
            return;
        }
        if (!content) {
            alert('내용을 입력하세요.');
            contentInput?.focus();
            return;
        }

        const form = document.getElementById('noticeCreateForm');
        if (!form) {
            console.error('[notice] noticeCreateForm가 없습니다. notice-modal.jsp에 숨은 폼을 추가하세요.');
            alert('제출 폼이 없습니다. 관리자에게 문의하세요.');
            return;
        }
        form.querySelector('input[name="noticeTitle"]').value = title;
        form.querySelector('input[name="noticeContent"]').value = content;
        
        form.submit();
    });

    window.openNoticeModal = openNoticeModal;
    window.openNoticeEditor = openNoticeEditor;
}

window.initNoticeModal = initNoticeModal;
