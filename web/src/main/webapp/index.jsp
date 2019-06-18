<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.impl.client.HttpClientBuilder" %>
<%@ page import="java.net.URI" %>
<%@ page import="org.apache.http.client.methods.RequestBuilder" %>
<%@ page import="org.apache.http.client.utils.URIBuilder" %>
<%@ page import="org.apache.http.client.methods.HttpUriRequest" %>
<%@ page import="java.net.URISyntaxException" %>
<%@ page import="littleaj.simpoll.model.Poll" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="littleaj.simpoll.model.Status" %>
<%@ page import="java.util.stream.Collectors" %>
<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Simpoll</title>
    </head>
    <body>
        <h1>Simpoll</h1>
        <%
            String errorMessage = null;
            HttpClient hc = HttpClientBuilder.create().build();
            URI uri = null;
            try {
                uri = new URIBuilder().setHost(System.getenv("SIMPOLL_API_HOSTNAME")).setPath("/polls").build();
            } catch (URISyntaxException use) {
                errorMessage = use.getMessage();
            }
            Collection<Poll> polls = new ArrayList<>();
            if (uri != null) {
                HttpUriRequest r = RequestBuilder.get().setUri(uri).build();
                HttpResponse pollResponse = hc.execute(r);
                try (InputStreamReader content = new InputStreamReader(pollResponse.getEntity().getContent())) {
                    if (pollResponse.getStatusLine().getStatusCode() < 400) {
                        Gson gson = new Gson();
                        TypeToken<Collection<Poll>> tt = new TypeToken<>();
                        polls = gson.fromJson(content, tt.getType());
                    } else {
                        errorMessage = "Error retrieving polls: " + pollResponse.getStatusLine().toString() + "\n";
                    }
                } catch (IOException ioe) {
                    errorMessage = "Error reading content: "+ioe.toString();
                }
            }
            if (errorMessage != null) {
        %>
        <h2>An error occurred.</h2>
        <p><%=errorMessage%></p>
        <%
            } else {
        %>
        <h2>Open Polls</h2>
        <p>Use the links to vote on these polls</p>
        <dl>
            <%
                for (Poll poll : polls.stream().filter(p -> p.getStatus() == Status.OPEN).collect(Collectors.toList())) {
            %>
            <dt><a href="/polls/<%=poll.getId().toString()%>" title="Vote on '<%=poll.getName()%>'"><%=poll.getName()%></a></dt>
            <dd><%=poll.getQuestion()%></dd>
            <%
                }
            %>
        </dl>
        <h2>Closed Polls</h2>
        <p>Use the links to view the results.</p>
        <dl>
            <%
                for (Poll poll : polls.stream().filter(p -> p.getStatus() == Status.CLOSED).collect(Collectors.toList())) {
            %>
            <dt><a href="/results/<%=poll.getId().toString()%>" title="View results for '<%=poll.getName()%>'"><%=poll.getName()%></a></dt>
            <dd><%=poll.getQuestion()%></dd>
            <%
                }
            %>
        </dl>
        <%
            }
        %>
    </body>
</html>
