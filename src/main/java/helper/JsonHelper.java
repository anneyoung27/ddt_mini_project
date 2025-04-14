package helper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonHelper {

    public JSONObject readJson(String filename) throws Exception {
        JSONParser jsonParser = new JSONParser();
        FileReader filereader = new FileReader((filename));

        JSONObject obj = (JSONObject) jsonParser.parse(filereader);
        return obj;
    }

    public JSONArray readJson(String filename, String JSONkey) throws Exception {
        JSONParser jsonParser = new JSONParser();
        FileReader filereader = new FileReader((filename));

        JSONObject obj = (JSONObject) jsonParser.parse(filereader);
        return (JSONArray) obj.get(JSONkey);
    }
}
