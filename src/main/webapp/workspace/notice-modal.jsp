<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div id="noticeModal" class="notice-modal" style="display:none;">
    <div class="modal-content" role="dialog" aria-modal="true" aria-labelledby="modalTitle">

        <!-- 보기 모드 -->
        <div id="viewSection">
            <h3 id="modalTitle" class="m-title"></h3>
            <div id="modalContent" class="m-body"></div>
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

<form id="noticeCreateForm"
      method="post"
      action="${pageContext.request.contextPath}/front?key=notice&methodName=createNotice"
      style="display:none;">
    <input type="hidden" name="wsId" value="${param.wsId}"/>
    <input type="hidden" name="noticeTitle"/>
    <input type="hidden" name="noticeContent"/>
</form>