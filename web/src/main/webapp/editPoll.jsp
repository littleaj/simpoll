<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="littleaj.simpoll.model.Status" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <c:set var="mode" value="New"/>
    <%--@elvariable id="poll" type="littleaj.simpoll.model.Poll"--%>
    <c:if test="${poll != null}">
        <c:set var="mode" value="Edit"/>
        <c:set var="pname" value="${poll.name}"/>
        <c:set var="pquestion" value="${poll.question}"/>
        <c:set var="pid" value="${poll.id}"/>
        <c:set var="separator" value="${System.lineSeparator()}"/>
        <c:set var="panswers" value="${fn:join(poll.answers.toArray(), separator)}" />
        <c:set var="pstatus" value="${poll.status}"/>
    </c:if>
    <title>${mode} Poll</title>
</head>
<body>
<h1>${mode} Poll</h1>
<c:set var="disableEdit" value="" />
<c:set var="disableClose" value="disabled" />
<c:choose>
    <c:when test="${pstatus == Status.OPEN}">
        <c:set var="disableEdit" value="disabled"/>
        <c:set var="disableClose" value=""/>
    </c:when>
    <c:when test="${pstatus == Status.CLOSED}">
        <c:set var="disableEdit" value="disabled"/>
        <c:set var="disableClose" value="disabled"/>
    </c:when>
</c:choose>
<form action="./submit" method="post">
    <c:if test="${pid != null}">
        <input type="hidden" name="poll_id" id="poll_id" value="${pid}"/>
    </c:if>
    <div>
        <label for="poll_name">Name: </label><input type="text" name="poll_name" id="poll_name" maxlength="64" size="64" value="${pname}" ${disableEdit}/>
    </div>
    <div>
        <label for="poll_question">Question: </label><input type="text" name="poll_question" id="poll_question" maxlength="256" size="64" value="${pquestion}" ${disableEdit}/>
    </div>
    <div>
        <label for="poll_answers">Answers (one per line):</label>
        <p><textarea name="poll_answers" id="poll_answers" ${disableEdit} rows="5" cols="92">${panswers}</textarea></p>
    </div>

    <div>
        <input type="submit" name="poll_save" id="poll_save" value="${"New".equals(mode) ? "Create" : "Save"}" ${disableEdit}/>
        <input type="submit" name="poll_activate" id="poll_activate" value="${"New".equals(mode) ? "Create" : "Save"} & Activate" ${disableActivate}/>
        <input type="submit" name="poll_close" id="poll_close" value="Close" ${disableClose}/>
    </div>
</form>
</body>
</html>
