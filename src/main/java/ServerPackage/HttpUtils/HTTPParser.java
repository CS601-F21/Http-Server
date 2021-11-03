/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : parsing the http request
 */
package ServerPackage.HttpUtils;

import ServerPackage.HttpUtils.Validators.HttpRequestValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;

/**
 * This is the class where we parse the http request, we take an inputstream from the client and then parse that.
 */

public class HTTPParser {


    /**
     * Logger for logging purposes
     */
    private static final Logger LOGGER = LogManager.getLogger(HTTPParser.class);

    /**
     * The input stream which we get from the client
     */
    private InputStream inputStream;

    /**
     * The class variables
     */
    private String fullRequest; //stores the full request we get from the input stream
    private HashMap <String, String> header; //hashmap containing the headers
    private StringBuffer messageBody; //the stringbuffer we use to keep appending the message body to
    private String body;  // the actual body
    private HashMap<String, String> request; //the request line broken down into the request, path and the http version
    private boolean requestIsValid; //boolean to know if the request is valid
    private boolean doneProcessingRequest;

    public HTTPParser (InputStream inputStream) throws IOException {
        /**
         * Instantiating the variables
         */
        this.inputStream = inputStream;
        this.header = new HashMap<>();
        this.messageBody = new StringBuffer();
        this.request = new HashMap<>();
        this.requestIsValid = true;
        this.doneProcessingRequest = false;

        /**
         * The generate full request takes in the input stream and converts it into a string, we then assign the string to the
         * full request variable
         */
        fullRequest = generateFullRequest(this.inputStream);
        LOGGER.info("Full request is : " + fullRequest);
        LOGGER.info("Length of full request is " + fullRequest.length());

        /**
         * Once we have the full request, we parse it, aka break it down into chunks like the header, body and the request line
         */
        parseFullRequest(fullRequest);
    }

    //did not make this method static on purpose, since we might have different threads accessing it, and might want to add more functionality
    private String generateFullRequest (InputStream inputStream) throws IOException {
        //method takes in an input stream, then reads all the input and returns the full string of the http request
        //https://medium.com/@himalee.tailor/reading-a-http-request-29edd181a6c5 to learn how to parse the entire http request
        /**
         * method takes in an input stream, created a stringBuilder and continuously appends characters from the stream to the stringBuilder
         * then it finally returns the stringBuilder.toString()
         *
         * Over here we are creating the stringBuilder
         */
        StringBuilder request = new StringBuilder();
        /**
         * Got this way of building the request form the link mentioned above
         */
        do {
            request.append((char) inputStream.read());
        } while (inputStream.available() > 0);
        /**
         * converting the stringBuilder to string
         */
        String parsedFullRequest =  request.toString();

        /**
         * returning the string
         */
        return parsedFullRequest;
    }

