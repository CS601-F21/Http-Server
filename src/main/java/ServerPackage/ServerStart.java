package ServerPackage;

import ServerPackage.Config.ConfigurationManager;
import ServerPackage.Handlers.*;
import ServerPackage.ServerUtils.RunningBoolean;
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

    public static void main (String[] args){
        BasicConfigurator.configure();

        ConfigurationManager configurationManager = new ConfigurationManager("/home/shubham/IdeaProjects/project3-shubham0831/configuration.json");

        int invertedIndexPort = configurationManager.getIndexPort();
        int slackBotPort = configurationManager.getSlackBotPort();

        String token = configurationManager.getSlackToken();

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
            server.addMapping("/shutdown", new ShutdownHandler());
            server.addMapping("/shutdown?", new ShutdownHandler());
//            server.addMapping("//favicon.ico", new PageNotFoundHandler());
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
            invertedIndexServer.addMapping("/shutdown", new ShutdownHandler());
            invertedIndexServer.addMapping("/shutdown?", new ShutdownHandler());
//            invertedIndexServer.addMapping("/favicon.ico", new PageNotFoundHandler());

            invertedIndexServer.start();
        });

        slackBotStartThread.start();
        invertedIndexStartThread.start();
    }
}
