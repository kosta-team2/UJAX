<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko" data-theme="dark">
<head>
    <meta charset="UTF-8"/>
    <title>공지 리스트</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/notice.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/noticeModal.css"/>

</head>
<body>
<main class="main-content">
    <section class="section notice-list-section">

        <div class="notice-header">
            <h1>공지 사항</h1>
            <%--            todo 사이드바의 isleader를 통해 공지 등록 보이고숨기기--%>
            <button class="register-btn" type="button" id="openEditorBtn">공지 등록</button>
        </div>

        <hr class="section-divider"/>

        <div class="notice-controls">
            <div class="search-sort">
                <form method="get" action="${pageContext.request.contextPath}/workspace/notice.jsp">
                    <input type="hidden" name="workspaceId" value="${param.workspaceId}"/>
                    <input type="hidden" name="size" value="${size}"/>
                </form>
            </div>
        </div>
        
        <c:choose>
            <c:when test="${empty notices}">
                <p style="color:var(--muted)">표시할 공지가 없습니다.</p>
            </c:when>
            <c:otherwise>
                <div class="notice-grid" id="noticeGrid">
                    <c:forEach items="${notices}" var="n">
                        <a href="#" class="notice-card"
                           data-title="${n.title}"
                           data-content="${n.content}">
                            <strong><c:out value="${n.title}"/></strong>
                            <p><c:out value="${n.content}"/></p>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <%--    페이지네이션    --%>
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <a class="page-btn ${!hasPrev ? 'disabled' : ''}"
                   href="${pageContext.request.contextPath}/workspace/notice.jsp?workspaceId=${param.workspaceId}&page=1&size=${size}"
                   aria-label="첫 페이지">&laquo;</a>

                <a class="page-btn ${!hasPrev ? 'disabled' : ''}"
                   href="${pageContext.request.contextPath}/workspace/notice.jsp?workspaceId=${param.workspaceId}&page=${hasPrev ? prevPage : page}&size=${size}"
                   aria-label="이전">&lsaquo;</a>

                <c:forEach var="pnum" begin="${startPage}" end="${endPage}">
                    <c:choose>
                        <c:when test="${pnum == page}">
                            <span class="page-btn current">${pnum}</span>
                        </c:when>
                        <c:otherwise>
                            <a class="page-btn"
                               href="${pageContext.request.contextPath}/workspace/notice.jsp?workspaceId=${param.workspaceId}&page=${pnum}&size=${size}">${pnum}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <a class="page-btn ${!hasNext ? 'disabled' : ''}"
                   href="${pageContext.request.contextPath}/workspace/notice.jsp?workspaceId=${param.workspaceId}&page=${hasNext ? nextPage : page}&size=${size}"
                   aria-label="다음">&rsaquo;</a>

                <a class="page-btn ${!hasNext ?  'disabled' : ''}"
                   href="${pageContext.request.contextPath}/workspace/notice.jsp?workspaceId=${param.workspaceId}&page=${totalPages}&size=${size}"
                   aria-label="마지막">&raquo;</a>
            </div>
        </c:if>
    </section>

    <!-- 동기 제출용 숨은 폼 -->
    <form id="noticeCreateForm"
          action="${pageContext.request.contextPath}/front"
          key="notice"
          methodName="createNotice"
          method="post">
        <input type="hidden" id="noticeWorkspaceIdHidden" name="workspaceId"/>
        <input type="hidden" id="noticeTitleHidden" name="title"/>
        <input type="hidden" id="noticeContentHidden" name="content"/>
    </form>
    </div>

    <jsp:include page="notice-modal.jsp"/>
</main>


<script defer src="${pageContext.request.contextPath}/workspace/js/notice.js"></script>
<script defer src="${pageContext.request.contextPath}/workspace/js/noticeModal.js"></script>

</body>
</html>