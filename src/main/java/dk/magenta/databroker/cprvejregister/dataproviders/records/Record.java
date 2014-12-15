package dk.magenta.databroker.cprvejregister.dataproviders.records;

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
    public void resetProcessed() {
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
        String value = this.get(key);
        try {
            return Integer.parseInt(value, 10);
        } catch (NumberFormatException e) {
            return 0;
        }
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
