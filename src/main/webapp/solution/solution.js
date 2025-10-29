(() => {
        // ===== helpers =====
        const $ = (sel, root = document) => root.querySelector(sel);
        const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
        const clamp = (n, min, max) => Math.max(min, Math.min(max, n));
        const NAME_VISIBLE = 5;
        const COMMENT_PAGE_SIZE = 5;

        // ====================== API ======================
        const FrontAPI = (() => {
            const qs = (obj) =>
                Object.entries(obj)
                    .filter(([, v]) => v !== undefined && v !== null)
                    .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`)
                    .join('&');

            async function fetchJSON(params, {method = 'GET', body} = {}) {
                const {key, methodName, ...rest} = params;
                const url = `/ajax?${qs({key, methodName, ...rest})}`;
                const opt = {method, headers: {'Accept': 'application/json'}, cache: 'no-store'};
                if (method === 'POST') {
                    opt.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
                    opt.body = new URLSearchParams(body || {}).toString();
                }
                const res = await fetch(url, opt);
                const txt = await res.text();
                let json = null;
                try {
                    json = txt ? JSON.parse(txt) : null;
                } catch (e) {
                    throw new Error(`Invalid JSON: ${e.message}`);
                }
                if (!res.ok) throw new Error(json?.error || `HTTP ${res.status}`);
                return json;
            }

            return {
                getSubmitters: (wsProblemId, page = 1, size = NAME_VISIBLE) =>
                    fetchJSON({
                        key: 'solution',
                        methodName: 'getSubmitters',
                        wsProblemId,
                        page,
                        size
                    }),
                getSolutionDetail: (solutionId) => {
                    const cfg = document.getElementById('jsConfig');
                    const wsMemberId = cfg?.dataset.workspaceMemberId;
                    return fetchJSON({
                        key: 'solution',
                        methodName: 'getSolutionDetail',
                        solutionId,
                        wsMemberId
                    });
                },
                toggleLike: (solutionId) => {
                    const cfg = document.getElementById('jsConfig');
                    const wsMemberId = cfg?.dataset.workspaceMemberId;
                    return fetchJSON({
                        key: 'solution',
                        methodName: 'toggleLike',
                        solutionId,
                        wsMemberId
                    });
                },
                getComments: (solutionId, page = 1, size = COMMENT_PAGE_SIZE) => {
                    const cfg = document.getElementById('jsConfig');
                    const wsMemberId = cfg?.dataset.workspaceMemberId;
                    return fetchJSON({
                        key: 'solution',
                        methodName: 'getComments',
                        solutionId,
                        page,
                        size,
                        wsMemberId
                    });
                },
                addComment: (solutionId, content) => {
                    const cfg = document.getElementById('jsConfig');
                    const wsMemberId = cfg?.dataset.workspaceMemberId;
                    return fetchJSON(
                        {
                            key: 'solution',
                            methodName: 'addComment'
                        },
                        {
                            method: 'POST',
                            body: {
                                solutionId,
                                wsMemberId,
                                content
                            }
                        }
                    );
                },
                deleteComment: (commentId) => {
                    const cfg = document.getElementById('jsConfig');
                    const wsMemberId = cfg?.dataset.workspaceMemberId;
                    return fetchJSON(
                        {
                            key: 'solution',
                            methodName: 'deleteComment'
                        },
                        {
                            method: 'POST',
                            body: {
                                commentId,
                                wsMemberId
                            }
                        }
                    );
                },
                deleteWsProblem: (wsProblemId) => {
                    const cfg = document.getElementById('jsConfig');
                    const wsMemberId = cfg?.dataset.workspaceMemberId;
                    return fetchJSON(
                        {
                            key: 'solution',
                            methodName: 'delete'
                        },
                        {
                            method: 'POST',
                            body: {
                                wsProblemId,
                                wsMemberId
                            }
                        }
                    );
                }
            };
        })();

        // ====================== 상태 & 공용 렌더 헬퍼 ======================
        const state = {
            wsProblemId: null,
            page: 1,
            pageSize: NAME_VISIBLE,
            total: 0
        };

        function setRightLoading() {
            $('#codeMeta').textContent = '로딩 중…';
            $('#signal').style.background = '#6b7280';
            $('#codeBox').textContent = '';
            $('#likeBtn').disabled = true;
            $('#likeCount').textContent = '';
            $('#commentCount').textContent = '';
        }

        function renderCodeLines(codeText) {
            const pre = $('#codeBox');
            pre.innerHTML = '';
            const lines = String(codeText || '').replace(/\r\n/g, '\n').split('\n');
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
            pre.appendChild(frag);
        }

        async function loadAndRenderSolution(solutionId) {
            if (!solutionId) {
                $('#codeMeta').textContent = '제출 내역이 없습니다.';
                $('#signal').style.background = '#6b7280';
                $('#codeBox').textContent = '';
                $('#likeBtn').disabled = true;
                $('#likeCount').textContent = '';
                $('#commentCount').textContent = '';
                return;
            }

            try {
                setRightLoading();

                const d = await FrontAPI.getSolutionDetail(solutionId);

                const statusText = d.status === 'success' ? '성공' : '실패';
                $('#codeMeta').textContent = `${statusText} · ${d.timeMs ?? '-'}ms / ${d.memoryMb ?? '-'}MB`;
                $('#signal').style.background = d.status === 'success' ? '#22C55E' : '#EF4444';
                renderCodeLines(d.code);

                const likeBtn = $('#likeBtn');
                likeBtn.disabled = false;
                likeBtn.textContent = d.like?.me ? '❤️ 취소' : '🤍 좋아요';
                $('#likeCount').textContent = `좋아요 ${d.like?.count ?? 0} ·`;
                $('#commentCount').textContent = `댓글 ${d.commentCount ?? 0}`;

                likeBtn.onclick = async () => {
                    const cfg = document.getElementById('jsConfig');
                    const wsMemberId = cfg?.dataset.workspaceMemberId;
                    if (!wsMemberId) {
                        alert('좋아요는 멤버만 가능합니다.');
                        return;
                    }
                    likeBtn.disabled = true;
                    try {
                        const next = await FrontAPI.toggleLike(solutionId);
                        likeBtn.textContent = next.me ? '❤️ 취소' : '🤍 좋아요';
                        $('#likeCount').textContent = `좋아요 ${next.count} ·`;
                    } catch (e) {
                        alert('좋아요 처리 실패: ' + e.message);
                    } finally {
                        likeBtn.disabled = false;
                    }
                };
            } catch (e) {
                $('#codeMeta').textContent = `상세 로드 실패: ${e.message}`;
                $('#signal').style.background = '#EF4444';
            }
        }

        function renderSubmitterTabs(data) {
            // data: { members[], page, pageSize, total, prime? }
            const tabsWrap = $('#nameTabs');
            const prev = $('#namePrev');
            const next = $('#nameNext');

            tabsWrap.innerHTML = '';
            (data.members || []).forEach(m => {
                const b = document.createElement('button');
                b.className = 'tab';
                b.textContent = m.nickname || `(ID:${m.wsMemberId})`;
                b.disabled = !m.hasSubmission;
                b.dataset.solutionId = m.solutionId || '';
                if (!m.hasSubmission) b.title = '제출 내역 없음';
                b.addEventListener('click', () => {
                    const sid = b.dataset.solutionId ? +b.dataset.solutionId : null;
                    state.curSolutionId = sid;
                    loadAndRenderSolution(sid);
                    if (!$('#commentsWrap')?.classList.contains('hidden') && sid) {
                        loadComments(sid, 1).catch(console.error);
                    }
                    $$('.tab', tabsWrap).forEach(el => el.classList.remove('active'));
                    b.classList.add('active');
                });
                tabsWrap.appendChild(b);
            });

            state.page = data.page;
            state.pageSize = data.pageSize;
            state.total = data.total;
            const lastPage = Math.max(1, Math.ceil(state.total / state.pageSize));
            prev.disabled = state.page <= 1;
            next.disabled = state.page >= lastPage;

            // prime 우선, 없으면 제출 있는 첫 탭
            const tabs = $$('.tab', tabsWrap);
            let auto = null;
            if (data.prime?.solutionId) {
                auto = tabs.find(el => +el.dataset.solutionId === +data.prime.solutionId);
            }
            if (!auto) auto = tabs.find(el => !el.disabled);
            if (auto) auto.click(); else {
                state.curSolutionId = null;
                loadAndRenderSolution(null);
            }
        }

        async function fetchSubmitterPage(page) {
            const data = await FrontAPI.getSubmitters(state.wsProblemId, page, state.pageSize);
            renderSubmitterTabs(data);
        }

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
                const cfg = document.getElementById('jsConfig');
                const wsProblemId = +cfg.dataset.wsProblemId;
                const isLeader = cfg?.dataset.isLeader === '1' || cfg?.dataset.isLeader === 'true';

                if (!isLeader) {
                    alert('삭제 권한이 없습니다.');
                    return;
                }

                okBtn.disabled = true;
                try {
                    await FrontAPI.deleteWsProblem(wsProblemId);
                    history.back();
                } catch (e) {
                    alert('삭제 실패: ' + e.message);
                } finally {
                    okBtn.disabled = false;
                    const modal = document.getElementById('problemDeleteModal');
                    modal?.classList.remove('open');
                    document.body.classList.remove('modal-open');
                }
            });
        }

        // ===== Code modal (코드 전체 보기) =====
        function renderCodeWithLineNumbersFrom(preSrc, preDst, large = false) {
            if (!preSrc || !preDst) return;

            const txs = preSrc.querySelectorAll('.tx');
            const lines = txs.length
                ? Array.from(txs, el => el.textContent)
                : String(preSrc.textContent || '').replace(/\r\n/g, '\n').split('\n');

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

        // ===== Comment helpers =====
        function escapeHtml(s = '') {
            // XSS 방지 (댓글 내용은 text로 다루는게 안전)
            return String(s)
                .replace(/&/g, '&amp;').replace(/</g, '&lt;')
                .replace(/>/g, '&gt;').replace(/"/g, '&quot;')
                .replace(/'/g, '&#39;');
        }

        function renderComments(page, json) {
            const list = $('#comments');
            const pagerText = $('#cPage');

            if (!json) {
                list.innerHTML = '<div class="muted">댓글 로딩 중…</div>';
                pagerText.textContent = '';
                return;
            }

            const items = json.items || [];
            const size = json.pageSize || COMMENT_PAGE_SIZE;
            const last = Math.max(1, Math.ceil((json.total || 0) / size));
            state.cPage = page;
            state.cLast = last;

            pagerText.textContent = `${page} / ${last}`;
            $('#cPrev').disabled = page <= 1;
            $('#cNext').disabled = page >= last;
            $('#commentCount').textContent = `댓글 ${json.total ?? 0}`;

            list.innerHTML = '';
            items.forEach((c) => {
                const uname = c.user?.name || c.user?.id || 'anon';
                // 안전하게 노드로 구성 (innerHTML 최소화)
                const item = document.createElement('div');
                item.className = 'subcard comment';

                const avatar = document.createElement('div');
                avatar.className = 'avatar';
                avatar.textContent = String(uname).charAt(0).toUpperCase() || 'U';

                const right = document.createElement('div');
                right.style.flex = '1';

                const header = document.createElement('div');
                header.className = 'row-between';
                header.style.marginBottom = '4px';

                const meta = document.createElement('div');
                meta.className = 'muted';
                meta.style.fontSize = '12px';
                meta.textContent = `${uname} · ${c.ts || ''}`;

                header.appendChild(meta);

                if (c.isOwner) {
                    const actions = document.createElement('div');
                    actions.className = 'ownerActions';
                    const delBtn = document.createElement('button');
                    delBtn.className = 'btn btn-xs deleteBtn';
                    delBtn.dataset.id = c.id;
                    delBtn.textContent = '삭제';
                    delBtn.addEventListener('click', async () => {
                        if (!confirm('정말로 이 댓글을 삭제하시겠습니까?')) return;
                        try {
                            await FrontAPI.deleteComment(+delBtn.dataset.id);
                            const goPage = state.cPage > 1 && items.length === 1 ? state.cPage - 1 : state.cPage;
                            await loadComments(state.curSolutionId, goPage);
                        } catch (e) {
                            alert('삭제 실패: ' + e.message);
                        }
                    });
                    actions.appendChild(delBtn);
                    header.appendChild(actions);
                }

                const body = document.createElement('div');
                body.textContent = c.text || ''; // textContent로 안전하게

                right.appendChild(header);
                right.appendChild(body);

                item.appendChild(avatar);
                item.appendChild(right);
                list.appendChild(item);
            });
        }

        async function loadComments(solutionId, page = 1) {
            const json = await FrontAPI.getComments(solutionId, page, COMMENT_PAGE_SIZE);
            renderComments(page, json);
        }


        // ===== Comments =====
        function initComments() {
            const wrap = $('#commentsWrap');
            const toggle = $('#cToggle');
            const prev = $('#cPrev');
            const next = $('#cNext');
            const input = $('#commentInlineInput');
            const submit = $('#commentInlineSubmit');

            if (!toggle) return;

            // 로그인 안된 경우 입력 비활성
            const cfg = $('#jsConfig');
            const viewer = cfg?.dataset.workspaceMemberId;
            if (!viewer && input) {
                input.disabled = true;
                input.placeholder = '로그인한 멤버만 댓글을 작성할 수 있습니다.';
                submit.disabled = true;
            }

            toggle.addEventListener('click', async () => {
                const open = wrap.classList.contains('hidden');
                if (open) {
                    if (!state.curSolutionId) return;
                    wrap.classList.remove('hidden');
                    toggle.textContent = '댓글 숨기기';
                    await loadComments(state.curSolutionId, 1);
                    input?.focus();
                } else {
                    wrap.classList.add('hidden');
                    toggle.textContent = '댓글 보기';
                    $('#comments').innerHTML = '';
                    $('#cPage').textContent = '';
                }
            });

            prev?.addEventListener('click', async () => {
                if (state.cPage <= 1) return;
                await loadComments(state.curSolutionId, state.cPage - 1);
            });
            next?.addEventListener('click', async () => {
                if (state.cPage >= state.cLast) return;
                await loadComments(state.curSolutionId, state.cPage + 1);
            });

            submit?.addEventListener('click', async () => {
                if (!state.curSolutionId) return;
                const text = (input?.value || '').trim();
                if (!text) {
                    input?.focus();
                    return;
                }
                submit.disabled = true;
                try {
                    await FrontAPI.addComment(state.curSolutionId, text);
                    input.value = '';
                    await loadComments(state.curSolutionId, 1);
                } catch (e) {
                    alert('댓글 등록 실패: ' + e.message);
                } finally {
                    submit.disabled = false;
                }
            });
        }

        // ===== Boot =====
        function boot() {
            initLeaderMenu();
            initProblemDelete();
            initCodeModal();
            initComments();
            initSplit();

            const cfg = $('#jsConfig');
            state.wsProblemId = cfg ? +cfg.dataset.wsProblemId : null;
            if (!state.wsProblemId) return;

            $('#namePrev')?.addEventListener('click', () => {
                const prev = Math.max(1, state.page - 1);
                if (prev !== state.page) fetchSubmitterPage(prev);
            });
            $('#nameNext')?.addEventListener('click', () => {
                const last = Math.max(1, Math.ceil(state.total / state.pageSize));
                const next = Math.min(last, state.page + 1);
                if (next !== state.page) fetchSubmitterPage(next);
            });

            // 첫 페이지 로드
            fetchSubmitterPage(1).catch(err => {
                $('#codeMeta').textContent = `제출자 로드 실패: ${err.message}`;
            });

        }

        document.addEventListener('DOMContentLoaded', boot);
    }

)
();