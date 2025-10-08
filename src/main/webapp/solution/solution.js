(function () {
    // ---------------------- CONFIG ----------------------
    const CURRENT_USER = 'devA';
    const IS_LEADER = true;           // 리더 권한: 관리 메뉴 노출
    const NAME_VISIBLE = 5;           // 탭 가시 멤버 수
    const PAGE_SIZE = 3;              // 댓글 페이징
    const CTX = (window.CTX || '');   // JSP에서 주입됨

    // ---------------------- STATE ----------------------
    let DATA = null;                  // fetch된 JSON
    let STATUS = {};
    let TIME = {};
    let MEM = {};
    let COMMENTS = {};
    let nameOffset = 0;
    let activeName = '';
    let likes = {};                   // by name
    let cPageByName = {};             // 댓글 페이지 by name
    let commentsOpen = false;
    let problemDeleted = false;

    // ---------------------- HELPERS ----------------------
    const $ = (sel) => document.querySelector(sel);
    const $$ = (sel) => Array.from(document.querySelectorAll(sel));
    const clamp = (n, min, max) => Math.max(min, Math.min(max, n));

    // 긴 코드(100줄)
    const LONG_CODE = (() => {
        let s = '// solve() – 데모용 100줄 코드 스텁\n';
        s += 'function solve(input){\n';
        for (let i = 1; i <= 96; i++) s += '  // TODO line ' + i + '\n';
        s += '  return 42;\n}';
        return s;
    })();

    function makeLongComment(seed) {
        return '문제 풀이 접근은 우선순위 큐 대신 덱을 사용해 양쪽에서 처리하는 방식이 효율적이었습니다. ' +
            '엣지 케이스로 동일 시점에 여러 명이 같은 종류를 선호하는 경우를 가정해 충돌을 방지했고, ' +
            '시간 복잡도는 입력 크기 합에 대해 O(N+M)으로 맞췄습니다. seed=' + seed;
    }

    function renderCodeWithLineNumbers(code, preEl, large = false) {
        preEl.innerHTML = '';
        if (large) preEl.classList.add('code-lg'); else preEl.classList.remove('code-lg');
        const frag = document.createDocumentFragment();
        const lines = code.split('\n');
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

    // ---------------------- RENDER ----------------------
    function renderProblem() {
        $('#pageProblemTitle').textContent = DATA.problem.title;
        $('#problemTitle').textContent = problemDeleted ? '(삭제됨)' : DATA.problem.title;

        const metaBox = $('#metaChips');
        metaBox.innerHTML = '';
        [
            {k: '시간 제한', v: DATA.problem.meta.timeLimit},
            {k: '메모리 제한', v: DATA.problem.meta.memoryLimit}
        ].forEach(c => {
            const div = document.createElement('div');
            div.className = 'chip';
            div.innerHTML = `<span class="muted">${c.k}</span><span style="font-weight:700">${c.v}</span>`;
            metaBox.appendChild(div);
        });

        const desc = $('#problemDesc');
        const inputD = $('#inputDesc');
        const samples = $('#samples');
        if (problemDeleted) {
            desc.textContent = '이 문제는 삭제되었습니다. (모의)';
            inputD.textContent = '';
            samples.innerHTML = '';
        } else {
            desc.textContent = DATA.problem.description;
            inputD.textContent = DATA.problem.inputDesc;
            samples.innerHTML = '';
            DATA.problem.samples.forEach((s, i) => {
                const wrap = document.createElement('div');
                wrap.style.display = 'grid';
                wrap.style.gap = '12px';
                wrap.style.gridTemplateColumns = '1fr 1fr';
                const a = document.createElement('div');
                a.className = 'subcard';
                a.innerHTML = `<div style="font-weight:600;margin-bottom:6px">예제 입력 ${i + 1}</div><pre class="code" style="margin:0;max-height:none">${s.in}</pre>`;
                const b = document.createElement('div');
                b.className = 'subcard';
                b.innerHTML = `<div style="font-weight:600;margin-bottom:6px">예제 출력 ${i + 1}</div><pre class="code" style="margin:0;max-height:none">${s.out}</pre>`;
                wrap.appendChild(a);
                wrap.appendChild(b);
                samples.appendChild(wrap);
            });
        }
        $('#bojLink').href = problemDeleted ? '#' : (DATA.problem.url || '#');

        // 리더 메뉴 표시/숨김
        const leader = $('#leaderActions');
        leader.classList.toggle('hidden', !IS_LEADER || problemDeleted);
    }

    function renderNameTabs() {
        const prevBtn = $('#namePrev');
        const nextBtn = $('#nameNext');
        const tabs = $('#nameTabs');
        const slice = DATA.members.slice(nameOffset, nameOffset + NAME_VISIBLE);
        prevBtn.disabled = nameOffset === 0;
        nextBtn.disabled = nameOffset + NAME_VISIBLE >= DATA.members.length;
        tabs.innerHTML = '';
        slice.forEach(n => {
            const b = document.createElement('button');
            b.className = 'tab' + (n === activeName ? ' active' : '');
            b.textContent = n;
            b.onclick = () => {
                activeName = n;
                renderRight();
            };
            tabs.appendChild(b);
        });
    }

    function renderRight() {
        renderNameTabs();
        const statusText = (STATUS[activeName] === 'success') ? '성공' : '실패';
        $('#codeMeta').textContent = `${activeName} · ${statusText} · ${TIME[activeName]}ms / ${MEM[activeName]}MB`;
        const sig = $('#signal');
        if (sig) {
            sig.style.background = (STATUS[activeName] === 'success') ? '#22C55E' : '#EF4444';
        }
        renderCodeWithLineNumbers(LONG_CODE, $('#codeBox'));

        const likeOn = !!likes[activeName];
        $('#likeBtn').textContent = likeOn ? '❤️ 취소' : '🤍 좋아요';
        $('#likeCount').textContent = `좋아요 ${likeOn ? 1 : 0} ·`;

        const list = COMMENTS[activeName] || [];
        $('#commentCount').textContent = `댓글 ${list.length}`;
        const page = cPageByName[activeName] || 1;
        const last = Math.max(1, Math.ceil(list.length / PAGE_SIZE));
        $('#cPage').textContent = `${page} / ${last}`;

        // render comments
        const box = $('#comments');
        box.innerHTML = '';
        list.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE).forEach((c, idx) => {
            const short = c.text.length > 90 ? c.text.slice(0, 90) + '…' : c.text;
            const isOwner = c.user === CURRENT_USER;
            const item = document.createElement('div');
            item.className = 'subcard comment';
            item.innerHTML =
                `<div class="avatar">${c.user[0].toUpperCase()}</div>` +
                `<div style="flex:1">
          <div class="row-between" style="margin-bottom:4px">
            <div class="muted" style="font-size:12px">${c.user} · ${c.ts}</div>
            ${isOwner ? '<div class="ownerActions"><button class="btn btn-xs deleteBtn" data-id="' + c.id + '">삭제</button></div>' : ''}
          </div>
          <div>${short} <button data-i="${idx}" class="btn btn-xs commentMore">자세히</button></div>
        </div>`;
            box.appendChild(item);
        });

        // pager buttons
        const list2 = COMMENTS[activeName] || [];
        const p = cPageByName[activeName] || 1;
        const last2 = Math.max(1, Math.ceil(list2.length / PAGE_SIZE));
        $('#cPrev').disabled = p <= 1;
        $('#cNext').disabled = p >= last2;

        // handlers
        $$('#comments .commentMore').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const i = +e.currentTarget.getAttribute('data-i');
                $('#commentFull').textContent = list2[(p - 1) * PAGE_SIZE + i].text;
                $('#commentModal').classList.add('open');
            });
        });
        $$('#comments .deleteBtn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const id = +e.currentTarget.getAttribute('data-id');
                if (!confirm('정말로 이 댓글을 삭제하시겠습니까?')) return;
                const idx = list2.findIndex(x => x.id === id);
                if (idx > -1) {
                    list2.splice(idx, 1);
                    renderRight();
                }
            });
        });
    }

    // ---------------------- EVENTS ----------------------
    function bindEvents() {
        $('#namePrev').onclick = () => {
            nameOffset = clamp(nameOffset - NAME_VISIBLE, 0, Math.max(0, DATA.members.length - NAME_VISIBLE));
            renderRight();
        };
        $('#nameNext').onclick = () => {
            nameOffset = clamp(nameOffset + NAME_VISIBLE, 0, Math.max(0, DATA.members.length - NAME_VISIBLE));
            renderRight();
        };
        $('#likeBtn').onclick = () => {
            likes[activeName] = !likes[activeName];
            renderRight();
        };
        $('#cPrev').onclick = () => {
            const p = cPageByName[activeName] || 1;
            cPageByName[activeName] = Math.max(1, p - 1);
            renderRight();
        };
        $('#cNext').onclick = () => {
            const list = COMMENTS[activeName] || [];
            const p = cPageByName[activeName] || 1;
            const last = Math.max(1, Math.ceil(list.length / PAGE_SIZE));
            cPageByName[activeName] = Math.min(last, p + 1);
            renderRight();
        };
        $('#backBtn').onclick = () => {
            alert('목업: 이전 화면으로 이동');
        };
        $('#bjToggle').onclick = () => {
            $('#bjBody').classList.toggle('hidden');
        };
        $('#cToggle').onclick = () => {
            commentsOpen = !commentsOpen;
            $('#commentsWrap').classList.toggle('hidden', !commentsOpen);
            $('#cToggle').textContent = commentsOpen ? '댓글 숨기기' : '댓글 보기';
        };

        // 코드 모달(오버레이 클릭 닫기)
        $('#codeBox').onclick = () => {
            renderCodeWithLineNumbers(LONG_CODE, $('#codeFull'), true);
            $('#codeModal').classList.add('open');
        };
        $('#codeClose').onclick = () => {
            $('#codeModal').classList.remove('open');
        };
        $('#codeModal').addEventListener('click', (e) => {
            if (e.target === e.currentTarget) e.currentTarget.classList.remove('open');
        });

        // 댓글 상세 모달(오버레이 클릭 닫기)
        $('#commentClose').onclick = () => {
            $('#commentModal').classList.remove('open');
        };
        $('#commentModal').addEventListener('click', (e) => {
            if (e.target === e.currentTarget) e.currentTarget.classList.remove('open');
        });

        // 인라인 댓글 등록
        $('#commentInlineSubmit').onclick = () => {
            const text = ($('#commentInlineInput').value || '').trim();
            if (!text) return;
            const list = COMMENTS[activeName] || [];
            list.unshift({id: Date.now(), user: CURRENT_USER, text, ts: '방금'});
            COMMENTS[activeName] = list;
            $('#commentInlineInput').value = '';
            renderRight();
        };

        // 리더 메뉴
        const menuBtn = $('#leaderMenuBtn');
        const menu = $('#leaderMenu');
        if (IS_LEADER) {
            $('#leaderActions').classList.remove('hidden');
            menuBtn.onclick = (e) => {
                e.stopPropagation();
                menu.classList.toggle('open');
            };
            document.addEventListener('click', () => {
                menu.classList.remove('open');
            });
            $('#problemDeleteBtn').onclick = (e) => {
                e.stopPropagation();
                menu.classList.remove('open');
                $('#problemDeleteModal').classList.add('open');
            };
            $('#problemDeleteCancel').onclick = () => {
                $('#problemDeleteModal').classList.remove('open');
            };
            $('#problemDeleteConfirm').onclick = () => {
                problemDeleted = true;
                $('#problemDeleteModal').classList.remove('open');
                renderProblem();
            };
        }
    }

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


    // ---------------------- INIT (fetch JSON) ----------------------
    async function boot() {
        try {
            const res = await fetch(`${CTX}/mock/solution.json`, {cache: 'no-store'});
            DATA = await res.json();
        } catch (err) {
            console.warn('solution.json 로드 실패, 내부 MOCK 사용', err);
            DATA = {
                problem: {
                    title: "회전초밥",
                    meta: {
                        timeLimit: "1초",
                        memoryLimit: "1024MB",
                        submissions: 1801,
                        correct: 687,
                        solvers: 532,
                        ratio: "38.607%"
                    },
                    description: "fallback desc...",
                    inputDesc: "fallback input...",
                    samples: [{in: "6\n3 5\n1 4 5", out: "1 3 0"}],
                    url: "https://www.acmicpc.net/problem/00000"
                },
                members: ["권광재", "박민용", "이진욱", "이은수", "김도윤", "안호진", "서지민", "이수현"]
            };
        }

        // 파생 데이터(상태/시간/메모리/댓글) 구성
        STATUS = Object.fromEntries(DATA.members.map((n, i) => [n, (i % 3 === 1) ? 'fail' : 'success']));
        TIME = Object.fromEntries(DATA.members.map((n, i) => [n, [92, 310, 120, 188, 140, 215, 170, 260][i % 8]]));
        MEM = Object.fromEntries(DATA.members.map((n, i) => [n, [128, 256, 192, 160, 128, 192, 160, 256][i % 8]]));

        COMMENTS = Object.fromEntries(DATA.members.map((n) => [n, [
            {id: Date.now() + 1, user: "devA", text: makeLongComment(1), ts: "2분 전"},
            {id: Date.now() + 2, user: "devB", text: makeLongComment(2), ts: "1분 전"},
            {id: Date.now() + 3, user: "devC", text: makeLongComment(3), ts: "방금"},
            {id: Date.now() + 4, user: "devD", text: makeLongComment(4), ts: "방금"},
            {id: Date.now() + 5, user: "devE", text: makeLongComment(5), ts: "방금"}
        ]]));

        activeName = DATA.members[0];

        bindEvents();
        renderProblem();
        renderRight();
        initSplit();

    }

    document.addEventListener('DOMContentLoaded', boot);
})();
