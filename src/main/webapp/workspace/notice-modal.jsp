<div id="noticeModal" class="notice-modal" style="display:none;">
    <div class="modal-content">
        <span class="modal-close">&times;</span>
        <h3 id="modalTitle">공지 보기</h3>  <!-- ✅ 추가 -->

        <!-- 보기 모드 -->
        <div id="viewSection">
            <p id="modalContent"></p>
            <div class="modal-actions">
                <button class="notice-delete-btn">삭제</button>
            </div>
        </div>

        <!-- 등록 모드 -->
        <div id="editSection" style="display:none;">
            <input type="text" id="noticeTitleInput" placeholder="제목 입력" />
            <textarea id="noticeContentInput" placeholder="내용 입력"></textarea>
            <div class="modal-actions">
                <button id="saveNoticeBtn">등록</button>
            </div>
        </div>
    </div>
</div>
