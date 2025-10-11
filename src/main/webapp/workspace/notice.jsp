<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="css/notice.css">
<link rel="stylesheet" href="css/noticeModal.css">
<section class="section notice-list-section">

    <div class="notice-header">
        <h3>공지 사항</h3>
        <button class="register-btn">공지 등록</button>
    </div>

    <hr class="section-divider">

    <div class="notice-controls">
        <div class="search-sort">
            <input type="text" class="search-input" placeholder="공지 제목 / 내용 검색">
            <button class="sort-btn">정렬 ▾</button>
        </div>
    </div>

    <div class="notice-grid" id="noticeGrid">

    </div>

<%--todo 페이지네이션 수정 예정--%>
    <div class="pagination">
        <button>1</button>
        <button>2</button>
        <button>3</button>
        <span>...</span>
        <button>10</button>
    </div>

</section>

<!-- 공지 읽기/등록 모달 -->
<div id="noticeModal" class="notice-modal" style="display:none;">
    <div class="modal-content">
        <span class="modal-close">&times;</span>
        <h3 id="modalTitle">공지 보기</h3>

        <!-- 읽기 영역 -->
        <div id="viewSection">
            <p id="modalContent"></p>
            <div class="modal-actions">
                <button class="notice-delete-btn">삭제</button>
            </div>
        </div>

        <!-- 등록/수정 영역 -->
        <div id="editSection" style="display:none;">
            <input type="text" id="noticeTitleInput" placeholder="제목 입력" />
            <textarea id="noticeContentInput" placeholder="내용 입력"></textarea>
            <div class="modal-actions">
                <button id="saveNoticeBtn">등록</button>
            </div>
        </div>
    </div>
</div>
