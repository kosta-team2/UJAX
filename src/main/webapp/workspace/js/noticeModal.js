// noticeModal.js — 최종본(등록 + 보기 + 삭제)
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
        const deleteBtn = document.getElementById('deleteNoticeBtn');        // ✅ 삭제 버튼
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
        function openNoticeModal({id = '', noticeId = '', title = '', content = ''} = {}) {
            if (!modal) return;
            // id/noticeId 둘 다 받아서 우선순위로 결정
            const nid = (noticeId || id || '').toString();
            modal.dataset.noticeId = nid;      // 삭제 버튼에서 여기 값을 사용
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

        // ====== 저장(등록) → /front ======
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

            // wsId: 폼 히든 → URL → body data-* 순
            const urlWsId = new URLSearchParams(location.search).get('wsId');
            const domWsId = form.querySelector('input[name="wsId"]')?.value || '';
            const bodyWsId = document.body?.dataset?.wsid || '';
            const resolvedWsId = domWsId || urlWsId || bodyWsId || '1';
            ensureHidden('wsId', resolvedWsId);

            // noticeTitle/noticeContent 값 세팅(없으면 생성)
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
            console.group('[Notice] Submit preview - create');
            console.log('METHOD:', (form.getAttribute('method') || 'GET').toUpperCase());
            console.log('ACTION:', form.action);
            for (const [k, v] of data.entries()) console.log(`${k}:`, v);
            console.groupEnd();

            // iframe 내부일 수 있으므로 상위로 보낼 수도 있음 (원하면 주석 해제)
            // form.target = '_top';

            if (typeof form.requestSubmit === 'function') form.requestSubmit();
            else form.submit();
        });

        // ====== 삭제 → /front ======
        deleteBtn?.addEventListener('click', () => {
            console.log('[Delete] nid =', document.getElementById('noticeModal')?.dataset?.noticeId);
            const noticeId = modal?.dataset?.noticeId || '';
            if (!noticeId) {
                alert('공지 ID를 찾을 수 없습니다.');
                return;
            }
            if (!confirm('정말 삭제하시겠습니까?')) return;

            const dform = document.getElementById('noticeDeleteForm');
            if (!dform) {
                alert('삭제 폼(#noticeDeleteForm)을 찾을 수 없습니다.');
                return;
            }

            // 보정 유틸(삭제 폼용)
            const ensureHiddenDel = (name, defaultValue) => {
                let el = dform.querySelector(`input[name="${name}"]`);
                if (!el) {
                    el = document.createElement('input');
                    el.type = 'hidden';
                    el.name = name;
                    el.value = defaultValue ?? '';
                    dform.appendChild(el);
                }
                if (!el.value && defaultValue != null) el.value = defaultValue;
                return el;
            };

            ensureHiddenDel('key', 'notice');
            ensureHiddenDel('methodName', 'deleteNotice');

            // wsId 보정: 폼 히든 → URL → body data-*
            const urlWsId = new URLSearchParams(location.search).get('wsId');
            const domWsId = dform.querySelector('input[name="wsId"]')?.value || '';
            const bodyWsId = document.body?.dataset?.wsid || '';
            const resolvedWsId = domWsId || urlWsId || bodyWsId || '1';
            ensureHiddenDel('wsId', resolvedWsId);

            // noticeId 설정
            let idField = dform.querySelector('input[name="noticeId"]');
            if (!idField) {
                idField = document.createElement('input');
                idField.type = 'hidden';
                idField.name = 'noticeId';
                dform.appendChild(idField);
            }
            idField.value = String(noticeId);

            // 제출 전 로깅
            const data = new FormData(dform);
            console.group('[Notice] Submit preview - delete');
            console.log('METHOD:', (dform.getAttribute('method') || 'GET').toUpperCase());
            console.log('ACTION:', dform.action);
            for (const [k, v] of data.entries()) console.log(`${k}:`, v);
            console.groupEnd();

            if (typeof dform.requestSubmit === 'function') dform.requestSubmit();
            else dform.submit();
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
