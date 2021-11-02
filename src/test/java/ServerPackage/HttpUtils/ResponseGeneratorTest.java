package ServerPackage.HttpUtils;

/**
 * TODO
 */

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResponseGeneratorTest {

    private ResponseGenerator responseGenerator;
    private static final Logger LOGGER = LogManager.getLogger(ResponseGeneratorTest.class);

    @BeforeEach
    void instantiateGenerator (){
        BasicConfigurator.configure();
        this.responseGenerator = new ResponseGenerator();
    }

    @Test
    void generateGETResponse() {
        String expectedResponse =
                "HTTP/1.1 404 Not Found" + HttpConstants.CRLF+
                        "Content-Length:95"+ HttpConstants.CRLF+
                        HttpConstants.CRLF+
                        "<html><head><title>Test App</title></head><body><h1>Error 404 Page Not Found</h1></body></html>"+
                        HttpConstants.CRLF + HttpConstants.CRLF;

        LOGGER.info(responseGenerator.generateNOTFOUNDResponse());
        assertEquals(expectedResponse, responseGenerator.generateNOTFOUNDResponse());
    }

    @Test
    void generateInvertedIndexResponse() {
        String expectedResponse =
                "HTTP/1.1 200 OK" + HttpConstants.CRLF+
                        "Content-Length:1147"+ HttpConstants.CRLF+
                        HttpConstants.CRLF+
                        "<!DOCTYPE html> <html lang=\"en\" style = \"height: 100%; width: 100%\"> <head> <link rel=\"shortcut icon\" href=\"#\">\n" +
                        " <title>Inverted Index</title></head><body style = \"height: 100%; width: 100%;\"><div class = \"main\" style=\"height: 100%; " +
                        "margin-left: 20%; margin-right: 20%; margin-top: 5%;\"><div class = \"formContainer\" style=\"display: flex; height: 5%;\">" +
                        "<form action = /invertedindex style = \" width:80% \"method=POST><label for = \"message\" style = " +
                        "\"margin-right:2%; margin-left:3.5%\">Enter value</label><input type = \"text\" id = \"message\" " +
                        "name=\"message\"/><input type = \"submit\" value=\"submit\"/></form><form action=\"/shutdown\" style=\" width:20%; display: " +
                        "flex; justify-content: right; \" method=\"GET\">\n" +
                        "    <input type=\"submit\" style=\"height: 70%; margin-right: 6%;\" value=\"shutdown\">\n" +
                        "</form></div><div class = \"outputContainer\" style=\"height: 80%; overflow: scroll;\"><ul class = \"list\" style=\"list-style-type: none;\">" +
                        "<li style=\"border: solid; border-bottom: none;\">1</li><li style=\"border: solid; border-bottom: solid;\">really long string</li></ul></div>" +
                        "</div></body> </html>\n"+
                        HttpConstants.CRLF + HttpConstants.CRLF;

        ArrayList<String> testOutputs = new ArrayList<>();
        testOutputs.add("1");
        testOutputs.add("really long string");
        String actualResponse = responseGenerator.generateInvertedIndexResponse("Inverted Index", "/invertedindex", "Enter value", testOutputs);

        LOGGER.info(actualResponse.length() + " ---> " + expectedResponse.length());
        LOGGER.info(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void generateSingleLineResponse() {
    }

    @Test
    void generateHomePageResponse() {
    }

    @Test
    void generateNOTFOUNDResponse() {
    }

    @Test
    void generateBADREQUESTResponse() {
    }

    @Test
    void generateMETHODNOTALLOWEDResponse() {
    }
}