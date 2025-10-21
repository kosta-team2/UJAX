<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet" href="css/mypage.css"> <!-- Test용임, 이후 통합 환경에서 사용할때는 제거해도 됨 -->
<div class="page" id="mypage-root">
    <div class="page-head">
        <h1 class="title">내 프로필 (My Page)</h1>
        <div class="actions">
            <button class="btn btn-ghost" id="editBtn">개인정보 변경</button>
            <button class="btn btn-danger-outline" id="deleteBtn">회원 탈퇴</button>
        </div>
    </div>

    <div class="stack-16">
        <!-- 내 정보 상세 -->
        <section class="card" aria-labelledby="infoTitle">
            <div class="card-header">
                <div class="card-title" id="infoTitle">내 정보 상세</div>
                <span class="pill" id="levelPill">
                    LV.
                    <c:choose>
                        <c:when test="${not empty userInfo.xp}">
                            <c:out value="${userInfo.xp / 100}" />
                        </c:when>
                        <c:otherwise>30</c:otherwise>
                    </c:choose>
                </span>
            </div>

            <div class="kv">
                <div class="k">닉네임</div>
                <div id="nickname"><strong><c:out value="${userInfo.nickname}" default="지눅왕" /></strong></div>

                <div class="k">이메일</div>
                <div id="email"><c:out value="${userInfo.email}" default="example.com" /></div>

                <div class="k">리워드</div>
                <div><strong id="reward"><c:out value="${userInfo.reward}" default="1000" />원</strong></div>

                <div class="k">경험치</div>
                <div>
                    <span id="exp"><c:out value="${userInfo.xp}" default="0" />xp</span>
                </div>
            </div>
        </section>
    </div>
</div>

<!-- Confirm Modal -->
<div class="modal" id="confirmModal" aria-hidden="true" role="dialog" aria-modal="true" aria-labelledby="confirmTitle">
    <div class="dialog">
        <h3 class="card-title" id="confirmTitle">정말로 탈퇴하시겠습니까?</h3>
        <p class="desc">회원탈퇴 시 모든 데이터가 삭제될 수 있습니다.</p>
        <div class="actions" style="justify-content:flex-end">
            <button class="btn btn-danger-outline" id="okConfirm">확인</button>
            <button class="btn btn-ghost" id="cancelConfirm">취소</button>
        </div>
    </div>
</div>

<!-- Result Modal -->
<div class="modal" id="resultModal" aria-hidden="true" role="dialog" aria-modal="true" aria-labelledby="resultTitle">
    <div class="dialog">
        <h3 class="card-title" id="resultTitle">정상적으로 탈퇴 처리되었습니다.</h3>
        <div class="actions" style="justify-content:flex-end;margin-top:8px">
            <button class="btn btn-ghost" id="closeResult">확인</button>
        </div>
    </div>
</div>

<!-- Page Alert -->
<div class="page-alert" id="pageAlert" role="alert">
    <span class="msg">정상적으로 탈퇴 처리되었습니다.</span>
    <button class="close" id="closeAlert" aria-label="알림 닫기">×</button>
</div>

<script defer src="${pageContext.request.contextPath}/workspace/js/mypage.js"></script>
