package ServerPackage.HttpUtils.Validators;

import java.util.HashMap;

public class HttpRequestValidator {
    
    public static boolean validateRequest (String request){
        RequestValidator requestValidator = new RequestValidator(request);
        boolean valid = requestValidator.isValid();
        return valid;
    }
    
    public static boolean validateHeader (String header){
        HeaderValidator headerValidator = new HeaderValidator(header);
        boolean valid = headerValidator.isValid();
        return valid;
    }
    
    public static boolean validateBody (HashMap<String, String> headers, String requestType, String body){
        MessageBodyValidator messageBodyValidator = new MessageBodyValidator(headers, requestType, body);
        boolean valid = messageBodyValidator.isValid();
        return valid;
    }

    /**
     * Method will be called once we have validated all our headers are in correct format, we validate whether
     * we have validate whether we have the required headers.
     * @param header
     * @return
     */
    public static boolean validateAllHeader(HashMap<String, String> header) {
        if (header.containsKey("Host")){
            return true;
        }
        return false;
    }
}
