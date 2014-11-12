package dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lars on 11-11-14.
 */
// Your basic hashmap for storing values by keys
// e.g. { a: 42 }
public class Level1Container<T> extends HashMap<String, T> {
    public boolean put(String key, T value, boolean reportCollision) {
        if (reportCollision && this.containsKey(key)) {
            return false;
        }
        this.put(key, value);
        return true;
    }

    public List<T> getList() {
        ArrayList<T> list = new ArrayList<T>();
        for (String key : this.keySet()) {
            list.add(this.get(key));
        }
        return list;
    }
}