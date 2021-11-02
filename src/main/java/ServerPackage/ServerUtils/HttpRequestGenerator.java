package ServerPackage.ServerUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class HttpRequestGenerator {

    private static final Logger LOGGER = LogManager.getLogger(HttpRequestGenerator.class);

    public static HashMap<String, String> generateRequest (String request){
        String[] brokenDownRequest = request.split(" ");

        HashMap<String, String> parsedRequest = new HashMap<>();
        parsedRequest.put("Type", brokenDownRequest[0]);
        parsedRequest.put("Path", brokenDownRequest[1]);
        parsedRequest.put("HttpVersion", brokenDownRequest[2]);
        return parsedRequest;
    }

    public static HashMap<String, String> generateHeader(String headerLine, HashMap<String, String> header) {

        String[] headerParts = headerLine.split(":", 2);
        String headerLabel = headerParts[0];
        String headerBody = headerParts[1];

//        LOGGER.info("header size before is : " + header.size());
//        LOGGER.info("req generator header label -----> " + headerLabel + " ---> header body ---> " + headerBody);

        header.put(headerLabel, headerBody);
//        LOGGER.info("header size after is : " + header.size());

        return header;
    }

    public static StringBuffer generateBody(String bodyLine, StringBuffer messageBody) {
        messageBody.append(bodyLine).append("\n\r");
        return messageBody;
    }

    public static String getBodyToString(StringBuffer body){
        String stringBody = body.toString();

        if (stringBody.strip().equals("")){
            return stringBody;
        }

        String[] splitBody = stringBody.split("=", 2);
        return splitBody[1];
    }
}
