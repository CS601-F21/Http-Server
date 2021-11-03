/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class purpose : To generate the HTML
 */
package ServerPackage.HtmlUtils;

import ServerPackage.HttpUtils.HttpConstants;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;

/**
 * All of our responses should have a html in them, this class generates the html which will then be used by the responses
 */

public class HtmlGenerator {

    public HtmlGenerator(){

    }

    /**
     * Method takes in a string, and generates a basic html which just displays the string
     * @param text
     * @return
     */
    public String getBasicHTML (String text) {
        String tempHTML =
                "<!DOCTYPE html>"+
                "<html>" +
                    "<head>" +
                        "<title>Test App</title>" +
                    "</head>" +
                    "<body>" +
                        "<h1>" + StringEscapeUtils.escapeHtml(text) + "</h1>" +
                    "</body>" +
                "</html>";
        return tempHTML;
    }

    /**
     * Since we need to generate an input form in which the client will actually pass their query, we need to generate the html for that.
     * This method does that :
     *  The action is the action for the form, aka where the form will redirect based on the submit button
     *  The title is the title of the webpage
     *  The textBox label is the label for the textbox
     * @param title
     * @param action
     * @param textBoxLabel
     * @return
     */
    public String getInputForm (String title, String action, String textBoxLabel) {
        /**
         * First we need to generate the head
         */
        String head = generateHead(title);
        /**
         * Then we generate the form
         */
        String form = generateForm(action, textBoxLabel);
        /**
         * Then we pass the form to the body so that it can encapsulate the form in itself
         */
        String body = generateBody(form);
        /**
         * finally we have to generate the html, that is just the head+body
         */
        String html = head + body;

        /**
         * returning html
         */
        return html;
    }

    /**
     * Method to generate the head of the HTML, takes in a string title, the title will be the title of the html page
     * @param title
     * @return
     */
    private String generateHead (String title){
        String head =
                "<!DOCTYPE html> <html lang=\"en\" style = \"height: 100%; width: 100%\"> <head> <link rel=\"shortcut icon\" href=\"#\"/>\n <title>" + StringEscapeUtils.escapeHtml(title) + "</title></head>";
        return head;
    }

    /**
     * Method to generate the body of the html, takes in the html, and encapsulates it in the body
     * @param html
     * @return
     */
    private String generateBody (String html){
        String body =
                "<body style = \"height: 100%; width: 100%;\">" +
                        "<div class = \"main\" style=\"height: 100%; margin-left: 20%; margin-right: 20%; margin-top: 5%;\">" +
                            html +
                        "</div>"+
                "</body>" +
         " </html>";
        return body;
    }

    /**
     * Method to generate the form, takes in the action parameter and the textbox label parameter.
     * The action redirects the form to the desired url
     * @param action
     * @param textBoxLabel
     * @return
     */
    private String generateForm (String action, String textBoxLabel){
        String form =
                "<div class = \"formContainer\" style=\"display: flex; height: 5%;\">" +
                        "<form action = \"" + action + "\" method = \"" + HttpConstants.POST + "\" style = \"width:80% \" >" +
                              "<label for = \"message\" style = \"margin-right:2%; margin-left:3.5%\">" + StringEscapeUtils.escapeHtml(textBoxLabel) + "</label>" +
                              "<input type = \"text\" id = \"message\" name=\"message\"/>" +
                              "<input type = \"submit\" value=\"submit\"/>" +
                       "</form>" +
                        "<form action=\"/shutdown\" style=\" width:20%; display: flex; justify-content: right; \" method=\"GET\">\n" +
                        "    <input type=\"submit\" style=\"height: 70%; margin-right: 6%;\" value=\"shutdown\"/>\n" +
                        "</form>"+
                "</div>";

        return form;
    }

    /**
     * The output of a query will be a list of objects, this generate the container for that list
     * Takes in the output list
     * @param output
     * @return
     */
    private String generateOutputContainer (ArrayList<String> output){

        //changing all but last item to a list item
        for (int i = 0; i < output.size()-1; i++){
            String item = output.get(i);
            output.set(i, generateLiItems(item));
        }

        //have to set border bottom for the last element, we do that manually over here
        int lastIndex = output.size()-1;
        String lastEl = output.get(lastIndex);
        lastEl = "<li style=\"border: solid; border-bottom: solid;\">" + StringEscapeUtils.escapeHtml(lastEl) + "</li>";
        output.set(lastIndex, lastEl);

        String outputContainer =
                "<div class = \"outputContainer\" style=\"height: 80%; overflow: scroll;\">" +
                "<ul class = \"list\" style=\"list-style-type: none;\">";

        for (String out : output){
            outputContainer += out;
        }

        outputContainer += "</ul>" + "</div>";

        return outputContainer;
    }

    /**
     * method to generate the list elements
     * @param item
     * @return
     */
    private String generateLiItems (String item){
        return "<li style=\"border: solid; border-bottom: none;\">" + StringEscapeUtils.escapeHtml(item) + "</li>";
    }

    /**
     * method to generate the output list, takes in the title, action, text box label and the search result
     * @param title
     * @param action
     * @param textBoxLabel
     * @param searchResults
     * @return
     */
    public String generateOutputList(String title, String action, String textBoxLabel, ArrayList<String> searchResults) {
        String head = generateHead(title);
        String form = generateForm(action, textBoxLabel);
        String output = generateOutputContainer(searchResults);
        String combinedOutput = form + output;
        String body = generateBody(combinedOutput);
        String html = head + body;
        return html;
    }

    /**
     * if we just want to have a single string as the output, we can use this
     * @param title
     * @param action
     * @param textBoxLabel
     * @param text
     * @return
     */
    public String generateSingleItemResponse(String title, String action, String textBoxLabel, String text) {
        String head = generateHead(title);
        String form = generateForm(action, textBoxLabel);

        //sending a new ArrayList with just one response, doing this so that we can then use the generateOutputContainer method
        ArrayList<String> outputList = new ArrayList<>();
        outputList.add(text);
        String output = generateOutputContainer(outputList);
        String combinedOutput = form + output;
        String body = generateBody(combinedOutput);
        String html = head + body;
        return html;
    }
}
