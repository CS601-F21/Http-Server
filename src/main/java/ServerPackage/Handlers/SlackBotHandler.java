/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class Purpose : slackbot handler
 */

package ServerPackage.Handlers;

import ServerPackage.Handlers.Slackutils.SlackResponseObject;
import ServerPackage.HttpUtils.*;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Called by the user if the client want to send a message on Slack
 */

public class SlackBotHandler implements Handler{
    //instantiating the logger
    private static final Logger LOGGER = LogManager.getLogger(SlackBotHandler.class);

    /**
     * The client will have to pass the Slack token
     */
    private String token;

    /**
     * These are the where we will store the headers for our POST request
     */
    private HashMap<String, String> headers;

    /**
     * This is used to parse the slack response, as well as converting our hashmap to a json
     * object
     */
    private Gson gson;

    public SlackBotHandler (String token) {
        /**
         * Instantiating the class variables
         */
        headers = new HashMap<>();
        gson = new Gson();
        this.token = token;

    }

    /**
     * Method to actually handle the slack chat-bot request
     * @param req
     * @param res
     */
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        /**
         * First we get the type of request the user has made
         */
        String httpMethod = req.getRequestType();

        //the response generator
        ResponseGenerator responseGenerator = new ResponseGenerator();

        /**
         * The post request will throw an IOException, so we catch that over here
         */
        try {
            LOGGER.info("Slack bot handler received the following request : " + httpMethod);

            /**
             * If the request is a get request, then we just return the /slackbot page
             */
            if (httpMethod.equals(HttpConstants.GET)){
                /**Generating the GET response**/
                String response = responseGenerator.GET("Slack Bot", "/slackbot", "Enter message");
                /**Writing the response**/
                res.writeResponse(response);
            } else if (httpMethod.equals(HttpConstants.POST)){
                /**
                 * If the request is a POST request, we first clean the body then get the headers for the POST request and then we finally call the
                 * doPost method which sends the POST request to slack
                 */
                //cleaning the body
                String body = req.cleanBody(req.getBody());
                //setting the headers
                headers = setSlackHeader();
                //the slack-api url
                String url = "https://slack.com/api/chat.postMessage";

                /**
                 * Method will return the slackResponse
                 */
                String slackResponse = doPost(url, headers, body);

                /**
                 * If we get an Internal Server Error, we generate the appropriate page
                 */
                if (slackResponse.equals(HttpConstants.INTERNAL_SERVER_ERROR)){
                    String response = responseGenerator.INTERNAL_SERVER_ERROR();
                    res.writeResponse(response);
                    return;
                }

                /**
                 * If we do get an 200 response from slack, we need to know whether our message was actually sent or not, so we build the
                 * SlackResponseObject
                 */
                SlackResponseObject slackResponseObject =  gson.fromJson(slackResponse, SlackResponseObject.class);

                /**
                 * Based on whether the message is sent or not, we generate the appropriate page
                 */
                String response;
                //if message is sent we let the user know that
                if (slackResponseObject.isOk()){
                    response = responseGenerator.singleLineResponse("Slack Bot", "/slackbot", "Enter Message", "Message Sent");
                }
                //if message is not set, we let the user know
                else {
                    LOGGER.info("Slack error -> "+ slackResponseObject.getError());
                    response = responseGenerator.singleLineResponse("Slack Bot", "/slackbot", "Enter Message", "Message Not Sent, slack error");
                }

                //finally we write the response
                res.writeResponse(response);
            } else {
                /**
                 * If method is neither GET or  POST, we send HttpError 405 Method Not Supported
                 */
                String response = responseGenerator.METHOD_NOT_ALLOWED();
                res.writeResponse(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method takes in the link, the headers and the body and posts to the slack api
     * @param link
     * @param headers
     * @param body
     * @return
     */
    private String doPost(String link, HashMap<String, String> headers, String body) {
        /**https://stackoverflow.com/a/35013372**/
        try {
            //Building the HttpURLConnection
            HttpURLConnection connection = getURLConnected(link, headers);

            //getting the message body
            byte[] messageBody = getMessageBody("cs601-project3", body);
            //we need to know the length of the body as well
            int length = messageBody.length;

            //We actually send the POST request and get the response from this method
            String response = sendMessageAndGetResponse(connection, messageBody, length);

            LOGGER.info("Slack response : " + response);

            //we finally return the response
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String sendMessageAndGetResponse(HttpURLConnection connection, byte[] messageBody, int length) throws IOException {
        /**
         * Method takes in a connection, the message body and the length and sends the POST request
         */

        //we have to first set the length of the body
        connection.setFixedLengthStreamingMode(length);

        //connecting to slack
        connection.connect();

        //sending the message body
        try(OutputStream outputStream = connection.getOutputStream()){
            outputStream.write(messageBody);
        }

        LOGGER.info("Got response code : " +connection.getResponseCode());
        LOGGER.info("Got response message : " +connection.getResponseMessage());

        //if response code is 200, then only we get the input stream
        if (connection.getResponseCode() == 200){
            String response = getInputStream(connection.getInputStream());
            LOGGER.info(response);
            //we then return the input stream
            return response;
        }

        //otherwise we return INTERNAL_SERVER_ERROR
        return HttpConstants.INTERNAL_SERVER_ERROR;
    }

    private byte[] getMessageBody(String channel, String body) {
        /**
         * the message body has to be in a json format, so we use gson to help achieve that
         */

        //first we build the hashmap
        HashMap<String, String> messageBody = new HashMap<>();
        messageBody.put("channel", channel);
        messageBody.put("text", body);

        //then we convert the hashmap to a string in json format
        String json = gson.toJson(messageBody);

        //we have to send the output stream in bytes so we get the byte array over here
        byte[] outputBody = json.getBytes(StandardCharsets.UTF_8);

        //we finally return the byte array
        return outputBody;
    }

    private HttpURLConnection getURLConnected (String link, HashMap<String, String> headers) throws IOException {
        /**
         * Method takes in a link and the headers, and sets the appropriate url connection with the appropriate
         * request property
         */
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("Connection", "close");

        /**
         * setting the appropriate request properties
         */
        for (String key : headers.keySet()){
            connection.setRequestProperty(key, headers.get(key));
        }

        /**
         * we have to do a post method
         */
        connection.setRequestMethod("POST");

        /**
         * we need both the input and the output stream
         */
        connection.setDoOutput(true);
        connection.setDoInput(true);

        /**
         * we return the connection
         */
        return connection;
    }

    private String getInputStream(InputStream inputStream) throws IOException {
        /**
         * method takes in an input stream, created a stringBuilder and continuously appends characters from the stream to the stringBuilder
         * then it finally returns the stringBuilder.toString()
         *
         * Over here we are creating the stringBuilder
         */
        StringBuilder request = new StringBuilder();
        /**
         * Got this way of building the request form the link mentioned above
         */
        do {
            request.append((char) inputStream.read());
        } while (inputStream.available() > 0);
        /**
         * converting the stringBuilder to string
         */
        String parsedFullRequest =  request.toString();

        /**
         * returning the string
         */
        return parsedFullRequest;
    }

    private HashMap<String, String> setSlackHeader(){
        /**
         * Method to set the slack header
         */
        headers.put("Content-Type", "application/json");
        headers.put("Authorization","Bearer "+ token);
//        headers.put("channel","cs601-project3");
//        headers.put("Connection","close");
        return headers;
    }

}
