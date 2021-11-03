package ServerPackage.HtmlUtils;

import ServerPackage.HttpUtils.HttpConstants;

import java.util.ArrayList;

public class HtmlGenerator {

    public HtmlGenerator(){

    }

    public String getBasicHTML (String text) {
        String tempHTML =
                "<html>" +
                    "<head>" +
                        "<title>Test App</title>" +
                    "</head>" +
                    "<body>" +
                        "<h1>" + text + "</h1>" +
                    "</body>" +
                "</html>";
        return tempHTML;
    }

    public String getInputForm (String title, String action, String textBoxLabel) {
        String head = generateHead(title);
        String form = generateForm(action, textBoxLabel);
        String body = generateBody(form);
        String html = head + body;
        return html;
    }

    private String generateHead (String title){
        String head =
                "<!DOCTYPE html> <html lang=\"en\" style = \"height: 100%; width: 100%\"> <head> <link rel=\"shortcut icon\" href=\"#\"/>\n <title>" + title + "</title></head>";
        return head;
    }

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

    private String generateForm (String action, String textBoxLabel){
        String form =
                "<div class = \"formContainer\" style=\"display: flex; height: 5%;\">" +
                        "<form action = \"" + action + "\" method = \"" + HttpConstants.POST + "\" style = \"width:80% \" >" +
                              "<label for = \"message\" style = \"margin-right:2%; margin-left:3.5%\">" + textBoxLabel + "</label>" +
                              "<input type = \"text\" id = \"message\" name=\"message\"/>" +
                              "<input type = \"submit\" value=\"submit\"/>" +
                       "</form>" +
                        "<form action=\"/shutdown\" style=\" width:20%; display: flex; justify-content: right; \" method=\"GET\">\n" +
                        "    <input type=\"submit\" style=\"height: 70%; margin-right: 6%;\" value=\"shutdown\"/>\n" +
                        "</form>"+
                "</div>";

        return form;
    }

    private String generateOutputContainer (ArrayList<String> output){

        //changing all but last item to a list item
        for (int i = 0; i < output.size()-1; i++){
            String item = output.get(i);
            output.set(i, generateLiItems(item));
        }

        //have to set border bottom for the last element, we do that manually over here
        int lastIndex = output.size()-1;
        String lastEl = output.get(lastIndex);
        lastEl = "<li style=\"border: solid; border-bottom: solid;\">" + lastEl + "</li>";
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

    private String generateLiItems (String item){
        return "<li style=\"border: solid; border-bottom: none;\">" + item + "</li>";
    }

    public String generateOutputList(String title, String action, String textBoxLabel, ArrayList<String> searchResults) {
        String head = generateHead(title);
        String form = generateForm(action, textBoxLabel);
        String output = generateOutputContainer(searchResults);
        String combinedOutput = form + output;
        String body = generateBody(combinedOutput);
        String html = head + body;
        return html;
    }

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
