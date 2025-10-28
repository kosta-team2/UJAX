<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Ujax | 로그인</title>
    <link rel="stylesheet" href="<c:url value='/auth/css/login.css'/>">
</head>
<body>
<c:if test="${not empty sessionScope.flashMessageJs}">
    <script>
        alert('${sessionScope.flashMessageJs}');
    </script>
    <c:remove var="flashMessageJs" scope="session"/>
</c:if>
<main class="page" role="main">
    <section class="stack" aria-label="로그인">
        <div class="img-slot-spacer" aria-hidden="true"><br><br><br></div>
        <p class="eyebrow">Welcome To <span class="brand">Ujax.</span></p>
        <h1 class="headline measure">문제 풀이와 리뷰를 한 곳에서!</h1>

        <div class="card measure--narrow" role="region" aria-labelledby="signinTitle">
            <h2 id="signinTitle" class="sr-only">로그인 폼</h2>

            <form id="loginForm"
                  action="<c:url value='/front'/>"
                  method="post"
                  autocomplete="on">

                <c:if test="${not empty param.redirect}">
                    <input type="hidden" name="redirect" value="${fn:escapeXml(param.redirect)}"/>
                </c:if>
                <input type="hidden" name="key" value="member"/>
                <input type="hidden" name="methodName" value="login"/>

                <div class="field">
                    <label for="email" class="label">Email</label>
                    <input id="email" name="email" type="email" class="input" placeholder="you@example.com" required/>
                </div>

                <div class="vspace-16"></div>

                <div class="field">
                    <label for="password" class="label">Password</label>
                    <input id="password" name="password" type="password" class="input" placeholder="••••••••"
                           minlength="6" required/>
                </div>

                <c:if test="${not empty error}">
                    <script>
                        alert('<c:out value="${error}" />');
                    </script>
                </c:if>

                <br>

                <div class="actions">
                    <button type="submit" class="btn btn-primary" id="loginBtn">로그인</button>
                    <a class="btn btn-outline" href="register.jsp">회원가입</a>
                </div>
            </form>
        </div>
    </section>
</main>
</body>
</html>
