<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/home.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/notice.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/noticeModal.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/problem.css"/>

<body>
<main class="main-content">

    <section class="section">
        <h2>📊 워크스페이스 랭킹</h2>

        <div class="chart-grid chart-grid-3">

            <section class="teamchar">
                <!-- 멤버 레벨 TOP 5 -->
                <div class="ranking-card">
                    <h3>🏆 레벨 TOP 5</h3>
                    <ul>
                        <c:forEach var="stat" items="${topLevel}">
                            <li>
                                <span>${stat.nickname}</span>
                                <span>${stat.xp} XP</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <!-- 문제 많이 푼 멤버 TOP 5 -->
                <div class="ranking-card">
                    <h3>🧮 풀이 TOP 5</h3>
                    <ul>
                        <c:forEach var="stat" items="${topSolved}">
                            <li>
                                <span>${stat.nickname}</span>
                                <span>${stat.solvedCount}문제</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <!-- 댓글 많이 남긴 멤버 TOP 5 -->
                <div class="ranking-card ranking-card--comment">
                    <h3>💬 댓글 TOP 5</h3>
                    <ul>
                        <c:forEach var="stat" items="${topComment}">
                            <li>
                                <span>${stat.nickname}</span>
                                <span>${stat.commentCount}개</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </section>

            <section class="section notice-section">
                <h2>📢 팀 공지</h2>
                <c:choose>
                    <c:when test="${empty notices}">
                        <p style="color:var(--muted)">표시할 공지가 없습니다.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="notice-grid" id="noticeGrid">
                            <c:forEach items="${notices}" var="n">
                                <a href="#"
                                   class="notice-card"
                                   data-isLeader="${param.isLeader}"
                                   data-notice-id="${n.noticeId}"
                                   data-title='<c:out value="${not empty n.title ? n.title.value : ''}"/>'
                                   data-content='<c:out value="${not empty n.content ? n.content.value : ''}"/>'>
                                    <strong><c:out value='${not empty n.title ? n.title.value : ""}'/></strong>
                                    <p class="preview">
                                        <c:out value='${not empty n.content ? n.content.value : ""}'/>
                                    </p>
                                </a>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>

            <section class="section problem-section">
                <div class="section-header">
                    <h2>🧩 알고리즘 문제</h2>
                </div>
                <c:choose>
                    <c:when test="${empty problems}">
                        <p style="color:var(--muted)">표시할 문제가 없습니다.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="problem-grid">
                            <c:forEach items="${problems}" var="p">
                                <div class="problem-card">
                                    <div class="card-top">
                                        <strong class="problem-title"><c:out value="${p.title}"/></strong>
                                        <div class="card-meta">
                                            <div class="status-badge ${p.submitted ? 'submitted' : 'not-submitted'}">
                                                <c:out value="${p.submitted ? '제출완료' : '미제출'}"/>
                                            </div>
                                            <span class="difficulty-level ${p.diffClass}">
                                    <c:out value="${p.diff}"/>
                                </span>
                                        </div>
                                    </div>

                                    <div class="tags">
                                        <c:forEach items="${p.tags}" var="tg">
                                            <span><c:out value="${tg.name}"/></span>
                                        </c:forEach>
                                    </div>

                                    <div class="card-bottom">
                                        <div class="bottom-left">
                                            <div class="deadline">마감: <c:out value="${p.deadline}"/></div>
                                            <div class="submit-count">제출자 <c:out value="${p.submitCount}"/>명</div>
                                        </div>

                                        <c:url var="solveUrl" value="/front">
                                            <c:param name="key" value="problem"/>
                                            <c:param name="methodName" value="getProblemDetail"/>
                                            <c:param name="problemId" value="${p.problemId}"/>
                                            <c:param name="isLeader" value="${param.isLeader}"/>
                                        </c:url>
                                        <a class="go-btn" href="${solveUrl}" target="_top">문제 풀기</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>

</main>

<div id="noticeModal" class="ws-modal" aria-hidden="true">
    <div class="ws-dialog" role="dialog" aria-modal="true" aria-labelledby="modalTitle">
        <button type="button" class="modal-close" id="modalClose" title="닫기">×</button>

        <div id="viewSection">
            <h3 id="modalTitle" class="m-title"></h3>
            <div id="modalContent" class="m-body"></div>
        </div>
    </div>
</div>

<jsp:include page="/workspace/notice-modal.jsp"/>
<script defer src="${pageContext.request.contextPath}/workspace/js/noticeModal.js"></script>
<script defer src="${pageContext.request.contextPath}/workspace/js/notice.js"></script>
<script defer src="${pageContext.request.contextPath}/workspace/js/teamChart.js"></script>

</body>
