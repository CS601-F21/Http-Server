/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : Start the inverted index server
 */
package ServerPackage;

import ServerPackage.Config.ConfigurationManager;
import ServerPackage.Handlers.FindHandler;
import ServerPackage.Handlers.HomePageHandler;
import ServerPackage.Handlers.ReviewSearchHandler;
import ServerPackage.Handlers.ShutdownHandler;
import ServerPackage.InvertedIndex.InvertedIndexUI;
import ServerPackage.InvertedIndex.QAList;
import ServerPackage.InvertedIndex.ReviewList;
import ServerPackage.ServerUtils.RunningBoolean;
import ServerPackage.Servers.Server;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.InvalidParameterException;

public class StartInvertedIndexServer {
    private static final Logger LOGGER = LogManager.getLogger(StartInvertedIndexServer.class);

    public static void main (String[] args){

        boolean validParameter = validateParameters(args); //validating params
        if (!validParameter){
            throw new InvalidParameterException("The input paramters are not in the expected format\n" +
                                                "The correct format for entering the parameters is -reviews <review_file_name> -qa <qa_file_name>");
        }

        String reviewFile = args[0];
        String qaFile = args[1];
        String configFile = args[2];

//        String reviewFile = "/home/shubham/IdeaProjects/project3-shubham0831/Cell_Phones_and_Accessories_5.json";
//        String qaFile = "/home/shubham/IdeaProjects/project3-shubham0831/qa_Cell_Phones_and_Accessories.json";
//        String configFile = "/home/shubham/IdeaProjects/project3-shubham0831/configuration.json";

        /**
         * Configuring the logger
         */
        BasicConfigurator.configure();

        /**
         * We have a config file with a hardcoded location, the file is in json format, and the configurationManager
         */
        ConfigurationManager configurationManager = new ConfigurationManager(configFile);

        /**
         * Getting the port number for the respective severs
         */
        int invertedIndexPort = configurationManager.getIndexPort();

        /**
         * The ReviewList and the QAList objects which are needed for starting the inverted index
         */
        ReviewList reviewList = new ReviewList("ISO-8859-1"); //creating ReviewList
        QAList qaList = new QAList("ISO-8859-1"); //creating QAList

        /**
         * Initializing the inverted index
         */
        InvertedIndexUI invertedIndex = new InvertedIndexUI(reviewList, reviewFile, qaList, qaFile);

        LOGGER.info("Inverted Index Server Starting at port : " + invertedIndexPort);

        /**
         * Instantitating the thread which will run the inverted index server
         */
        Thread invertedIndexStartThread = new Thread(() -> {
            Server invertedIndexServer = null;
            try {
                /**
                 * Creating the inverted index server
                 */
                invertedIndexServer = new Server(invertedIndexPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             * Adding mappings
             */
            invertedIndexServer.addMapping("/", new HomePageHandler());
            invertedIndexServer.addMapping("/find", new FindHandler(invertedIndex));
            invertedIndexServer.addMapping("/reviewsearch", new ReviewSearchHandler(invertedIndex));
            invertedIndexServer.addMapping("/shutdown", new ShutdownHandler());
            invertedIndexServer.addMapping("/shutdown?", new ShutdownHandler());
//            invertedIndexServer.addMapping("/favicon.ico", new PageNotFoundHandler());

            invertedIndexServer.start();
        });

        /**
         * Starting the thread and hence the server
         */
        invertedIndexStartThread.start();
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
