// noticeModal.js — 최종본
(function () {
    function initNoticeModal() {
        if (window.__noticeModalInited) return;
        window.__noticeModalInited = true;

        // ====== 엘리먼트 캐시 ======
        const modal = document.getElementById('noticeModal');
        const viewSection = document.getElementById('viewSection');
        const editSection = document.getElementById('editSection');
        const closeBtn = modal?.querySelector('.modal-close');
        const saveBtn = document.getElementById('saveNoticeBtn');
        const titleInput = document.getElementById('noticeTitleInput');
        const contentInput = document.getElementById('noticeContentInput');
        const modalTitle = document.getElementById('modalTitle');
        const modalContent = document.getElementById('modalContent');

        // ====== 유틸 ======
        const $ = (sel, root = document) => root.querySelector(sel);

        function showModal() {
            if (modal) modal.style.display = 'flex';
        }

        function hideModal() {
            if (modal) modal.style.display = 'none';
        }

        function showViewMode() {
            viewSection?.removeAttribute('style'); // 보이기
            if (editSection) editSection.style.display = 'none';
        }

        function showEditMode() {
            editSection?.removeAttribute('style'); // 보이기
            if (viewSection) viewSection.style.display = 'none';
        }

        // ====== 공개 API ======
        function openNoticeModal({title = '', content = ''} = {}) {
            if (!modal) return;
            showViewMode();
            if (modalTitle) modalTitle.textContent = title;
            if (modalContent) modalContent.textContent = content;
            showModal();
        }

        function openNoticeEditor() {
            if (!modal) return;
            showEditMode();
            if (titleInput && titleInput.value == null) titleInput.value = '';
            if (contentInput && contentInput.value == null) contentInput.value = '';
            showModal();
            titleInput?.focus();
        }

        // ====== 닫기 바인딩 ======
        closeBtn?.addEventListener('click', hideModal);
        // 오버레이 클릭으로 닫기
        modal?.addEventListener('click', (e) => {
            if (e.target === modal) hideModal();
        });

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
                alert('제출 폼(#noticeCreateForm)을 찾을 수 없습니다.');
                return;
            }

            // 보정 유틸
            const ensureHidden = (name, defaultValue) => {
                let el = form.querySelector(`input[name="${name}"]`);
                if (!el) {
                    el = document.createElement('input');
                    el.type = 'hidden';
                    el.name = name;
                    el.value = defaultValue ?? '';
                    form.appendChild(el);
                }
                if (!el.value && defaultValue != null) el.value = defaultValue;
                return el;
            };

            // 필수 파라미터 보정
            ensureHidden('key', 'notice');
            ensureHidden('methodName', 'createNotice');

            // wsId: 폼 히든이 비어있으면 URL 또는 data-*에서 가져와 채움
            const urlWsId = new URLSearchParams(location.search).get('wsId');
            const domWsId = document.querySelector('#noticeCreateForm input[name="wsId"]')?.value || '';
            const bodyWsId = document.body?.dataset?.wsid; // 선택: <body data-wsid="...">
            const resolvedWsId = domWsId || urlWsId || bodyWsId || '1';
            ensureHidden('wsId', resolvedWsId);

            // noticeTitle/noticeContent 없으면 생성 후 값 세팅
            let titleField = form.querySelector('input[name="noticeTitle"]');
            if (!titleField) {
                titleField = document.createElement('input');
                titleField.type = 'hidden';
                titleField.name = 'noticeTitle';
                form.appendChild(titleField);
            }
            let contentField = form.querySelector('input[name="noticeContent"]');
            if (!contentField) {
                contentField = document.createElement('input');
                contentField.type = 'hidden';
                contentField.name = 'noticeContent';
                form.appendChild(contentField);
            }
            titleField.value = title;
            contentField.value = content;

            // 제출 전 로깅
            const data = new FormData(form);
            console.group('[Notice] Submit preview');
            console.log('METHOD:', (form.getAttribute('method') || 'GET').toUpperCase());
            console.log('ACTION:', form.action);
            for (const [k, v] of data.entries()) console.log(`${k}:`, v);
            console.groupEnd();

            // 제출
            if (typeof form.requestSubmit === 'function') form.requestSubmit();
            else form.submit();
        });


        // ====== 접근성: 입력에서 Enter로 저장 (Ctrl+Enter는 내용 입력에서도 허용) ======
        titleInput?.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                saveBtn?.click();
            }
        });
        contentInput?.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' && (e.ctrlKey || e.metaKey)) {
                e.preventDefault();
                saveBtn?.click();
            }
        });

        // 전역 노출
        window.openNoticeModal = openNoticeModal;
        window.openNoticeEditor = openNoticeEditor;
    }

    window.initNoticeModal = initNoticeModal;
})();
