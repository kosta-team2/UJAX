<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KOSTA - Workspace</title>
    <link rel="stylesheet" href="css/base.css">
    <link rel="stylesheet" href="css/header.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/home.css">
    <link rel="stylesheet" href="css/modal.css">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="main-container">
    <jsp:include page="left-sidebar.jsp"/>
<%-- todo 메뉴 버튼에 따라 메인 컨텐츠가 변경되게 수정    --%>
    <main class="main-content" id="mainContent">
        <jsp:include page="home.jsp" />
    </main>
    <jsp:include page="right-sidebar.jsp"/>
</div>
<%--공지 modal창--%>
<div id="noticeModal" class="modal" style="display:none;">
    <div class="modal-content">
        <span class="modal-close">&times;</span>
        <h3 id="modalTitle"></h3>
        <p id="modalContent"></p>
    </div>
</div>

<script src="script.js" defer></script>
</body>
</html>
