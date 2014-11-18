package dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers;

import java.util.*;

/**
 * Created by lars on 11-11-14.
 */
// Your basic hashmap for storing values by keys
// e.g. { a: 42 }
public class Level1Container<T> extends HashMap<String, T> {

    public T get(int ident1) {
        return this.get(""+ident1);
    }

    public boolean put(String key, T value, boolean reportCollision) {
        if (reportCollision && this.containsKey(key)) {
            return false;
        }
        this.put(key, value);
        return true;
    }

    public void put(int ident1, T value) {
        this.put(""+ident1, value);
    }

    public boolean put(int ident1, T value, boolean reportCollision) {
        return this.put(""+ident1, value, reportCollision);
    }

    public List<T> getList() {
        ArrayList<T> list = new ArrayList<T>();
        for (String key : this.keySet()) {
            list.add(this.get(key));
        }
        return list;
    }


    public Set<Integer> intKeySet() {
        HashSet<Integer> intSet = new HashSet<Integer>();
        for (String key : this.keySet()) {
            intSet.add(Integer.parseInt(key, 10));
        }
        return intSet;
    }
}