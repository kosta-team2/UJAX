<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/mypage.css">

<div class="page" id="mypage-root">
    <section class="card" id="mypage-card">
        <div class="card-header">
            <h1 class="title">내 프로필 (My Page)</h1>
            <div class="actions">
                <a class="btn btn-success-outline"
                   href="${pageContext.request.contextPath}/workspace/create.jsp">워크스페이스 생성</a>
                <button class="btn btn-ghost" id="editBtn">개인정보 변경</button>
                <button class="btn btn-danger-outline" id="deleteBtn">회원 탈퇴</button>
            </div>
        </div>

        <div class="stack-16">
            <section class="card" aria-labelledby="infoTitle">
                <div class="card-header">
                    <div class="card-title" id="infoTitle">내 정보 상세</div>
                    <span class="pill" id="levelPill">
                    LV.
                    <c:choose>
                        <c:when test="${not empty userInfo.xp}">
                            <c:out value="${userInfo.xp / 100}"/>
                        </c:when>
                        <c:otherwise>30</c:otherwise>
                    </c:choose>
                </span>
                </div>

                <div class="kv">
                    <div class="k">닉네임</div>
                    <div id="nickname"><strong><c:out value="${userInfo.nickname}"/></strong></div>

                    <div class="k">이메일</div>
                    <div id="email"><c:out value="${userInfo.email}"/></div>

                    <div class="k">리워드</div>
                    <div><strong id="reward"><c:out value="${userInfo.reward}"/>원</strong></div>

                    <div class="k">경험치</div>
                    <div>
                        <span id="exp"><c:out value="${userInfo.xp}"/>xp</span>
                    </div>
                </div>
            </section>
        </div>
    </section>
</div>

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

<script defer src="${pageContext.request.contextPath}/workspace/js/mypage.js?v=${System.currentTimeMillis()}"></script>
