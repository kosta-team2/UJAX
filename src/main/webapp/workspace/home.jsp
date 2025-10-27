<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/home.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/notice.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/noticeModal.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/problem.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/teamChart.css"/>

<body>
<main class="main-content">

    <section class="section">
        <h2>팀 차트</h2>

        <div class="chart-grid chart-grid-3">

            <section class="teamchar">
                <!-- 왼쪽: 랭킹 카드 -->
                <div class="rank-col">
                    <div class="ranking-card">
                        <div class="ranking-card__head">
                            <span class="ranking-card__title">멤버 레벨 TOP 5</span>
                        </div>
                        <div class="ranking-card__body">
                            <c:forEach items="${members}" var="m" varStatus="st">
                                <c:if test="${st.index lt 5}">
                                    <div class="rank-row">
                                        <div class="rank-row__left">
                  <span class="rank-badge">
                    <c:choose>
                        <c:when test="${st.index == 0}"><span class="rank-badge rank-1">1</span></c:when>
                        <c:when test="${st.index == 1}"><span class="rank-badge rank-1">2</span></c:when>
                        <c:when test="${st.index == 2}"><span class="rank-badge rank-1">3</span></c:when>
                        <c:otherwise><c:out value="${st.index + 1}"/></c:otherwise>
                    </c:choose>
                  </span>
                                            <strong class="rank-name"><c:out value="${m.nickname}"
                                                                             default="익명"/></strong>
                                        </div>
                                        <div class="rank-row__right">
                                            <span class="rank-xp"><fmt:formatNumber value="${m.xp}"
                                                                                    type="number"/> XP</span>
                                            <c:if test="${not empty topXp and topXp gt 0}">
                                                <div class="xpbar">
                                                    <div class="xpbar__fill"
                                                         style="width:${ (m.xp * 100.0) / topXp }%;"></div>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <!-- 가운데: 이번 주 풀이 수 -->
                <div class="mini-stat mini-stat--solved">
                    <span>이번 주 풀이 수</span><br/>
                    <strong><c:out value="${weeklySolved}" default="0"/></strong>
                </div>

                <!-- 오른쪽: 평균 정답률 -->
                <div class="mini-stat mini-stat--accuracy">
                    <span>평균 정답률</span><br/>
                    <strong><c:out value="${avgAccuracy}" default="0"/>%</strong>
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
                    <h3>알고리즘 문제</h3>
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
