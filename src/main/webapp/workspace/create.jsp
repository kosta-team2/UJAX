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
                  target="_top"
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
                        <input type="checkbox" id="isHintView" name="isHintView" value="true" checked/>
                        힌트 공개 (팀원에게 풀이 힌트를 보이기)
                    </label>
                </div>

                <div class="actions">
                    <button type="submit" class="btn" id="createBtn">생성하기</button>
                </div>
                <p class="foot">생성 후에도 워크스페이스 이름, 사용 언어는 언제든 변경할 수 있습니다.</p>
            </form>
        </section>

    </div>
</main>

<script defer src="${pageContext.request.contextPath}/workspace/js/create.js"></script>

</body>
</html>