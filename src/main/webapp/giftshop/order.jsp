<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/giftshop/css/order.css"/>

<main class="container">
    <section class="card success">
        <h1>결제가 완료되었습니다</h1>
        <p><strong>이메일로 기프티콘을 발송했습니다.</strong></p>
        <c:if test="${not empty orderId}">
            <p>주문번호: <strong>${orderId}</strong></p>
        </c:if>
        <p>결제 금액: <strong><fmt:formatNumber value="${productPrice}" type="number" groupingUsed="true"/>원</strong></p>
        <c:if test="${not empty remain}">
            <p>남은 리워드: <strong><fmt:formatNumber value="${remain}" type="number" groupingUsed="true"/>원</strong>
            </p>
        </c:if>

        <div class="actions">
            <!-- 목록 페이지로 -->
            <a class="btn btn-primary" target="mainFrame"
               href="${pageContext.request.contextPath}/front?key=giftshop&methodName=showGiftPage&size=8&page=1">목록으로</a>
        </div>
    </section>
</main>
