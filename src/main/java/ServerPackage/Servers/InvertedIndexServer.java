/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : Inverted Index server
 */
package ServerPackage.Servers;

import ServerPackage.InvertedIndex.InvertedIndexUI;
import ServerPackage.InvertedIndex.QAList;
import ServerPackage.InvertedIndex.ReviewList;
import ServerPackage.ServerUtils.ServerThreads.ServerThread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * This is the server class for starting the inverted index server, this class extends the vanilla server class and hence has access to variables such as RunningBoolean and the Socket for
 * actually listening to the request and the ServerSocket itself.
 *
 * This class just takes in the port number, and then initializes the inverted index itself. Once we get a request, it passes the already initialized inverted index
 * to the serverThread, ensuring that we do not have to start the inverted index every time we get a new request.
 */
public class InvertedIndexServer extends Server{

    /**
     * The UI class for starting the inverted index
     */
    private InvertedIndexUI invertedIndex;
    //initializing the Logger
    private static final Logger LOGGER = LogManager.getLogger(InvertedIndexServer.class);

    public InvertedIndexServer(int port, String reviewFile, String qaFile) throws IOException {
        /**
         * Instantiating the Server class, this lets us have access to the following variables :
         *      1) The server socket
         *      2) The PathHandlerMap -> which contains the supported path for this server
         *      3) The RunningBoolean -> Object which keeps track of whether the user called the shutdown method or not
         *      3) The running boolean -> The actual boolean which tells the class whether the server has to continue running or not
         */
        super(port);

        /**
         * Initializing the inverted index, since this is a server meant for a specific purpose
         * the file paths have been hardcoded and not passed as a parameter.
         *
         * Every server (on different ports) will get one inverted index of their own.
         */

        /**
         * The json files path, which we need to parse and store in the index
         */
//        String reviewFile = "/home/shubham/IdeaProjects/project3-shubham0831/Cell_Phones_and_Accessories_5.json";
//        String qaFile = "/home/shubham/IdeaProjects/project3-shubham0831/qa_Cell_Phones_and_Accessories.json";

        /**
         * The ReviewList and the QAList objects which are needed for starting the inverted index
         */
        ReviewList reviewList = new ReviewList("ISO-8859-1"); //creating ReviewList
        QAList qaList = new QAList("ISO-8859-1"); //creating QAList

        /**
         * Initializing the inverted index
         */
        this.invertedIndex = new InvertedIndexUI(reviewList, reviewFile, qaList, qaFile);

        LOGGER.info("Inverted index is initialized and server is up and ready...");
    }

    /**
     * The main run() method, where we actually start, run and listen for request to our server.
     */
    @Override
    public void run() {
        try {
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
                 * Since it is an inverted index server, we have to pass the inverted index to it as well
                 */
                serverThread.setInvertedIndex(invertedIndex);

                /**
                 * Starting the server thread
                 */
                serverThread.start();

                /**
                 * Keeping track of whether to keep running the server or not
                 */
                running = runningBoolean.isRunning();
                LOGGER.info("In inverted index running is : " + running);
            }
        } catch (IOException e) {
            //logging error
            LOGGER.error("Error in setting up the socket : \n" + e);
        }finally {
            try {
                LOGGER.info("Closing down inverted index server");
                /**
                 * Have to close the server
                 */
                server.close();
            } catch (IOException e) {
                //logging error
                LOGGER.error("Error in closing server socket : \n" + e);
                e.printStackTrace();
            }
        }
    }

//    private boolean checkIfShutdown (Socket socket){
//        try (
//                InputStream inputStream = socket.getInputStream();
//                HttpWriter response = new HttpWriter(socket);
//        ) {
//            LOGGER.info("Checking for shutdown request");
//            HTTPParser httpParser = new HTTPParser(inputStream);
//            String path = httpParser.getRequestPath();
//            ResponseGenerator responseGenerator = new ResponseGenerator();
//            if (httpParser.isRequestIsValid() && path.equals("/shutdown")) {
//                LOGGER.info("Shutting down the server");
//                running = false;
//                String res = responseGenerator.generateSingleLineResponse("test", "/doesnotmatter", "Server shutting down", "Shutdown");
//                response.writeResponse(res);
//            }else {
//                LOGGER.info("Server stays alive");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return running;
//    }

}
