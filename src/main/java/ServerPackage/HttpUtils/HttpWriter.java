/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : takes in a socket and writes to the output stream of the socket
 */
package ServerPackage.HttpUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * is the response object, takes in the socket and instantiates it, then when the user given in a string
 * it converts the string to bytes and writes it to the sockets output stream
 */
public class HttpWriter implements AutoCloseable {
    /**
     * creating the class variables
     */
    Socket socket;
    /**
     * the writer will be the one doing the actual writing
     */
    OutputStream writer;

    public HttpWriter (Socket socket) throws IOException {
        /**
         * instantialting the class variables
         */
        this.socket = socket;
        this.writer = socket.getOutputStream();
    }

    public void writeResponse (String response) throws IOException {
        /**
         * the user calls this every time they have a properly formatted response and they want to send it to the browser
         */
        writer.write(response.getBytes());
    }


    @Override
    public void close() throws IOException {
        /**
         * we have to close the socket as well.
         * We have made this class autocloseable so that the user can use the try with resources method on it
         */
        writer.close();
    }
}
