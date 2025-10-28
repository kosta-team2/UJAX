<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/mypage.css?v=${System.currentTimeMillis()}">

<c:set var="xp" value="${empty userInfo.xp ? 0 : userInfo.xp}"/>
<c:set var="level" value="${xp / 100}"/>
<c:set var="cap" value="${100}"/>
<c:set var="progress" value="${xp % 100}"/>
<c:set var="remain" value="${cap - progress}"/>
<c:set var="percent" value="${(progress * 100) / cap}"/>

<div class="page" id="mypage-root">
    <section class="card" id="mypage-card">
        <div class="card-header">
            <h1 class="title">내 프로필 (My Page)</h1>
            <div class="actions">
                <button class="btn btn-success-outline" id="createBtn">워크스페이스 생성</button>
                <button class="btn btn-ghost" id="editBtn">개인정보 변경</button>
                <button class="btn btn-danger-outline" id="deleteBtn">회원 탈퇴</button>
            </div>
        </div>

        <div class="stack-16">
            <section class="card" aria-labelledby="infoTitle">
                <div class="card-header">
                    <div class="card-title" id="infoTitle">내 정보 상세</div>
                    <span class="pill" id="levelPill">LV.<fmt:formatNumber value="${level}" maxFractionDigits="0"/></span>
                </div>

                <div class="kv">
                    <div class="k">닉네임</div>
                    <div id="nickname"><strong><c:out value="${userInfo.nickname}"/></strong></div>

                    <div class="k">이메일</div>
                    <div id="email"><c:out value="${userInfo.email}"/></div>

                    <div class="k">리워드</div>
                    <div><strong id="reward"><c:out value="${userInfo.reward}"/>원</strong></div>

                    <div class="k">총 경험치</div>
                    <div><span id="exp"><c:out value="${xp}"/>xp</span></div>
                </div>
            </section>

            <section class="card progress" aria-labelledby="xpTitle">
                <div class="card-title" id="xpTitle">내 경험치 현황</div>
                <div class="bar"><span style="width:${percent}%"></span></div>
                <div class="legend">
                    <span><c:out value="${progress}"/> / <c:out value="${cap}"/></span>
                    <span><c:out value="${percent}"/>%</span>
                </div>
                <div class="legend">
                    <span>다음 레벨까지 <strong><c:out value="${remain}"/>xp</strong> 남음</span>
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
