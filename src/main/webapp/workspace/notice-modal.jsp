<div id="noticeModal" class="notice-modal" style="display:none;">
    <div class="modal-content">
        <span class="modal-close">&times;</span>
        <div id="noticeModal" class="ws-modal" style="display:none;">
            <div class="ws-dialog" role="dialog" aria-modal="true" aria-labelledby="modalTitle">
                <button type="button" class="modal-close" id="modalClose" title="닫기">×</button>

                <div id="viewSection">
                    <h3 id="modalTitle" class="m-title"></h3>
                    <div id="modalContent" class="m-body"></div>
                </div>

                <div id="editSection" style="display:none; margin-top:12px;">
                    <input type="text" id="noticeTitleInput" placeholder="제목 입력"
                           style="width:100%; margin-bottom:8px;"/>
                    <textarea id="noticeContentInput" rows="6" placeholder="내용 입력" style="width:100%;"></textarea>
                    <div class="modal-actions"
                         style="display:flex; justify-content:flex-end; gap:.5rem; margin-top:.75rem;">
                        <button class="btn" id="saveNoticeBtn" type="button">등록</button>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="notice-modal.jsp"/>
        <!-- 보기 모드 -->
        <div id="viewSection">
            <p id="modalContent"></p>
            <div class="modal-actions">
                <button class="notice-delete-btn">삭제</button>
            </div>
        </div>

        <!-- 등록 모드 -->
        <div id="editSection" style="display:none;">
            <input type="text" id="noticeTitleInput" placeholder="제목 입력"/>
            <textarea id="noticeContentInput" placeholder="내용 입력"></textarea>
            <div class="modal-actions">
                <button id="saveNoticeBtn">등록</button>
            </div>
        </div>
    </div>
</div>
