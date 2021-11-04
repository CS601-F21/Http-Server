/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class purpose : Bad request handler
 */
package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.HttpUtils.HTTPParser;
import ServerPackage.HttpUtils.HttpWriter;

/**
 * If we get a bad request, we call this handler, it generates the bad request html and the bad request http code
 */

import java.io.IOException;

public class BadRequestHandler implements Handler{
    /**
     * Every handler takes in a request and a response
     * @param req
     * @param res
     */
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            /**
             * generating the badrequest response
             */
            String response = responseGenerator.BAD_REQUEST();

            /**
             * writing the response
             */
            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
