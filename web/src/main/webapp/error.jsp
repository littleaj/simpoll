<%--@elvariable id="message" type="java.lang.String"--%>
<%--@elvariable id="exception" type="java.lang.Exception"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Simpoll - Error</title>
</head>
<body>
    <h1>Error</h1>
    <c:if test="${message != null}">
        <p>${message}</p>
    </c:if>
    <c:if test="${exception != null}">
        <p>${exception.toString()}</p>
    </c:if>
</body>
</html>
