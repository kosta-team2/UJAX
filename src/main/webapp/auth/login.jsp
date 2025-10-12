<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="ko">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>CodeHub | 로그인</title>
  <link rel="stylesheet" href="css/login.css">
</head>
<body>
<main class="page" role="main">
  <section class="stack" aria-label="로그인">
    <div class="img-slot-spacer" aria-hidden="true"><br><br><br></div>
    <p class="eyebrow">Welcome To <span class="brand">CodeHub.</span></p>
    <h1 class="headline measure">문제부터 리뷰까지 한 곳에서.</h1>

    <div class="card measure--narrow" role="region" aria-labelledby="signinTitle">
      <h2 id="signinTitle" class="sr-only">로그인 폼</h2>
      <form id="loginForm" autocomplete="on" novalidate>
        <div class="field">
          <label for="email" class="label">Email</label>
          <input id="email" name="email" type="email" class="input" placeholder="you@example.com" required />
        </div>
        <div class="vspace-16"></div>
        <div class="field">
          <label for="password" class="label">Password</label>
          <input id="password" name="password" type="password" class="input" placeholder="••••••••" minlength="6" required />
        </div>

        <br>

        <div class="actions">
          <button type="submit" class="btn btn-primary" id="loginBtn">로그인</button>
          <button type="button" class="btn btn-outline" id="signupBtn">회원가입</button>
        </div>
      </form>
    </div>
  </section>
</main>
<script defer src="js/login.js"></script>
</body>
</html>
