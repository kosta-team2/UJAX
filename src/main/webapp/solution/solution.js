(() => {
    // ====================== SIMPLE CONFIG ======================
    // mock 경로는 페이지(/solution/solution.jsp) 기준 ../mock/solution/ 입니다.
    // 상대경로 문제(404) 방지를 위해 URL 객체로 절대경로화합니다.
    const SOLUTION_ID = 1014;
    const MOCK_BASE = new URL('../mock/solution/', location.href).toString().replace(/\/$/, '');
    const NAME_VISIBLE = 5;
    const PAGE_SIZE = 3;

    const API = {
        solution: () => `${MOCK_BASE}/${SOLUTION_ID}.json`,
        detail: (detailId) => `${MOCK_BASE}/details/${detailId}.json`,
        comments: (detailId, page = 1, size = PAGE_SIZE) =>
            `${MOCK_BASE}/details/comments/${detailId}.json?page=${page}&pageSize=${size}`,
    };

    /** =========================
     *  PROD API 로 바꿀 때 ↓ 이 블록만 활성
     *  const WS_ID = 1111;
     *  const PROD_BASE = `/workspaces/${WS_ID}/solutions/${SOLUTION_ID}`;
     *  const API = {
     *    solution: () => `${PROD_BASE}`,
     *    detail:   (id) => `${PROD_BASE}/details/${id}`,
     *    comments: (id, page = 1, size = PAGE_SIZE) =>
     *      `${PROD_BASE}/details/${id}/comments?page=${page}&pageSize=${size}`,
     *  };
     *  ========================= */

    // ====================== MINI UTIL ======================
    function clamp(n, min, max) {
        return Math.max(min, Math.min(max, n));
    }

    function sanitizeCodeString(raw) {
        let s = String(raw ?? '');
        if (s.startsWith('"code":')) {
            const firstQuote = s.indexOf('"', 7);
            s = s.slice(firstQuote + 1);
            if (s.endsWith('"')) s = s.slice(0, -1);
        }
        if (s.length >= 2 && s.startsWith('"') && s.endsWith('"')) s = s.slice(1, -1);
        return s.replace(/\r\n/g, '\n');
    }

    function writeJSON(id, obj) {
        let el = document.getElementById(id);
        if (!el) {
            el = document.createElement('script');
            el.type = 'application/json';
            el.id = id;
            document.body.appendChild(el);
        }
        el.textContent = JSON.stringify(obj);
    }

    function readJSON(id) {
        const el = document.getElementById(id);
        if (!el) return null;
        try {
            return JSON.parse(el.textContent || 'null');
        } catch {
            return null;
        }
    }

    function getNameOffset() {
        return parseInt(document.getElementById('nameTabs').dataset.offset || '0', 10);
    }

    function setNameOffset(v) {
        document.getElementById('nameTabs').dataset.offset = String(v);
    }

    function getActiveDetailId() {
        const q = new URLSearchParams(location.search).get('detail');
        const m0 = getMembers()[0]?.id;
        return q ? +q : m0;
    }

    function getActivePage() {
        return +(new URLSearchParams(location.search).get('page') || 1);
    }

    function setURL(next) {
        const url = new URL(location.href);
        Object.entries(next).forEach(([k, v]) => {
            if (v === null || v === undefined) url.searchParams.delete(k);
            else url.searchParams.set(k, v);
        });
        history.replaceState({}, '', url);
    }

    function getMembers() {
        return readJSON('data-solution')?.members || [];
    }

    function usernameByDetailId(detailId) {
        const m = getMembers().find(m => m.id === detailId);
        return m?.user?.name || '';
    }

    function renderCodeWithLineNumbers(code, preEl, large = false) {
        preEl.innerHTML = '';
        if (large) preEl.classList.add('code-lg'); else preEl.classList.remove('code-lg');
        const frag = document.createDocumentFragment();
        const lines = String(code || '').replace(/\r\n/g, '\n').split('\n');
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
        preEl.appendChild(frag);
    }

    // ====================== LEFT: 문제 ======================
    function renderProblem() {
        const p = readJSON('data-solution').problem;

        document.getElementById('pageProblemTitle').textContent = p.title;
        document.getElementById('problemTitle').textContent = p.title;

        const metaBox = document.getElementById('metaChips');
        metaBox.innerHTML = '';
        [{k: '시간 제한', v: p.meta?.timeLimitRaw || '-'},
            {k: '메모리 제한', v: p.meta?.memoryLimitRaw || '-'}].forEach(({k, v}) => {
            const div = document.createElement('div');
            div.className = 'chip';
            div.innerHTML = `<span class="muted">${k}</span><span style="font-weight:700">${v}</span>`;
            metaBox.appendChild(div);
        });

        document.getElementById('problemDesc').innerHTML = p.problem_description || '';
        document.getElementById('inputDesc').innerHTML = p.problem_input || '';
        document.getElementById('outputDesc').innerHTML = p.problem_output || '';

        const samples = document.getElementById('samples');
        samples.innerHTML = '';
        (p.samples || []).forEach(s => {
            const wrap = document.createElement('div');
            wrap.style.display = 'grid';
            wrap.style.gap = '12px';
            wrap.style.gridTemplateColumns = '1fr 1fr';
            const a = document.createElement('div');
            a.className = 'subcard';
            a.innerHTML = `<div style="font-weight:600;margin-bottom:6px">예제 입력 ${s.index}</div><pre class="code" style="margin:0;max-height:none">${s.input || ''}</pre>`;
            const b = document.createElement('div');
            b.className = 'subcard';
            b.innerHTML = `<div style="font-weight:600;margin-bottom:6px">예제 출력 ${s.index}</div><pre class="code" style="margin:0;max-height:none">${s.output || ''}</pre>`;
            wrap.appendChild(a);
            wrap.appendChild(b);
            samples.appendChild(wrap);
        });

        const a = document.getElementById('bojLink');
        if (p.url) {
            a.href = p.url;
            a.target = '_blank';
            a.rel = 'noopener noreferrer';
            a.classList.remove('disabled');
        } else {
            a.removeAttribute('href');
            a.removeAttribute('target');
            a.removeAttribute('rel');
            a.classList.add('disabled');
        }
    }

    // ====================== 탭 ======================
    function renderNameTabs() {
        const prevBtn = document.getElementById('namePrev');
        const nextBtn = document.getElementById('nameNext');
        const tabs = document.getElementById('nameTabs');

        const offset = getNameOffset();
        const list = getMembers();
        const total = list.length;
        const slice = list.slice(offset, offset + NAME_VISIBLE);
        const active = getActiveDetailId();

        prevBtn.disabled = offset === 0;
        nextBtn.disabled = offset + NAME_VISIBLE >= total;

        tabs.innerHTML = '';
        slice.forEach(m => {
            const b = document.createElement('button');
            b.className = 'tab' + (m.id === active ? ' active' : '');
            b.textContent = m.user?.name || '(이름없음)';
            b.addEventListener('click', async () => {
                closeComments(true);
                setURL({detail: m.id});
                resetRightForLoading();
                await routeFromURL();
            });
            tabs.appendChild(b);
        });
    }

    // ====================== 우측 패널 (초기화/렌더) ======================
    function resetRightForLoading() {
        document.getElementById('codeMeta').textContent = '로딩 중…';
        document.getElementById('signal').style.background = '#6b7280';
        renderCodeWithLineNumbers('', document.getElementById('codeBox'));
        const likeBtn = document.getElementById('likeBtn');
        likeBtn.disabled = true;
        likeBtn.textContent = '🤍 좋아요';
        document.getElementById('likeCount').textContent = '';
        document.getElementById('commentCount').textContent = '';
    }

    function renderRightFromDetail() {
        const d = readJSON('data-detail');
        if (!d) {
            resetRightForLoading();
            return;
        }

        const uname = usernameByDetailId(d.id);
        const statusText = (d.status === 'success') ? '성공' : '실패';
        document.getElementById('codeMeta').textContent = `${uname} · ${statusText} · ${d.timeMs ?? '-'}ms / ${d.memoryMb ?? '-'}MB`;
        document.getElementById('signal').style.background = (d.status === 'success') ? '#22C55E' : '#EF4444';

        renderCodeWithLineNumbers(sanitizeCodeString(d.code), document.getElementById('codeBox'));

        const likeBtn = document.getElementById('likeBtn');
        likeBtn.disabled = false;
        likeBtn.textContent = d.like?.me ? '❤️ 취소' : '🤍 좋아요';
        document.getElementById('likeCount').textContent = `좋아요 ${d.like?.count ?? 0} ·`;
        document.getElementById('commentCount').textContent = `댓글 ${d.commentCount ?? 0}`;
    }

    // ====================== 댓글 ======================
    function closeComments(force = false) {
        const wrap = document.getElementById('commentsWrap');
        if (force || !wrap.classList.contains('hidden')) {
            wrap.classList.add('hidden');
            document.getElementById('cToggle').textContent = '댓글 보기';
            // 리스트/페이저 비우기
            document.getElementById('comments').innerHTML = '';
            document.getElementById('cPage').textContent = '';
        }
    }

    function renderComments(page, json) {
        const box = document.getElementById('comments');
        const pagerText = document.getElementById('cPage');
        if (!json) {
            box.innerHTML = '<div class="muted">댓글 로딩 중…</div>';
            pagerText.textContent = '';
            return;
        }
        const items = json.items || [];
        const size = json.pageSize || PAGE_SIZE;
        const last = Math.max(1, Math.ceil((json.total || 0) / size));
        pagerText.textContent = `${page} / ${last}`;

        box.innerHTML = '';
        items.forEach((c, idx) => {
            const uname = c.user?.name || c.user?.id || 'anon';
            const short = (c.text || '').length > 90 ? (c.text || '').slice(0, 90) + '…' : (c.text || '');
            const item = document.createElement('div');
            item.className = 'subcard comment';
            item.innerHTML =
                `<div class="avatar">${uname[0].toUpperCase()}</div>
         <div style="flex:1">
           <div class="row-between" style="margin-bottom:4px">
             <div class="muted" style="font-size:12px">${uname} · ${c.ts || ''}</div>
             ${c.isOwner ? '<div class="ownerActions"><button class="btn btn-xs deleteBtn" data-id="' + c.id + '">삭제</button></div>' : ''}
           </div>
           <div>${short} <button data-i="${idx}" class="btn btn-xs commentMore">자세히</button></div>
         </div>`;
            box.appendChild(item);
        });

        document.getElementById('cPrev').disabled = page <= 1;
        document.getElementById('cNext').disabled = page >= last;

        // 모달
        Array.from(box.querySelectorAll('.commentMore')).forEach(btn => {
            btn.addEventListener('click', (e) => {
                const i = +e.currentTarget.getAttribute('data-i');
                document.getElementById('commentFull').textContent = (items[i]?.text || '');
                document.getElementById('commentModal').classList.add('open');
                document.body.classList.add('modal-open');
            });
        });
        // (목업) 삭제
        Array.from(box.querySelectorAll('.deleteBtn')).forEach(btn => {
            btn.addEventListener('click', () => {
                if (!confirm('정말로 이 댓글을 삭제하시겠습니까?')) return;
                // 목업이므로 화면만 갱신
                const rest = items.filter(x => x.id !== +btn.dataset.id);
                renderComments(page, {...json, items: rest, total: Math.max(0, (json.total || 0) - 1)});
            });
        });
    }

    async function loadComments(detailId, page) {
        const res = await fetch(API.comments(detailId, page, PAGE_SIZE), {cache: 'no-store'});
        if (!res.ok) {
            document.getElementById('comments').innerHTML = `<div class="muted">댓글 로드 실패: ${res.status}</div>`;
            document.getElementById('cPage').textContent = '';
            return;
        }
        renderComments(page, await res.json());
    }

    // ====================== DETAIL ======================
    async function loadDetail(detailId) {
        resetRightForLoading();         // 먼저 초기화
        renderNameTabs();               // 탭 active 갱신

        const res = await fetch(API.detail(detailId), {cache: 'no-store'});
        if (!res.ok) throw new Error(String(res.status));

        const json = await res.json();
        writeJSON('data-detail', json);
        renderRightFromDetail();
    }

    // ====================== EVENTS ======================
    function bindEvents() {
        // 탭 슬라이더
        document.getElementById('namePrev').addEventListener('click', () => {
            const list = getMembers();
            const next = clamp(getNameOffset() - NAME_VISIBLE, 0, Math.max(0, list.length - NAME_VISIBLE));
            setNameOffset(next);
            renderNameTabs();
        });
        document.getElementById('nameNext').addEventListener('click', () => {
            const list = getMembers();
            const next = clamp(getNameOffset() + NAME_VISIBLE, 0, Math.max(0, list.length - NAME_VISIBLE));
            setNameOffset(next);
            renderNameTabs();
        });

        // 좋아요 (목업: 현재 상세 JSON만 조작)
        document.getElementById('likeBtn').addEventListener('click', () => {
            const d = readJSON('data-detail');
            if (!d) return;
            const me = !!d.like?.me;
            const cnt = d.like?.count ?? 0;
            d.like = {me: !me, count: me ? Math.max(0, cnt - 1) : cnt + 1};
            writeJSON('data-detail', d);
            renderRightFromDetail();
        });

        // 댓글 열고/닫기
        document.getElementById('cToggle').addEventListener('click', async () => {
            const wrap = document.getElementById('commentsWrap');
            const open = wrap.classList.contains('hidden');
            if (open) {
                wrap.classList.remove('hidden');
                document.getElementById('cToggle').textContent = '댓글 숨기기';
                const page = getActivePage();
                await loadComments(getActiveDetailId(), page);
            } else {
                closeComments(true);
            }
        });

        // 댓글 페이징
        document.getElementById('cPrev').addEventListener('click', async () => {
            const cur = getActivePage();
            const next = Math.max(1, cur - 1);
            setURL({page: next});
            await loadComments(getActiveDetailId(), next);
        });
        document.getElementById('cNext').addEventListener('click', async () => {
            const cur = getActivePage();
            const next = cur + 1; // renderComments에서 last에 따라 disable 처리됨
            setURL({page: next});
            await loadComments(getActiveDetailId(), next);
        });

        // 코드 모달
        document.getElementById('codeBox').addEventListener('click', () => {
            const code = sanitizeCodeString(readJSON('data-detail')?.code || '');
            renderCodeWithLineNumbers(code, document.getElementById('codeFull'), true);
            document.getElementById('codeModal').classList.add('open');
            document.body.classList.add('modal-open');
        });
        document.getElementById('codeClose').addEventListener('click', () => {
            document.getElementById('codeModal').classList.remove('open');
            document.body.classList.remove('modal-open');
        });
        document.getElementById('codeModal').addEventListener('click', (e) => {
            if (e.target === e.currentTarget) {
                e.currentTarget.classList.remove('open');
                document.body.classList.remove('modal-open');
            }
        });

        // 댓글 모달 닫기
        document.getElementById('commentClose').addEventListener('click', () => {
            document.getElementById('commentModal').classList.remove('open');
            document.body.classList.remove('modal-open');
        });
        document.getElementById('commentModal').addEventListener('click', (e) => {
            if (e.target === e.currentTarget) {
                e.currentTarget.classList.remove('open');
                document.body.classList.remove('modal-open');
            }
        });
    }

    // ====================== SPLITTER ======================
    function initSplit() {
        const container = document.getElementById('split');
        const bar = document.getElementById('splitter');
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
    }

    // ====================== ROUTER ======================
    async function routeFromURL() {
        renderNameTabs();
        resetRightForLoading();
        try {
            await loadDetail(getActiveDetailId());
            // 댓글은 기본 닫힘(탭 전환 시도 마찬가지). 필요 시 사용자가 열면 로드.
            closeComments(true);
        } catch (err) {
            document.getElementById('codeMeta').textContent = `상세 로드 실패: ${err.message}`;
        }
    }

    // ====================== BOOT ======================
    async function boot() {
        try {
            const res = await fetch(API.solution(), {cache: 'no-store'});
            if (!res.ok){
                throw new Error(`목록 ${res.status}`);
            }
            writeJSON('data-solution', await res.json());

            renderProblem();
            setNameOffset(0);
            bindEvents();
            initSplit();

            await routeFromURL();
        } catch (err) {
            document.getElementById('pageProblemTitle').textContent = '로드 실패';
            document.getElementById('problemTitle').textContent = '로드 실패';
            document.getElementById('problemDesc').textContent = err.message;
        }
    }

    document.addEventListener('DOMContentLoaded', boot);
})();