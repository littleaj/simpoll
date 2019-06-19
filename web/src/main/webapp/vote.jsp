<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="poll" type="littleaj.simpoll.model.Poll"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Vote - ${poll.name}</title>
</head>
<body>
<h1>${poll.name}</h1>
<h2>${poll.question}</h2>
<form action="vote" method="post">
    <input type="hidden" name="poll_id" id="poll_id" value="${poll.id}"/>
<c:forEach var="answer" varStatus="status" items="${poll.answers}">
    <c:if test="${status.first}">
        <ul>
    </c:if>
    <li><input type="submit" name="answer" id="answer_${status.index}" value="${answer}"/></li>
    <c:if test="${status.last}">
        </ul>
    </c:if>
</c:forEach>
</form>
</body>
</html>
