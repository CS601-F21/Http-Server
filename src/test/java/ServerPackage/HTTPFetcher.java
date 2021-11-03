package ServerPackage;

import com.google.gson.Gson;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that provides static methods to send HTTP GET and POST requests.
 */
class HTTPFetcher {
    /**
     * Execute an HTTP GET for the specified URL and return the
     * body of the response as a String.
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
        } catch(URISyntaxException | IOException | InterruptedException e) {
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
            HttpRequest request = builder.POST((HttpRequest.BodyPublishers.ofString(body)))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            return response;

        } catch(URISyntaxException | IOException | InterruptedException e) {
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

        } catch(URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Helper method to set the headers of any HttpRequest.Builder.
     * @param builder
     * @param headers
     * @return
     */
    private static HttpRequest.Builder setHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        if(headers != null) {
            for (String key : headers.keySet()) {
                builder = builder.setHeader(key, headers.get(key));
            }
        }
        return builder;
    }

//    public static void main(String[] args) {
//
////        String url = "https://www.cs.usfca.edu/~srollins/test.html";
////        String response = doGet(url);
//
////        String response = doPost(url, null, "msg=here is a message");
////        System.out.println(response);
////        System.out.println(HtmlValidator.isValid(response));
//
//        Gson gson = new Gson();
//
//        Token tokenObject = null;
//        try {
//            tokenObject = gson.fromJson(new FileReader("token.json"), Token.class);
//        } catch (FileNotFoundException fnf) {
//            fnf.printStackTrace();
//            System.exit(1);
//        }
//
//        String token = tokenObject.getToken();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Bearer " + token);
//        String url = "https://slack.com/api/conversations.list";
//        String response = doGet(url, headers);
//
//        System.out.println("Response: " + response);
//    }
}
