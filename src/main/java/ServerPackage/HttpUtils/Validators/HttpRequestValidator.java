/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : to validate the request
 */
package ServerPackage.HttpUtils.Validators;

import java.util.HashMap;

/**
 * Called by the client to ensure that the request they have gotten is valid or not
 *
 * The only argument in the constructor is the request line itself
 *
 * All the methods are static as they are independent of each other
 */

public class HttpRequestValidator {

    public static boolean validateRequest (String request){
        /**
         * Method called by the client ot ensure that the request line is valid or not.
         * This in turn calls the request validator and hands the job to that object
         */
        RequestValidator requestValidator = new RequestValidator(request);

        /**
         * Once the request validator has validated the request line, we return the boolean
         */
        boolean valid = requestValidator.isValid();

        /**
         * returning the boolean
         */
        return valid;
    }
    
    public static boolean validateHeader (String header){
        /**
         * Method called by the client ot validate the header line
         *
         * This in turn calls the header validator and assigns the job to that object
         */
        HeaderValidator headerValidator = new HeaderValidator(header);
        /**
         * Once the header validator has validated the header line, we return the boolean
         */
        boolean valid = headerValidator.isValid();

        /**
         * returning the boolean
         */
        return valid;
    }
    
    public static boolean validateBody (HashMap<String, String> headers, String requestType, String body){
        /**
         * Method called by the client to validate the body, to validate the body we will need the request type and the headers
         * as well so we take those as the input parameters.
         *
         * We then hand over this task to the MessageBody validator
         */
        MessageBodyValidator messageBodyValidator = new MessageBodyValidator(headers, requestType, body);

        /**
         * Once the message body validator has validated the message body, we return the boolean
         */
        boolean valid = messageBodyValidator.isValid();

        /**
         * returning the boolean
         */
        return valid;
    }

    /**
     * Method will be called once we have validated all our headers are in correct format, we validate whether
     * we have validate whether we have the required headers.
     * @param header
     * @return
     */
    public static boolean validateAllHeader(HashMap<String, String> header) {
        /**
         * So far the only required header I know is the Host header, so we check for that over here
         */
        if (header.containsKey("Host")){
            /**
             * if the host header is available, we return true
             */
            return true;
        }

        /**
         * if the host header is not available we return false
         */
        return false;
    }
}
