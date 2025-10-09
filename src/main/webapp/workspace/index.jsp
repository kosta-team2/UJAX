<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KOSTA - Workspace</title>
    <link rel="stylesheet" href="../common/css/darkmode.css">
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="css/noticeModal.css">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="main-container">
    <jsp:include page="left-sidebar.jsp"/>

    <main class="main-content" id="mainContent">
        <jsp:include page="main.jsp" />
    </main>

    <jsp:include page="right-sidebar.jsp"/>
</div>

<%--공지 modal창--%>
<div id="noticeModal" class="notice-modal" style="display:none;">
    <div class="modal-content">
        <span class="modal-close">&times;</span>
        <h3 id="modalTitle"></h3>
        <p id="modalContent"></p>

        <div class="modal-actions">
            <button class="notice-delete-btn">삭제</button>
        </div>
    </div>
</div>

<script src="js/script.js" defer></script>
<script src="js/leftSidebar.js" defer></script>
<script src="js/rightSidebar.js" defer></script>
</body>
</html>
