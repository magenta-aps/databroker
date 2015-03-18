package dk.magenta.databroker.register.records;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by lars on 15-12-14.
 */
public abstract class Record extends HashMap<String, String> {

    private boolean visited;
    public boolean getVisited() {
        return visited;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public void setVisited() {
        this.setVisited(true);
    }
    public void resetVisited() {
        this.visited = false;
    }

    public String getRecordType() {
        return null;
    }
    public String getRecordClass() {
        String[] classParts = this.getClass().getCanonicalName().split("\\.");
        return classParts[classParts.length-1];
    }

    public int getInt(String key) {
        return this.getInt(key, false);
    }

    public int getInt(String key, boolean lenient) {
        String value = this.get(key);
        if (lenient) {
            value = value.replaceAll("[^\\d]", "");
        }
        try {
            return Integer.parseInt(value, 10);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public long getLong(String key) {
        return this.getLong(key, false);
    }

    public long getLong(String key, boolean lenient) {
        String value = this.get(key);
        if (lenient) {
            value = value.replaceAll("[^\\d]", "");
        }
        try {
            return Long.parseLong(value, 10);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean getBoolean(String key) {
        return this.getInt(key) == 1;
    }

    public Character getChar(String key) {
        String value = this.get(key);
        return value != null && !value.isEmpty() ? value.charAt(0) : null;
    }


    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", this.getRecordClass());
        for (String key : this.keySet()) {
            obj.put(key, this.get(key));
        }
        return obj;
    }
}
