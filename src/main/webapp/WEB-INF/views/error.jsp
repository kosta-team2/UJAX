<%--
  Created by IntelliJ IDEA.
  User: gwongwangjae
  Date: 2025. 10. 19.
  Time: 오전 11:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>문제가 발생했습니다.</h2>
<p>${requestScope.errorMessage}</p>
<a href="<%= request.getContextPath() %>/front/index.jsp">홈으로</a>
</body>
</html>
