<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/giftshop/css/giftshop-detail.css"/>

<div id="mainContent">

    <section class="order-detail" data-product-id="${productId}">
        <header class="topbar">
            <h1 class="title">상세 주문 페이지</h1>
            <a class="btn btn-primary" target="mainFrame"
               href="${pageContext.request.contextPath}/front?key=giftshop&methodName=showGiftPage&size=8&page=1">목록으로</a>
        </header>

        <article class="card" role="article" aria-label="상품 상세">

            <div class="thumb">
                <c:choose>
                    <c:when test="${not empty productImage}">
                        <img src="${productImage}" alt="${productName}"/>
                    </c:when>
                    <c:otherwise>
                        <div class="ph">이미지를 불러오지 못했습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="info">
                <h2 class="name"><c:out value="${productName}"/></h2>
                <div class="price-row">
                    <fmt:formatNumber value="${productPrice}" type="number" groupingUsed="true"/>원
                </div>

                <form id="orderForm" method="post"
                      action="${pageContext.request.contextPath}/front?key=giftshop&methodName=checkMemberReward">
                    <input type="hidden" name="productId" value="${productId}"/>
                    <input type="hidden" name="productPrice" value="${productPrice}"/>
                    <div class="actions">
                        <button type="submit" class="btn-pay">결제하기</button>
                    </div>
                </form>

            </div>
        </article>
    </section>

</div>
