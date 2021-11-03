package ServerPackage.Handlers;

import ServerPackage.HttpUtils.HttpConstants;
import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HomePageHandler implements Handler {
    private static final Logger LOGGER = LogManager.getLogger(HomePageHandler.class);

    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        String httpMethod = req.getRequestType();
        ResponseGenerator responseGenerator = new ResponseGenerator();
        String response;
        try {
            LOGGER.info("Got Home page request with following method : " + httpMethod);

            if (!httpMethod.equals(HttpConstants.GET) && !httpMethod.equals(HttpConstants.POST)){
                response = responseGenerator.generateMETHODNOTALLOWEDResponse();
            }
            else {
                response = responseGenerator.generateHomePageResponse("HOME");
            }

            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
