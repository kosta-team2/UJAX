<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="css/giftshop.css" />

<main class="giftshop">
  <header class="topbar">
    <h1 class="page-title">기프티콘 샵</h1>
  </header>

  <section class="grid" id="shopGrid" aria-live="polite"></section>

  <nav class="pagination" aria-label="페이지 내비게이션">
    <button class="page-btn prev" type="button" aria-label="이전 페이지">&lt;</button>
    <ol class="page-list"></ol>
    <button class="page-btn next" type="button" aria-label="다음 페이지">&gt;</button>
  </nav>

  <div class="state" id="shopState" hidden></div>
</main>
