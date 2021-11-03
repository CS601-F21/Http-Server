/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class function : Testing Http parser
 */

/**
 * These test cases only test for whether the HttpParser is able to parse the Http requests properly or not. In this class we are not bothered with
 * how it is able to tell us whether a given Http request is valid or not.
 *
 * Testing of the individual methods, such as methods which actually validate the individual components of the requests are done in the HttpRequestValidatorTest
 * file and can be found over there
 */
package ServerPackage.ServerUtils;

import ServerPackage.HttpUtils.HTTPParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HTTPParserTest {

    private static final Logger LOGGER = LogManager.getLogger(HTTPParserTest.class);
    private HashMap<String,String> getRequestHeaders;

    @BeforeEach
    void makeLoggerWork (){
        BasicConfigurator.configure();
    }

    void runAllTests(){
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();
        test9();
        test10();
        test11();
        test12();
        test13();
        test14();
        test15();
        test16();
    }

    @Test
    @DisplayName("Normal GET request - should work")
    void test1 () {
        try {
            String httpRequest = "GET /slackbot HTTP/1.1\n" +
                    "Host: localhost:9090\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertTrue(isValid);

            HashMap<String, String> request = parser.getRequest();
            HashMap<String, String> headers = parser.getHeader();

            assertTrue(headers.size() ==  12);
            assertTrue(request.get("Type").equals("GET"));
            assertTrue(request.get("Path").equals("/slackbot"));
            assertTrue(request.get("HttpVersion").equals("HTTP/1.1"));
            assertTrue(parser.getBody().equals(""));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Normal POST request - should work")
    void test2 () {
        try {
            String httpRequest = "POST /reviewsearch HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Referer: http://localhost:8080/reviewsearch\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: 15\n" +
                    "Origin: http://localhost:8080\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: same-origin\n" +
                    "Sec-Fetch-User: ?1\n" +
                    "Cache-Control: max-age=0\n" +
                    "\n" +
                    "message=testing";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertTrue(isValid);

            HashMap<String, String> request = parser.getRequest();
            HashMap<String, String> headers = parser.getHeader();

            assertTrue(headers.size() ==  17);
            assertTrue(request.get("Type").equals("POST"));
            assertTrue(request.get("Path").equals("/reviewsearch"));
            assertTrue(request.get("HttpVersion").equals("HTTP/1.1"));

            String body = parser.getBody();
            assertEquals(body,"message=testing");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("GET request with no path - should not work")
    void test3 () {
        try {
            String httpRequest = "GET HTTP/1.1\n" +
                    "Host: localhost:9090\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("request with no method - should not work")
    void test4 () {
        try {
            String httpRequest = "/slackbot HTTP/1.1\n" +
                    "Host: localhost:9090\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Request with no HTTP version - should not work")
    void test5 () {
        try {
            String httpRequest = "GET /slackbot\n" +
                    "Host: localhost:9090\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Request without request (lol) - should not work")
    void test6 () {
        try {
            String httpRequest =
                    "Host: localhost:9090\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Request with no headers - should not work")
    void test7 () {
        try {
            String httpRequest = "GET /slackbot HTTP/1.1\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Request with header key not having an associated value - should not work")
    void test8 () {
        try {
            String httpRequest = "GET /slackbot HTTP/1.1\n" +
                    "Host: \n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Request with non HTTP method - should not work")
    void test9 () {
        try {
            String httpRequest = "NOMETHOD /slackbot HTTP/1.1\n" +
                    "Host: localhost:9090\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Request with HTTP method not capitalized - should not work")
    void test10 () {
        try {
            String httpRequest = "get /slackbot HTTP/1.1\n" +
                    "Host: localhost:9090\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Request with incorrect HTTP version - should not work")
    void test11 () {
        try {
            String httpRequest = "GET /slackbot HTTP/9000\n" +
                    "Host: localhost:9090\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-User: ?1\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("POST request with no content length - should not work")
    void test12 () {
        /**
         * POST Request does not have to have a content length, this error will be caught by the HttpRequestValidator, when it checks that
         * the content length field is empty and it has a body
         */
        try {
            String httpRequest = "POST /reviewsearch HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Referer: http://localhost:8080/reviewsearch\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Origin: http://localhost:8080\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: same-origin\n" +
                    "Sec-Fetch-User: ?1\n" +
                    "Cache-Control: max-age=0\n" +
                    "\n" +
                    "message=testing";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertTrue(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("POST request with incorrect content length - should not work")
    void test13 () {
        try {
            String httpRequest = "POST /reviewsearch HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Referer: http://localhost:8080/reviewsearch\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: 18\n" +
                    "Origin: http://localhost:8080\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: same-origin\n" +
                    "Sec-Fetch-User: ?1\n" +
                    "Cache-Control: max-age=0\n" +
                    "\n" +
                    "message=testing";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("POST request with no body - should not work")
    void test14 () {
        try {
            String httpRequest = "POST /reviewsearch HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Referer: http://localhost:8080/reviewsearch\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: 18\n" +
                    "Origin: http://localhost:8080\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: same-origin\n" +
                    "Sec-Fetch-User: ?1\n" +
                    "Cache-Control: max-age=0\n\n";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("POST request with no blank space before body - should not work")
    void test15 () {
        try {
            String httpRequest = "POST /reviewsearch HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Referer: http://localhost:8080/reviewsearch\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: 18\n" +
                    "Origin: http://localhost:8080\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-Site: same-origin\n" +
                    "Sec-Fetch-User: ?1\n" +
                    "Cache-Control: max-age=0\n" +
                    "message=testing";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("No request - should not work (obviously)")
    void test16 () {
        try {
            String httpRequest = "";

            HTTPParser parser = getLoadedParser(httpRequest);
            boolean isValid = parser.isRequestIsValid();
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HTTPParser getLoadedParser (String httpRequest) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());
        HTTPParser parser = new HTTPParser(inputStream);
        return parser;
    }
}