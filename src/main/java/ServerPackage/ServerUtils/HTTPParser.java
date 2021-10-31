package ServerPackage.ServerUtils;

import ServerPackage.ServerUtils.Validators.HttpRequestValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;

public class HTTPParser {

    /**
     * TODO create a boolean called valid, the value will initially br true.
     *      for validating the request, lets say we sent the requestLine to get validated, and it is not valid, we change the boolean
     *      to false
     *      then for validating the next next request, header, if valid == true, then only we move along with rest of the code else we return.
     *      this will ensure that even if one part of our request is not valid, we do not get a false positive
     */

    private static final Logger LOGGER = LogManager.getLogger(HTTPParser.class);

    private InputStream inputStream;
    private String fullRequest;
    private HashMap <String, String> header;
    private StringBuffer messageBody;
    private String body;
    private HashMap<String, String> request;
    private boolean requestIsValid;
    private boolean doneProcessingRequest;

    public HTTPParser (InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.header = new HashMap<>();
        this.messageBody = new StringBuffer();
        this.request = new HashMap<>();
        this.requestIsValid = true;
        this.doneProcessingRequest = false;
        fullRequest = generateFullRequest(inputStream);
        LOGGER.info("Full request is : " + fullRequest);
        LOGGER.info("Length of full request is " + fullRequest.length());
        parseFullRequest(fullRequest);
    }

    //did not make this method static on purpose, since we might have different threads accessing it, and might want to add more functionality
    private String generateFullRequest (InputStream inputStream) throws IOException {
        //method takes in an input stream, then reads all the input and returns the full string of the http request
        //https://medium.com/@himalee.tailor/reading-a-http-request-29edd181a6c5 to learn how to parse the entire http request
        StringBuilder request = new StringBuilder();
        do {
            request.append((char) inputStream.read());
        } while (inputStream.available() > 0);
        String parsedFullRequest =  request.toString();
        return parsedFullRequest;
    }

    private void parseFullRequest(String fullRequest) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(fullRequest));

        //the first line will always be the request, so we pass the first line to the generateRequest method
        /**Getting the full request**/
        request = generateRequest(reader.readLine());
        LOGGER.info("Received request : " + request + " valid request : " + requestIsValid);
        //if request is not valid, we return
        if (!requestIsValid){
            LOGGER.info("Got incorrect request");
            return;
        }

        //next to process the headers, we keep sending it in line by line till we encounter an empty line
        String headerLine = reader.readLine();
        while (headerLine.length() > 0){
            //generating header
            header = generateHeader(headerLine, header);
            headerLine = reader.readLine();
        }

        //if request is not valid, we return
        if (!requestIsValid){
            LOGGER.info("Got incorrect header");
            return;
        }

        LOGGER.info("Parsed headers, total number is : " + header.size());


        //now we get the body
        String bodyLine = reader.readLine();
        //we do the same as we did for headers, but this time with  a string instead of a hashmap
        while (bodyLine != null){
            messageBody = generateBody(bodyLine, messageBody);
            bodyLine = reader.readLine();
        }

        body = messageBody.toString();
        String requestType = getRequestType();
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);

        LOGGER.info("Body is : " + getBody());

        return;
    }

    private HashMap<String, String> generateRequest (String requestToBeParsed) {
        String generatedRequest = requestToBeParsed;

        LOGGER.info ("Full request received is : \n" + generatedRequest);

        requestIsValid = HttpRequestValidator.validateRequest(generatedRequest);

        if (requestIsValid){
            HashMap<String, String> parsedRequest = HttpRequestGenerator.generateRequest(generatedRequest);
            return parsedRequest;
        }

        return new HashMap<>();
    }

    private HashMap<String, String> generateHeader (String headerLine, HashMap<String, String> header) {
//        LOGGER.info("Parsing header : " + headerLine);

        requestIsValid = HttpRequestValidator.validateHeader(headerLine);

//        LOGGER.info("Parsing header is valid : " + requestIsValid);

        if (requestIsValid){
            HttpRequestGenerator.generateHeader(headerLine, header);
            return header;
        }

        return new HashMap<>();
    }

    private StringBuffer generateBody (String bodyLine, StringBuffer messageBody) {
        if (requestIsValid){
            messageBody = HttpRequestGenerator.generateBody(bodyLine, messageBody);
            return messageBody;
        }
        return new StringBuffer();
    }

    public String cleanBody (String body){
        String[] splitBody = body.split("=",2);
        String uncleanBody = splitBody[1].strip();
        String[] brokenDownBody = uncleanBody.split("\\+");
        String cleanBody = String.join(" ", brokenDownBody);

        LOGGER.info("Body after cleaning is : " + cleanBody);
        return cleanBody;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        //send unfiltered body
        return body;
    }

    public String getRequestType () {
        return request.get("Type");
    }

    public String getRequestPath () {
        return request.get("Path");
    }

    public String getRequestHttpVersion () {
        return request.get("httpVersion");
    }

    public HashMap<String, String> getRequest () {
        return request;
    }

    public boolean isRequestIsValid (){
        return requestIsValid;
    }
}
