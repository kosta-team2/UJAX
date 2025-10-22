<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>워크스페이스 문제 페이지</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/problem.css"/>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/mock">
    <jsp:param name="key" value="problem"/>
    <jsp:param name="methodName" value="getProblems"/>
    <jsp:param name="wsId" value="${param.wsId}"/>
    <jsp:param name="page" value="${empty param.page ? 1 : param.page}"/>
    <jsp:param name="size" value="${empty param.size ? 6 : param.size}"/>
</jsp:include>

<section class="section problem-list-section">
    <div class="problem-header">
        <h3>문제 리스트</h3>
        <a class="problem-register-btn"
           href="${pageContext.request.contextPath}/workspace/problem-register.jsp?wsId=${param.wsId}"
           target="mainFrame">문제 등록</a>
    </div>

    <hr class="section-divider"/>

    <div class="problem-controls">
        <div class="search-sort">
            <form method="get" action="${pageContext.request.contextPath}/workspace/problem.jsp">
                <input type="hidden" name="wsId" value="${param.wsId}"/>
                <input type="hidden" name="size" value="${size}"/>
                <input type="text" class="search-input" name="q" placeholder="문제 제목 / 태그 검색(목업)">
                <button class="sort-btn" type="submit">정렬 ▾</button>
            </form>
        </div>
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
                               target="_top">문제 풀기</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- 페이지네이션: « ‹ 1 2 3 › » -->
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <a class="page-btn ${!hasPrev ? 'disabled' : ''}"
               href="${pageContext.request.contextPath}/workspace/problem.jsp?wsId=${param.wsId}&page=1&size=${size}"
               aria-label="첫 페이지">&laquo;</a>

            <a class="page-btn ${!hasPrev ? 'disabled' : ''}"
               href="${pageContext.request.contextPath}/workspace/problem.jsp?wsId=${param.wsId}&page=${hasPrev ? prevPage : page}&size=${size}"
               aria-label="이전">&lsaquo;</a>

            <c:forEach var="pnum" begin="${startPage}" end="${endPage}">
                <c:choose>
                    <c:when test="${pnum == page}">
                        <span class="page-btn current">${pnum}</span>
                    </c:when>
                    <c:otherwise>
                        <a class="page-btn"
                           href="${pageContext.request.contextPath}/workspace/problem.jsp?wsId=${param.wsId}&page=${pnum}&size=${size}">${pnum}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <a class="page-btn ${!hasNext ? 'disabled' : ''}"
               href="${pageContext.request.contextPath}/workspace/problem.jsp?wsId=${param.wsId}&page=${hasNext ? nextPage : page}&size=${size}"
               aria-label="다음">&rsaquo;</a>

            <a class="page-btn ${!hasNext ? 'disabled' : ''}"
               href="${pageContext.request.contextPath}/workspace/problem.jsp?wsId=${param.wsId}&page=${totalPages}&size=${size}"
               aria-label="마지막">&raquo;</a>
        </div>
    </c:if>
</section>

</body>
</html>
