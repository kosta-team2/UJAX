<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko" data-theme="dark">
<head>
    <meta charset="UTF-8"/>
    <title>공지 리스트</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/notice.css"/>

</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/mock">
    <jsp:param name="key" value="notice"/>
    <jsp:param name="methodName" value="getNotices"/>
    <jsp:param name="wsId" value="${param.wsId}"/>
    <jsp:param name="page" value="${empty param.page ? 1 : param.page}"/>
    <jsp:param name="size" value="${empty param.size ? 6 : param.size}"/>
</jsp:include>

<main class="main-content">
    <section class="section notice-list-section">

        <div class="notice-header">
            <h3>공지 사항</h3>
            <button class="register-btn" type="button" id="openEditorBtn">공지 등록</button>
        </div>

        <hr class="section-divider"/>

        <div class="notice-controls">
            <div class="search-sort">
                <form method="get" action="${pageContext.request.contextPath}/workspace/notice.jsp">
                    <input type="hidden" name="wsId" value="${param.wsId}"/>
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

        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <a class="page-btn ${!hasPrev ? 'disabled' : ''}"
                   href="${pageContext.request.contextPath}/workspace/notice.jsp?wsId=${param.wsId}&page=1&size=${size}"
                   aria-label="첫 페이지">&laquo;</a>

                <a class="page-btn ${!hasPrev ? 'disabled' : ''}"
                   href="${pageContext.request.contextPath}/workspace/notice.jsp?wsId=${param.wsId}&page=${hasPrev ? prevPage : page}&size=${size}"
                   aria-label="이전">&lsaquo;</a>

                <c:forEach var="pnum" begin="${startPage}" end="${endPage}">
                    <c:choose>
                        <c:when test="${pnum == page}">
                            <span class="page-btn current">${pnum}</span>
                        </c:when>
                        <c:otherwise>
                            <a class="page-btn"
                               href="${pageContext.request.contextPath}/workspace/notice.jsp?wsId=${param.wsId}&page=${pnum}&size=${size}">${pnum}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <a class="page-btn ${!hasNext ? 'disabled' : ''}"
                   href="${pageContext.request.contextPath}/workspace/notice.jsp?wsId=${param.wsId}&page=${hasNext ? nextPage : page}&size=${size}"
                   aria-label="다음">&rsaquo;</a>

                <a class="page-btn ${!hasNext ? 'disabled' : ''}"
                   href="${pageContext.request.contextPath}/workspace/notice.jsp?wsId=${param.wsId}&page=${totalPages}&size=${size}"
                   aria-label="마지막">&raquo;</a>
            </div>
        </c:if>
    </section>
</main>

<div id="noticeModal" class="ws-modal" style="display:none;">
    <div class="ws-dialog" role="dialog" aria-modal="true" aria-labelledby="modalTitle">
        <button type="button" class="modal-close" id="modalClose" title="닫기">×</button>

        <div id="viewSection">
            <h3 id="modalTitle" class="m-title"></h3>
            <div id="modalContent" class="m-body"></div>
        </div>

        <div id="editSection" style="display:none; margin-top:12px;">
            <input type="text" id="noticeTitleInput" placeholder="제목 입력" style="width:100%; margin-bottom:8px;"/>
            <textarea id="noticeContentInput" rows="6" placeholder="내용 입력" style="width:100%;"></textarea>
            <div class="modal-actions" style="display:flex; justify-content:flex-end; gap:.5rem; margin-top:.75rem;">
                <button class="btn" id="saveNoticeBtn" type="button">등록</button>
            </div>
        </div>
    </div>
</div>

<script defer src="js/notice.js"></script>

</body>
</html>