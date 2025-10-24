<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/giftshop-detail.css"/>

<div id="mainContent">

    <section class="order-detail" data-product-id="${productId}">
        <header class="topbar">
            <h1 class="title">상세 주문 페이지</h1>
        </header>

        <article class="card" role="article" aria-label="상품 상세">

            <div class="thumb">
                <c:choose>
                    <c:when test="${not empty productImage}">
                        <img
                                src="<c:url value='/giftshop/thumbnail'>
         <c:param name='id' value='${imageId}'/>
       </c:url>"
                                alt="${productName}"
                                loading="lazy"/>
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
                <button class="btn-pay" id="payBtn" aria-haspopup="dialog" aria-controls="confirmModal">결제하기</button>
                <div class="chip" data-field="chip" hidden>인기 상품</div>
            </div>
        </article>

        <%--todo 결제 시스템--%>
        <%--        <!-- 결제 창 -->--%>
        <%--        <div class="modal" id="confirmModal" role="dialog" aria-modal="true" aria-hidden="true"--%>
        <%--             aria-labelledby="confirmTitle">--%>
        <%--            <div class="dialog" role="document">--%>
        <%--                <div class="dialog-head">--%>
        <%--                    <svg class="icon" viewBox="0 0 24 24" aria-hidden="true">--%>
        <%--                        <rect x="2" y="6" width="20" height="12" rx="2"/>--%>
        <%--                        <path d="M2 10h20"/>--%>
        <%--                    </svg>--%>
        <%--                    <h2 class="dialog-title" id="confirmTitle">리워드로 결제</h2>--%>
        <%--                </div>--%>
        <%--                <div class="dialog-body" id="confirmMsg"></div>--%>
        <%--                <div class="dialog-actions">--%>
        <%--                    <button class="btn btn-ghost" data-close>취소</button>--%>
        <%--                    <button class="btn btn-primary" id="confirmPayBtn">결제하기</button>--%>
        <%--                </div>--%>
        <%--            </div>--%>
        <%--        </div>--%>

        <%--        <!-- 잔액 부족 -->--%>
        <%--        <div class="modal" id="insufModal" role="dialog" aria-modal="true" aria-hidden="true"--%>
        <%--             aria-labelledby="insufTitle">--%>
        <%--            <div class="dialog" role="document">--%>
        <%--                <div class="dialog-head">--%>
        <%--                    <svg class="icon warn" viewBox="0 0 24 24" aria-hidden="true">--%>
        <%--                        <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>--%>
        <%--                        <line x1="12" y1="9" x2="12" y2="13"/>--%>
        <%--                        <line x1="12" y1="17" x2="12.01" y2="17"/>--%>
        <%--                    </svg>--%>
        <%--                    <h2 class="dialog-title" id="insufTitle">리워드가 부족합니다</h2>--%>
        <%--                </div>--%>
        <%--                <div class="dialog-body" id="insufMsg"></div>--%>
        <%--                <div class="dialog-actions">--%>
        <%--                    <button class="btn btn-primary" data-close>확인</button>--%>
        <%--                </div>--%>
        <%--            </div>--%>
        <%--        </div>--%>

        <%--        <!-- 결제 성공 창 -->--%>
        <%--        <div class="modal" id="successModal" role="dialog" aria-modal="true" aria-hidden="true"--%>
        <%--             aria-labelledby="payTitle">--%>
        <%--            <div class="dialog" role="document">--%>
        <%--                <div class="dialog-head">--%>
        <%--                    <svg class="icon" viewBox="0 0 24 24" aria-hidden="true">--%>
        <%--                        <path d="M20 6L9 17l-5-5"/>--%>
        <%--                    </svg>--%>
        <%--                    <h2 class="dialog-title" id="payTitle">결제가 완료되었습니다</h2>--%>
        <%--                </div>--%>
        <%--                <div class="dialog-body" id="successMsg"></div>--%>
        <%--                <div class="dialog-actions">--%>
        <%--                    <button class="btn btn-primary" data-close>확인</button>--%>
        <%--                </div>--%>
        <%--            </div>--%>
        <%--        </div>--%>
    </section>

</div>

<%--<script defer src="${pageContext.request.contextPath}/workspace/js/giftshop-detail.js"></script>--%>
