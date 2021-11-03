/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : to validate the message body
 */
package ServerPackage.HttpUtils.Validators;

import ServerPackage.HttpUtils.HttpConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Once we have the request line and the headers, we can then validate the message body
 */
public class MessageBodyValidator {
    /**
     * the headers we will get from the constructor
     */
    private HashMap<String, String> headers;

    /**
     * request type is given by the user
     */
    private String requestType;

    /**
     * the body itself
     */
    private String body;

    /**
     * boolean to keep a track of whether the body is in valid format or not
     */
    private boolean valid;

    /**
     * Not all methods warrant a body, so for those methods, even if they do not have a body it is fine
     */
    private boolean requestWarrantsBody;

    //ignore this
    private String[] requestsWhichRequireBody;

    /**
     * Logger for loggin purposes
     */
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
        /**
         * instatiating the class variabkles
         */
        this.headers = headers;
        this.requestType = requestType;
        this.body = body.strip();
//        LOGGER.info("Received body =========================> " + body + " length of body is ====> " + body.length());
        this.valid = true;
        this.requestWarrantsBody = false;
        requestsWhichRequireBody = HttpConstants.bodyMethods;

        /**
         * starting the validation
         */
        startValidation();
    }

    private void startValidation(){
        /**
         * validates whether the request requires a body
         *
         * Earlier this method did not let any POST, PUT or DELETE Method to not have a body, but apparently we can have post method
         * without a body
         */
        validateRequestRequiresABody();

        /**
         * we do further tests only if the request requires a body
         */
        if (requestWarrantsBody) {
            /**
             * we check if the content length is present
             */
            validateContentLengthIsPresent();
            /**
             * we validate the content length
             */
            validateContentLength();
        }
    }

    private void validateRequestRequiresABody() {
//        for (String methods : requestsWhichRequireBody){
//            if (requestType.equals(methods)){
//                requestWarrantsBody = true;
//                return;
//            }
//        }

        /**
         * only if the header contains the content-length field, is when it warrants a body
         */
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

        /**
         * If it does not have the content length field it is not valid
         */
        if (!headers.containsKey("Content-Length")){
            valid = false;
            LOGGER.info("Content Length or Content Type is not present");
            return;
        }
    }

    private void validateContentLength() {
        if (valid) {
//        LOGGER.info("Content Length in header is : " + headers.get("Content-Length") + " type is : " + headers.get("Content-Length").getClass());
            /**
             * If the request is valid so far, we convert the content length to an int and then compare the given content length with the
             * actual length of the body
             */
            try {
                /**
                 * converting the content-length to an int
                 */
                int expectedBodyLength = Integer.parseInt(headers.get("Content-Length").strip());

                /**
                 * if the content length and the actual length are the same, then the body is valid
                 */
                valid = (expectedBodyLength == body.length());
            } catch (NumberFormatException e){
                /**
                 * if for some reason we cannot convert the content-length to a int, then it is not valid
                 */
                LOGGER.info("Content-Length is not a number");
                valid = false;
            }
        }
    }


    public boolean isValid() {
        /**
         * called by the user to know whether the body is valid or not
         */
        return valid;
    }

//    public boolean requestWarrantsBody(){
//        return requestWarrantsBody;
//    }
}
