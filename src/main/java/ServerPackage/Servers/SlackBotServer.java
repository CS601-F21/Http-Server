package ServerPackage.Servers;

import ServerPackage.ServerThreads.ServerThread;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class SlackBotServer extends Server{

    private static final Logger LOGGER = LogManager.getLogger(SlackBotServer.class);

    MethodsClient methods;

    public SlackBotServer(int port, String token) throws IOException {
        super(port);

        Slack slack = Slack.getInstance();
        this.methods = slack.methods(token);
//        ChatPostMessageRequest request = ChatPostMessageRequest.builder().channel("#cs601-project3").text("test").build();
//        ChatPostMessageResponse response = methods.chatPostMessage(request);
        LOGGER.info("Slack bot server has been initialized");
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket listenerSocket = server.accept();
                LOGGER.info("Connection accepted : " + listenerSocket.getInetAddress());
                ServerThread serverThread = new ServerThread(listenerSocket, map, runningBoolean);
                serverThread.setSlackBot(methods);
                serverThread.start();
                running = runningBoolean.isRunning();
                LOGGER.info("In slack bot server running is + " + running);
            }
        } catch (IOException e) {
            LOGGER.error("Error in setting up the socket : \n" + e);
        }finally {
            try {
                LOGGER.info("Closing down slack bot server");
                server.close();
            } catch (IOException e) {
                LOGGER.error("Error in closing server socket : \n" + e);
                e.printStackTrace();
            }
        }
    }
}
