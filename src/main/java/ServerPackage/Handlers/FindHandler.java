/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class purpose : Find Handler
 */
package ServerPackage.Handlers;

import ServerPackage.HttpUtils.HttpConstants;
import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.InvertedIndex.InvertedIndexUI;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Called by the user if the client wants to do a find operation
 */

public class FindHandler implements Handler{

    /**
     * The client will have to pass the inverted index for us to perform the find operation on
     */
    private InvertedIndexUI invertedIndex;

    //instantiating the logger
    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class);

    /**Constructor will be empty**/
    public FindHandler (){
    }

    //method to initialize the invertedIndex in the Handler
    public void initializeIndex (InvertedIndexUI invertedIndex){
        this.invertedIndex = invertedIndex;
    }

    /**
     * Method to actually handle the find request
     * @param req
     * @param res
     */
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            /**
             * First we get the type of request the user has made
             */
            String httpMethod = req.getRequestType();
            LOGGER.info("Find handler received http method : " + httpMethod);

            /**
             * If the request is a get request, then we just return the /find page
             */
            if (httpMethod.equals(HttpConstants.GET)){
                ResponseGenerator responseGenerator = new ResponseGenerator();
                /**Generating the GET response**/
                String response = responseGenerator.generateGETResponse("Find", "/find", "Enter ASIN");
                /**Writing the response**/
                res.writeResponse(response);
            } else if (httpMethod.equals(HttpConstants.POST)){
                /**
                 * If we get a post response, we have to first get the body and clean it
                 */
                String body = req.cleanBody(req.getBody());
                /**
                 * Instantiating the response generator
                 */
                ResponseGenerator responseGenerator = new ResponseGenerator();
                try {
                    /**
                     * carrying out the find operation
                     */
                    ArrayList<String> results = invertedIndex.getAsin(body);
                    LOGGER.info("Got ASIN " + body + " found " + results.size() + " results");

                    /**
                     * If we do not get any results, then we have to generate a different page, to let the user know
                     */
                    if (results.size() == 0){
                        //since we did not find any result we have to send a different response
                        /**Generating the single line response**/
                        String response = responseGenerator.generateSingleLineResponse("Find", "/find", "Enter Asin", "No item found");
                        /**Writing the response**/
                        res.writeResponse(response);
                        return;
                    }

                    /**Otherwise we pass the arraylist to the response generator and get the response**/
                    String response = responseGenerator.generateInvertedIndexResponse("Find", "/find", "Enter ASIN ", results);

                    /**Writing the response**/
                    res.writeResponse(response);
                }catch (InvalidParameterException e){
                    /**
                     * if the inverted index sends an error we tell the client that
                     */
                    String response = responseGenerator.generateSingleLineResponse("Find", "/find", "Enter Asin", "Invalid Argument");
                    res.writeResponse(response);
                    return;
                }
            } else {
                /**
                 * If method is neither GET or  POST, we send HttpError 405 Method Not Supported
                 */
                ResponseGenerator responseGenerator = new ResponseGenerator();
                String response = responseGenerator.generateMETHODNOTALLOWEDResponse();
                res.writeResponse(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
