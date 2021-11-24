package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;

import java.io.IOException;

public class ShutdownHandler implements Handler{
    private volatile boolean running;

    public ShutdownHandler (){
        this.running = true;
    }

    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.homePageResponse("Server is shut down");
            initiateShutdown();

            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initiateShutdown (){
        this.running = false;
    }

    public boolean getRunning (){
        return running;
    }

    public void setRunning(boolean running){
        this.running = running;
    }
}
