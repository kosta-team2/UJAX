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

<section class="section problem-list-section">
    <div class="problem-header">
        <h3>문제 리스트</h3>
        <a class="problem-register-btn"
           href="${pageContext.request.contextPath}/workspace/problem-register.jsp?workspaceId=${param.workspaceId}&workspaceMemberId=${param.workspaceMemberId}&isLeader=${param.isLeader}"
           target="mainFrame">문제 등록</a>
    </div>

    <hr class="section-divider"/>

    <div class="problem-controls">
        <div class="search-sort">
            <form method="get" action="${pageContext.request.contextPath}/workspace/problem.jsp">
                <input type="hidden" name="workspaceId" value="${param.workspaceId}"/>
                <input type="hidden" name="workspaceMemberId" value="1"/>
                <input type="hidden" name="size" value="${empty size ? 6 : size}"/>
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
                                <c:param name="wsProblemId" value="${p.wsProblemId}"/>
                                <c:param name="workspaceId" value="${param.workspaceId}"/>
                                <c:param name="workspaceMemberId" value="${param.workspaceMemberId}"/>
                                <c:param name="isLeader" value="${param.isLeader}"/>
                            </c:url>
                            <a class="go-btn" href="${solveUrl}" target="_top">문제 풀기</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <a class="page-btn"
               href="${pageContext.request.contextPath}/front?key=problem&methodName=getProblems&workspaceId=${param.workspaceId}&workspaceMemberId=1&page=1&size=6"
               aria-label="첫 페이지">&laquo;</a>

            <c:forEach var="pnum" begin="${1}" end="${totalPages}">
                <c:choose>
                    <c:when test="${pnum == page}">
                        <span class="page-btn current">${pnum}</span>
                    </c:when>
                    <c:otherwise>
                        <a class="page-btn"
                           href="${pageContext.request.contextPath}/front?key=problem&methodName=getProblems&workspaceId=${param.workspaceId}&workspaceMemberId=1&page=${pnum}&size=6">${pnum}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <a class="page-btn"
               href="${pageContext.request.contextPath}/front?key=problem&methodName=getProblems&workspaceId=${param.workspaceId}&workspaceMemberId=1&page=${totalPages}&size=6"
               aria-label="마지막">&raquo;</a>
        </div>
    </c:if>
</section>

</body>
</html>
