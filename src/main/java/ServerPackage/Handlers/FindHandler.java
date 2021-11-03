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

public class FindHandler implements Handler{

    private InvertedIndexUI invertedIndex;
    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class);

    public FindHandler (){
    }

    //method to initialize the invertedIndex in the Handler
    public void initializeIndex (InvertedIndexUI invertedIndex){
        this.invertedIndex = invertedIndex;
    }

    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            String httpMethod = req.getRequestType();
            LOGGER.info("Find handler received http method : " + httpMethod);
            if (httpMethod.equals(HttpConstants.GET)){
                ResponseGenerator responseGenerator = new ResponseGenerator();
                String response = responseGenerator.generateGETResponse("Find", "/find", "Enter ASIN");
                res.writeResponse(response);
            } else if (httpMethod.equals(HttpConstants.POST)){

                String body = req.cleanBody(req.getBody());
                ResponseGenerator responseGenerator = new ResponseGenerator();
                try {
                    ArrayList<String> results = invertedIndex.getAsin(body);
                    LOGGER.info("Got ASIN " + body + " found " + results.size() + " results");

                    if (results.size() == 0){
                        //since we did not find any result we have to send a different response
                        String response = responseGenerator.generateSingleLineResponse("Find", "/find", "Enter Asin", "No item found");
                        res.writeResponse(response);
                        return;
                    }

                    String response = responseGenerator.generateInvertedIndexResponse("Find", "/find", "Enter ASIN ", results);

                    res.writeResponse(response);
                }catch (InvalidParameterException e){
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
