<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko" data-theme="dark">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>상세 보기 · 문제 풀이 페이지</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/darkmode.css">
    <!-- 이 페이지 전용 스타일 -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/solution/solution.css">
    <script>window.CTX = '<%=request.getContextPath()%>';</script>
</head>
<body class="page-solution">
<div class="wrap">
    <div class="container">
        <div class="page">
            <!-- 로컬 툴바 -->
            <div class="localbar">
                <button class="btn" id="backBtn">← 돌아가기</button>
                <div class="title">상세 보기 · <span id="pageProblemTitle">로딩 중…</span></div>
            </div>

            <div id="split" class="grid-2 split">
                <section class="card pane" id="problemPanel">
                    <header class="panel-head">
                        <div><h2 id="problemTitle">문제 제목</h2></div>
                        <div id="leaderActions" class="menu-wrap hidden">
                            <button class="btn" id="leaderMenuBtn" title="관리">⋯</button>
                            <div class="menu-panel" id="leaderMenu">
                                <button class="menu-item" id="problemDeleteBtn">🗑 문제 삭제…</button>
                            </div>
                        </div>
                    </header>
                    <div class="divider"></div>
                    <div class="meta" id="metaChips"></div>

                    <section class="block">
                        <div class="block-title">문제</div>
                        <div class="divider"></div>
                        <p class="muted" id="problemDesc"></p>
                    </section>

                    <section class="block">
                        <div class="block-title">입력</div>
                        <div class="divider"></div>
                        <p class="muted" id="inputDesc"></p>
                    </section>

                    <section class="block">
                        <div class="block-title">출력</div>
                        <div class="divider"></div>
                        <p class="muted" id="outputDesc"></p>
                    </section>

                    <section id="samples"></section>

                    <!-- Bookmark for Baekjoon -->
                    <div class="bookmark" id="bjToggle" title="백준 북마크" role="link" tabindex="0">
                        <img alt="Baekjoon"
                             src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='28' height='28'%3E%3Crect width='28' height='28' rx='6' ry='6' fill='%231e293b'/%3E%3Ccircle cx='8' cy='14' r='4' fill='%235b86e5'/%3E%3Ccircle cx='20' cy='14' r='4' fill='%2339d98a'/%3E%3C/svg%3E"/>
                        <div>
                            <div class="bm-title">백준 문제 페이지</div>
                            <div class="muted small">링크 가기</div>
                        </div>
                        <span class="chev">↗</span>
                    </div>
                    <div class="bookmark-body hidden" id="bjBody">
                        <a id="bojLink" href="#" target="_blank">https://www.acmicpc.net/problem/00000</a>
                    </div>
                </section>

                <div id="splitter" class="splitter" role="separator"
                     aria-orientation="vertical" tabindex="0">
                </div>

                <!-- Right: 팀 탭 + 코드 + 댓글 -->
                <section class="card pane" id="rightPanel" style="position:relative">
                    <div class="tab-nav">
                        <button class="btn" id="namePrev">◀</button>
                        <div class="tabs" id="nameTabs"></div>
                        <button class="btn" id="nameNext">▶</button>
                    </div>

                    <div class="subcard code-card">
                        <div id="signal" class="signal"></div>
                        <div class="muted code-meta" id="codeMeta"></div>
                        <pre class="code" id="codeBox" title="코드를 클릭하면 전체 보기"></pre>
                        <div class="row code-actions">
                            <button class="btn" id="likeBtn">🤍 좋아요</button>
                            <span class="muted" id="likeCount">좋아요 0 ·</span>
                            <button class="btn" id="cToggle">댓글 보기</button>
                            <span class="muted" id="commentCount"></span>
                        </div>
                    </div>

                    <div id="commentsWrap" class="hidden">
                        <div class="subcard comment-editor">
                            <textarea id="commentInlineInput" rows="2" placeholder="댓글을 입력하세요..."></textarea>
                            <div class="row right">
                                <button class="btn" id="commentInlineSubmit">등록</button>
                            </div>
                        </div>

                        <div id="comments"></div>
                        <div class="row-between pager">
                            <button class="btn" id="cPrev">이전</button>
                            <span class="muted" id="cPage">1 / 1</span>
                            <button class="btn" id="cNext">다음</button>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </div>
</div>

<!-- Modals -->
<div class="modal" id="codeModal">
    <div class="panel">
        <div class="row-between mb8"><strong>코드 상세</strong>
            <button class="btn" id="codeClose">닫기</button>
        </div>
        <pre class="code code-lg" id="codeFull"></pre>
    </div>
</div>
<div class="modal" id="commentModal">
    <div class="panel">
        <div class="row-between mb8"><strong>댓글 상세</strong>
            <button class="btn" id="commentClose">닫기</button>
        </div>
        <pre id="commentFull"></pre>
    </div>
</div>
<div class="modal" id="problemDeleteModal">
    <div class="panel">
        <div class="strong mb6">문제를 정말로 삭제하시겠습니까?</div>
        <div class="muted">다시 복구할 수 없습니다.</div>
        <div class="row right mt14">
            <button class="btn" id="problemDeleteCancel">취소</button>
            <button class="btn" id="problemDeleteConfirm">삭제</button>
        </div>
    </div>
</div>

<!-- 이 페이지 전용 스크립트 -->
<script defer src="<%=request.getContextPath()%>/solution/solution.js"></script>
</body>
</html>
