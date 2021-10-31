package ServerPackage.ServerUtils;

import ServerPackage.ServerUtils.Validators.HttpRequestValidator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestValidatorTest {

    private static final Logger LOGGER = LogManager.getLogger(HttpRequestValidator.class);

    @BeforeEach
    void configureLogger (){
        BasicConfigurator.configure();
    }

    @Test
    void validateRequest() {
        LOGGER.info("Validate Request Test 1");
        String request = "GET /favicon.ico HTTP/1.1";
        boolean valid = HttpRequestValidator.validateRequest(request);
        assertTrue(valid);

        LOGGER.info("Validate Request Test 2");
        request = "GETT /favicon.ico HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);

        LOGGER.info("Validate Request Test 3");
        request = "GET HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);

        LOGGER.info("Validate Request Test 4");
        request = "/favicon.ico HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);

        LOGGER.info("Validate Request Test 5");
        request = "";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);

        LOGGER.info("Validate Request Test 6");
        request = "GET favicon.ico HTTP/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);

        LOGGER.info("Validate Request Test 7");
        request = "GET /favicon.ico HTTP/1.2";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);

        LOGGER.info("Validate Request Test 8");
        request = "GET /favicon.ico http/1.1";
        valid = HttpRequestValidator.validateRequest(request);
        assertFalse(valid);
    }

    @Test
    void validateHeader() {
        LOGGER.info("Validate Header Test 1");
        String header = "Accept-Encoding: gzip, deflate";
        boolean valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);

        LOGGER.info("Validate Header Test 2");
        header = "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);


        LOGGER.info("Validate Header Test 3");
//        LOGGER.info("No semi-colon after header name");
        header = "User-Agent Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);

        LOGGER.info("Validate Header Test 4");
        header = " : Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:93.0) Gecko/20100101 Firefox/93.0";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);

        LOGGER.info("Validate Header Test 5");
        header = "someHeader:";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);

        LOGGER.info("Validate Header Test 6");
        header = "";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);

        //because a valid header is just one which can be split into 2, if we split by ":"
        LOGGER.info("Validate Header Test 7");
        header = "User-Agent: :::::::::::::::::::::::::::::::::::::::::::::";
        valid = HttpRequestValidator.validateHeader(header);
        assertTrue(valid);

        //invalid, because header name is not valid
        LOGGER.info("Validate Header Test 8");
        header = "someHeader: :::::::::::::::::::::::::::::::::::::::::::::";
        valid = HttpRequestValidator.validateHeader(header);
        assertFalse(valid);

    }

    @Test
    void validateBody() {
        LOGGER.info("Validate Body Test 1");
        String body = "this is the body";
        String requestType = "POST";
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Length", String.valueOf(body.length()));
        header.put("Content-Type", "Wtv goes in here");
        boolean requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);
        assertTrue(requestIsValid);

        LOGGER.info("Validate Body Test 2");
        body = "this is the body";
        requestType = "POST";
        header = new HashMap<>();
        header.put("Content-Length", String.valueOf(body.length()+1));
        header.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);
        assertFalse(requestIsValid);

        //should return true, because it doesn't care about the body as the method is GET
        LOGGER.info("Validate Body Test 3");
        body = "this is the body";
        requestType = "GET";
        header = new HashMap<>();
        header.put("Content-Length", String.valueOf(body.length()+1));
        header.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);
        assertTrue(requestIsValid);

        LOGGER.info("Validate Body Test 4");
        body = "";
        requestType = "POST";
        header = new HashMap<>();
        header.put("Content-Length", String.valueOf(body.length()+1));
        header.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);
        assertFalse(requestIsValid);

        LOGGER.info("Validate Body Test 5");
        body = "";
        requestType = "POST";
        header = new HashMap<>();
        header.put("Content-Length", String.valueOf(body.length()));
        header.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);
        assertTrue(requestIsValid);

        LOGGER.info("Validate Body Test 5");
        body = "blah blah ___----B^%8L";
        requestType = "POST";
        header = new HashMap<>();
        header.put("Content-Length", String.valueOf(body.length()));
        header.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);
        assertTrue(requestIsValid);

        LOGGER.info("Validate Body Test 6");
        body = "blah blah ___----B^%8L";
        requestType = "PUT";
        header = new HashMap<>();
        header.put("Content-Length", String.valueOf(body.length()));
        header.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);
        assertTrue(requestIsValid);

        LOGGER.info("Validate Body Test 7");
        body = "blah blah ___----B^%8L";
        requestType = "DELETE";
        header = new HashMap<>();
        header.put("Content-Length", String.valueOf(body.length()));
        header.put("Content-Type", "Wtv goes in here");
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);
        assertTrue(requestIsValid);

    }
}