/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class purpose : Review search handler
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
 * If the client wants to do a review search, that request is handled by this object
 */
public class ReviewSearchHandler implements Handler{
    /**
     * The client will have to pass the inverted index for us to perform the find operation on
     */
    private InvertedIndexUI invertedIndex;

    //instantiating the logger
    private static final Logger LOGGER = LogManager.getLogger(ReviewSearchHandler.class);

    /**Constructor will be empty**/
    public ReviewSearchHandler(){
    }

    //method to initialize the invertedIndex in the Handler
    public void initializeIndex (InvertedIndexUI invertedIndex){
        this.invertedIndex = invertedIndex;
    }

    /**
     * Method to actually handle the search request
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
            LOGGER.info("Review search handler received following http method : " + httpMethod);

            /**
             * If the request is a get request, then we just return the /reviewsearch page
             */
            if (httpMethod.equals(HttpConstants.GET)){
                ResponseGenerator responseGenerator = new ResponseGenerator();
                /**Generating the GET response**/
                String response = responseGenerator.generateGETResponse("Review Search", "/reviewsearch", "Enter Search Item");
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
                     * carrying out the search operation
                     */
                    ArrayList<String> results = invertedIndex.getReviewSearch(body);
                    LOGGER.info("Review search term : " + body + " total occurrences in index " + results.size());

                    /**
                     * If we do not get any results, then we have to generate a different page, to let the user know
                     */
                    if (results.size() == 0) {
                        //since we did not find any result we have to send a different response
                        /**Generating the single line response**/
                        String response = responseGenerator.generateSingleLineResponse("Review Search", "/reviewsearch", "Enter Search Item", "No item found");
                        /**Writing the response**/
                        res.writeResponse(response);
                        return;
                    }

                    /**Otherwise we pass the arraylist to the response generator and get the response**/
                    String response = responseGenerator.generateInvertedIndexResponse("Review Search", "/reviewsearch", "Enter Search Item", results);
                    /**Writing the response**/
                    res.writeResponse(response);
                }catch (InvalidParameterException e){
                    /**
                     * if the inverted index sends an error we tell the client that
                     */
                    String response = responseGenerator.generateSingleLineResponse("Review Search", "/reviewsearch", "Enter Search Item", "Invalid Argument");
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
