package frc.display;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JsonTab {

    String file;
    String name;

    JSONObject jo;

    public JsonTab(String file) {
        this.file = file;
    }

    public void load() throws Exception {
        // Parsing File
        Object obj = new JSONParser().parse(new FileReader(file));

        // Typecasting to JSONObject
        jo = (JSONObject) obj;

        name = (String) jo.get("tabName");

        for(Iterator iterator = jo.keySet().iterator(); iterator.hasNext();) {

        }

    }

}