    private void parseFullRequest(String fullRequest) throws IOException {
        /**
         * using a buffered reader to read the string line by line
         */
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
        //this check is there, in case we get a request with no headers
        if (headerLine != null) {
            while (headerLine.length() > 0) {
                //generating header
                header = generateHeader(headerLine, header);
                if (!requestIsValid) break;
                headerLine = reader.readLine();
            }
        } else {
            //if there is no headers, then we set the request is valid boolean to false
            requestIsValid = false;
        }

        //if request is not valid, we return
        if (!requestIsValid){
            LOGGER.info("Got incorrect header format");
            return;
        }

        /**
         * Once we have the whole header hashmap, we check whether it has the required fields such as the host
         */
        requestIsValid = HttpRequestValidator.validateAllHeader(header);

        /**
         * if it does not, we say that the request is not valid
         */
        if (!requestIsValid){
            LOGGER.info("Invalid headers, no Host field");
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

        /**
         * converting the string builder to a string
         */
        body = messageBody.toString();
        String requestType = getRequestType();
        /**
         * to check if the body is valid, we need to know the request type and have all the header fields available to us as well
         * particularly the content-length field
         */
        requestIsValid = HttpRequestValidator.validateBody(header, requestType, body);

        LOGGER.info("Body is : " + getBody());

        return;
    }

    private HashMap<String, String> generateRequest (String requestToBeParsed) {
        /**
         * method takes in a line (namely the first line from the request) and breaks it down into the
         * requestType, the path and the Httpversion
         */

        /**
         * doing this assignment here, because the variable name is much more convienient
         */
        String generatedRequest = requestToBeParsed;

        LOGGER.info ("Full request received is : \n" + generatedRequest);

        /**
         * first we validate the request line, because if it is not valid, we will get an error trying to break it down
         */
        requestIsValid = HttpRequestValidator.validateRequest(generatedRequest);

        if (requestIsValid){
            /**
             * The request generator takes in the valid generatedRequest and returns a hashmap of the requst line broken down into smaller
             * pieces
             */
            HashMap<String, String> parsedRequest = HttpRequestGenerator.generateRequest(generatedRequest);
            return parsedRequest;
        }

        /**
         * if the request is not valid, we return an empty hashmap
         */
        return new HashMap<>();
    }

    private HashMap<String, String> generateHeader (String headerLine, HashMap<String, String> header) {
        /**
         * method takes in a headerline, validates whether it is in the correct format and then adds it to the header hashmap
         */
//        LOGGER.info("Parsing header : " + headerLine);

        /**
         * checking for validity
         */
        requestIsValid = HttpRequestValidator.validateHeader(headerLine);

//        LOGGER.info("Parsing header is valid : " + requestIsValid);

        if (requestIsValid){
            /**
             * if the line is a valid header line, we add it to the hash map
             */
            HttpRequestGenerator.generateHeader(headerLine, header);
            return header;
        }

        /**
         * if the line is not valid, we return an empty hashmap
         */
        return new HashMap<>();
    }

    private StringBuffer generateBody (String bodyLine, StringBuffer messageBody) {
        /**
         * method takes in the bodyline and keeps appending it to the messagebody string builder if it is valid
         */
        if (requestIsValid){
            messageBody = HttpRequestGenerator.generateBody(bodyLine, messageBody);
            return messageBody;
        }
        /**
         * we then return the string builder
         */
        return new StringBuffer();
    }

    public String cleanBody (String body){
        /**
         * method takes in the body string and cleans it up for future use
         */
        /**
         * This is mainly for the test cases, as the http fetcher is sending an already clean body
         */
        if (!body.startsWith("message=")){
            return body.strip();
        }

        /**
         * the body we get is in the format message=something
         * we do not need the message= string, so we remove that over here
         */
        LOGGER.info("Received body " + body);
        String[] splitBody = body.split("=",2);
        String uncleanBody = splitBody[1].strip();
        /**
         * split the body based on any extra spaces
         */
        String[] brokenDownBody = uncleanBody.split("\\+");
        /**
         * combine the array again, and then strip() the body
         */
        String cleanBody = String.join(" ", brokenDownBody).strip();

        LOGGER.info("Body after cleaning is : " + cleanBody);
        /**
         * finally we return the clean body
         */
        return cleanBody;
    }

    /**
     * getters
     * @return
     */
    public HashMap<String, String> getHeader() {
        /**
         * the method the user calls when they wants to get the headers hashmap
         */
        return header;
    }

    public String getBody() {
        /**
         * method user calls when they want to get the body
         */
        return body.strip();
    }

    public String getRequestType () {
        /**
         * method called by the user when they want to know the type of the request
         */
        return request.get("Type");
    }

    public String getRequestPath () {
        /**
         * method called by the user if they want to know the path of the request
         */
        return request.get("Path");
    }

    public String getRequestHttpVersion () {
        /**
         * method called by the user if they want to know the http version
         */
        return request.get("HttpVersion");
    }

    public HashMap<String, String> getRequest () {
        /**
         * method to return the request hashmap
         */
        return request;
    }

    public boolean isRequestIsValid (){
        /**
         * method called by the user to check if the request is valid or not
         */
        return requestIsValid;
    }
}
