package ServerPackage.HttpUtils.Validators;

import ServerPackage.HttpUtils.HttpConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class MessageBodyValidator {
    private HashMap<String, String> headers;
    private String requestType;
    private String body;
    private boolean valid;
    private boolean requestWarrantsBody;
    private String[] requestsWhichRequireBody;
    private static final Logger LOGGER = LogManager.getLogger(MessageBodyValidator.class);

//    /**
//     * Since the body we receive will be in the following format :
//     *      message=120401325X
//     * The length of the body will be  8 + len(message_content)
//     * in this case it is 8 + 10 = 18.
//     * The way the code is written, the 8 will be a constant
//     * This variable will be used to validate the length of the body
//     *
//     * The String body which we receive in the constructor will just be the message content
//     * we have to subtract this int from the content length of add it to the body.length()
//     */
//    private final int removeFromBodyLength = 8;

    public MessageBodyValidator(HashMap<String, String> headers, String requestType, String body) {
        this.headers = headers;
        this.requestType = requestType;
        this.body = body.strip();
//        LOGGER.info("Received body =========================> " + body + " length of body is ====> " + body.length());
        this.valid = true;
        this.requestWarrantsBody = false;
        requestsWhichRequireBody = HttpConstants.bodyMethods;
        startValidation();
    }

    private void startValidation(){
        //validates that the header can indeed be broken down into 2 parts
        validatePostRequest();
        if (requestWarrantsBody) {
            validateContentLengthIsPresent();
            validateContentLength();
        }
    }

    private void validatePostRequest() {
//        for (String methods : requestsWhichRequireBody){
//            if (requestType.equals(methods)){
//                requestWarrantsBody = true;
//                return;
//            }
//        }
        if (headers.containsKey("Content-Length")){
            requestWarrantsBody = true;
            LOGGER.info("Request has Content-Length field");
            return;
        }

        LOGGER.info("Request does not have a content-length field");
    }

    private void validateContentLengthIsPresent() {
//        for (String key : headers.keySet()){
//            LOGGER.info("Header contains key ---> " +key);
//        }
        if (!headers.containsKey("Content-Length")){
            valid = false;
            LOGGER.info("Content Length or Content Type is not present");
            return;
        }
    }

    private void validateContentLength() {
        if (valid) {
//        LOGGER.info("Content Length in header is : " + headers.get("Content-Length") + " type is : " + headers.get("Content-Length").getClass());
            try {
                int expectedBodyLength = Integer.parseInt(headers.get("Content-Length").strip());
                valid = (expectedBodyLength == body.length());
            } catch (NumberFormatException e){
                LOGGER.info("Content-Length is not a number");
                valid = false;
            }
        }
    }


    public boolean isValid() {
        return valid;
    }

    public boolean requestWarrantsBody(){
        return requestWarrantsBody;
    }
}
