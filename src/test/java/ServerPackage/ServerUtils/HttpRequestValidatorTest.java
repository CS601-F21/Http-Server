package ServerPackage.ServerUtils;

import ServerPackage.HttpUtils.Validators.HttpRequestValidator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestValidatorTest {

    private static final Logger LOGGER = LogManager.getLogger(HttpRequestValidator.class);
    private String request;
    private String header;
    private boolean valid;
    private String body;
    private String requestType;
    private HashMap<String, String> headerParams;
    private boolean requestIsValid;

    @BeforeEach
    void configureLogger (){
        BasicConfigurator.configure();
    }

    @DisplayName("Passing Valid request, should work")
    @Test
    void test1 (){
        LOGGER.info("Validate Request Test 1");
        request = "GET /favicon.ico HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertTrue(valid);
    }

    @DisplayName("Incorrect HTTP Method")
    @Test
    void test2 (){
        LOGGER.info("Validate Request Test 2");
        request = "GETT /favicon.ico HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);
    }

    @DisplayName("Not passing path")
    @Test
    void test3(){
        LOGGER.info("Validate Request Test 3");
        request = "GET HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);
    }

    @DisplayName("Not passing method")
    @Test
    void test4(){
        LOGGER.info("Validate Request Test 4");
        request = "/favicon.ico HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);
    }

    @DisplayName("Empty Request")
    @Test
    void test5(){
        LOGGER.info("Validate Request Test 5");
        request = "";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);
    }

    @DisplayName("No / before path")
    @Test
    void test6(){
        LOGGER.info("Validate Request Test 6");
        request = "GET favicon.ico HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);
    }

    @DisplayName("Wrong HTTP Format")
    @Test
    void test7(){
        LOGGER.info("Validate Request Test 7");
        request = "GET /favicon.ico HTTP/1.2";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);
    }

    @DisplayName("HTTP not in caps")
    @Test
    void test8() {
        LOGGER.info("Validate Request Test 8");
        request = "GET /favicon.ico http/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);
    }

    @DisplayName("Correct Header")
    @Test
    void test9(){
        LOGGER.info("Validate Header Test 1");
        header = "Accept-Encoding: gzip, deflate";
        valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);
    }

    @DisplayName("Correct Header, with different key-value")
    @Test
    void test10(){
        LOGGER.info("Validate Header Test 2");
        header = "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);
    }

    @DisplayName("No semi-colon after header name")
    @Test
    void test11(){
        LOGGER.info("Validate Header Test 3");
        header = "User-Agent Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv;93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);
    }

    @DisplayName("No header name")
    @Test
    void test12(){
        LOGGER.info("Validate Header Test 3");
        header = " : (X11; Ubuntu; Linux x86_64; rv;93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);
    }

    @DisplayName("No header body")
    @Test
    void test13(){
        LOGGER.info("Validate Header Test 5");
        header = "someHeader:";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);
    }

    @DisplayName("Empty String")
    @Test
    void test14(){
        LOGGER.info("Validate Header Test 5");
        header = "";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);
    }

    @DisplayName("Header value only semi-colon")
    @Test
    void test15(){
        //because a valid header is just one which can be split into 2, if we split by ":"
        LOGGER.info("Validate Header Test 7");
        header = "User-Agent: :::::::::::::::::::::::::::::::::::::::::::::";
        valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);
    }

    @DisplayName("Valid body")
    @Test
    void test16(){
        LOGGER.info("Validate Body Test 1");
        body = "this is the body";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertTrue(requestIsValid);
    }

    @DisplayName("Incorrect body length")
    @Test
    void test17(){
        LOGGER.info("Validate Body Test 2");
        body = "this is the body";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()+1));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertFalse(requestIsValid);
    }

    @DisplayName("Body in GET Request")
    @Test
    void test18(){
        LOGGER.info("Validate Body Test 3");
        body = "this is the body";
        requestType = "GET";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()+1));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertFalse(requestIsValid);
    }

    @DisplayName("Empty body with incorrect length")
    @Test
    void test19(){
        LOGGER.info("Validate Body Test 4");
        body = "";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()+1));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertFalse(requestIsValid);
    }

    @DisplayName("Empty body with correct length")
    @Test
    void test20(){
        LOGGER.info("Validate Body Test 5");
        body = "";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertTrue(requestIsValid);
    }

    @DisplayName("Body with non-alphanumeric characters")
    @Test
    void test21(){
        LOGGER.info("Validate Body Test 5");
        body = "blah blah ___----B^%8L";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertTrue(requestIsValid);
    }

    @DisplayName("PUT Request with body")
    @Test
    void test22(){
        LOGGER.info("Validate Body Test 6");
        body = "blah blah ___----B^%8L";
        requestType = "PUT";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertTrue(requestIsValid);
    }

    @DisplayName("DELETE Request with body")
    @Test
    void test23(){
        LOGGER.info("Validate Body Test 6");
        body = "blah blah ___----B^%8L";
        requestType = "DELETE";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertTrue(requestIsValid);
    }

}