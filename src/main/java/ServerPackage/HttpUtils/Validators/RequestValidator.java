/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : to validate the request line
 */
package ServerPackage.HttpUtils.Validators;

import ServerPackage.HttpUtils.HttpConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The purpose of this class is to validate the request line
 */

public class RequestValidator {
    /**
     * logger for logging purposes
     */
    private static final Logger LOGGER = LogManager.getLogger(RequestValidator.class);

    /**
     * boolean to keep a track of whether the request is valid or not
     */
    private boolean valid;

    /**
     * the request itself
     */
    private String request;

    /**
     * the broken down request, if the request is valid, it will have a length of 3
     */
    String brokenDownRequest[];

    public RequestValidator (String request){
        /**
         * the constructor takes in the requst line from the client
         */
        this.request = request;

        /**
         * initial assumption is that the request is valid
         */
        this.valid = true;

        /**
         * then we start the validation
         */
        startValidation();
    }

    private void startValidation(){
        /**
         * validate requestLength checks for whether the request can be broken down into 3 parts or not
         */
        validateRequestLength();
        /**
         * validate requestType checks for whether the request method is a Http method or not
         */
        validateRequestType();
        /**
         * validate requestpath checks for whether the request path is present or not, if yes then whether it is in the right format
         */
        validateRequestPath();
        /**
         * finally we validate the httpversion
         */
        validateHttpVersion();
    }

    private void validateRequestLength(){
//        LOGGER.info("Validating length");
        /**
         * method checks if the request can be split into 3 parts or not
         * if not then it sets valid as false
         */
        brokenDownRequest = request.split(" ");
        if (brokenDownRequest.length != 3){
            valid = false;
        }
//        LOGGER.info("Length is " + valid);
    }

    private void validateRequestType(){
        /**
         * if the request can indeed be broken down into 3 parts, we then check whether the method in the request is
         * actually a http method or not
         */
        if (valid) {
//            LOGGER.info("Validating method");
            /**
             * getting an array of all the http methods
             */
            String[] allRequestTypes = HttpConstants.allMethods;

            /**
             * boolean which keeps a track of whether the method in the request is a http method or not
             * initially it is set to false
             */
            boolean requestInRequestType = false;

            /**
             * getting the method itself
             */
            String requestMethod = brokenDownRequest[0];

            /**
             * runnning a for loop over all the http methods, if we find one, then we update the requestInRequestType
             * and break the loop
             */
            for (int i = 0; i < allRequestTypes.length; i++) {
                if (requestMethod.equals(allRequestTypes[i])) {
                    requestInRequestType = true;
                    break;
                }
            }

            /**
             * setting the value of valid to the requestinRequestType
             */
            valid = requestInRequestType;
//            LOGGER.info("Method is " + valid);
        }
    }

    private void validateRequestPath(){
        /**
         * if the method is also valid, then we check whether the path is in the correct format or not
         * that just means that it should start with a /
         */
        if (valid) {
//            LOGGER.info("Validating Path");
            String path = brokenDownRequest[1];

            //getting the path and then breaking it down to char array
            char[] brokenDownPath = path.toCharArray();

            //only requirement for path is that it should start with a / afaik
            //checking if the first character is a / or not
            if (!(brokenDownPath[0] == '/')) {
                valid = false;
            }
//            LOGGER.info("Path is " + valid);
        }
    }

    private void validateHttpVersion(){
        /**
         * we only support Http1.1, so if a request is of another version we return false
         */
        if (valid) {
//            LOGGER.info("Validating Http Version");
            String version = brokenDownRequest[2];
            valid = (version.equals(HttpConstants.VERSION));
//            LOGGER.info("Version is " + valid);
        }

    }

    public boolean isValid(){
        /**
         * called by the user to check whether the request is valid or not
         */
        return valid;
    }
}
