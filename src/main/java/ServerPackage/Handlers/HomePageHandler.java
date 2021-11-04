/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class purpose : Home page handler
 */
package ServerPackage.Handlers;

import ServerPackage.HttpUtils.HttpConstants;
import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * This handler generates the homepage
 */

public class HomePageHandler implements Handler {

    //logger for logging purposes
    private static final Logger LOGGER = LogManager.getLogger(HomePageHandler.class);

    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        /**
         * First we get the request type, since we will only be supporting the GET and the POST request
         */
        String httpMethod = req.getRequestType();
        ResponseGenerator responseGenerator = new ResponseGenerator();
        /**
         * Our response will depend on the request type
         */
        String response;
        try {
            LOGGER.info("Got Home page request with following method : " + httpMethod);

            /**
             * Generating the appropriate responses
             */
            if (!httpMethod.equals(HttpConstants.GET) && !httpMethod.equals(HttpConstants.POST)){
                response = responseGenerator.METHOD_NOT_ALLOWED();
            }
            else {
                response = responseGenerator.homePageResponse("HOME");
            }

            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
