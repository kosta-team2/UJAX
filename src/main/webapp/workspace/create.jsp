<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko" data-theme="dark">
<head>
    <meta charset="UTF-8"/>
    <title>공지 리스트</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/darkmode.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/workspace/css/create.css">

</head>
<body>
<main class="main-content">
    <div class="page">
        <h1 class="title">워크스페이스 생성</h1>

        <section class="panel">
            <form id="createForm"
                  action="${pageContext.request.contextPath}/front?key=workspace&methodName=create"
                  method="post" novalidate>

                <div class="field">
                    <label for="wsName">워크스페이스 이름</label>
                    <input id="wsName"
                           class="input"
                           type="text"
                           name="workspaceName"
                           minlength="2"
                           maxlength="60"
                           required
                           placeholder="예) 알고리즘 스터디"/>
                </div>

                <div class="field">
                    <label for="langSelect">사용 언어</label>
                    <select id="langSelect"
                            class="select"
                            name="workspaceLanguage"
                            required>
                        <option value="" selected disabled>언어 선택</option>
                    </select>
                </div>

                <div class="field">
                    <label class="checkbox">
                        <input type="checkbox" id="isHintView" name="isHintView" checked/>
                        힌트 공개 (팀원에게 풀이 힌트를 보이기)
                    </label>
                </div>

                <div class="actions">
                    <button type="submit" class="btn" id="createBtn">생성하기</button>
                </div>
                <p class="foot">생성 후에도 워크스페이스 이름, 사용 언어는 언제든 변경할 수 있습니다.</p>
            </form>
        </section>

        <%--        todo 현재 redirect로 완료 모달이 안보임 하지만 생성되면 새로 생긴 워크스페이스로 이동할테니 없애는 것도 고려--%>
        <!-- 생성 완료 모달 -->
        <%--        <div class="dialog-backdrop" id="createdBackdrop" hidden>--%>
        <%--            <div class="dialog" id="createdDialog" role="dialog" aria-labelledby="modalTitle" aria-modal="true">--%>
        <%--                <div class="dialog-head">--%>
        <%--                    <svg class="check" viewBox="0 0 24 24" fill="none" stroke="currentColor"--%>
        <%--                         stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">--%>
        <%--                        <path d="M20 6L9 17l-5-5"/>--%>
        <%--                    </svg>--%>
        <%--                    <h2 id="modalTitle" class="dialog-title">워크스페이스가 생성되었습니다</h2>--%>
        <%--                </div>--%>
        <%--                <p class="dialog-body">이제 멤버가 초대장을 통해 참여할 수 있어요.</p>--%>
        <%--                <div class="dialog-actions">--%>
        <%--                    <button type="button" class="btn-close btn-primary" id="modalOk">확인</button>--%>
        <%--                </div>--%>
        <%--            </div>--%>
        <%--        </div>--%>
    </div>
</main>

<script defer src="${pageContext.request.contextPath}/workspace/js/create.js"></script>

</body>
</html>