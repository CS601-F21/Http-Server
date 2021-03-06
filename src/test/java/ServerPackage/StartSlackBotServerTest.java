package ServerPackage;

import ServerPackage.Config.ConfigurationManager;
import ServerPackage.Handlers.HomePageHandler;
import ServerPackage.Handlers.ShutdownHandler;
import ServerPackage.Handlers.SlackBotHandler;
import ServerPackage.HtmlUtils.HtmlValidator;
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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartSlackBotServerTest {

    //declaring the logger
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
        int slackBotPort = configurationManager.getSlackBotPort();

        String token = configurationManager.getSlackToken();

        Server slackServer = null;
        try {
            slackServer = new Server(slackBotPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        slackServer.addMapping("/", new HomePageHandler());
        slackServer.addMapping("/slackbot", new SlackBotHandler(token));
        slackServer.addMapping("/shutdown", new ShutdownHandler());
        slackServer.addMapping("/shutdown?", new ShutdownHandler());
        slackServer.start();

    }

    @DisplayName("Testing basic response, should work")
    @Test
    void test1 (){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:9090/slackbot", getHeaders());
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing basic POST request")
    @Test
    void test2 (){
        String message = "from test";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:9090/slackbot", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing basic POST request with no body")
    @Test
    void test3 (){
        String message = "";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:9090/slackbot", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing PUT request")
    @Test
    void test4(){
        String message = "from put";
        HttpResponse<String> response = HTTPFetcher.doPut("http://localhost:9090/slackbot", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 405);
    }

    @DisplayName("Giving invalid address")
    @Test
    void test5(){
        String message = "from test";
        HttpResponse<String> response = HTTPFetcher.doPost("http://localhost:9090/slackbott", getHeaders(), message);
        int responseCode = response.statusCode();
        assertEquals(responseCode, 404);
    }

    @DisplayName("Get request on /slackbot")
    @Test
    void test6(){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:9090/slackbot", getHeaders());
        int responseCode = response.statusCode();
        assertEquals(responseCode, 200);
    }

    @DisplayName("Testing generated XHTML on /slackbot")
    @Test
    void test7(){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:9090/slackbot", getHeaders());
        String html = response.body();
        assertTrue(HtmlValidator.isValid(html));
    }

    @DisplayName("Testing generated XHTML on home page")
    @Test
    void test8(){
        HttpResponse<String> response = HTTPFetcher.doGet("http://localhost:9090/", getHeaders());
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