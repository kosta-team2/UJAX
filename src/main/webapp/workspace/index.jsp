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

<jsp:include page="notice-modal.jsp"/>

<!-- index.jsp 하단 (</body> 직전), 모든 js를 한 번만 로드 -->
<script defer src="js/header.js"></script>
<script defer src="js/leftSidebar.js"></script>
<script defer src="js/rightSidebar.js"></script>

<!-- 페이지/컴포넌트 모듈(자동 실행 금지! init 함수만 등록) -->

<script defer src="js/noticeModal.js"></script>
<script defer src="js/homeNotice.js"></script>
<script defer src="js/teamChart.js"></script>
<script defer src="js/homeProblem.js"></script>
<script defer src="js/notice.js"></script>
<script defer src="js/problem.js"></script>
<script defer src="js/problem-register.js"></script>
<script defer src="js/info.js"></script>
<script defer src="js/mypage.js"></script>

<!-- 라우터/오케스트레이터: reload(page) 정의 -->
<script defer src="js/script.js"></script>
</body>
</html>
