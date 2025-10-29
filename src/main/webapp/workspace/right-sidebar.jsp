<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/rightSidebar.css">

<c:if test="${not empty sessionScope.SessionUser}">
    <jsp:include page="/front">
        <jsp:param name="key" value="member"/>
        <jsp:param name="methodName" value="getSidebar"/>
    </jsp:include>
</c:if>

<c:set var="xp" value="${empty userInfo.xp ? 0 : userInfo.xp}" />
<c:set var="level" value="${xp / 100}" />
<c:set var="cap" value="${100}" />
<c:set var="progress" value="${xp % 100}" />
<c:set var="remain" value="${cap - progress}" />
<c:set var="percent" value="${(progress * 100) / cap}" />

<aside class="right-sidebar">
    <div class="profile-box" id="profileBox" page="mypage">
        <a href="${pageContext.request.contextPath}/front?key=member&methodName=getInfo" target="mainFrame">
            <div class="profile-header">
                <div class="nickname"><c:out value="${userInfo.nickname}"/> 님 오늘도 화이팅!</div>
            </div>

            <div class="level">
                LV.<fmt:formatNumber value="${level}" maxFractionDigits="0"/>
            </div>

            <div class="progress">
                <div class="bar" style="width:${percent}%"></div>
            </div>

            <div class="stats">
                <span><c:out value="${progress}"/> / <c:out value="${cap}"/></span>
                <span> (<c:out value="${percent}"/>%) </span>
            </div>

            <div class="stats">
                <span>다음 레벨까지 <strong><c:out value="${remain}"/>xp</strong> 남음</span>
            </div>
        </a>
    </div>

    <!-- 광고 영역 (심사 중 표시용) -->
    <div class="ads-review-box">
        <div class="ads-label">광고</div>
        <div class="ads-review-content">
            <p>Google Ads 심사 중입니다.</p>
            <p class="ads-sub">곧 광고가 표시될 예정이에요.</p>
        </div>
    </div>
</aside>