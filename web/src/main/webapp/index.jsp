<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="littleaj.simpoll.model.Status" %>
<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Simpoll</title>
    </head>
    <body>
        <h1>Simpoll</h1>
        <%--@elvariable id="pollsByStatus" type="java.util.Map<littleaj.simpoll.model.Status, java.util.List<littleaj.simpoll.model.Poll>>"--%>
        <p><a href="poll/new">Create new poll</a></p>
        <c:choose>
            <c:when test="${pollsByStatus.size() > 0}">
                <% System.out.println("polls exist"); %>
                <c:if test="${pollsByStatus.get(Status.OPEN).size() > 0}">
                    <% System.out.println("open polls exist"); %>
                    <c:set var="polls" value="${pollsByStatus.get(Status.OPEN)}" scope="request" />
                    <jsp:include page="/pollList.jsp">
                        <jsp:param name="sectionTitle" value="Open Polls"/>
                        <jsp:param name="description" value="Use the links below to vote." />
                    </jsp:include>
                </c:if>
                <c:if test="${pollsByStatus.get(Status.CLOSED).size() > 0}">
                    <% System.out.println("closed polls exist"); %>
                    <c:set var="polls" value="${pollsByStatus.get(Status.CLOSED)}" scope="request" />
                    <jsp:include page="/pollList.jsp">
                        <jsp:param name="sectionTitle" value="Closed Polls"/>
                        <jsp:param name="description" value="Use the links below to view results." />
                    </jsp:include>
                </c:if>
                <c:if test="${pollsByStatus.get(Status.CREATED).size() > 0}">
                    <% System.out.println("created polls exist"); %>
                    <c:set var="polls" value="${pollsByStatus.get(Status.CREATED)}" scope="request" />
                    <jsp:include page="/pollList.jsp">
                        <jsp:param name="sectionTitle" value="Created Polls"/>
                        <jsp:param name="description" value="Use the links below to <strike>edit</strike>/activate." />
                    </jsp:include>
                </c:if>
            </c:when>
            <c:otherwise>
                <p>No polls</p>
            </c:otherwise>
        </c:choose>
    </body>
</html>
