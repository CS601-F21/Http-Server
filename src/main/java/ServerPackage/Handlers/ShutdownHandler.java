package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;

import java.io.IOException;

public class ShutdownHandler implements Handler{
    private volatile boolean running;
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.NOT_FOUND();

            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown (){
        this.running = false;
    }

    public boolean getRunning (){
        return running;
    }

    public void setRunning(boolean running){
        this.running = running;
    }
}
