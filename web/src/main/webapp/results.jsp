<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="poll" type="littleaj.simpoll.model.Poll"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${poll.name} Results</title>
</head>
<body>
<h1>${poll.name} Results</h1>
<h2>${poll.question}</h2>
<c:forEach var="answer" varStatus="status" items="${poll.answers}">
    <%--@elvariable id="results" type="littleaj.simpoll.model.PollResults"--%>
    <c:if test="${status.first}">
    <ul>
    </c:if>
    <li>${answer}: <c:out value="${results.results[answer].voteCount}" default="0"/></li>
    <c:if test="${status.last}">
    </ul>
    </c:if>
</c:forEach>
</body>
</html>
