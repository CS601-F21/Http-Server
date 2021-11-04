/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : has a bunch of constants we will be using to make the code more readable
 */
package ServerPackage.HttpUtils;

/**
 * Class has a bunch of constants we will be using throughout the project
 */

public class HttpConstants {

    /**USED FOR CORRECT FORMAT OF RESPONSE**/
    public static final String CRLF = "\r\n";

    /**HTTP METHODS**/
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String HEAD = "HEAD";
    public static final String DELETE = "DELETE";
    public static final String CONNECT = "CONNECT";
    public static final String OPTIONS = "OPTIONS";
    public static final String TRACE = "TRACE";
    public static final String PATCH = "PATCH";
    public static final String VERSION = "HTTP/1.1";

    public static final String[] allMethods = {
            GET, POST, PUT, HEAD, DELETE, CONNECT, OPTIONS, TRACE, PATCH
    };

    /**HTTP CODES**/
    public static final String OK = "200 OK";
    public static final String BAD_REQUEST = "400 Bad Request";
    public static final String NOT_FOUND = "404 Not Found";
    public static final String NOT_ALLOWED = "405 Method Not Allowed";
    public static final String INTERNAL_SERVER_ERROR = "500 Internal Server Error";
    /**FOR SPECIFYING INFORMATION**/
    public static final String CONTENT_LENGTH = "Content-Length:";
    public static final String CONNECTION_CLOSE = "Connection: close";


    /**
     * All the methods which require a body
     */
    public static final String[] bodyMethods = {
            HttpConstants.POST, HttpConstants.PATCH, HttpConstants.PUT, HttpConstants.DELETE, HttpConstants.HEAD
    };

    /**
     * Supported methods for our servers
     */
    public static final String[] supportedMethods = {
            HttpConstants.GET, HttpConstants.POST
    };




}
