<%--@elvariable id="polls" type="java.util.List<littleaj.simpoll.model.Poll>"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% System.out.println("pollList.jsp"); %>
<h2>${param.sectionTitle}</h2>
<p>${param.description}</p>
<c:forEach items="${polls}" var="poll" varStatus="status">
    <c:if test="${status.first}">
        <dl>
    </c:if>
    <dt><a href="poll/${poll.id}">${poll.name}</a></dt>
    <dd>${poll.question}</dd>
    <c:if test="${status.last}">
        </dl>
    </c:if>
</c:forEach>