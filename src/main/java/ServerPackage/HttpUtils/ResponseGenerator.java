/**
 * Author name : Shubham Pareek
 * Author email : spareek@dons.usfca.edu
 * Class purpose : Generate a Http Response
 */
package ServerPackage.HttpUtils;

import ServerPackage.HtmlUtils.HtmlGenerator;
import ServerPackage.HtmlUtils.HtmlValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * This is the class where we generate the response.
 * This class is generally called by the handlers, but not always.
 * Generating a response requires generating the appropriate html, and the response line, then finally putting them
 * together
 *
 * Every response generates an appropriate html, then combines it with the response and returns that.
 */

public class ResponseGenerator {
    /**
     * Logger for logging purposes
     */
    private static final Logger LOGGER = LogManager.getLogger(ResponseGenerator.class);
    public ResponseGenerator() {}

    /**
     * Method takes in the title, the form action and the textbox label, generated the appropriate
     * HTML for the request and then generates the response line.
     * Then finally it calls the getResponse method to combine the two together to generate a proper
     * HTTP response
     * @param title
     * @param action
     * @param textBoxLabel
     * @return
     */
    public String generateGETResponse (String title, String action, String textBoxLabel){
        LOGGER.info("Generating GET response -------------");
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getInputForm(title, action, textBoxLabel);
        String response = getResponse(generatedHtml, HttpConstants.OK);

        return response;
    }

    /**
     * Method is called by the invertedIndex handlers, once we have some result, we need to generate the appropriate html and then
     * generate a response from that
     * @param title
     * @param action
     * @param textBoxLabel
     * @param searchResults
     * @return
     */
    public String generateInvertedIndexResponse (String title, String action, String textBoxLabel, ArrayList<String> searchResults){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.generateOutputList(title, action, textBoxLabel, searchResults);
        String response = getResponse(generatedHtml, HttpConstants.OK);
        return response;
    }

    /**
     * This method is generally called if the request was valid, but for some reason we could not find the results.
     * Like if the user searches for something in the index which does not exist, we do not want them to go back to the home page,
     * but instead we inform them that the results were not found.
     * Also used by the slack handler to let the user know whether the message was sent or not
     * @param title
     * @param action
     * @param textBoxLabel
     * @param text
     * @return
     */
    public String generateSingleLineResponse(String title, String action, String textBoxLabel, String text) {
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.generateSingleItemResponse(title, action, textBoxLabel, text);
        String response = getResponse(generatedHtml, HttpConstants.OK);
        return response;
    }

    /**
     * A simple home page, with just one line of text.
     * @param text
     * @return
     */
    public String generateHomePageResponse (String text){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getBasicHTML(text);
        String response = getResponse(generatedHtml, HttpConstants.OK);

        return response;
    }

    /**
     * If we do not find a path, we call this response. The http code is 404
     * @return
     */
    public String generateNOTFOUNDResponse (){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getBasicHTML("Error 404 Page Not Found");
        String response = getResponse(generatedHtml, HttpConstants.NOT_FOUND);

        return response;
    }

    /**
     * If the user send a bad request, this is going to be the response
     * @return
     */
    public String generateBADREQUESTResponse (){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getBasicHTML("Error 400 Bad Request");
        String response = getResponse(generatedHtml, HttpConstants.BAD_REQUEST);

        return response;
    }

    /**
     * If the user uses a method which is not allowed, this is going to be the response
     * @return
     */
    public String generateMETHODNOTALLOWEDResponse (){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generateHtml = htmlGenerator.getBasicHTML("Error 405 Method Not Allowed");
        String response = getResponse(generateHtml, HttpConstants.NOT_ALLOWED);

        return response;
    }

    /**
     * This method is used to combine the Http response and the generated html.
     * @param generatedHtml
     * @param STATUS
     * @return
     */
    private String getResponse (String generatedHtml, String STATUS){
        String CRLF = HttpConstants.CRLF;
        String HEADER = generateHeader(HttpConstants.VERSION, STATUS);
        String CONTENT_LENGTH = HttpConstants.CONTENT_LENGTH;

        boolean validHtml = HtmlValidator.isValid(generatedHtml);
        System.out.println("Generated HTML is valid is ===============> " + validHtml);

        String response =
                HEADER + //this is the status line of the response
                        CONTENT_LENGTH +generatedHtml.getBytes().length + CRLF + //the header, currently we only have the length
                        CRLF +
                        generatedHtml + //actual html content
                        CRLF + CRLF;

        return response;
    }

    /**
     * The header of our response will be different based on the HTTP response code, we generate that over here
     * @param VERSION
     * @param CODE
     * @return
     */
    public static String generateHeader (String VERSION, String CODE){
        String HEADER = VERSION + " " + CODE + HttpConstants.CRLF;
        return HEADER;
    }
}
