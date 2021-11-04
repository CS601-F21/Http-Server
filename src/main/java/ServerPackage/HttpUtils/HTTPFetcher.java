package ServerPackage.HttpUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * A class that provides static methods to send HTTP GET and POST requests.
 */
public class HTTPFetcher {
    private static final Logger LOGGER = LogManager.getLogger(HTTPFetcher.class);

    /**
     * Execute an HTTP GET for the specified URL and return the
     * body of the response as a String.
     *
     * @param url
     * @return
     */
    public static HttpResponse<String> doGet(String url) {
        return doGet(url, null);
    }

    /**
     * Execute an HTTP GET for the specified URL and return
     * the body of the response as a String. Allows request
     * headers to be set.
     *
     * @param url
     * @param headers
     * @return
     */
    public static HttpResponse<String> doGet(String url, Map<String, String> headers) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
            builder = setHeaders(builder, headers);
            HttpRequest request = builder.GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Execute an HTTP POST for the specified URL and return the body of the
     * response as a String.
     * Headers for the request are provided in the map headers.
     * The body of the request is provided as a String.
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static HttpResponse<String> doPost(String url, Map<String, String> headers, String body) {

        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
            builder = setHeaders(builder, headers);
            LOGGER.info("Header in the fetcher is ");
            HttpRequest request = builder.POST((HttpRequest.BodyPublishers.ofString(body)))
                    .build();

            LOGGER.info("The request generated is ");
            LOGGER.info(request);

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            return response;

        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Execute an HTTP PUT for the specified URL and return the body of the
     * response as a String.
     * Headers for the request are provided in the map headers.
     * The body of the request is provided as a String.
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static HttpResponse<String> doPut(String url, Map<String, String> headers, String body) {

        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
            builder = setHeaders(builder, headers);
            HttpRequest request = builder.PUT((HttpRequest.BodyPublishers.ofString(body)))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            return response;

        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Helper method to set the headers of any HttpRequest.Builder.
     *
     * @param builder
     * @param headers
     * @return
     */
    private static HttpRequest.Builder setHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                builder = builder.setHeader(key, headers.get(key));
            }
        }
        return builder;
    }
}
