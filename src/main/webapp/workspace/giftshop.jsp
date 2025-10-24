<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/giftshop.css"/>

<main class="giftshop">
    <header class="topbar">
        <h1 class="page-title">기프티콘 샵</h1>
    </header>

    <section class="grid" aria-live="polite">
        <c:choose>
            <c:when test="${empty products}">
                <p style="color:var(--muted)">표시할 상품이 없습니다.</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${products}" var="p">
                    <a class="card"
                       href="${pageContext.request.contextPath}/front?key=giftshop&methodName=showGift&productId=${p.productId}">
                        <div class="thumb">
                            <c:if test="${empty p.productImage}">
                                <span class="ph">이미지를 불러오지 못 했습니다.</span>
                            </c:if>
                            <c:if test="${not empty p.productImage}">
                                <img src="${p.productImage}" alt="${p.productName}">
                            </c:if>
                        </div>
                        <h3 class="name"><c:out value="${p.productName}"/></h3>
                        <div class="price-row">
                            <div class="price">
                                <fmt:formatNumber value="${p.productPrice}" type="number" groupingUsed="true"/>원
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- 페이지네이션 -->
    <%--    페이지네이션    --%>
    <c:if test="${totalPages > 1}">
        <div class="pagination">
                <%-- 첫 페이지 --%>
            <c:url var="firstUrl" value="${pageContext.request.contextPath}/front">
                <c:param name="key" value="giftshop"/>
                <c:param name="methodName" value="showGiftPage"/>
                <c:param name="page" value="1"/>
                <c:param name="size" value="${size}"/>
            </c:url>
            <a class="page-btn ${!hasPrev ? 'disabled' : ''}" href="${firstUrl}" aria-label="첫 페이지">&laquo;</a>

                <%-- 이전 --%>
            <c:url var="prevUrl" value="${pageContext.request.contextPath}/front">
                <c:param name="key" value="giftshop"/>
                <c:param name="methodName" value="showGiftPage"/>
                <c:param name="page" value="${hasPrev ? prevPage : page}"/>
                <c:param name="size" value="${size}"/>
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
                            <c:param name="key" value="giftshop"/>
                            <c:param name="methodName" value="showGiftPage"/>
                            <c:param name="page" value="${pnum}"/>
                            <c:param name="size" value="${size}"/>
                        </c:url>
                        <a class="page-btn" href="${numUrl}">${pnum}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

                <%-- 다음 --%>
            <c:url var="nextUrl" value="${pageContext.request.contextPath}/front">
                <c:param name="key" value="giftshop"/>
                <c:param name="methodName" value="showGiftPage"/>
                <c:param name="page" value="${hasNext ? nextPage : page}"/>
                <c:param name="size" value="${size}"/>
            </c:url>
            <a class="page-btn ${!hasNext ? 'disabled' : ''}" href="${nextUrl}" aria-label="다음">&rsaquo;</a>

                <%-- 마지막 --%>
            <c:url var="lastUrl" value="${pageContext.request.contextPath}/front">
                <c:param name="key" value="giftshop"/>
                <c:param name="methodName" value="showGiftPage"/>
                <c:param name="page" value="${totalPages}"/>
                <c:param name="size" value="${size}"/>
            </c:url>
            <a class="page-btn ${!hasNext ? 'disabled' : ''}" href="${lastUrl}" aria-label="마지막">&raquo;</a>
        </div>
    </c:if>
</main>
