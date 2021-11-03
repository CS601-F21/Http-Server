package ServerPackage.HttpUtils.Validators;

import ServerPackage.HttpUtils.HttpConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RequestValidator {
    private static final Logger LOGGER = LogManager.getLogger(RequestValidator.class);
    private boolean valid;
    private String request;
    String brokenDownRequest[];
    public RequestValidator (String request){
        this.request = request;
        this.valid = true;
        startValidation();
    }

    private void startValidation(){
        validateRequestLength();
        validateRequestType();
        validateRequestPath();
        validateHttpVersion();
    }

    private void validateRequestLength(){
//        LOGGER.info("Validating length");
        brokenDownRequest = request.split(" ");
        if (brokenDownRequest.length != 3){
            valid = false;
        }
//        LOGGER.info("Length is " + valid);
    }

    private void validateRequestType(){
        if (valid) {
//            LOGGER.info("Validating method");
            String[] allRequestTypes = HttpConstants.allMethods;
            boolean requestInRequestType = false;
            String requestMethod = brokenDownRequest[0];

            for (int i = 0; i < allRequestTypes.length; i++) {
                if (requestMethod.equals(allRequestTypes[i])) {
                    requestInRequestType = true;
                    break;
                }
            }
            valid = requestInRequestType;
//            LOGGER.info("Method is " + valid);
        }
    }

    private void validateRequestPath(){
        if (valid) {
//            LOGGER.info("Validating Path");
            String path = brokenDownRequest[1];
            char[] brokenDownPath = path.toCharArray();

            //only requirement for path is that it should start with a / afaik
            if (!(brokenDownPath[0] == '/')) {
                valid = false;
            }
//            LOGGER.info("Path is " + valid);
        }
    }

    private void validateHttpVersion(){
        if (valid) {
//            LOGGER.info("Validating Http Version");
            String version = brokenDownRequest[2];
            valid = (version.equals(HttpConstants.VERSION));
//            LOGGER.info("Version is " + valid);
        }

    }

    public boolean isValid(){
        return valid;
    }
}
