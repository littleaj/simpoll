<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <c:set var="mode" value="New"/>
    <%--@elvariable id="poll" type="littleaj.simpoll.model.Poll"--%>
    <c:if test="${poll != null}">
        <c:set var="mode" value="Edit"/>
        <c:set var="pname" value="${poll.name}"/>
        <c:set var="pquestion" value="${poll.question}"/>
    </c:if>
    <title>${mode} Poll</title>
</head>
<body>
<h1>${mode} Poll</h1>
<c:set var="disableEdit" value="" />
<c:set var="disableActivate" value="" />
<c:set var="disableClose" value="disabled" />
<form action="./submit" method="post">
    <div>
        <label for="poll_name">Name: </label><input type="text" name="poll_name" id="poll_name" maxlength="64" size="64"  ${disableEdit}/>
    </div>
    <div>
        <label for="poll_question">Question: </label><input type="text" name="poll_question" id="poll_question" maxlength="256" size="64" ${disableEdit}/>
    </div>
    <div>
        <label for="poll_answers">Answers (one per line):</label>
        <p><textarea name="poll_answers" id="poll_answers" ${disableEdit} rows="5" cols="92"></textarea></p>
    </div>

    <div>
        <input type="submit" name="poll_save" id="poll_save" value="Save" ${disableEdit}/>
        <input type="submit" name="poll_activate" id="poll_activate" value="${disableEdit=="" ? "Save & Activate" : "Activate"}" ${disableActivate}/>
        <input type="submit" name="poll_close" id="poll_close" value="Close" ${disableClose}/>
    </div>
</form>
</body>
</html>
