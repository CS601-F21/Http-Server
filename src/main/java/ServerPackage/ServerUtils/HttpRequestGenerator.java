package ServerPackage.ServerUtils;

import java.util.HashMap;

public class HttpRequestGenerator {

    public static HashMap<String, String> generateRequest (String request){
        String[] brokenDownRequest = request.split(" ");

        HashMap<String, String> parsedRequest = new HashMap<>();
        parsedRequest.put("Type", brokenDownRequest[0]);
        parsedRequest.put("Path", brokenDownRequest[1]);
        parsedRequest.put("httpVersion", brokenDownRequest[2]);
        return parsedRequest;
    }

    public static HashMap<String, String> generateHeader(String headerLine, HashMap<String, String> header) {
        String[] headerParts = headerLine.split(":", 2);
        String headerLabel = headerParts[0];
        String headerBody = headerParts[1];

        header.put(headerLabel, headerBody);
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
