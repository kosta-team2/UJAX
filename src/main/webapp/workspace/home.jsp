<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>워크스페이스 홈</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/notice.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/teamChart.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/problem.css"/>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/mock">
    <jsp:param name="key" value="workspaceHome"/>
    <jsp:param name="methodName" value="view"/>
    <jsp:param name="wsId" value="${param.wsId}"/>
</jsp:include>

<main class="main-content">
    <section class="section notice-section">
        <h2>📢 팀 공지</h2>

        <c:choose>
            <c:when test="${empty notices}">
                <p style="color:var(--muted)">등록된 공지가 없습니다.</p>
            </c:when>
            <c:otherwise>
                <div class="notice-grid" id="noticeGrid">
                    <c:forEach items="${notices}" var="n" varStatus="st" begin="0" end="2">
                        <a class="notice-card"
                           href="#"
                           data-title="${n.title}"
                           data-content="${n.content}">
                            <strong><c:out value="${n.title}"/></strong>
                            <p><c:out value="${n.content}"/></p>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="section">
        <h2>팀 차트</h2>
        <div class="chart-grid chart-grid-3">
            <div class="grass-wrapper">

            </div>

            <div class="mini-stat">
                <span>이번 주 풀이 수</span><br/>
                <strong><c:out value="${weeklySolved}" default="0"/></strong><br/>
            </div>

            <div class="mini-stat">
                <span>평균 정답률</span><br/>
                <strong><c:out value="${avgAccuracy}" default="0"/>%</strong><br/>
            </div>
        </div>
    </section>

    <section class="section">
        <div class="section-header">
            <h3>알고리즘 문제</h3>
        </div>
        <c:choose>
            <c:when test="${empty problems}">
                <p style="color:var(--muted)">표시할 문제가 없습니다.</p>
            </c:when>
            <c:otherwise>
                <div class="problem-grid">
                    <c:forEach items="${problems}" var="p" varStatus="st" begin="0" end="5">
                        <div class="problem-card">
                            <div class="card-top">
                                <strong class="problem-title"><c:out value="${p.title}"/></strong>
                                <div class="card-meta">
                                    <div class="status-badge ${p.submitted ? 'submitted' : 'not-submitted'}">
                                        <c:out value="${p.submitted ? '제출완료' : '미제출'}"/>
                                    </div>
                                    <span class="difficulty-level ${p.difficultyClass}">
                                        <c:out value="${p.difficulty}"/>
                                    </span>
                                </div>
                            </div>

                            <div class="tags">
                                <c:forEach items="${p.tags}" var="tg">
                                    <span><c:out value="${tg}"/></span>
                                </c:forEach>
                            </div>

                            <div class="card-bottom">
                                <div class="bottom-left">
                                    <div class="deadline">마감: <c:out value="${p.deadline}"/></div>
                                    <div class="submit-count">제출자 <c:out value="${p.submitCount}"/>명</div>
                                </div>

                                <a class="go-btn"
                                   href="${pageContext.request.contextPath}/solution/solution.jsp?id=${p.id}&wsId=${param.wsId}"
                                   target="_top">
                                    문제 풀기
                                </a>
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

<script defer src="js/notice.js"></script>

</body>
</html>
