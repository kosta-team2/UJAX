(() => {
    // ===== helpers =====
    const $ = (sel, root = document) => root.querySelector(sel);
    const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
    const clamp = (n, min, max) => Math.max(min, Math.min(max, n));
    const NAME_VISIBLE = 5;

    // ===== Splitter =====
    function initSplit() {
        const container = $('#split');
        const bar = $('#splitter');
        if (!container || !bar) return;

        const clampPct = p => Math.max(20, Math.min(80, p));
        const moveTo = (x) => {
            const rect = container.getBoundingClientRect();
            const pct = clampPct(((x - rect.left) / rect.width) * 100);
            container.style.setProperty('--split-left', pct + '%');
        };

        let dragging = false;
        const onMove = (e) => {
            if (dragging) moveTo(e.clientX);
        };
        const stop = (e) => {
            if (!dragging) return;
            dragging = false;
            bar.releasePointerCapture?.(e.pointerId);
            container.classList.remove('dragging');
            bar.removeEventListener('pointermove', onMove);
            bar.removeEventListener('pointerup', stop);
            const val = getComputedStyle(container).getPropertyValue('--split-left').trim();
            localStorage.setItem('solution.splitLeft', val);
        };

        bar.addEventListener('pointerdown', (e) => {
            e.preventDefault();
            dragging = true;
            bar.setPointerCapture?.(e.pointerId);
            container.classList.add('dragging');
            bar.addEventListener('pointermove', onMove);
            bar.addEventListener('pointerup', stop, {once: true});
            moveTo(e.clientX);
        });

        const saved = localStorage.getItem('solution.splitLeft');
        if (saved) container.style.setProperty('--split-left', saved);
    }

    // ===== Leader menu & delete modal =====
    function initLeaderMenu() {
        const btn = $('#leaderMenuBtn');
        const panel = $('#leaderMenu');
        if (!btn || !panel) return;

        btn.setAttribute('type', 'button');
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            panel.classList.toggle('open');
        });
        document.addEventListener('click', () => panel.classList.remove('open'));
    }

    function initProblemDelete() {
        const deleteBtn = $('#problemDeleteBtn');
        const modal = $('#problemDeleteModal');
        const cancelBtn = $('#problemDeleteCancel');
        const okBtn = $('#problemDeleteConfirm');
        if (!deleteBtn || !modal || !cancelBtn || !okBtn) return;

        deleteBtn.setAttribute('type', 'button');

        deleteBtn.addEventListener('click', (e) => {
            e.preventDefault();
            const menu = $('#leaderMenu');
            if (menu) menu.classList.remove('open');
            modal.classList.add('open');
            document.body.classList.add('modal-open');
        });

        cancelBtn.addEventListener('click', () => {
            modal.classList.remove('open');
            document.body.classList.remove('modal-open');
        });

        okBtn.addEventListener('click', async () => {
            // TODO: 실삭제 API 연동
            // 예시) const url = deleteBtn.dataset.deleteUrl; await fetch(url, {method:'DELETE'});
            modal.classList.remove('open');
            document.body.classList.remove('modal-open');
            // TODO: 삭제 후 이동/갱신
            // history.back(); 또는 location.href='...';
        });
    }

    // ===== Tabs (서버가 .tab 버튼들을 렌더한다면 사용) =====
    function setupNameTabs() {
        const tabsWrap = $('#nameTabs');
        const prev = $('#namePrev');
        const next = $('#nameNext');
        if (!tabsWrap) return;

        const tabs = $$('.tab', tabsWrap);
        if (!tabs.length) {
            if (prev) prev.disabled = true;
            if (next) next.disabled = true;
            return;
        }

        const state = {offset: 0};

        function render() {
            const total = tabs.length;
            const start = state.offset;
            const end = start + NAME_VISIBLE;
            tabs.forEach((el, i) => {
                el.style.display = (i >= start && i < end) ? '' : 'none';
            });
            if (prev) prev.disabled = start === 0;
            if (next) next.disabled = end >= total;
        }

        prev && prev.addEventListener('click', () => {
            state.offset = clamp(state.offset - NAME_VISIBLE, 0, Math.max(0, tabs.length - NAME_VISIBLE));
            render();
        });
        next && next.addEventListener('click', () => {
            state.offset = clamp(state.offset + NAME_VISIBLE, 0, Math.max(0, tabs.length - NAME_VISIBLE));
            render();
        });

        // 탭 클릭 시 이동 URL이 있으면 data-url로 처리
        tabs.forEach(el => {
            const url = el.dataset.url;
            if (url) el.addEventListener('click', () => {
                location.href = url;
            });
        });

        render();
    }

    // ===== Code modal (코드 전체 보기) =====
    function renderCodeWithLineNumbersFrom(preSrc, preDst, large = false) {
        if (!preSrc || !preDst) return;
        const lines = (preSrc.textContent || '').replace(/\r\n/g, '\n').split('\n');
        preDst.innerHTML = '';
        if (large) preDst.classList.add('code-lg'); else preDst.classList.remove('code-lg');

        const frag = document.createDocumentFragment();
        lines.forEach((t, i) => {
            const line = document.createElement('div');
            line.className = 'line';
            const ln = document.createElement('span');
            ln.className = 'ln';
            ln.textContent = String(i + 1);
            const tx = document.createElement('span');
            tx.className = 'tx';
            tx.textContent = t.length ? t : '\u00A0';
            line.appendChild(ln);
            line.appendChild(tx);
            frag.appendChild(line);
        });
        preDst.appendChild(frag);
    }

    function initCodeModal() {
        const src = $('#codeBox');
        const dst = $('#codeFull');
        const modal = $('#codeModal');
        const close = $('#codeClose');
        if (!src || !dst || !modal) return;

        src.addEventListener('click', () => {
            renderCodeWithLineNumbersFrom(src, dst, true);
            modal.classList.add('open');
            document.body.classList.add('modal-open');
        });
        close && close.addEventListener('click', () => {
            modal.classList.remove('open');
            document.body.classList.remove('modal-open');
        });
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.classList.remove('open');
                document.body.classList.remove('modal-open');
            }
        });
    }

    // ===== Comments (UI 토글만, 서버 연동은 TODO) =====
    function initComments() {
        const wrap = $('#commentsWrap');
        const toggle = $('#cToggle');
        const prev = $('#cPrev');
        const next = $('#cNext');
        if (!wrap || !toggle) return;

        toggle.addEventListener('click', () => {
            const open = wrap.classList.contains('hidden');
            if (open) {
                wrap.classList.remove('hidden');
                toggle.textContent = '댓글 숨기기';
                // TODO: 서버에서 댓글 로드
            } else {
                wrap.classList.add('hidden');
                toggle.textContent = '댓글 보기';
                const list = $('#comments');
                if (list) list.innerHTML = '';
                const pg = $('#cPage');
                if (pg) pg.textContent = '';
            }
        });

        prev && prev.addEventListener('click', () => { /* TODO: 이전 페이지 로드 */
        });
        next && next.addEventListener('click', () => { /* TODO: 다음 페이지 로드 */
        });
    }

    // ===== Boot =====
    function boot() {
        initLeaderMenu();
        initProblemDelete();
        setupNameTabs();
        initCodeModal();
        initComments();
        initSplit();
    }

    document.addEventListener('DOMContentLoaded', boot);
})();