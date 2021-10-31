package ServerPackage.ServerUtils.Validators;

import ServerPackage.ServerUtils.Validators.HeaderValidator;
import ServerPackage.ServerUtils.Validators.MessageBodyValidator;
import ServerPackage.ServerUtils.Validators.RequestValidator;

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
}
