/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class function : start the  slack bot server
 */
package ServerPackage;

import ServerPackage.Config.ConfigurationManager;
import ServerPackage.Servers.InvertedIndexServer;
import ServerPackage.Servers.SlackBotServer;

import ServerPackage.Handlers.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * There are a lot of classes in this project, the flow of data goes the following way :
 *  We create a new server for inverted index and for the slack bot, assign those a port number and add the supported paths
 *  for those servers, then we finally start the servers.
 *  Each server will be running on a separate thread for better performance, as this allow for multiple users to access the servers.
 *
 *  In the slackbot server we also pass the slack token which we got from the slack api website.
 *
 *  Creating the individual server types :
 *      1) SlackBot server - In the slackbot server we instantiate the slack object with the token we get and listen for requests
 *          from the clients. Every time we get a request, we instantiate the ServerThread and pass the Slack.methods() into the thread.
 *          Each request on an individual post will be handled by a different thread, this is for performance gains. Then after starting the
 *          server thread, we keep checking for whether the user has requested the server to be closed or not, if not then we continue listening
 *          for request, otherwise we close the server.
 *      2) InvertedIndex server - Similar to the slackbot server, once instantiated, this class will then instantiate the inverted index, listen for requests,
 *          and then pass the instantiated inverted index into the server thread. This will also keep checking for whether the user wants to close the server
 *          or not, if yes the server is then closed.
 *
 *  The main handling of the requests happen in the ServerThread class. The first thing the ServerThread does is parse the HttpRequest, and if the request is not
 *  in a valid format, it lets the bad request handler handle the request.
 *  If the request is valid in a valid format, it first gets the path of the request, then lets the appropriate handler take care of the request.
 *
 *  We have used a class called RunningBoolean to store whether the server should still be running or not, this is because the class gets passed by reference, so
 *  changing the state of the class in the ServerThread will make the change visible to the individual servers
 */

public class StartSlackBotServer {

    //declaring the logger
    private static final Logger LOGGER = LogManager.getLogger(StartSlackBotServer.class);

    public static void main (String[] args){

        boolean validParameter = validateParameters(args); //validating params
        if (!validParameter){
            throw new InvalidParameterException("The input paramters are not in the expected format\n" +
                    "The correct format for entering the parameters is -reviews <review_file_name> -qa <qa_file_name>");
        }

        String configFile = args[2];

        /**
         * Configuring the logger
         */
        BasicConfigurator.configure();

        /**
         * We have a config file with a hardcoded location, the file is in json format, and the configurationanager
         */
        ConfigurationManager configurationManager = new ConfigurationManager(configFile);

        /**
         * Getting the port number for the respective severs
         */
        int slackBotPort = configurationManager.getSlackBotPort();

        /**
         * Getting the slack token
         */
        String token = configurationManager.getSlackToken();

        LOGGER.info("Slack Bot Server Starting at port : " + slackBotPort);

        /**
         * Instantiating the thread which will run the SlackBot server
         */
        Thread slackBotStartThread = new Thread(() -> {
            SlackBotServer slackServer = null;
            try {
                /**
                 * Creating the SlackBot server
                 * We will also pass the slack token into this server
                 */
                slackServer = new SlackBotServer(slackBotPort, token);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             * Adding mappings
             */
            slackServer.addMapping("/", new HomePageHandler());
            slackServer.addMapping("/slackbot", new SlackBotHandler());
            slackServer.addMapping("/shutdown", new ShutdownHandler());
            slackServer.addMapping("/shutdown?", new ShutdownHandler());
//            server.addMapping("//favicon.ico", new PageNotFoundHandler());
            slackServer.start();
        });

        /**
         * Starting the thread and hence the server
         */
        slackBotStartThread.start();
    }

    private static boolean validateParameters(String[] args) {
        for (String name : args){
            if (!name.endsWith(".json")){
                return false;
            }
        }

        return true;
    }
}
