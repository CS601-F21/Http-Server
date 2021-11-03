/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class Purpose : contains the boolean which lets us know whether the server is still running
 */

package ServerPackage.ServerUtils;

/**
 * The purpose of this class is to let us know whether the server is still running or not.
 * The reason for creating a separate class is that since, for every request on a server, we create a separate thread
 * to handle it, we might not be able to close the server when the user wants to.
 * Since classes are passed by reference, any changes made over here will be shown everywhere in the projects and we can
 * then close down the server.
 */

public class RunningBoolean {
    /**
     * Using a volatile boolean as we want this value to be written in the memory directly
     */
    private volatile boolean running = true;

    /**
     * If the user calls the shutdown method, then this method will be called
     */
    public void setRunningToFalse(){
        running = false;
    }

    /**
     * We call this method everytime in the while loop to check whether the server is still running or not
     */
    public boolean isRunning(){
        return running;
    }
}
