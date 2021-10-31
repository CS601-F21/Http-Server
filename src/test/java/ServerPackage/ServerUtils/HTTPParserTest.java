package ServerPackage.ServerUtils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class HTTPParserTest {

    private static final Logger LOGGER = LogManager.getLogger(HTTPParserTest.class);

    @BeforeEach
    void makeLoggerWork (){
        BasicConfigurator.configure();
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
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
                    "Sec-Fetch-User: ?1";

            HTTPParser parser = getLoadedParser(httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("POST request with no content length - should not work")
    void test12 () {
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
                    "Cache-Control: max-age=0\n";

            HTTPParser parser = getLoadedParser(httpRequest);
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