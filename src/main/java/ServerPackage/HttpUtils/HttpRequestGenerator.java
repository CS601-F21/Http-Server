/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : class where we actually generate the request
 */
package ServerPackage.HttpUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Generating the request is done in this class, as it is a specific job and having a new class for it seemed to keep things a lot more simpler
 *
 * All methods are static as they are independent and do not rely on anything outside the method itself
 *
 */
public class HttpRequestGenerator {

    //setting up the logger
    private static final Logger LOGGER = LogManager.getLogger(HttpRequestGenerator.class);

    public static HashMap<String, String> generateRequest (String request){
        /**
         * Method to generate the request, is called only if the request is valid.
         * The method takes in a single line and breaks the request down into the
         * request type, the path and the http version
         */
        LOGGER.info("Request generator got line => " + request);
        /**
         * we split on spaces as that is the http convention
         */
        String[] brokenDownRequest = request.split(" ");

        /**
         * creating anf then filling up the request hashmap
         */
        HashMap<String, String> parsedRequest = new HashMap<>();
        parsedRequest.put("Type", brokenDownRequest[0]);
        parsedRequest.put("Path", brokenDownRequest[1]);
        parsedRequest.put("HttpVersion", brokenDownRequest[2]);

        /**
         * returning the request hashmap
         */
        return parsedRequest;
    }

    public static HashMap<String, String> generateHeader(String headerLine, HashMap<String, String> header) {
        /**
         * method to generate the header, takes in the headerLine and the header hashmap. The method is only called if the header line
         * is a valid
         */

        /**
         * We split the header on the semi-colon, the 2 is there to indicate that we want to split the string into 2 parts
         */
        String[] headerParts = headerLine.split(":", 2);
        /**
         * the first part of the split will be the header label and the second part of the string will be the
         * header body
         */
        String headerLabel = headerParts[0];
        String headerBody = headerParts[1];

//        LOGGER.info("header size before is : " + header.size());
//        LOGGER.info("req generator header label -----> " + headerLabel + " ---> header body ---> " + headerBody);

        //populating the header into the hashmap
        header.put(headerLabel, headerBody);
//        LOGGER.info("header size after is : " + header.size());

        /**
         * returning the hashmap
         */
        return header;
    }

    public static StringBuffer generateBody(String bodyLine, StringBuffer messageBody) {
        /**
         * method to generate the body
         * takes in the bodyLine string and the string buffer and appends the string to the string buffer
         *
         * method is only called if the bodyline is in a valid format
         */

        /**
         * appending the string over here
         */
        messageBody.append(bodyLine).append("\n\r");

        /**
         * returning the string buffer with the appended string
         */
        return messageBody;
    }

    public static String getBodyToString(StringBuffer body){
        /**
         *  unused but helpful method, takes in the stringbuffer and returns the stringbuffer.toString()
         */
        String stringBody = body.toString();

        /**
         * if the string buffer is empty, we just return the empty string
         */
        if (stringBody.strip().equals("")){
            return stringBody;
        }

        /**
         * else we clean up the body and then return it.
         * this was done before we had a separate clean method
         */
        String[] splitBody = stringBody.split("=", 2);
        return splitBody[1];
    }
}
