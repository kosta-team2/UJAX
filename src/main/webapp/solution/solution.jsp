<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko" data-theme="dark">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>상세 보기 · 문제 풀이 페이지</title>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/darkmode.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/solution/solution.css">

</head>
<body class="page-solution">
<div class="wrap">
    <div class="container">
        <div class="page">

            <div class="localbar">
                <button class="btn" id="backBtn" onclick="history.back()">← 돌아가기</button>
                <div class="title">상세 보기 · <span id="pageProblemTitle"><c:out value="${detail.title}"/></span></div>
            </div>

            <div id="split" class="grid-2 split">
                <section class="card pane" id="problemPanel">
                    <header class="panel-head">
                        <div>
                            <h2 id="problemTitle">
                                <c:out value="${detail.title}"/>
                            </h2></div>

                        <c:if test="${isLeader}">
                            <div id="leaderActions" class="menu-wrap">
                                <button class="btn" id="leaderMenuBtn" title="관리">⋯</button>
                                <div class="menu-panel" id="leaderMenu">
                                    <button class="menu-item" id="problemDeleteBtn">🗑 문제 삭제…</button>
                                </div>
                            </div>
                        </c:if>
                    </header>

                    <div class="divider"></div>

                    <div class="meta" id="metaChips">
                        <div class="chip">
                            <span class="muted">시간 제한</span>
                            <span style="font-weight:700"><c:out value="${detail.timeLimit}"/></span>
                        </div>
                        <div class="chip">
                            <span class="muted">메모리 제한</span>
                            <span style="font-weight:700"><c:out value="${detail.memoryLimit}"/></span>
                        </div>
                    </div>

                    <section class="block">
                        <div class="block-title">문제</div>
                        <div class="divider"></div>
                        <p class="muted" id="problemDesc">
                            <c:out value="${detail.description}" escapeXml="false"/>
                        </p>
                    </section>

                    <section class="block">
                        <div class="block-title">입력</div>
                        <div class="divider"></div>
                        <p class="muted" id="inputDesc">
                            <c:out value="${detail.input}" escapeXml="false"/>
                        </p>
                    </section>

                    <section class="block">
                        <div class="block-title">출력</div>
                        <div class="divider"></div>
                        <p class="muted" id="outputDesc">
                            <c:out value="${detail.output}" escapeXml="false"/>
                        </p>
                    </section>


                    <section id="samples">
                        <c:forEach items="${detail.samples}" var="s" varStatus="st">
                            <section class="block sample">
                                <div class="block-title">
                                    예제
                                    <c:out value="${st.index + 1}"/>
                                </div>
                                <div class="divider"></div>

                                <div class="sample-io">
                                    <div class="io">
                                        <div class="small muted">입력</div>
                                        <pre class="code"><c:out value="${s.input}"/></pre>
                                    </div>
                                    <div class="io">
                                        <div class="small muted">출력</div>
                                        <pre class="code"><c:out value="${s.output}"/></pre>
                                    </div>
                                </div>
                            </section>
                        </c:forEach>
                    </section>

                    <c:set var="bojHref" value="#"/>
                    <c:if test="${not empty detail.url}">
                        <c:set var="bojHref" value="${detail.url}"/>
                    </c:if>
                    <c:if test="${empty detail.url and not empty detail.problemNum}">
                        <c:set var="bojHref" value="https://www.acmicpc.net/problem/${detail.problemNum}"/>
                    </c:if>

                    <a class="bookmark" id="bojLink" href="${bojHref}" target="_blank" rel="noopener noreferrer">
                        <img alt="Baekjoon"
                             src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='28' height='28'%3E%3Crect width='28' height='28' rx='6' ry='6' fill='%231e293b'/%3E%3Ccircle cx='8' cy='14' r='4' fill='%235b86e5'/%3E%3Ccircle cx='20' cy='14' r='4' fill='%2339d98a'/%3E%3C/svg%3E"/>
                        <div>
                            <div class="bm-title">백준 문제 페이지</div>
                            <div class="muted small">링크 가기</div>
                        </div>
                        <span class="chev">↗</span>
                    </a>
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
                            <label for="commentInlineInput"></label>
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
<%--<script defer src="<%=request.getContextPath()%>/solution/solution.js"></script>--%>
</body>
</html>
