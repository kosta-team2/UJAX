(function () {
    // ---------------------- CONFIG ----------------------
    // [CHANGE] JSP가 넣어주는 CTX만 사용 (그 외 서버 주입값은 없음)
    const CTX = (window.CTX || '');
    const BASE = `${CTX}/mock/solution`; // mock에서만 읽음

    // UI 설정
    const NAME_VISIBLE = 5;
    const PAGE_SIZE_FALLBACK = 3; // comments 응답에 pageSize 없을 때
    const clamp = (n, min, max) => Math.max(min, Math.min(max, n));

    // ---------------------- STATE ----------------------
    let SOL = null;                   // solution.json (문제/멤버 목록)
    let DETAIL_CACHE = new Map();     // detailId -> (100.json 등)
    let COMMENT_CACHE = new Map();    // `${detailId}:${page}` -> (100c.json 등)
    let nameOffset = 0;
    let activeDetailId = null;
    let activeUserName = '';
    let commentsOpen = false;
    let pageByDetailId = {};          // detail별 현재 페이지(댓글)
    const isLeader = true;            // [CHANGE] 목업용 플래그(원하면 JSP로 주입)

    // ---------------------- DOM HELPERS ----------------------
    const $ = (sel) => document.querySelector(sel);
    const $$ = (sel) => Array.from(document.querySelectorAll(sel));

    function renderCodeWithLineNumbers(code, preEl, large = false) {
        preEl.innerHTML = '';
        if (large) preEl.classList.add('code-lg'); else preEl.classList.remove('code-lg');
        const frag = document.createDocumentFragment();
        const lines = (code || '').split('\n');
        lines.forEach((t, idx) => {
            const line = document.createElement('div');
            line.className = 'line';
            const ln = document.createElement('span');
            ln.className = 'ln';
            ln.textContent = String(idx + 1);
            const tx = document.createElement('span');
            tx.className = 'tx';
            tx.textContent = t.length ? t : '\u00A0';
            line.appendChild(ln);
            line.appendChild(tx);
            frag.appendChild(line);
        });
        preEl.appendChild(frag);
    }

    // ---------------------- API (mock 파일 경로만) ----------------------
    const API = {
        solution: () => `${BASE}/solution.json`,
        detail: (id) => `${BASE}/${id}.json`,
        comments: (id, page) => `${BASE}/${id}c.json?page=${page}` // 쿼리는 무시되어도 OK
    };

    async function fetchJSON(url) {
        const res = await fetch(url, {cache: 'no-store'});
        if (!res.ok) throw new Error(`Fetch failed: ${res.status} ${url}`);
        return res.json();
    }

    // ---------------------- RENDER: LEFT ----------------------
    function renderProblem() {
        const p = SOL.problem;

        // [CHANGE] 스키마에 맞춤
        $('#pageProblemTitle').textContent = p.title;
        $('#problemTitle').textContent = p.title;

        const metaBox = $('#metaChips');
        metaBox.innerHTML = '';
        [
            {k: '시간 제한', v: p.meta?.timeLimitRaw || '-'},
            {k: '메모리 제한', v: p.meta?.memoryLimitRaw || '-'}
        ].forEach(c => {
            const div = document.createElement('div');
            div.className = 'chip';
            div.innerHTML = `<span class="muted">${c.k}</span><span style="font-weight:700">${c.v}</span>`;
            metaBox.appendChild(div);
        });

        // 본문 (서버에서 sanitize 했다는 가정)
        $('#problemDesc').innerHTML = p.problem_description || '';
        $('#inputDesc').innerHTML = p.problem_input || '';
        $('#outputDesc').innerHTML = p.problem_output || '';

        const samples = $('#samples');
        samples.innerHTML = '';
        (p.samples || []).forEach(s => {
            const wrap = document.createElement('div');
            wrap.style.display = 'grid';
            wrap.style.gap = '12px';
            wrap.style.gridTemplateColumns = '1fr 1fr';

            const a = document.createElement('div');
            a.className = 'subcard';
            a.innerHTML =
                `<div style="font-weight:600;margin-bottom:6px">예제 입력 ${s.index}</div>` +
                `<pre class="code" style="margin:0;max-height:none">${s.input || ''}</pre>`;

            const b = document.createElement('div');
            b.className = 'subcard';
            b.innerHTML =
                `<div style="font-weight:600;margin-bottom:6px">예제 출력 ${s.index}</div>` +
                `<pre class="code" style="margin:0;max-height:none">${s.output || ''}</pre>`;

            wrap.appendChild(a);
            wrap.appendChild(b);
            samples.appendChild(wrap);
        });

        $('#bojLink').href = p.url || '#';

        // 리더 메뉴
        $('#leaderActions').classList.toggle('hidden', !isLeader);
    }

    function renderNameTabs() {
        const prevBtn = $('#namePrev');
        const nextBtn = $('#nameNext');
        const tabs = $('#nameTabs');

        const total = (SOL.members || []).length;
        const slice = SOL.members.slice(nameOffset, nameOffset + NAME_VISIBLE);

        prevBtn.disabled = nameOffset === 0;
        nextBtn.disabled = nameOffset + NAME_VISIBLE >= total;

        tabs.innerHTML = '';
        slice.forEach(m => {
            const b = document.createElement('button');
            b.className = 'tab' + (m.id === activeDetailId ? ' active' : '');
            b.textContent = m.user?.name || '(이름없음)';
            b.onclick = () => setActiveMember(m.id, m.user?.name || '');
            tabs.appendChild(b);
        });
    }

    // ---------------------- RENDER: RIGHT ----------------------
    function renderRight() {
        renderNameTabs();

        const detail = DETAIL_CACHE.get(activeDetailId);
        if (!detail) {
            // 로딩 플레이스홀더
            $('#signal').style.background = '#6b7280';
            $('#codeMeta').textContent = '로딩 중…';
            renderCodeWithLineNumbers('', $('#codeBox'));
            $('#likeBtn').disabled = true;
            $('#likeCount').textContent = '';
            $('#commentCount').textContent = '';
            return;
        }

        const {status, timeMs, memoryMb, like, commentCount, code} = detail;
        const statusText = (status === 'success') ? '성공' : '실패';
        $('#codeMeta').textContent =
            `${activeUserName} · ${statusText} · ${timeMs ?? '-'}ms / ${memoryMb ?? '-'}MB`;

        // 신호등
        $('#signal').style.background = (status === 'success') ? '#22C55E' : '#EF4444';

        // 코드 박스
        const cleaned = sanitizeCodeString(code);
        renderCodeWithLineNumbers(cleaned, $('#codeBox'));

        // 좋아요/댓글
        $('#likeBtn').disabled = false;
        $('#likeBtn').textContent = like?.me ? '❤️ 취소' : '🤍 좋아요';
        $('#likeCount').textContent = `좋아요 ${like?.count ?? 0} ·`;
        $('#commentCount').textContent = `댓글 ${commentCount ?? 0}`;
    }

    // [ADD] 일부 파일에서 말미에 \" 가 남아있는 케이스 대비
    function sanitizeCodeString(raw) {
        let s = String(raw ?? '');
        // "code": "..." 같은 오염 문자열로 시작할 때 제거
        if (s.startsWith('"code":')) {
            const firstQuote = s.indexOf('"', 7);
            s = s.slice(firstQuote + 1);
            if (s.endsWith('"')) s = s.slice(0, -1);
        }
        // 앞뒤 큰따옴표만 덮어쓴 경우
        if (s.length >= 2 && s.startsWith('"') && s.endsWith('"')) {
            s = s.slice(1, -1);
        }
        // 통일된 개행
        s = s.replace(/\r\n/g, '\n');
        return s;
    }

    // ---------------------- COMMENTS ----------------------
    function keyComments(id, page) {
        return `${id}:${page}`;
    }

    function renderComments(detailId, page) {
        const key = keyComments(detailId, page);
        const data = COMMENT_CACHE.get(key);
        const box = $('#comments');
        const pagerText = $('#cPage');

        if (!data) {
            box.innerHTML = '<div class="muted">댓글 로딩 중…</div>';
            pagerText.textContent = '';
            return;
        }

        const {items, total, pageSize} = data;
        const size = pageSize || PAGE_SIZE_FALLBACK;
        const last = Math.max(1, Math.ceil((total || 0) / size));
        pagerText.textContent = `${page} / ${last}`;

        box.innerHTML = '';
        (items || []).forEach((c, idx) => {
            const uname = c.user?.name || c.user?.id || 'anon';
            const short = (c.text || '').length > 90 ? (c.text || '').slice(0, 90) + '…' : (c.text || '');
            const item = document.createElement('div');
            item.className = 'subcard comment';
            item.innerHTML =
                `<div class="avatar">${uname[0].toUpperCase()}</div>` +
                `<div style="flex:1">
          <div class="row-between" style="margin-bottom:4px">
            <div class="muted" style="font-size:12px">${uname} · ${c.ts || ''}</div>
            ${c.isOwner ? '<div class="ownerActions"><button class="btn btn-xs deleteBtn" data-id="' + c.id + '">삭제</button></div>' : ''}
          </div>
          <div>${short} <button data-i="${idx}" class="btn btn-xs commentMore">자세히</button></div>
        </div>`;
            box.appendChild(item);
        });

        $('#cPrev').disabled = page <= 1;
        $('#cNext').disabled = page >= last;

        // 상세 모달
        $$('#comments .commentMore').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const i = +e.currentTarget.getAttribute('data-i');
                $('#commentFull').textContent = (items || [])[i]?.text || '';
                $('#commentModal').classList.add('open');
            });
        });

        // [ADD] 목업 삭제(클라 캐시만 조작)
        $$('#comments .deleteBtn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const id = +e.currentTarget.getAttribute('data-id');
                if (!confirm('정말로 이 댓글을 삭제하시겠습니까?')) return;
                const arr = (COMMENT_CACHE.get(key)?.items || []);
                const ix = arr.findIndex(x => x.id === id);
                if (ix > -1) {
                    arr.splice(ix, 1);
                    COMMENT_CACHE.set(key, {
                        ...COMMENT_CACHE.get(key),
                        items: arr,
                        total: (COMMENT_CACHE.get(key).total || 1) - 1
                    });
                    renderComments(detailId, page);
                }
            });
        });
    }

    async function loadComments(detailId, page) {
        const key = keyComments(detailId, page);
        if (COMMENT_CACHE.has(key)) {
            renderComments(detailId, page);
            return;
        }
        try {
            const json = await fetchJSON(API.comments(detailId, page));
            COMMENT_CACHE.set(key, json);
            renderComments(detailId, page);
        } catch (err) {
            $('#comments').innerHTML = `<div class="muted">댓글 로드 실패: ${err.message}</div>`;
            $('#cPage').textContent = '';
        }
    }

    // ---------------------- DETAIL ----------------------
    async function ensureDetail(detailId) {
        if (DETAIL_CACHE.has(detailId)) return DETAIL_CACHE.get(detailId);
        const json = await fetchJSON(API.detail(detailId));
        DETAIL_CACHE.set(detailId, json);
        return json;
    }

    async function setActiveMember(detailId, userName) {
        activeDetailId = detailId;
        activeUserName = userName;

        // 우측 초기화
        $('#codeMeta').textContent = '로딩 중…';
        $('#signal').style.background = '#6b7280';
        renderCodeWithLineNumbers('', $('#codeBox'));

        try {
            await ensureDetail(detailId);
            renderRight();

            if (commentsOpen) {
                const page = pageByDetailId[detailId] || 1;
                await loadComments(detailId, page);
            } else {
                const d = DETAIL_CACHE.get(detailId);
                $('#commentCount').textContent = `댓글 ${d.commentCount ?? 0}`;
            }
        } catch (err) {
            $('#codeMeta').textContent = `상세 로드 실패: ${err.message}`;
        }
    }

    // ---------------------- EVENTS ----------------------
    function bindEvents() {
        // 탭 좌우
        $('#namePrev').onclick = () => {
            nameOffset = clamp(nameOffset - NAME_VISIBLE, 0, Math.max(0, (SOL.members || []).length - NAME_VISIBLE));
            renderNameTabs();
        };
        $('#nameNext').onclick = () => {
            nameOffset = clamp(nameOffset + NAME_VISIBLE, 0, Math.max(0, (SOL.members || []).length - NAME_VISIBLE));
            renderNameTabs();
        };

        // 좋아요 토글(목업: 캐시만)
        $('#likeBtn').onclick = () => {
            const d = DETAIL_CACHE.get(activeDetailId);
            if (!d) return;
            const me = !!d.like?.me;
            const cnt = d.like?.count ?? 0;
            d.like = {me: !me, count: me ? Math.max(0, cnt - 1) : cnt + 1};
            DETAIL_CACHE.set(activeDetailId, d);
            renderRight();
        };

        // 댓글 열고/닫기
        $('#cToggle').onclick = async () => {
            commentsOpen = !commentsOpen;
            $('#commentsWrap').classList.toggle('hidden', !commentsOpen);
            $('#cToggle').textContent = commentsOpen ? '댓글 숨기기' : '댓글 보기';
            if (commentsOpen) {
                const page = pageByDetailId[activeDetailId] || 1;
                await loadComments(activeDetailId, page);
            }
        };

        // 댓글 페이징
        $('#cPrev').onclick = async () => {
            const cur = pageByDetailId[activeDetailId] || 1;
            const next = Math.max(1, cur - 1);
            pageByDetailId[activeDetailId] = next;
            await loadComments(activeDetailId, next);
        };
        $('#cNext').onclick = async () => {
            const cur = pageByDetailId[activeDetailId] || 1;
            const data = COMMENT_CACHE.get(keyComments(activeDetailId, cur));
            const size = data?.pageSize || PAGE_SIZE_FALLBACK;
            const last = Math.max(1, Math.ceil((data?.total || 0) / size));
            const next = Math.min(last, cur + 1);
            pageByDetailId[activeDetailId] = next;
            await loadComments(activeDetailId, next);
        };

        // 코드 모달
        $('#codeBox').onclick = () => {
            const d = DETAIL_CACHE.get(activeDetailId);
            const code = sanitizeCodeString(d?.code || '');
            renderCodeWithLineNumbers(code, $('#codeFull'), true);
            $('#codeModal').classList.add('open');
            document.body.classList.add('modal-open');
        };
        $('#codeClose').onclick = () => {
            $('#codeModal').classList.remove('open');
            document.body.classList.remove('modal-open');
        }
        $('#codeModal').addEventListener('click', (e) => {
            if (e.target === e.currentTarget) e.currentTarget.classList.remove('open');
            document.body.classList.remove('modal-open');
        });

        // 댓글 상세 모달
        $('#commentClose').onclick = () => {
            $('#commentModal').classList.remove('open');
            document.body.classList.remove('modal-open');
        }

        $('#commentModal').addEventListener('click', (e) => {
            if (e.target === e.currentTarget) e.currentTarget.classList.remove('open');
            document.body.classList.remove('modal-open');
        });

        // 백준 카드 클릭 시 즉시 새 탭으로 이동
        $('#bjToggle').onclick = () => {
            const url = $('#bojLink')?.href || SOL?.problem?.url || '';
            if (!url || url === '#') {
                alert('문제 URL이 없습니다.');
                return;
            }
            window.open(url, '_blank', 'noopener,noreferrer');
        };

        // 리더 메뉴(목업)
        const menuBtn = $('#leaderMenuBtn');
        const menu = $('#leaderMenu');
        if (isLeader) {
            $('#leaderActions').classList.remove('hidden');
            menuBtn.onclick = (e) => {
                e.stopPropagation();
                menu.classList.toggle('open');
            };
            document.addEventListener('click', () => menu.classList.remove('open'));
            $('#problemDeleteBtn').onclick = (e) => {
                e.stopPropagation();
                menu.classList.remove('open');
                $('#problemDeleteModal').classList.add('open');
            };
            $('#problemDeleteCancel').onclick = () => $('#problemDeleteModal').classList.remove('open');
            $('#problemDeleteConfirm').onclick = () => {
                $('#problemDeleteModal').classList.remove('open');
                $('#problemTitle').textContent = '(삭제됨)';
                $('#problemDesc').textContent = '이 문제는 삭제되었습니다. (모의)';
                $('#inputDesc').textContent = '';
                $('#samples').innerHTML = '';
                $('#bojLink').href = '#';
                $('#leaderActions').classList.add('hidden');
            };
        }
    }

    // ---------------------- SPLIT BAR ----------------------
    function initSplit() {
        const container = document.getElementById('split');
        const bar = document.getElementById('splitter');
        if (!container || !bar) return;

        const saved = localStorage.getItem('solution.splitLeft');
        if (saved) container.style.setProperty('--split-left', saved);

        const clampPct = p => Math.max(20, Math.min(80, p));
        const moveTo = (clientX) => {
            const rect = container.getBoundingClientRect();
            const pct = clampPct(((clientX - rect.left) / rect.width) * 100);
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
            window.removeEventListener('pointermove', onMove);
            window.removeEventListener('pointerup', stop);
            const val = getComputedStyle(container).getPropertyValue('--split-left').trim();
            localStorage.setItem('solution.splitLeft', val);
        };

        bar.addEventListener('pointerdown', (e) => {
            e.preventDefault();
            dragging = true;
            bar.setPointerCapture?.(e.pointerId);
            container.classList.add('dragging');
            window.addEventListener('pointermove', onMove);
            window.addEventListener('pointerup', stop, {once: true});
            moveTo(e.clientX);
        });
    }

    // ---------------------- BOOT ----------------------
    async function boot() {
        try {
            // 1) 문제/멤버 목록 로딩
            SOL = await fetchJSON(API.solution());
            renderProblem();

            // 2) 첫 멤버를 활성화
            const first = (SOL.members || [])[0];
            if (!first) {
                renderNameTabs();
                $('#codeMeta').textContent = '제출한 멤버 없음';
                renderCodeWithLineNumbers('', $('#codeBox'));
                return;
            }
            activeDetailId = first.id;
            activeUserName = first.user?.name || '';

            // 3) 상세 로딩 후 렌더
            await ensureDetail(activeDetailId);
            renderRight();

            // 댓글은 기본 닫힘
            $('#commentsWrap').classList.add('hidden');
            $('#cToggle').textContent = '댓글 보기';
            pageByDetailId[activeDetailId] = 1;

            // 이벤트/스플릿
            bindEvents();
            initSplit();
        } catch (err) {
            $('#pageProblemTitle').textContent = '로드 실패';
            $('#problemTitle').textContent = '로드 실패';
            $('#problemDesc').textContent = err.message;
        }
    }

    document.addEventListener('DOMContentLoaded', boot);
})();
