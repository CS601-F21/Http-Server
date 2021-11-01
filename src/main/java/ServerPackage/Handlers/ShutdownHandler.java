package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;

import java.io.IOException;

public class ShutdownHandler implements Handler{
    private volatile boolean running;
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.generateNOTFOUNDResponse();

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
