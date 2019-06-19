package littleaj.simpoll.web;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.Vote;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// handles: "/poll/new", "/poll/{pollId}", "/poll/{pollId}/results"
@WebServlet(name="poll servlet", urlPatterns = "/poll/*")
public class PollsServlet extends HttpServlet {

    private HttpClient apiClient;

    private URI apiUrl;

    private String initErrorMessage = "";

    private Exception initException;

    @Override
    public void init() throws ServletException {
        apiClient = HttpClientBuilder.create().build();
        final String apiEndpoint = System.getenv("SIMPOLL_API_HOSTNAME_PORT");
        final String[] split = apiEndpoint.split(":");
        final URIBuilder uriBuilder = new URIBuilder().setHost(split[0]).setScheme("http");
        if (split.length > 1) {
            int port = Integer.parseInt(split[1]);
            uriBuilder.setPort(port);
        }
        try {
            apiUrl = uriBuilder.build();
        } catch (URISyntaxException e) {
            initErrorMessage = "Error parsing api endpoint";
            initException = e;
            log(initErrorMessage, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log("in doPost: /poll"+req.getPathInfo());
        if (shouldBail(resp)) {
            return;
        }
        final Map<String, String[]> params = req.getParameterMap();
        if (params == null) {
            log("params is null");
            return;
        }
        if (params.isEmpty()) {
            log("params is empty");
            return;
        }
        for (Entry<String, String[]> entry : params.entrySet()) {
            log(entry.getKey() + ": "+Arrays.toString(entry.getValue()));
        }



        try {
            final String[] parts = pathInfoParts(req);
            if (parts.length == 1) {
                if ("submit".equals(parts[0])) {
                    createOrUpdatePoll(req, resp);
                    return;
                } else if ("vote".equals(parts[0])) {
                    voteOnPoll(req, resp);
                    return;
                }
            }
            try {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException ioe) {
                log("Error sending 404 response " + Arrays.toString(parts), ioe);
            }
        } catch (ApiCommunicationException ace) {
            try {
                int sc = HttpServletResponse.SC_SERVICE_UNAVAILABLE;
                if (ace.getStatusCode() == 404) {
                    sc = HttpServletResponse.SC_NOT_FOUND;
                }
                log("Exception handling: "+req.getPathInfo(), ace);
                resp.sendError(sc, ace.getLocalizedMessage());
            } catch (IOException ioe) {
                log("Error sending error code: "+ace.getLocalizedMessage(), ioe);
            }
        }
    }

    private void voteOnPoll(HttpServletRequest req, HttpServletResponse resp) throws ApiCommunicationException {
        String answer = req.getParameter("answer");
        String id = req.getParameter("poll_id");
        URI uri = constructUri("/polls/"+id+"/vote");
        final Vote vote = new Vote();
        vote.setAnswer(answer);
        vote.setPollId(new PollId(id));
        HttpUriRequest apiRequest;
        try {
            apiRequest = RequestBuilder.post(uri).setHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(new Gson().toJson(vote))).build();
        } catch (UnsupportedEncodingException e) {
            throw new ApiCommunicationException(e);
        }
        log("Submitting vote for '"+id+"': "+answer);
        final HttpResponse apiResponse = sendApiRequest(apiRequest);
        if (apiResponse.getStatusLine().getStatusCode() >= 400) {
            throw new ApiCommunicationException("Error creating poll: "+apiResponse.getStatusLine());
        }
        redirect(""+id+"/results", resp);
    }

    private void createOrUpdatePoll(HttpServletRequest req, HttpServletResponse resp) throws ApiCommunicationException {
        String id = req.getParameter("poll_id");
        String name = req.getParameter("poll_name");
        String question = req.getParameter("poll_question");
        String answersRaw = req.getParameter("poll_answers");
        List<String> answers = Arrays.asList(answersRaw.split("\\s+"));
        boolean activate = req.getParameter("poll_activate") != null;
        boolean save = req.getParameter("poll_save") != null || activate;
        boolean close = req.getParameter("poll_close") != null;

        PollId createdId = null;
        if (save) {
            Poll poll = new Poll();
            poll.setName(name);
            poll.setQuestion(question);
            poll.setAnswers(answers);
            String action = "Created";
            if (id != null) {
                poll.setId(new PollId(id));
                action = "Updated";
            }
            Poll created = submitPoll(poll);
            createdId = created.getId();
            log(action+" poll: "+createdId);
        }
        if (createdId == null) {
            createdId = new PollId(id);
        }
        if (activate) {
            activatePoll(createdId);
            log("Activated poll: "+createdId);
        }
        if (close) {
            closePoll(createdId);
            log("Closed poll: "+createdId);
            redirect(""+createdId+"/results", resp);
        } else {
            redirect(""+createdId, resp);
        }
    }

    private void redirect(String path, HttpServletResponse resp) {
        try {
            resp.sendRedirect(path);
        } catch (IOException e) {
            log("Error redirecting to "+path, e);
        }
    }

    private boolean shouldBail(HttpServletResponse resp) {
        boolean shouldBail = !initErrorMessage.equals("") || initException != null;
        if (shouldBail) {
            final int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            try {
                resp.sendError(statusCode, initErrorMessage +"\n"+ initException.getLocalizedMessage());
            } catch (IOException e) {
                log("Error responding "+statusCode, e);
            }
        }
        return shouldBail;
    }

    private void closePoll(PollId createdId) throws ApiCommunicationException {
        final URI uri = constructUri("/polls/" + createdId + "/close");
        final HttpResponse httpResponse = sendApiRequest(RequestBuilder.put(uri).build());
        if (httpResponse.getStatusLine().getStatusCode() >= 400) {
            throw new ApiCommunicationException(httpResponse.getStatusLine().getStatusCode(), "Error closing poll: "+createdId);
        }
    }

    private void activatePoll(PollId createdId) throws ApiCommunicationException {
        final URI uri = constructUri("/polls/" + createdId + "/open");
        final HttpResponse httpResponse = sendApiRequest(RequestBuilder.put(uri).build());
        if (httpResponse.getStatusLine().getStatusCode() >= 400) {
            throw new ApiCommunicationException(httpResponse.getStatusLine().getStatusCode(), "Error closing poll: "+createdId);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log("in doGet: /poll"+req.getPathInfo());
        if (shouldBail(resp)) {
            return;
        }
        String[] parts = pathInfoParts(req);
        log("parts = "+ Arrays.toString(parts));
        try {
            switch (parts.length) {
                case 0:
                    // /poll/ -> /
                    req.getRequestDispatcher("/").forward(req, resp);
                    return;
                case 1:
                    // should be an id or new
                    if ("new".equals(parts[0])) {
                        req.getRequestDispatcher("/editPoll.jsp").forward(req, resp);
                    } else {
                        Poll poll = retrievePoll(parts[0]);
                        if (poll == null) {
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No poll found for id="+parts[0]);
                            return;
                        }
                        req.setAttribute("poll", poll);
                        switch(poll.getStatus()) {
                            case CREATED:
                                req.getRequestDispatcher("/editPoll.jsp").forward(req, resp);
                                return;
                            case OPEN:
                                req.getRequestDispatcher("/vote.jsp").forward(req, resp);
                                return;
                            case CLOSED:
                                req.getRequestDispatcher("/results.jsp").forward(req, resp);
                                return;
                        }
                    }
                    break;
                case 2:
                    // first should be id, second should be results, otherwise 404
                    if (!parts[1].equals("results")) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                    Poll poll = retrievePoll(parts[0]);
                    PollResults results = retrieveResults(parts[0]);
                    req.setAttribute("poll", poll);
                    req.getRequestDispatcher("/results.jsp").forward(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            try {
                req.setAttribute("exception", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
            } catch (Exception e2) {
                log("Could not dispatch to error page.", e2);
            }
        }
    }

    private PollResults retrieveResults(String pollId) throws ApiCommunicationException {
        URI uri = constructUri("/polls/" + pollId + "/results");

        final HttpResponse response = sendApiRequest(RequestBuilder.get(uri).build());
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 404) {
            return null;
        } else if (statusCode >= 400) {
            throw new ApiCommunicationException(response.getStatusLine().getStatusCode(), "Error from api: GET " + uri + " returned " + response.getStatusLine());
        }

        return parseResults(getInputStreamOrThrow(response));
    }

    private PollResults parseResults(InputStream content) throws ApiCommunicationException {
        try (InputStreamReader reader = new InputStreamReader(content)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, PollResults.class);
        } catch (Exception e) {
            String message = "Error reading get pollResults stream";
            if (e instanceof JsonParseException) {
                message = "Error parsing json in get pollResults response";
            }
            log(message, e);
            throw new ApiCommunicationException(message, e);
        }
    }

    private String[] pathInfoParts(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "";
        } else if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        return pathInfo.split("/");
    }

    private Poll submitPoll(Poll poll) throws ApiCommunicationException {
        URI uri = constructUri("/polls");
        HttpUriRequest apiRequest;
        try {
            RequestBuilder builder;
            if (poll.getId() == null) {
                builder = RequestBuilder.post(uri);
            } else {
                builder = RequestBuilder.put(uri);
            }
            apiRequest = builder.setHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(new Gson().toJson(poll))).build();
        } catch (UnsupportedEncodingException e) {
            throw new ApiCommunicationException(e);
        }
        final HttpResponse apiResponse = sendApiRequest(apiRequest);
        if (apiResponse.getStatusLine().getStatusCode() >= 400) {
            throw new ApiCommunicationException("Error creating poll: "+apiResponse.getStatusLine());
        }

        return parsePoll(getInputStreamOrThrow(apiResponse));
    }

    private static class ApiCommunicationException extends Exception {
        private final int statusCode;

        ApiCommunicationException(String message) {
            this(0, message);
        }

        ApiCommunicationException(String message, Throwable cause) {
            this(0, message, cause);
        }

        ApiCommunicationException(Throwable cause) {
            this(0, null, cause);
        }

        ApiCommunicationException(int statusCode, String message) {
            super(message);
            this.statusCode = statusCode;
        }

        ApiCommunicationException(int statusCode, String message, Throwable cause) {
            super(message, cause);
            this.statusCode = statusCode;
        }

        int getStatusCode() {
            return statusCode;
        }
    }

    private Poll retrievePoll(String pollId) throws ApiCommunicationException {
        URI uri = constructUri("/polls/" + pollId);

        final HttpResponse response = sendApiRequest(RequestBuilder.get(uri).build());
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 404) {
            return null;
        } else if (statusCode >= 400) {
            throw new ApiCommunicationException(response.getStatusLine().getStatusCode(), "Error from api: GET " + uri + " returned " + response.getStatusLine());
        }

        return parsePoll(getInputStreamOrThrow(response));
    }

