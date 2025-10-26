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
            <c:if test="${param.isLeader == 'true' or param.isLeader == '1'}">
                <button class="register-btn" type="button" id="openEditorBtn">공지 등록</button>
            </c:if>
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
                        <a href="#"
                           class="notice-card"
                           data-isLeader="${param.isLeader}}"
                           data-notice-id="${n.noticeId}"
                           data-title="<c:out value='${not empty n.title ? n.title.value : ""}'/>"
                           data-content="<c:out value='${not empty n.content ? n.content.value : ""}'/>">
                            <strong><c:out value='${not empty n.title ? n.title.value : ""}'/></strong>
                            <p class="preview"><c:out value='${not empty n.content ? n.content.value : ""}'/></p>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <%--    페이지네이션    --%>
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                    <%-- 첫 페이지 --%>
                <c:url var="firstUrl" value="${pageContext.request.contextPath}/front">
                    <c:param name="key" value="notice"/>
                    <c:param name="methodName" value="getNotices"/>
                    <c:param name="wsId" value="${wsId}"/>
                    <c:param name="page" value="1"/>
                    <c:param name="size" value="${size}"/>
                    <c:param name="sort" value="${sort}"/>
                </c:url>
                <a class="page-btn ${!hasPrev ? 'disabled' : ''}" href="${firstUrl}" aria-label="첫 페이지">&laquo;</a>

                    <%-- 이전 --%>
                <c:url var="prevUrl" value="${pageContext.request.contextPath}/front">
                    <c:param name="key" value="notice"/>
                    <c:param name="methodName" value="getNotices"/>
                    <c:param name="wsId" value="${wsId}"/>
                    <c:param name="page" value="${hasPrev ? prevPage : page}"/>
                    <c:param name="size" value="${size}"/>
                    <c:param name="sort" value="${sort}"/>
                </c:url>
                <a class="page-btn ${!hasPrev ? 'disabled' : ''}" href="${prevUrl}" aria-label="이전">&lsaquo;</a>

                    <%-- 숫자 버튼 --%>
                <c:forEach var="pnum" begin="${startPage}" end="${endPage}">
                    <c:choose>
                        <c:when test="${pnum == page}">
                            <span class="page-btn current">${pnum}</span>
                        </c:when>
                        <c:otherwise>
                            <c:url var="numUrl" value="${pageContext.request.contextPath}/front">
                                <c:param name="key" value="notice"/>
                                <c:param name="methodName" value="getNotices"/>
                                <c:param name="wsId" value="${wsId}"/>
                                <c:param name="page" value="${pnum}"/>
                                <c:param name="size" value="${size}"/>
                                <c:param name="sort" value="${sort}"/>
                            </c:url>
                            <a class="page-btn" href="${numUrl}">${pnum}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                    <%-- 다음 --%>
                <c:url var="nextUrl" value="${pageContext.request.contextPath}/front">
                    <c:param name="key" value="notice"/>
                    <c:param name="methodName" value="getNotices"/>
                    <c:param name="wsId" value="${wsId}"/>
                    <c:param name="page" value="${hasNext ? nextPage : page}"/>
                    <c:param name="size" value="${size}"/>
                    <c:param name="sort" value="${sort}"/>
                </c:url>
                <a class="page-btn ${!hasNext ? 'disabled' : ''}" href="${nextUrl}" aria-label="다음">&rsaquo;</a>

                    <%-- 마지막 --%>
                <c:url var="lastUrl" value="${pageContext.request.contextPath}/front">
                    <c:param name="key" value="notice"/>
                    <c:param name="methodName" value="getNotices"/>
                    <c:param name="wsId" value="${wsId}"/>
                    <c:param name="page" value="${totalPages}"/>
                    <c:param name="size" value="${size}"/>
                    <c:param name="sort" value="${sort}"/>
                </c:url>
                <a class="page-btn ${!hasNext ? 'disabled' : ''}" href="${lastUrl}" aria-label="마지막">&raquo;</a>
            </div>
        </c:if>
    </section>

    <!-- 동기 제출용 숨은 폼 -->
    <form id="noticeCreateForm"
          action="${pageContext.request.contextPath}/front"
          key="notice"
          methodName="createNotice"
          method="post">
        <input type="hidden" id="noticeWorkspaceIdHidden" name="wsId"/>
        <input type="hidden" id="noticeTitleHidden" name="title"/>
        <input type="hidden" id="noticeContentHidden" name="content"/>
    </form>
    </div>

</main>
<jsp:include page="/workspace/notice-modal.jsp"/>
<script defer src="${pageContext.request.contextPath}/workspace/js/noticeModal.js"></script>
<script defer src="${pageContext.request.contextPath}/workspace/js/notice.js"></script>

</body>
</html>