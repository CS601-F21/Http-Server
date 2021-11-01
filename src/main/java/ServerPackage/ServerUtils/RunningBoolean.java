package ServerPackage.ServerUtils;

public class RunningBoolean {
    private volatile boolean running = true;

    public void setRunningToFalse(){
        running = false;
    }

    public boolean isRunning(){
        return running;
    }
}
