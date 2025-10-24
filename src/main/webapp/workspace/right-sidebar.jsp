<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/rightSidebar.css">

<c:if test="${not empty sessionScope.SessionUser}">
    <jsp:include page="/front">
        <jsp:param name="key" value="member"/>
        <jsp:param name="methodName" value="getSidebar"/>
    </jsp:include>
</c:if>


<aside class="right-sidebar">
    <div class="profile-box" id="profileBox" page="mypage">
        <a href="${pageContext.request.contextPath}/front?key=member&methodName=getInfo" target="mainFrame">
            <div class="profile-header">
                <div class="nickname">현재 로그인 계정 : <c:out value="${userInfo.nickname}"/></div>
            </div>
            <div class="level">
                EXP : <c:out value="${userInfo.xp}"/>
            </div>
        </a>
    </div>
</aside>