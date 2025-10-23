<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/leftSidebar.css">

<jsp:include page="/mock">
    <jsp:param name="key" value="workspace"/>
    <jsp:param name="methodName" value="getList"/>
</jsp:include>

<aside class="left-sidebar">
    <nav class="nav-links">

        <div class="workspace-list">
            <c:choose>
                <c:when test="${not empty workspaces}">
                    <c:forEach items="${workspaces}" var="ws">
                        <div class="workspace-toggle" data-workspace="${ws.id}">
                            <button class="workspace-name" type="button">${ws.name}</button>
                            <div class="workspace-menu">
                                <a class="nav-btn" target="mainFrame"
                                   href="${pageContext.request.contextPath}/workspace/home.jsp?wsId=${ws.id}">
                                    워크스페이스 홈
                                </a>
                                <c:url var="noticeUrl" value="${pageContext.request.contextPath}/front">
                                    <c:param name="key" value="notice"/>
                                    <c:param name="methodName" value="getNotices"/>
                                    <c:param name="wsId" value="${ws.id}"/> <!-- 여기가 핵심 -->
                                    <c:param name="page" value="1"/>
                                    <c:param name="size" value="12"/>
                                    <c:param name="sort" value="latest"/>
                                </c:url>

                                <a class="nav-btn" target="mainFrame" href="${noticeUrl}">공지 전체 보기</a>
                                <a class="nav-btn" target="mainFrame"
                                   href="${pageContext.request.contextPath}/workspace/problem.jsp?wsId=${ws.id}">
                                    문제 전체 보기
                                </a>
                                <a class="nav-btn" target="mainFrame"
                                   href="${pageContext.request.contextPath}/workspace/info.jsp?wsId=${ws.id}">
                                    워크스페이스 관리
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </div>

        <hr class="sidebar-divider">

        <div class="workspace-toggle">
            <a class="workspace-name" target="mainFrame"
               href="${pageContext.request.contextPath}/workspace/giftshop.jsp">
                기프티콘 샵
            </a>
        </div>
    </nav>
</aside>