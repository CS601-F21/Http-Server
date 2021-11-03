package ServerPackage.Handlers;

import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;

public interface Handler {
    /**
     * Interface for the Handler objects, this will be useful for the addMapping purpose as well, as we can then have key-value pairs where
     * the key is the filePath and the value is some Handler
     *
     * Only has one method called handle, this method is where the different Handlers will actually do what they have to do handle the HTTP
     * request
     */

    public void handle (HTTPParser req, HttpWriter res);
}
