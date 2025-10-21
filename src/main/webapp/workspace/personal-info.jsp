<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<section class="pi-page">
    <div class="pi-top">
        <button class="pi-back" id="pi-backBtn" aria-label="뒤로 가기">←</button>
    </div>

    <h1 class="pi-title">개인정보 변경</h1>

    <form id="pi-form" method="POST" action="${pageContext.request.contextPath}/front" class="pi-card">
        <input type="hidden" name="key" value="user">
        <input type="hidden" name="methodName" value="updateUser">

        <!-- 현재 비밀번호 -->
        <div class="pi-field">
            <label for="currentPw">현재 비밀번호</label>
            <input type="password" id="currentPw" name="currentPw" placeholder="현재 비밀번호를 입력하세요" required>
            <p class="pi-hint">본인 확인을 위해 현재 사용 중인 비밀번호를 입력해주세요.</p>
        </div>

        <!-- 새 비밀번호 -->
        <div class="pi-field">
            <label for="newPw">새 비밀번호</label>
            <input type="password" id="newPw" name="newPw" placeholder="새 비밀번호를 입력하세요" required>
        </div>

        <!-- 새 비밀번호 확인 -->
        <div class="pi-field">
            <label for="confirmPw">새 비밀번호 확인</label>
            <input type="password" id="confirmPw" placeholder="새 비밀번호를 다시 입력하세요" required>
        </div>

        <!-- 닉네임 -->
        <div class="pi-field">
            <label for="newNickname">닉네임</label>
            <input type="text" id="newNickname" name="newNickname" placeholder="변경할 닉네임을 입력하세요" required>
        </div>

        <div class="pi-actions">
            <button type="submit" class="pi-btn">변경하기</button>
        </div>
    </form>
</section>

<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/personal-info.css">
<script defer src="${pageContext.request.contextPath}/workspace/js/personal-info.js"></script>
