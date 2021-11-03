package ServerPackage.Handlers;

import ServerPackage.HttpUtils.HttpConstants;
import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.slack.api.Slack;

import java.io.IOException;
import java.util.HashMap;

public class SlackBotHandler implements Handler{
    private static final Logger LOGGER = LogManager.getLogger(SlackBotHandler.class);
    private String token;
    private HashMap<String, String> headers;

    public SlackBotHandler () {
        headers = new HashMap<>();
    }

    @Override
    public void handle(HTTPParser req, HttpWriter res) {

        String httpMethod = req.getRequestType();
        ResponseGenerator responseGenerator = new ResponseGenerator();
        try {
            LOGGER.info("Slack bot handler received the following request : " + httpMethod);
            if (httpMethod.equals(HttpConstants.GET)){
                String response = responseGenerator.generateGETResponse("Slack Bot", "/slackbot", "Enter message");
                res.writeResponse(response);
            } else if (httpMethod.equals(HttpConstants.POST)){
                String body = req.cleanBody(req.getBody());

                ChatPostMessageRequest slackRequest = ChatPostMessageRequest.builder().channel("#cs601-project3").text(body).build();
                ChatPostMessageResponse  slackResponse = methods.chatPostMessage(slackRequest);

                LOGGER.info("Sent Message, response is :");
                LOGGER.info(slackResponse);
                String response = responseGenerator.generateSingleLineResponse("Slack Bot", "/slackbot", "Enter Message", "Message Sent");
                res.writeResponse(response);
            } else {
                /**
                 * If method is neither GET or  POST, we send HttpError 405 Method Not Supported
                 */
                String response = responseGenerator.generateMETHODNOTALLOWEDResponse();
                res.writeResponse(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SlackApiException e) {
            //even if slack is not able to send the message, our response will still be OK, because our server is still responding well
            String response = responseGenerator.generateSingleLineResponse("Slack Bot" , "/slackbot", "Enter Message", "Message Not Sent");
            try {
                res.writeResponse(response);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private HashMap<String, String> getSlackHeader(){
        headers.put()
    }

    public void initializeToken(String token) {
        this.token = token;
    }
}
