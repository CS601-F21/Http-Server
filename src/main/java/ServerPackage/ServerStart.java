package ServerPackage;

import ServerPackage.Handlers.FindHandler;
import ServerPackage.Handlers.HomePageHandler;
import ServerPackage.Handlers.ReviewSearchHandler;
import ServerPackage.Handlers.SlackBotHandler;
import ServerPackage.Servers.InvertedIndexServer;
import ServerPackage.Servers.Server;


import ServerPackage.Servers.SlackBotServer;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ServerStart {

    private static final Logger LOGGER = LogManager.getLogger(ServerStart.class);

    public static void main (String[] args) throws SlackApiException, IOException {

        BasicConfigurator.configure();
        int invertedIndexPort = 8080;
        int slackBotPort = 9090;

        String token = "Not yet";

        LOGGER.info("Inverted Index Server Starting at port : " + invertedIndexPort);
        LOGGER.info("Slack Bot Server Starting at port : " + slackBotPort);

        Thread slackBotStartThread = new Thread(() -> {
            SlackBotServer server = null;
            try {
                server = new SlackBotServer(slackBotPort, token);
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.addMapping("/", new HomePageHandler());
            server.addMapping("/slackbot", new SlackBotHandler());
//            server.addMapping("//favicon.ico", new FaviconHandler());
            server.start();
        });

        Thread invertedIndexStartThread = new Thread(() -> {
            InvertedIndexServer invertedIndexServer = null;
            try {
                invertedIndexServer = new InvertedIndexServer(invertedIndexPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            invertedIndexServer.addMapping("/", new HomePageHandler());
            invertedIndexServer.addMapping("/find", new FindHandler());
            invertedIndexServer.addMapping("/reviewsearch", new ReviewSearchHandler());
//            invertedIndexServer.addMapping("/favicon.ico", new FaviconHandler);

            invertedIndexServer.start();
        });

//        slackBotStartThread.start();
//        invertedIndexStartThread.start();

        Slack chat = Slack.getInstance();
        MethodsClient methods = chat.methods(token);
        ChatPostMessageRequest request = ChatPostMessageRequest.builder().channel("#cs601-project3").text("test test").build();
//        methods.chatDelete()
        ChatPostMessageResponse  slackResponse = methods.chatPostMessage(request);
        System.out.println(request);

    }
}
