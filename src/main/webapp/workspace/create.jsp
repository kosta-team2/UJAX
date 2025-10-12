<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>워크스페이스 생성</title>
  <!-- 다크 테마 공통 변수 -->
  <link rel="stylesheet" href="../common/css/darkmode.css"/>
  <!-- 이 페이지 전용 스타일 -->
  <link rel="stylesheet" href="./css/create.css"/>
</head>
<body>
  <div class="page">
    <div class="top">
      <a class="back" href="javascript:history.back()" aria-label="뒤로 가기">
        <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M15 18l-6-6 6-6"/>
        </svg>
      </a>
    </div>

    <h1 class="title">워크스페이스 생성</h1>

    <section class="panel">
      <form id="createForm" novalidate>
        <!-- 이름 -->
        <div class="field">
          <label for="wsName">워크스페이스 이름</label>
          <input id="wsName" class="input" type="text" name="name" minlength="2" maxlength="60" required placeholder="예) 알고리즘 스터디"/>
        </div>

        <!-- 언어 -->
        <div class="field">
          <label for="langSelect">사용 언어</label>
          <select id="langSelect" class="select" name="lang" required>
            <option value="" selected disabled>언어 선택</option>
            <!-- 옵션은 create.js가 mock/language.json으로부터 주입 -->
          </select>
        </div>

        <div class="actions">
          <button type="submit" class="btn" id="createBtn">생성하기</button>
        </div>
        <p class="foot">생성 후에도 워크스페이스 이름, 사용 언어는 언제든 변경할 수 있습니다.</p>
      </form>
    </section>
  </div>

  <!-- 생성 완료 모달 -->
  <div id="createdModal" class="modal" aria-hidden="true">
    <div class="dialog" role="dialog" aria-labelledby="modalTitle" aria-modal="true">
      <div class="dialog-head">
        <svg class="check" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M20 6L9 17l-5-5"/>
        </svg>
        <h2 id="modalTitle" class="dialog-title">워크스페이스가 생성되었습니다</h2>
      </div>
      <p class="dialog-body">이제 멤버가 초대장을 통해 참여할 수 있어요.</p>
      <div class="dialog-actions">
        <button type="button" class="btn-close btn-primary" id="modalOk">확인</button>
      </div>
    </div>
  </div>

  <script src="./js/create.js" defer></script>
</body>
</html>
