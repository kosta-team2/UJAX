<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/giftshop.css"/>

<jsp:include page="${pageContext.request.contextPath}/mock">
    <jsp:param name="key" value="giftshop"/>
    <jsp:param name="methodName" value="list"/>
    <jsp:param name="page" value="${empty param.page ? 1 : param.page}"/>
    <jsp:param name="size" value="${empty param.size ? 6 : param.size}"/>
</jsp:include>

<main class="giftshop">
    <header class="topbar">
        <h1 class="page-title">기프티콘 샵</h1>
    </header>

    <section class="grid" aria-live="polite">
        <c:choose>
            <c:when test="${empty products}">
                <p class="state">표시할 상품이 없습니다.</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${products}" var="p">
                    <a class="card"
                       href="${pageContext.request.contextPath}/workspace/giftshop-detail.jsp?productId=${p.productId}">
                        <div class="thumb">
                            <c:if test="${empty p.img}">
                                <span class="ph">이미지 1:1 영역</span>
                            </c:if>
                            <c:if test="${not empty p.img}">
                                <img src="${p.img}" alt="${p.name}">
                            </c:if>
                        </div>
                        <div class="brand"><c:out value="${p.brand}"/></div>
                        <h3 class="name"><c:out value="${p.name}"/></h3>
                        <div class="price-row">
                            <div class="price">
                                <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>원
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- 페이지네이션 -->
    <c:if test="${totalPages > 1}">
        <nav class="pagination" aria-label="페이지 내비게이션">
            <!-- 처음/이전 -->
            <a class="page-btn ${!hasPrev ? 'is-disabled' : ''}"
               href="${pageContext.request.contextPath}/workspace/giftshop.jsp?page=1&size=${size}"
               aria-label="첫 페이지">&laquo;</a>
            <a class="page-btn ${!hasPrev ? 'is-disabled' : ''}"
               href="${pageContext.request.contextPath}/workspace/giftshop.jsp?page=${hasPrev ? prevPage : page}&size=${size}"
               aria-label="이전 페이지">&lsaquo;</a>

            <ol class="page-list">
                <c:forEach var="pnum" begin="${startPage}" end="${endPage}">
                    <li>
                        <c:choose>
                            <c:when test="${pnum == page}">
                                <span class="page is-active">${pnum}</span>
                            </c:when>
                            <c:otherwise>
                                <a class="page"
                                   href="${pageContext.request.contextPath}/workspace/giftshop.jsp?page=${pnum}&size=${size}">${pnum}</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
            </ol>

            <!-- 다음/마지막 -->
            <a class="page-btn ${!hasNext ? 'is-disabled' : ''}"
               href="${pageContext.request.contextPath}/workspace/giftshop.jsp?page=${hasNext ? nextPage : page}&size=${size}"
               aria-label="다음 페이지">&rsaquo;</a>
            <a class="page-btn ${!hasNext ? 'is-disabled' : ''}"
               href="${pageContext.request.contextPath}/workspace/giftshop.jsp?page=${totalPages}&size=${size}"
               aria-label="마지막 페이지">&raquo;</a>
        </nav>
    </c:if>
</main>
