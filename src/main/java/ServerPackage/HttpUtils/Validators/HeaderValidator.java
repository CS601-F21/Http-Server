/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : validate the header
 */
package ServerPackage.HttpUtils.Validators;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * called by the user to ensure that the header they have is indeed in the right format
 */

public class HeaderValidator {

    //instantiating the logegr
    private static final Logger LOGGER = LogManager.getLogger(HeaderValidator.class);

    //boolean to keep track of whether the header is valid or not
    private boolean valid;

    //the header we received
    private String header;

    //the header parts to ensure that a header can indeed be broken down into two parts
    private String headerParts[];

    public HeaderValidator (String header){
        /**
         * instantiating the class variables
         */
//        LOGGER.info("Validating header ==> " + header);
        this.header = header;
        this.valid = true;

        /**
         * starting the validation immediately
         */
        startValidation();
    }

    private void startValidation(){
        //validates that the header can indeed be broken down into 2 parts
        /**
         * first we have to validate the length of the header and that it can indeed be broken down into two parts
         */
        validateHeaderLength();

        /**
         * One property of all the headers that I know of so far is that they begin we a capital letter,
         * this check is to ensure that they do
         */
        validateHeaderStartsWithCapitalLetter();

        /**
         * this was a previous method to ensure that we have the correct header names, but the task to have every possible
         * header name was too cumbersome, and it defeated the purpose as well as it gave a lot of false negatives
         */
//        validateHeaderName();
    }

    private void validateHeaderLength() {
        /**
         * all headers are split into key value pairs, with the key coming before the first colon and the valud coming after it
         * we do that split over here, and split the text into two parts
         */
        headerParts = header.split(":", 2);

//        LOGGER.info("header parts length is : " + headerParts.length);
        /**
         * if the length is not two, then we know that the header is invalid
         * so we return immediately
         */
        if (headerParts.length != 2){
            valid = false;
            return;
        }

        /**
         * next we ensure that neither the key nor the value is empty
         */

        /**
         * checking if the key is empty or not
         */
        if (headerParts[0].strip() == ""){
            LOGGER.info("Header validator, key is empty");
            valid = false;
            return;
        }

        /**
         * checking if the value is empty or not
         */
        if (headerParts[1].strip() == ""){
            LOGGER.info("Header validator, value is empty");
            valid = false;
            return;
        }
    }

    private void validateHeaderStartsWithCapitalLetter() {
        /**
         * this check only happens if we passeed the previous check and the header can indeed be broken down into two parts
         * this is to ensure that even if the header starts with a capital letter, we do not mistakenly change the valid boolean to be true
         * if the header cannot be broken down into two parts
         */
        if (valid) {
            /**
             * checking if the first char of the title is in upper case
             */
            String headerTitle = headerParts[0];
            char[] brokerDownTitle = headerTitle.toCharArray();
            char firstLetter = brokerDownTitle[0];

            if (!Character.isUpperCase(firstLetter)) {
                valid = false;
            }
        }
    }


//    private void validateHeaderName() {
//        if (valid) {
//            String headerName = headerParts[0];
//            boolean validHeaderName = false;
//            String[] commonHeaders = HttpConstants.commonHTTPHeaders;
//
//            for (String name : commonHeaders) {
//                if (headerName.equals(name)) {
//                    validHeaderName = true;
//                    break;
//                }
//            }
//            valid = validHeaderName;
//
//            if (!validHeaderName){
//                LOGGER.info("Header " + headerName + " does not exist" );
//            }
//        }
//    }

    public boolean isValid() {
        /**
         * called by the client so that they can know whether the header is valid or not
         */
        return valid;
    }
}
