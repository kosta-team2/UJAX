<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="css/problem.css">
<section class="section problem-list-section">
    <div class="problem-header">
        <h3>문제 리스트</h3>
        <button class="register-btn">문제 등록</button>
    </div>

    <hr class="section-divider">

    <div class="problem-controls">
        <div class="search-sort">
            <input type="text" class="search-input" placeholder="문제 제목 / 태그 검색">
            <button class="sort-btn">정렬 ▾</button>
        </div>
    </div>

    <div class="problem-grid" id="problemGrid">

    </div>

<%--    todo 페이지네이션--%>
    <div class="pagination">
        <button>1</button>
        <button>2</button>
        <button>3</button>
        <span>...</span>
        <button>100</button>
    </div>

</section>
