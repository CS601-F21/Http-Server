/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class Purpose : slack response object
 */
package ServerPackage.Handlers.Slackutils;

/**
 * To get to know whether the message was actually sent or not, we need this object.
 * This object will be used by gson to convert the json string
 */

public class SlackResponseObject {
    /**
     * We only care about whether the message was sent or no
     */
    private boolean ok;

    /**
     * If the message was not sent, we want to know the error
     */
    private String error;

    public SlackResponseObject (boolean ok, String error){
        /**
         * Constructing the class variables
         */
        this.ok = ok;
        this.error = error;
    }

    /**
     * getter which lets the user know whether the message was sent or not
     * @return
     */
    public boolean isOk (){
        return ok;
    }

    /**
     * If the message was not sent, the user can know what the error was
     * @return
     */
    public String getError(){
        return error;
    }
}
