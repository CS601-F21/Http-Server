package ServerPackage.HttpUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpWriter implements AutoCloseable {
    Socket socket;
    OutputStream writer;

    public HttpWriter (Socket socket) throws IOException {
        this.socket = socket;
        this.writer = socket.getOutputStream();
    }

    public void writeResponse (String response) throws IOException {
        writer.write(response.getBytes());
    }


    @Override
    public void close() throws IOException {
        writer.close();
    }
}
