package littleaj.simpoll.web;

import com.google.gson.Gson;
import littleaj.simpoll.model.Poll;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// handles: "/poll/new", "/poll/{pollId}", "/poll/{pollId}/results"
@WebServlet(name="poll servlet", urlPatterns = "/poll/*")
public class PollsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Map<String, String[]> params = req.getParameterMap();
        if (params == null) {
            System.out.println("params is null");
            return;
        }
        if (params.isEmpty()) {
            System.out.println("params is empty");
            return;
        }
        for (Entry<String, String[]> entry : params.entrySet()) {
            System.out.println(entry.getKey() + ": "+Arrays.toString(entry.getValue()));
        }

        String name = req.getParameter("poll_name");
        String question = req.getParameter("poll_question");
        String answersRaw = req.getParameter("poll_answers");
        List<String> answers = Arrays.asList(answersRaw.split("\\s+"));
        boolean save = req.getParameter("poll_save") != null;
        boolean actiaate = req.getParameter("poll_activate") != null;
        boolean close = req.getParameter("poll_close") != null;

        Poll poll = new Poll();
        poll.setName(name);
        poll.setQuestion(question);
        poll.setAnswers(answers);

        Poll created = submitPoll(poll);
        resp.sendRedirect("/poll/"+created.getId().toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "";
        } else if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        String[] parts = pathInfo.split("/");
        System.out.println("parts = "+ Arrays.toString(parts));
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
                        return;
                    } else {
                        Poll poll = retrievePoll(parts[0]);
                        if (poll == null) {
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No poll found for id="+parts[0]);
                            return;
                        }
                        switch(poll.getStatus()) {
                            case CREATED:
                                resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, poll.getName());
                                // TODO if poll is created, fwd to edit
                                return;
                            case OPEN:
                                resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, poll.getName());
                                // TODO if poll is open, fwd to vote
                                return;
                            case CLOSED:
                                resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, poll.getName());
                                // TODO if poll is closed, fwd to results
                                return;
                        }
                    }
                    break;
                case 2:
                    // first should be id, second should be results, otherwise 404
                    Poll poll = retrievePoll(parts[0]);
                    resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            try {
                req.setAttribute("exception", e);
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
            } catch (Exception e2) {
                System.err.println("Could not dispatch to error page.");
                e2.printStackTrace();
            }
        }
    }

    private Poll submitPoll(Poll poll) {
        // TODO
        return null;
    }

    private Poll retrievePoll(String pollId) throws URISyntaxException, IOException {
        HttpClient hc = HttpClientBuilder.create().build();
        final String apiEndpoint = System.getenv("SIMPOLL_API_HOSTNAME_PORT");
        final String[] split = apiEndpoint.split(":");
        final URIBuilder uriBuilder = new URIBuilder().setHost(split[0]).setScheme("http");
        if (split.length > 1) {
            int port = Integer.parseInt(split[1]);
            uriBuilder.setPort(port);
        }
        URI uri = uriBuilder.setPath("/polls/" + pollId).build();
        final HttpResponse response = hc.execute(RequestBuilder.get(uri).build());
        final int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 404) {
            return null;
        } else if (statusCode >= 400) {
            throw new IOException("Error from api: GET " + uri + " returned " + response.getStatusLine());
        }

        try (InputStreamReader content = new InputStreamReader(response.getEntity().getContent())) {
            Gson gson = new Gson();
            return gson.fromJson(content, Poll.class);
        }
    }
}
