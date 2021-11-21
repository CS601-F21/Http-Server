package ServerPackage;

import ServerPackage.Config.ConfigurationManager;
import ServerPackage.Handlers.*;
import ServerPackage.HtmlUtils.HtmlValidator;
import ServerPackage.InvertedIndex.InvertedIndexUI;
import ServerPackage.InvertedIndex.QAList;
import ServerPackage.InvertedIndex.ReviewList;
import ServerPackage.Servers.Server;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class StartInvertedIndexServerTest {

    private static final Logger LOGGER = LogManager.getLogger(StartSlackBotServerTest.class);
    @BeforeAll
    static void startServer (){
        /**
         * Configuring the logger
         */
        BasicConfigurator.configure();

        /**
         * We have a config file with a hardcoded location, the file is in json format, and the configurationanager
         */
        ConfigurationManager configurationManager = new ConfigurationManager("/home/shubham/IdeaProjects/project3-shubham0831/configuration.json");

        /**
         * Getting the port number for the respective severs
         */
        int indexPort = configurationManager.getIndexPort();

        String file1 = "/home/shubham/IdeaProjects/project3-shubham0831/Cell_Phones_and_Accessories_5.json";
        String file2 = "/home/shubham/IdeaProjects/project3-shubham0831/qa_Cell_Phones_and_Accessories.json";

        /**
         * The ReviewList and the QAList objects which are needed for starting the inverted index
         */
        ReviewList reviewList = new ReviewList("ISO-8859-1"); //creating ReviewList
        QAList qaList = new QAList("ISO-8859-1"); //creating QAList

        /**
         * Initializing the inverted index
         */
        InvertedIndexUI invertedIndex = new InvertedIndexUI(reviewList, file1, qaList, file2);


        Server indexServer = null;
        try {
            indexServer = new Server(indexPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexServer.addMapping("/", new HomePageHandler());
        indexServer.addMapping("/find", new FindHandler(invertedIndex));
        indexServer.addMapping("/reviewsearch", new ReviewSearchHandler(invertedIndex));
        indexServer.addMapping("/shutdown", new ShutdownHandler());
        indexServer.addMapping("/shutdown?", new ShutdownHandler());
        indexServer.start();

    }

    @DisplayName("Testing basic response for /find, should work")
    @Test
    void test1 (){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:8080/find", getHeaders());
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing basic response for /reviewsearch, should work")
    @Test
    void test2 (){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:8080/reviewsearch", getHeaders());
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing basic response for homepage, should work")
    @Test
    void test3 (){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:8080/", getHeaders());
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing basic POST request on review search")
    @Test
    void test4 (){
        String message = "fireworks";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:8080/reviewsearch", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing POST request on reviewsearch with non-alphanumeric characters")
    @Test
    void test5 (){
        String message = "fireworks$%^&";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:8080/reviewsearch", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }


    @DisplayName("Testing basic POST request on find")
    @Test
    void test6 (){
        String message = "8288853439";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:8080/find", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing POST request on find with non-alphanumeric characters")
    @Test
    void test7 (){
        String message = "fireworks$%^&";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:8080/find", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing PUT request on /reviewsearch")
    @Test
    void test8(){
        String message = "from put";
        HttpResponse<String> response = HTTPFetcher.doPut("http://localhost:8080/reviewsearch", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 405);
    }

    @DisplayName("Testing PUT request on /find")
    @Test
    void test9(){
        String message = "from put";
        HttpResponse<String> response = HTTPFetcher.doPut("http://localhost:8080/find", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 405);
    }

    @DisplayName("Giving invalid address")
    @Test
    void test10(){
        String message = "from test";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:8080/slackbott", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 404);
    }

    @DisplayName("Testing generated XHTML on /reviewsearch GET")
    @Test
    void test11(){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:8080/reviewsearch", getHeaders());
        String html = response.body();
        assertTrue(HtmlValidator.isValid(html));
    }

    @DisplayName("Testing generated XHTML on /reviewsearch POST")
    @Test
    void test12(){
        String message = "fireworks";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:8080/reviewsearch", getHeaders(), message);
        String html = response.body();
        assertTrue(HtmlValidator.isValid(html));
    }

    @DisplayName("Testing generated XHTML on /find GET")
    @Test
    void test13(){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:8080/reviewsearch", getHeaders());
        String html = response.body();
        assertTrue(HtmlValidator.isValid(html));
    }

    @DisplayName("Testing generated XHTML on /find POST")
    @Test
    void test14(){
        String message = "8288853439";
        HttpResponse<String> response = HTTPFetcher.doPut("http://localhost:8080/reviewsearch", getHeaders(), message);
        String html = response.body();
        assertTrue(HtmlValidator.isValid(html));
    }

    @DisplayName("Testing generated XHTML on home page")
    @Test
    void test15(){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:8080/", getHeaders());
        String html = response.body();
        assertTrue(HtmlValidator.isValid(html));
    }

    private HashMap<String,String> getHeaders() {
//        String request =
//                "POST /slackbot HTTP/1.1\n" +
//                        "Host: localhost:9090\n" +
//                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
//                        "Content-Type: application/x-www-form-urlencoded\n" +
//                        "Content-Length: 9\n" +
//                        "Origin: http://localhost:9090\n" +
//                        "DNT: 1\n" +
//                        "Connection: keep-alive\n" +
//                        "\n" +
//                        "message=a";

        HashMap<String, String> map = new HashMap<>();
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
        map.put("Content-Type","application/x-www-form-urlencoded");
        map.put("Origin", "http://localhost:9090");
        map.put("DNT","1");
        return map;
    }

}