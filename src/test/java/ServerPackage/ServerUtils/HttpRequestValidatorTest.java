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

    @DisplayName("Running all tests")
    @Test
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
        test17();
        test18();
        test19();
        test20();
        test21();
        test22();
        test23();
        test24();
        test25();
        test26();
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
        LOGGER.info("Validate Header Test 9");
        header = "Accept-Encoding: gzip, deflate";
        valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);
    }

    @DisplayName("Correct Header, with different key-value")
    @Test
    void test10(){
        LOGGER.info("Validate Header Test 10");
        header = "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);
    }

    @DisplayName("No semi-colon after header name")
    @Test
    void test11(){
        LOGGER.info("Validate Header Test 11");
        header = "User-Agent Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv;93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);
    }

    @DisplayName("No header name")
    @Test
    void test12(){
        LOGGER.info("Validate Header Test 12");
        header = " : (X11; Ubuntu; Linux x86_64; rv;93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);
    }

    @DisplayName("No header body")
    @Test
    void test13(){
        LOGGER.info("Validate Header Test 13");
        header = "someHeader:";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);
    }

    @DisplayName("Empty String")
    @Test
    void test14(){
        LOGGER.info("Validate Header Test 14");
        header = "";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);
    }

    @DisplayName("Header value only semi-colon")
    @Test
    void test15(){
        //because a valid header is just one which can be split into 2, if we split by ":"
        LOGGER.info("Validate Header Test 15");
        header = "User-Agent: :::::::::::::::::::::::::::::::::::::::::::::";
        valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);
    }

    @DisplayName("Valid body")
    @Test
    void test16(){
        LOGGER.info("Validate Body Test 16");
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
        LOGGER.info("Validate Body Test 17");
        body = "this is the body";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()+1));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertFalse(requestIsValid);
    }

    @DisplayName("Required header present")
    @Test
    void test18(){
        LOGGER.info("Validate Body Test 18");
        body = "this is the body";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()));
        headerParams.put("Content-Type", "Wtv goes in here");
        headerParams.put("Host", "localhost:9090/blahblah");
        requestIsValid = HttpRequestValidator.validateAllHeader(headerParams);
        assertTrue(requestIsValid);
    }

    @DisplayName("Required header not present")
    @Test
    void test19(){
        LOGGER.info("Validate Body Test 19");
        body = "this is the body";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateAllHeader(headerParams);
        assertFalse(requestIsValid);
    }

    @DisplayName("Body in GET Request")
    @Test
    void test20(){
        LOGGER.info("Validate Body Test 20");
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
    void test21(){
        LOGGER.info("Validate Body Test 21");
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
    void test22(){
        LOGGER.info("Validate Body Test 22");
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
    void test23(){
        LOGGER.info("Validate Body Test 23");
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
    void test24(){
        LOGGER.info("Validate Body Test 24");
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
    void test25(){
        LOGGER.info("Validate Body Test 25");
        body = "blah blah ___----B^%8L";
        requestType = "DELETE";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", String.valueOf(body.length()));
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertTrue(requestIsValid);
    }

    @DisplayName("No body length")
    @Test
    void test26(){
        LOGGER.info("Validate Body Test 26");
        body = "this is the body";
        requestType = "POST";
        headerParams = new HashMap<>();
        headerParams.put("Content-Length", "");
        headerParams.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(headerParams, requestType, body);
        assertFalse(requestIsValid);
    }
}