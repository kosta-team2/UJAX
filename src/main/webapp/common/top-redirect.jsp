<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head><meta charset="UTF-8"></head>
<body>
<script>
    (function () {
        var target = '<c:out value="${target}"/>';
        if (window.top && window.top !== window) {
            window.top.location.replace(target);
        } else {
            window.location.replace(target);
        }
    })();
</script>
</body>
</html>
