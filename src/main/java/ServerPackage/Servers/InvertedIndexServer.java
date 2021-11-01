package ServerPackage.Servers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.InvertedIndex.InvertedIndex;
import ServerPackage.InvertedIndex.InvertedIndexUI;
import ServerPackage.InvertedIndex.QAList;
import ServerPackage.InvertedIndex.ReviewList;
import ServerPackage.ServerThreads.ServerThread;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class InvertedIndexServer extends Server{

    private InvertedIndexUI invertedIndex;
    private static final Logger LOGGER = LogManager.getLogger(InvertedIndexServer.class);

    public InvertedIndexServer(int port) throws IOException {
        super(port);

        /**
         * Initializing the inverted index, since this is a server meant for a specific purpose
         * the file paths have been hardcoded and not passed as a parameter.
         * This ensures that the server is indeed a general purpose server
         *
         * Every server (on different ports will get one inverted index of their own)
         */

        String reviewFile = "/home/shubham/IdeaProjects/project3-shubham0831/Cell_Phones_and_Accessories_5.json";
        String qaFile = "/home/shubham/IdeaProjects/project3-shubham0831/qa_Cell_Phones_and_Accessories.json";

        ReviewList reviewList = new ReviewList("ISO-8859-1"); //creating ReviewList
        QAList qaList = new QAList("ISO-8859-1"); //creating QAList
        this.invertedIndex = new InvertedIndexUI(reviewList, reviewFile, qaList, qaFile);
        LOGGER.info("Inverted index is initialized and server is up and ready...");
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket listenerSocket = server.accept();
                LOGGER.info("Connection accepted : " + listenerSocket.getInetAddress());
                ServerThread serverThread = new ServerThread(listenerSocket, map, runningBoolean);
                serverThread.setInvertedIndex(invertedIndex);
                serverThread.start();
                running = runningBoolean.isRunning();
                LOGGER.info("In inverted index running is : " + running);
            }
        } catch (IOException e) {
            LOGGER.error("Error in setting up the socket : \n" + e);
        }finally {
            try {
                LOGGER.info("Closing down inverted index server");
                server.close();
            } catch (IOException e) {
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
