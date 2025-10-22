<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KOSTA - Workspace</title>

    <link rel="stylesheet" id="theme-style" href="../common/css/darkmode.css">
    <link rel="stylesheet" href="css/index.css">
</head>

<body>
<jsp:include page="header.jsp"/>

<div class="main-container">
    <jsp:include page="left-sidebar.jsp"/>

    <jsp:include page="/mock">
        <jsp:param name="key" value="workspace" />
        <jsp:param name="methodName" value="getList" />
    </jsp:include>

    <main class="main-content">
        <iframe id="mainFrame"
                name="mainFrame"
                src="${pageContext.request.contextPath}/workspace/create.jsp"
                style="width:100%; height:100%; border:none;"
        ></iframe>
    </main>

    <jsp:include page="right-sidebar.jsp"/>
</div>

<script defer src="js/header.js"></script>
<script defer src="js/leftSidebar.js"></script>
<script defer src="js/index-boot.js"></script>
<script defer src="js/rightSidebar.js"></script>

</body>
</html>
