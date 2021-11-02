package ServerPackage.ServerThreads;

import ServerPackage.Handlers.*;
import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.InvertedIndex.InvertedIndexUI;
import ServerPackage.Mapping.PathHandlerMap;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;
import ServerPackage.ServerUtils.RunningBoolean;
import com.slack.api.methods.MethodsClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;


/**
 * Since we want multi-threading support, ie. every instance to run independent of one another we use this class,
 * to abstract out the work, and run the work on a separate thread
 */
public class ServerThread extends Thread {
    private Socket socket;
    private PathHandlerMap map;
    private static final Logger LOGGER = LogManager.getLogger(ServerThread.class);
    private InvertedIndexUI invertedIndex;
    private MethodsClient methods;
    private RunningBoolean runningBoolean;

    public ServerThread (Socket socket, PathHandlerMap map, RunningBoolean runningBoolean){
        this.socket = socket;
        this.map = map;
        this.runningBoolean = runningBoolean;
    }

    public void setInvertedIndex (InvertedIndexUI invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public void setSlackBot(MethodsClient methods){
        this.methods = methods;
    }

    @Override
    public void run() {
        try (
                InputStream inputStream = socket.getInputStream();
                HttpWriter response = new HttpWriter(socket);
        ){
            LOGGER.info("In server thread");
            //https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
            HTTPParser httpParser = new HTTPParser(inputStream);
            boolean validRequest = httpParser.isRequestIsValid();

            if (validRequest) {
//                //if either the request map or the header map is empty, then it is a bad request and we generate that page
//                if (httpParser.getRequest().isEmpty() || httpParser.getHeader().isEmpty()) {
//                    LOGGER.info("Received incorrect request");
//                    new BadRequestHandler().handle(httpParser, response);
//                }
                LOGGER.info("request is valid");


                LOGGER.info("Server -> request type is : " + httpParser.getRequestType());
                LOGGER.info("Server -> request path is : " + httpParser.getRequestPath().strip());
                LOGGER.info("Server -> request httpVersion is : " + httpParser.getRequestHttpVersion());

                String path = httpParser.getRequestPath().strip();


                if (!map.contains(path)) {
                    LOGGER.info("Map does not contain the path : " + path);
                    new PageNotFoundHandler().handle(httpParser, response);
                } else {
                    LOGGER.info("Map contains the path : " + path);
                    /**
                     * Not every task needs an inverted index, so we only send the inverted index to the
                     * handlers that actually need it
                     */
                    if (path.equals("/find")) {
                        FindHandler findHandler = (FindHandler) map.getObject(path);
                        findHandler.initializeIndex(invertedIndex);
                        findHandler.handle(httpParser, response);
                    } else if (path.equals("/reviewsearch")) {
                        ReviewSearchHandler searchHandler = (ReviewSearchHandler) map.getObject(path);
                        searchHandler.initializeIndex(invertedIndex);
                        searchHandler.handle(httpParser, response);
                    } else if (path.equals("/slackbot")) {
                        SlackBotHandler slackBotHandler = (SlackBotHandler) map.getObject(path);
                        slackBotHandler.initializeMethod(methods);
                        slackBotHandler.handle(httpParser, response);
                    } else if (path.equals("/shutdown") || path.equals("/shutdown?")){
                        ResponseGenerator responseGenerator = new ResponseGenerator();
                        String res = responseGenerator.generateHomePageResponse("Shutdown");
                        response.writeResponse(res);
                        runningBoolean.setRunningToFalse();
                    } else {
                        map.getObject(path).handle(httpParser, response);
                    }
                }
            } else {
                LOGGER.info("ServerThread -------------> Request is not valid");
                new BadRequestHandler().handle(httpParser, response);
            }

        } catch (IOException e){
            LOGGER.error("Got IO Exception");
        } finally {
            try {
                LOGGER.info("Socket is closing");
                socket.close();
                LOGGER.info("Socket closed \n");
            } catch (IOException e) {
                LOGGER.error("Error in closing the socket");
                e.printStackTrace();
            }

        }
    }

}
