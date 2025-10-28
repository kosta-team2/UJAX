<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div id="noticeModal" class="notice-modal" style="display:none;">
    <div class="modal-content" role="dialog" aria-modal="true" aria-labelledby="modalTitle">

        <!-- 보기 모드 -->
        <div id="viewSection">
            <h3 id="modalTitle" class="m-title"></h3>
            <div id="modalContent" class="m-body"></div>
            <div class="modal-actions">
                <c:if test="${param.isLeader == 'true' or param.isLeader == '1'}">
                    <button type="button" class="notice-delete-btn" id="deleteNoticeBtn">삭제</button>
                </c:if>
            </div>
        </div>

        <!-- 등록 모드 -->
        <div id="editSection" style="display:none; margin-top:12px;">
            <input type="text" id="noticeTitleInput" placeholder="제목 입력" style="width:100%; margin-bottom:8px;"/>
            <textarea id="noticeContentInput" rows="6" placeholder="내용 입력" style="width:100%;"></textarea>
            <div class="modal-actions" style="display:flex; justify-content:flex-end; gap:.5rem; margin-top:.75rem;">
                <button class="btn" id="saveNoticeBtn" type="button">등록</button>
            </div>
        </div>
    </div>
</div>

<%--공지 생성용 숨은 폼--%>
<form id="noticeCreateForm"
      method="post"
      action="${pageContext.request.contextPath}/front"
      accept-charset="UTF-8"
      style=" display:none;">
    <input type="hidden" name="key" value="notice">
    <input type="hidden" name="methodName" value="createNotice">
    <input type="hidden" name="wsId" value="${empty param.wsId ? '' : param.wsId}">
    <input type="hidden" name="noticeTitle">
    <input type="hidden" name="noticeContent">
    <input type="hidden" name="isLeader" value="${param.isLeader}">
</form>

<%--공지 삭제용 숨은 폼--%>
<form id="noticeDeleteForm"
      method="post"
      action="${pageContext.request.contextPath}/front"
      style="display:none;">
    <input type="hidden" name="key" value="notice"/>
    <input type="hidden" name="methodName" value="deleteNotice"/>
    <input type="hidden" name="wsId" value="${empty param.wsId ? '' : param.wsId}"/>
    <input type="hidden" name="noticeId"/>
    <input type="hidden" name="isLeader" value="${param.isLeader}">
</form>