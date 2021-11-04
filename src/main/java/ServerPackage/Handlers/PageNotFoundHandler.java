/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class purpose : Page not found handler
 */

package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;

import java.io.IOException;

/**
 * If the client gives a path which we do not have yet, we will call this handler
 */

public class PageNotFoundHandler implements Handler{
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            /**
             * Generating the NOTFOUND response
             */
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.NOT_FOUND();

            /**
             * Writing the response down
             */
            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
