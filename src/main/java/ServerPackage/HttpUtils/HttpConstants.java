package ServerPackage.HttpUtils;

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

    /**FOR SPECIFYING INFORMATION**/
    public static final String CONTENT_LENGTH = "Content-Length:";
    public static final String CONNECTION_CLOSE = "Connection: close";

    public static String generateHeader (String VERSION, String CODE){
        String HEADER = VERSION + " " + CODE + CRLF;
        return HEADER;
    }

    public static final String commonHTTPHeaders[] ={
            "WWW-Authenticate", "Authorization", "Proxy-Authenticate", "Proxy-Authorization",
            "Age", "Cache-Control", "Clear-Site-Data", "Expires", "Pragma", "Warning", "Accept-CH",
            "Accept-CH-Lifetime", "Content-DPR", "Device-Memory", "DPR", "Viewport-Width", "Width",
            "Downlink", "ECT", "RTT", "Save-Data", "Last-Modified", "ETag", "If-Match", "If-None-Match",
            "If-Modified-Since", "If-Unmodified-Since", "Vary", "Connection", "Keep-Alive", "Accept",
            "Accept-Encoding", "Accept-Language", "Expect", "Cookie", "Set-Cookie", "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers", "Access-Control-Allow-Methods",
            "Access-Control-Expose-Headers", "Access-Control-Max-Age", "Access-Control-Request-Headers",
            "Access-Control-Request-Method", "Origin", "Timing-Allow-Origin", "Content-Disposition",
            "Content-Length", "Content-Type", "Content-Encoding", "Content-Language", "Content-Location",
            "Forwarded", "X-Forwarded-For", "X-Forwarded-Host", "X-Forwarded-Proto", "Via", "Location",
            "From", "Host", "Referer", "Referrer-Policy", "User-Agent", "Allow", "Server",
            "Accept-Ranges", "Range", "If-Range", "Content-Range", "Public-Key-Pins", "Public-Key-Pins-Report-Only",
            "Sec-Fetch-Site", "Sec-Fetch-Mode", "Sec-Fetch-User", "Sec-Fetch-Dest",
            "Cross-Origin-Embedder-Policy", "Cross-Origin-Opener-Policy", "Cross-Origin-Resource-Policy",
            "Content-Security-Policy", "Content-Security-Policy-Report-Only",
            "Expect-CT", "Feature-Policy", "Origin-Isolation", "Strict-Transport-Security",
            "Upgrade-Insecure-Requests", "X-Content-Type-Options", "X-Download-Options", "X-Frame-Options",
            "X-Permitted-Cross-Domain-Policies", "X-Powered-By", "X-XSS-Protection",
            "Transfer-Encoding", "TE", "Trailer", "Alt-Svc", "Date", "Early-Data", "Large-Allocation",
            "Link", "Push-Policy", "Retry-After", "Accept-Push-Policy", "Accept-Signature",
            "Signature", "Signed-Headers", "Server-Timing", "Service-Worker-Allowed",
            "SourceMap", "Upgrade", "X-DNS-Prefetch-Control", "DNT"
    };

    public static final String[] bodyMethods = {
            HttpConstants.POST, HttpConstants.PATCH, HttpConstants.PUT, HttpConstants.DELETE, HttpConstants.HEAD
    };

    public static final String[] supportedMethods = {
            HttpConstants.GET, HttpConstants.POST
    };




}
