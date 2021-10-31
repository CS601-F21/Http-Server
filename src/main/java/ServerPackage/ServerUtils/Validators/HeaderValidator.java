package ServerPackage.ServerUtils.Validators;

import ServerPackage.HttpUtils.HttpConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class HeaderValidator {

    private static final Logger LOGGER = LogManager.getLogger(HeaderValidator.class);
    private boolean valid;
    private String header;
    private String headerParts[];

    public HeaderValidator (String header){
//        LOGGER.info("Validating header ==> " + header);
        this.header = header;
        this.valid = true;
        startValidation();
    }

    private void startValidation(){
        //validates that the header can indeed be broken down into 2 parts
        validateHeaderLength();
        validateHeaderStartsWithCapitalLetter();
        validateHeaderName();
    }

    private void validateHeaderLength() {
        headerParts = header.split(":", 2);

//        LOGGER.info("header parts length is : " + headerParts.length);
        if (headerParts.length != 2){
            valid = false;
            return;
        }

        if (headerParts[0].strip() == ""){
            LOGGER.info("Header validator, key is empty");
            valid = false;
            return;
        }

        if (headerParts[1].strip() == ""){
            LOGGER.info("Header validator, value is empty");
            valid = false;
            return;
        }
    }

    private void validateHeaderStartsWithCapitalLetter() {
        if (valid) {
            String headerTitle = headerParts[0];
            char[] brokerDownTitle = headerTitle.toCharArray();
            char firstLetter = brokerDownTitle[0];

            if (!Character.isUpperCase(firstLetter)) {
                valid = false;
            }
        }
    }

    private void validateHeaderName() {
        if (valid) {
            String headerName = headerParts[0];
            boolean validHeaderName = false;
            String[] commonHeaders = HttpConstants.commonHTTPHeaders;

            for (String name : commonHeaders) {
                if (headerName.equals(name)) {
                    validHeaderName = true;
                    break;
                }
            }
            valid = validHeaderName;
        }
    }

    public boolean isValid() {
        return valid;
    }
}
