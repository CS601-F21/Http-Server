/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class Purpose : contains the server thread which will actually parse the request and decide what to do with it
 */

package ServerPackage.ServerUtils.ServerThreads;

import ServerPackage.Handlers.*;
import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.InvertedIndex.InvertedIndexUI;
import ServerPackage.ServerUtils.Mapping.PathHandlerMap;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;
import ServerPackage.ServerUtils.RunningBoolean;
import com.slack.api.methods.MethodsClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * For every request we get on a particular server, the request will be handled by a different thread. This is that thread.
 * This class is where we actually parse the request and decide what to do with it.
 */
public class ServerThread extends Thread {
    /**
     * This is the listener socket from the server themselves, and this is the socket from where we will actually get the inputStream
     * and the outputStream
     */
    private Socket socket;

    /**
     * This is the path map, which contains the mapping between the paths and the handlers. If a path does not exist in this map, then we
     * do not support that path and will return a PageNotFound Error
     */
    private PathHandlerMap map;

    /**
     * Logger for logging purposes
     */
    private static final Logger LOGGER = LogManager.getLogger(ServerThread.class);

    /**
     * The inverted index, this will only be instantiated if the user is running an inverted index server. To instantiate this
     * the user will have to call the setInvertedIndex method
     */
    private InvertedIndexUI invertedIndex;

    /**
     * The slack MethodsClient object, this will only be instantiated if the user is running the slack bot server. To instantiate this
     * the user will have to call the setSlackBot method
     */
    private MethodsClient methods;

    /**
     * The running boolean, if the user calls the shutdown method, we use the setRunningtoFalse method in this class to let the server know
     * to shutdown
     */
    private RunningBoolean runningBoolean;

    public ServerThread (Socket socket, PathHandlerMap map, RunningBoolean runningBoolean){
        /**
         * This is where we instantiate the required objects for a basic server
         */
        this.socket = socket;
        this.map = map;
        this.runningBoolean = runningBoolean;
    }

    public void setInvertedIndex (InvertedIndexUI invertedIndex) {
        /**
         * This is where we instantiate the inverted index
         */
        this.invertedIndex = invertedIndex;
    }

    public void setSlackBot(MethodsClient methods){
        /**
         * This is where we instantiate the MethodsClient object from slack
         */
        this.methods = methods;
    }

    @Override
    public void run() {
        try (
                /**
                 * Getting the input stream from the socket, this will then be sent to the HttpParser to be parsed
                 */
                InputStream inputStream = socket.getInputStream();

                /**
                 * This is the response object, we send a Http response to it, and it sends it as the outputStream to the socket
                 */
                HttpWriter response = new HttpWriter(socket);
        ){
            LOGGER.info("In server thread");
            //https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
            /**
             * The first thing we do is parse the input stream from the socket to a Http Request
             */
            HTTPParser httpParser = new HTTPParser(inputStream);

            /**
             * Once the request is parsed, we check whether the response is valid or not, we will have different outputs based on that
             * If the request is valid, then it goes to the Handlers to be handled, else we raise a Bad Request Error
             */
            boolean validRequest = httpParser.isRequestIsValid();

            /**
             * The checkShutdown() method checks the running boolean to check whether the server is shutdown or ot
             */
            boolean isShutDown = checkShutdown();

            /**
             * If the request is valid and the server has not yet shutdown, we get the path and assign the request to the appropriate handlers
             */
            if (validRequest && !isShutDown) {
                LOGGER.info("request is valid");

                LOGGER.info("Server -> request type is : " + httpParser.getRequestType());
                LOGGER.info("Server -> request path is : " + httpParser.getRequestPath().strip());
                LOGGER.info("Server -> request httpVersion is : " + httpParser.getRequestHttpVersion());

                /**
                 * Getting the path from the http parser
                 */
                String path = httpParser.getRequestPath().strip();


                /**
                 * If the map does not contain the path, we will raise a Page Not Found error
                 */
                if (!map.contains(path)) {
                    LOGGER.info("Map does not contain the path : " + path);
                    new PageNotFoundHandler().handle(httpParser, response);
                }
                /**
                 * Otherwise we will assign the request to the appropriate handlers
                 */
                else {
                    LOGGER.info("Map contains the path : " + path);
                    /**
                     * Not every task needs every object, so we initialize the objects for different paths separately.
                     * Each handler is passes the HttpParser Object (aka the request) and the HttpWrite Object (aka the response)
                     */
                    if (path.equals("/find")) {
                        /**
                         * Getting the find handler object
                         */
                        FindHandler findHandler = (FindHandler) map.getObject(path);
                        /**
                         * Since the find handler requires an Inverted index, we initialize it over here
                         */
                        findHandler.initializeIndex(invertedIndex);
                        findHandler.handle(httpParser, response);
                    } else if (path.equals("/reviewsearch")) {
                        /**
                         * Getting the reviewsearch handler object
                         */
                        ReviewSearchHandler searchHandler = (ReviewSearchHandler) map.getObject(path);
                        /**
                         * Since the reviewsearch handler requires an Inverted index, we initialize it over here
                         */
                        searchHandler.initializeIndex(invertedIndex);
                        searchHandler.handle(httpParser, response);
                    } else if (path.equals("/slackbot")) {
                        /**
                         * Getting the slackbot handler object
                         */
                        SlackBotHandler slackBotHandler = (SlackBotHandler) map.getObject(path);
                        /**
                         * The slack bot handler required the MethodsClient object from slack so we initialize it over here
                         */
                        slackBotHandler.initializeMethod(methods);
                        slackBotHandler.handle(httpParser, response);
                    } else if (path.equals("/shutdown") || path.equals("/shutdown?")){
                        /**
                         * Handling the shutdown request from the user
                         */
                        ResponseGenerator responseGenerator = new ResponseGenerator();
                        String res = responseGenerator.generateHomePageResponse("Shutdown");
                        response.writeResponse(res);
                        runningBoolean.setRunningToFalse();
                    } else {
                        /**
                         * This is just the / path for now
                         */
                        map.getObject(path).handle(httpParser, response);
                    }
                }
            } else {
                if (isShutDown){
                    /**
                     * If the server has shutdown, but before it could shut down, we get another thread, we generate a homepage
                     * telling the user the server has shutdown
                     * Generally after shutting down, the server is accepting a request before actually shutting down.
                     */
                    ResponseGenerator responseGenerator = new ResponseGenerator();
                    String res = responseGenerator.generateHomePageResponse("Shutdown");
                    response.writeResponse(res);
                    return;
                }
                /**
                 * If the request is not valid, we let the BadRequestHandler handle that
                 */
                LOGGER.info("ServerThread -------------> Request is not valid");
                new BadRequestHandler().handle(httpParser, response);
            }

        } catch (IOException e){
            LOGGER.error("Got IO Exception");
        } finally {
            try {
                /**
                 * Finally we have to shutdown the socket
                 */
                LOGGER.info("Socket is closing");
                socket.close();
                LOGGER.info("Socket closed \n");
            } catch (IOException e) {
                LOGGER.error("Error in closing the socket");
                e.printStackTrace();
            }

        }
    }

    /**
     * The checkShutdown method only reads the current value from the runningBoolean and return the inverse of it.
     * That is due to the name of the method.
     */
    private boolean checkShutdown()  {
        return !runningBoolean.isRunning();
    }

}
