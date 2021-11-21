/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class Purpose : The vanilla server
 */

package ServerPackage.Servers;

import ServerPackage.Handlers.Handler;
import ServerPackage.ServerUtils.Mapping.PathHandlerMap;
import ServerPackage.ServerUtils.ServerThreads.ServerThread;
import ServerPackage.ServerUtils.RunningBoolean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the class for the basic server, it has all the features a normal server should have, and if we do not have to pass an external object
 * to handle the responses, the user can use this class instead
 */

public class Server extends Thread{
    /**
     * Port number
     */
    protected int port;
    /**
     * ServerSocket
     */
    protected ServerSocket server;
    /**
     * Instantiating the Logger
     */
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    /**
     * This is the PathHandlerMap Object, it stores all the path the server will have handlers for
     */
    protected PathHandlerMap map;
    //protected volatile boolean running;
    /**
     * The RunningBoolean is a simple class which has a boolean object, since it is a class, if we pass this object
     * down a level, and make changes to the object over there, we will still get the latest state of the object over here
     */
    protected RunningBoolean runningBoolean;
    /**
     * Boolean to keep tack of whether to continue running the server or not
     */
    protected volatile boolean running;

    //declaring threadpool
    private ExecutorService threadPool;


    public Server (int port) throws IOException {
        /**
         * Instantiating all the objects we declared earlier
         */
        this.port = port;
        /**
         * The server socket will listen to requests at the given port
         */
        this.server = new ServerSocket(port);
        this.map = new PathHandlerMap();
        this.runningBoolean = new RunningBoolean();

        /**
         * The current state of the running variable will be the state of the runningBoolean.isRunning() object
         */
        this.running = runningBoolean.isRunning();

        //initializing thread pool
        this.threadPool = Executors.newFixedThreadPool(100);
    }

    /**
     * Method takes in a path and an object then lets the PathHandlerMap add the key-value mapping
     * @param path
     * @param object
     */
    public void addMapping (String path, Handler object){
        map.addMapping(path, object);
    }

    /**
     * The main run method, this is where the server will actually start, run and listen for incoming requests
     */
    @Override
    public void run() {
        try {
            while (running) {
                /**
                 * The listener socket, which we will use to parse requests which the server socket gets
                 */
                Socket listenerSocket = server.accept();
                LOGGER.info("Connection accepted : " + listenerSocket.getInetAddress());

                /**
                 * Submitting job to thread pool
                 */
                threadPool.submit(new ServerThread(listenerSocket, map, runningBoolean));
//                ServerThread serverThread = new ServerThread(listenerSocket, map, runningBoolean);
//                serverThread.start();

                /**
                 * Keeping a track of whether the user wants to shut down the server or not
                 */
                running = runningBoolean.isRunning();
                LOGGER.info("In vanilla server running is + " + running);
            }
        } catch (IOException e) {
            //logging error
            LOGGER.error("Error in setting up the socket : \n" + e);
        } finally {
            try {
                /**
                 * Closing the server once the user wants to
                 */
                server.close();
                threadPool.shutdown();
            } catch (IOException e) {
                //logging error
                LOGGER.error("Error in closing server socket : \n" + e);
                e.printStackTrace();
            }
        }
    }
}
