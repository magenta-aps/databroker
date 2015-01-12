package dk.magenta.databroker.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lars on 08-01-15.
 */
public class DataProviderConfiguration extends HashMap<String, List<String>> {
    public DataProviderConfiguration() {
    }

    public DataProviderConfiguration(String jsonString) throws JSONException {
        this(new JSONObject(jsonString != null && !jsonString.isEmpty() ? jsonString : "{}"));
    }
    public DataProviderConfiguration(JSONObject object) {
        for (Object oKey : object.keySet()) {
            String key = (String) oKey;
            Object value = object.get(key);
            if (value instanceof JSONArray) {
                JSONArray jArr = (JSONArray) value;
                ArrayList<String> pArr = new ArrayList<String>(jArr.length());
                for (int i=0; i<jArr.length(); i++) {
                    pArr.add((String) jArr.get(i));
                }
                this.put(key, pArr);
            } else {
                ArrayList<String> pArr = new ArrayList<String>(1);
                pArr.add((String) value);
                this.put(key, pArr);
            }
        }
    }
    public DataProviderConfiguration(Map<String, String[]> map) {
        for (String key : map.keySet()) {
            this.put(key, this.arrayToList(map.get(key)));
        }
    }


    public boolean update(String key, String value) {
        return this.update(key, new String[]{value});
    }
    public boolean update(String key, String[] value) {
        List<String> oldValues = this.get(key);
        List<String> newValues = this.arrayToList(value);
        if (!newValues.equals(oldValues)) {
            this.put(key, newValues);
            return true;
        }
        return false;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        for (String key : this.keySet()) {
            List<String> values = this.get(key);
            if (values.size() == 1) {
                object.put(key, values.get(0));
            } else {
                object.put(key, new JSONArray(values));
            }
        }
        return object;
    }
    public String toString() {
        return this.toJSON().toString();
    }

    public Map<String, String[]> toArrayMap() {
        HashMap<String, String[]> map = new HashMap<String, String[]>();
        for (String key : this.keySet()) {
            map.put(key, this.listToArray(this.get(key)));
        }
        return map;
    }



    private List<String> arrayToList(String[] values) {
        ArrayList<String> list = new ArrayList<String>(values.length);
        for (int i=0; i<values.length; i++) {
            list.add(values[i]);
        }
        return list;
    }

    private String[] listToArray(List<String> values) {
        return values.toArray(new String[values.size()]);
    }
}
