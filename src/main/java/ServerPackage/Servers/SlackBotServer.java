/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : Slack bot server
 */
package ServerPackage.Servers;

import ServerPackage.ServerUtils.ServerThreads.ServerThread;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * This is the server class for starting the slackbot server, this class extends the vanilla server class and hence has access to variables such as RunningBoolean and the Socket for
 * actually listening to the request and the ServerSocket itself.
 *
 * This class takes in the port number and the slack token from the user. The slack token is then used to instantiate the Slack class and get the slack.methods() objects.
 *
 * Once we get a request, this class will then pass the slack.methods() object to the ServerThread and start the server
 */

public class SlackBotServer extends Server{

    /**
     * Instantiating the Logger
     */
    private static final Logger LOGGER = LogManager.getLogger(SlackBotServer.class);

    /**
     * MethodsClient object we get from the Slack API, this is the object we will use to actually send the message to the slack chat
     */
    MethodsClient methods;

    public SlackBotServer(int port, String token) throws IOException {
        /**
         * Instantiating the Server class, this lets us have access to the following variables :
         *      1) The server socket
         *      2) The PathHandlerMap -> which contains the supported path for this server
         *      3) The RunningBoolean -> Object which keeps track of whether the user called the shutdown method or not
         *      3) The running boolean -> The actual boolean which tells the class whether the server has to continue running or not
         */
        super(port);

        /**
         * Instantiating the Slack object and the MethodsClient object
         */
        Slack slack = Slack.getInstance();
        this.methods = slack.methods(token);
//        ChatPostMessageRequest request = ChatPostMessageRequest.builder().channel("#cs601-project3").text("test").build();
//        ChatPostMessageResponse response = methods.chatPostMessage(request);
        LOGGER.info("Slack bot server has been initialized");
    }

    /**
     * We override the run method from the vanilla Server class, as we want to make a few changes of our own
     */
    @Override
    public void run() {
        try {
            /**
             * While the server is running, we keep listening to requests
             */
            while (running) {
                /**
                 * Socket which will listen to the incoming requests
                 */
                Socket listenerSocket = server.accept();
                LOGGER.info("Connection accepted : " + listenerSocket.getInetAddress());
                /**
                 * Every time we get a new request we initialize a server thread and pass the listenerSocket, the map and the runningBoolean
                 * into it
                 *
                 * The map is a class which contains the path supported by the server
                 */
                ServerThread serverThread = new ServerThread(listenerSocket, map, runningBoolean);
                /**
                 * Since it is a slack bot server, we also have to pass the MethodsClient object as well
                 */
                serverThread.setSlackBot(methods);
                /**
                 * We finally start the server
                 */
                serverThread.start();
                /**
                 * We keep a track of the running boolean constantly
                 */
                running = runningBoolean.isRunning();
                LOGGER.info("In slack bot server running is + " + running);
            }
        } catch (IOException e) {
            //logging the error
            LOGGER.error("Error in setting up the socket : \n" + e);
        }finally {
            try {
                /**
                 * Finally we have to close the server, if that is what the user wants
                 */
                LOGGER.info("Closing down slack bot server");
                server.close();
            } catch (IOException e) {
                //logging the error
                LOGGER.error("Error in closing server socket : \n" + e);
                e.printStackTrace();
            }
        }
    }
}
