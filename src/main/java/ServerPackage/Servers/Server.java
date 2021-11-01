package ServerPackage.Servers;

import ServerPackage.Handlers.Handler;
import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.Mapping.PathHandlerMap;
import ServerPackage.ServerThreads.ServerThread;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;
import ServerPackage.ServerUtils.RunningBoolean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{
    protected int port;
    protected ServerSocket server;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    protected PathHandlerMap map;
//    protected volatile boolean running;
    protected RunningBoolean runningBoolean;
    protected boolean running;


    public Server (int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(port);
        this.map = new PathHandlerMap();
        this.runningBoolean = new RunningBoolean();
        this.running = runningBoolean.isRunning();
    }

    public void addMapping (String path, Handler object){
        map.addMapping(path, object);
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket listenerSocket = server.accept();
                LOGGER.info("Connection accepted : " + listenerSocket.getInetAddress());
                ServerThread serverThread = new ServerThread(listenerSocket, map, runningBoolean);
                serverThread.start();
                running = runningBoolean.isRunning();
                LOGGER.info("In vanilla server running is + " + running);
            }
        } catch (IOException e) {
            LOGGER.error("Error in setting up the socket : \n" + e);
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                LOGGER.error("Error in closing server socket : \n" + e);
                e.printStackTrace();
            }
        }
    }

    protected boolean checkIfShutdown (Socket socket){
        try (
                InputStream inputStream = socket.getInputStream();
                HttpWriter response = new HttpWriter(socket);
        ) {
            LOGGER.info("Checking for shutdown request");
            HTTPParser httpParser = new HTTPParser(inputStream);
            String path = httpParser.getRequestPath();
            ResponseGenerator responseGenerator = new ResponseGenerator();
            if (httpParser.isRequestIsValid() && path.equals("/shutdown")) {
                LOGGER.info("Shutting down the server");
                running = false;
                String res = responseGenerator.generateSingleLineResponse("test", "/doesnotmatter", "Server shutting down", "Shutdown");
                response.writeResponse(res);
            }else {
                LOGGER.info("Server stays alive");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return running;
    }
}
