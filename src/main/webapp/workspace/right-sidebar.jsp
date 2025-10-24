<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<link rel="stylesheet" href="../common/css/darkmode.css"/>
<link rel="stylesheet" href="css/rightSidebar.css">

<jsp:include page="${pageContext.request.contextPath}/mock">
    <jsp:param name="key" value="rightSidebar"/>
    <jsp:param name="methodName" value="view"/>
</jsp:include>


<aside class="right-sidebar">
    <div class="profile-box" id="profileBox" page="mypage">
        <a href="${pageContext.request.contextPath}/workspace/mypage.jsp" target="mainFrame">
            <div class="profile-header">
                <div class="nickname"><c:out value="${profile.nickname}"/></div>
            </div>
            <div class="level">
                LV.<c:out value="${profile.level}"/>
                · EXP <c:out value="${profile.exp}"/> / <c:out value="${profile.expMax}"/>
            </div>
            <div class="progress">
                <div class="progress">
                    <div class="bar" style="width: <c:out value='${profile.expPercent}'/>%;"></div>
                </div>
                <div class="stats">정답률 <c:out value="${profile.accuracy}"/>%</div>
            </div>
        </a>
    </div>
</aside>


<script defer src="js/notice.js"></script>