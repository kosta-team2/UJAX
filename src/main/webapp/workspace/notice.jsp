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