    private Poll parsePoll(InputStream content) throws ApiCommunicationException {
        try (InputStreamReader reader = new InputStreamReader(content)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Poll.class);
        } catch (Exception e) {
            String message = "Error reading get poll stream";
            if (e instanceof JsonParseException) {
                message = "Error parsing json in get poll response";
            }
            log(message, e);
            throw new ApiCommunicationException(message, e);
        }
    }

    private InputStream getInputStreamOrThrow(HttpResponse response) throws ApiCommunicationException {
        InputStream content;
        try {
            content = response.getEntity().getContent();
        } catch (IOException e) {
            final String message = "Error retrieving get poll content";
            log(message, e);
            throw new ApiCommunicationException(message, e);
        }
        return content;
    }

    private HttpResponse sendApiRequest(HttpUriRequest apiRequest) throws ApiCommunicationException {
        HttpResponse response;
        try {
            response = apiClient.execute(apiRequest);
        } catch (IOException e) {
            final String message = "Error sending request " + apiRequest.getMethod() + " " + apiRequest.getURI();
            log(message, e);
            throw new ApiCommunicationException(e);
        }
        return response;
    }

    private URI constructUri(String path) throws ApiCommunicationException {
        URI uri;
        try {
            uri = new URIBuilder(apiUrl).setPath(path).build();
        } catch (URISyntaxException e) {
            final String message = "Error constructing get poll url, path=" + path;
            log(message, e);
            throw new ApiCommunicationException(message, e);
        }
        return uri;
    }
}
