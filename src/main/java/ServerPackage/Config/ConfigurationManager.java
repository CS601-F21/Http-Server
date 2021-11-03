/**
 * Author Name : Shubham Pareek
 * Author Email : spareek@dons.usfca.edu
 * Class purpose : To read the config file
 */
package ServerPackage.Config;

/**
 * https://www.baeldung.com/gson-json-to-map to learn how to use gson to parse json to a map
 */

/**
 * Class takes in as input the location of the config file and then reads it.
 * In our case the config file only has 3 configuration, so we just parse those
 */

import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;

public class ConfigurationManager {

    /**
     * file path for the config file
     */
    private String filePath;

    /**
     * The hashmap containing the configuration
     */
    private HashMap<String, String> configuration;

    public ConfigurationManager (String filePath){
        /**
         * constructing the filepath and getting the configs
         */
        this.filePath = filePath;
        getConfig(this.filePath);
    }

    private void getConfig (String filePath){
        /**
         * The config file is in json format, so we have to use gson to parse it
         */
        Gson gson = new Gson();
        File file = new File(filePath);
        /**Since our config file is small, we can just keep concatenating all the lines in the json file, then use Gson to parse them**/
        String fullJson = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = br.readLine()) != null){
                fullJson += line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration = gson.fromJson(fullJson, HashMap.class);
    }

    public String getSlackToken (){
        /**
         * Getter for the slack token
         */
        return configuration.get("slack token");
    }

    public int getSlackBotPort (){
        /**
         * Getter for the slackbot port
         */
        String portString = configuration.get("slack-bot port");
        int port = Integer.parseInt(portString);
        return port;
    }

    public int getIndexPort (){
        /**
         * Getter for the invertedIndex port
         */
        String portString = configuration.get("index port");
        int port = Integer.parseInt(portString);
        return port;
    }


}
