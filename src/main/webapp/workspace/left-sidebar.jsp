<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/leftSidebar.css">

<c:if test="${not empty sessionScope.SessionUser}">
    <jsp:include page="/front">
        <jsp:param name="key" value="workspace"/>
        <jsp:param name="methodName" value="getSidebar"/>
    </jsp:include>
</c:if>

<aside class="left-sidebar">
    <nav class="nav-links">

        <div class="workspace-list">
            <c:choose>
                <c:when test="${not empty workspaces}">
                    <c:forEach items="${workspaces}" var="ws">
                        <div class="workspace-toggle" data-workspace="${ws.workspaceId}">
                            <button class="workspace-name" type="button">${ws.wsName}</button>
                            <div class="workspace-menu">
                                <a class="nav-btn" target="mainFrame"
                                   href="${pageContext.request.contextPath}/workspace/home.jsp?workspaceId=${ws.workspaceId}&workspaceMemberId=${ws.workspaceMemberId}&isLeader=${ws.leader}">
                                    워크스페이스 홈
                                </a>
                                <a class="nav-btn" target="mainFrame"
                                   href="${pageContext.request.contextPath}/workspace/notice.jsp?workspaceId=${ws.workspaceId}&workspaceMemberId=${ws.workspaceMemberId}&isLeader=${ws.leader}">
                                    공지 전체 보기
                                </a>
                                <a class="nav-btn" target="mainFrame"
                                   href="${pageContext.request.contextPath}/front?key=problem&methodName=getProblems&workspaceId=${ws.workspaceId}&workspaceMemberId=${ws.workspaceMemberId}&isLeader=${ws.leader}&page=1&size=6">
                                    문제 전체 보기
                                </a>
                                <a class="nav-btn" target="mainFrame"
                                   href="${pageContext.request.contextPath}/front?key=workspace&methodName=show&workspaceId=${ws.workspaceId}">
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