<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/giftshop/css/check-order.css"/>

<main class="container">
    <h1>결제 확인</h1>

    <section class="card">
        <p>현재 리워드: <strong><fmt:formatNumber value="${userReward}" type="number" groupingUsed="true"/>원</strong></p>
        <p>결제 금액: <strong><fmt:formatNumber value="${productPrice}" type="number" groupingUsed="true"/>원</strong></p>

        <c:choose>
            <c:when test="${remain >= 0}">
                <p>결제 후 남는 리워드: <strong><fmt:formatNumber value="${remain}" type="number"
                                                          groupingUsed="true"/>원</strong></p>

                <form method="post"
                      action="${pageContext.request.contextPath}/front?key=giftshop&methodName=confirmPayment&productPrice=${productPrice}&remain=${remain}">
                    <input type="hidden" name="productId" value="${productId}"/>
                    <div class="actions">
                        <a class="btn"
                           href="${pageContext.request.contextPath}/front?key=giftshop&methodName=showGift&productId=${productId}">취소</a>
                        <button type="submit" class="btn btn-primary">결제하기</button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <p class="warn">리워드가 부족합니다. 부족 금액:
                    <strong><fmt:formatNumber value="${-remain}" type="number" groupingUsed="true"/>원</strong>
                </p>
                <div class="actions">
                    <a class="btn btn-primary"
                       href="${pageContext.request.contextPath}/front?key=giftshop&methodName=showGift&productId=${productId}">확인</a>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>