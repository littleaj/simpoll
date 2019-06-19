package littleaj.simpoll.web;

import com.google.gson.Gson;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollsList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

@WebServlet(name="list polls", urlPatterns = {"/", "/list"})
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 6076404907866357513L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("index servlet");
        Exception exception = null;
        String errorMessage = null;
        HttpClient hc = HttpClientBuilder.create().disableAuthCaching().useSystemProperties().build();
        URI uri = null;
        try {
            final String apiEndpoint = System.getenv("SIMPOLL_API_HOSTNAME_PORT");
            System.out.println("apiEndpoint="+apiEndpoint);
            final String[] split = apiEndpoint.split(":");
            final URIBuilder uriBuilder = new URIBuilder().setHost(split[0]).setScheme("http");
            try {
                if (split.length > 1) {
                    int port = Integer.parseInt(split[1]);
                    uriBuilder.setPort(port);
                }
                uri = uriBuilder.setPath("/polls").build();
                System.out.println("uri = "+uri.toASCIIString());
            } catch (NumberFormatException nfe) {
                exception = nfe;
                errorMessage = "Error parsing port from SIMPOLL_API_HOSTNAME_PORT: "+nfe.getLocalizedMessage();
            }
        } catch (URISyntaxException use) {
            exception = use;
            errorMessage = use.getMessage();
        }
        PollsList polls = new PollsList();
        if (uri != null) {
            HttpUriRequest r = RequestBuilder.get().setUri(uri).build();
            HttpResponse pollResponse = hc.execute(r); // TODO try/catch, also for getContent and fromJson
            try (BufferedReader content = new BufferedReader(new InputStreamReader(pollResponse.getEntity().getContent()))) {
                final String jsonString = content.lines().collect(Collectors.joining(System.lineSeparator()));
                System.out.println("GET /polls response:\n"+jsonString);
                if (pollResponse.getStatusLine().getStatusCode() < 400) {
                    Gson gson = new Gson();
                    polls = gson.fromJson(jsonString, PollsList.class);
                } else {
                    errorMessage = "Error retrieving polls: " + pollResponse.getStatusLine().toString() + "\n";
                }
            } catch (IOException ioe) {
                exception = ioe;
                errorMessage = "Error reading content: "+ioe.toString();
            }
        } else {
            System.err.println("uri is null");
        }

        if (errorMessage != null) {
            req.setAttribute("exception", exception);
            req.setAttribute("message", errorMessage);
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        } else {
            System.out.println("errorMessage is null");
        }

        req.setAttribute("pollsByStatus", polls.getPolls().stream().collect(Collectors.groupingBy(Poll::getStatus)));
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }
}
