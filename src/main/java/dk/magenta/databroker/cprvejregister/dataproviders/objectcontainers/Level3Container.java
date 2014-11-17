package dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lars on 11-11-14.
 */

// One level further up
// e.g. { a: { b: { c: 42 }, d: { e: 23, f: 0 } } }
public class Level3Container<T> extends HashMap<String, Level2Container<T>> {
    public T get(String ident3, String ident2, String ident1) {
        if (this.containsKey(ident3)) {
            return this.get(ident3).get(ident2, ident1);
        }
        return null;
    }

    public void put(String ident3, String ident2, String ident1, T value) {
        if (!this.containsKey(ident3)) {
            this.put(ident3, new Level2Container<T>());
        }
        this.get(ident3).put(ident2, ident1, value);
    }

    public boolean put(String ident3, String ident2, String ident1, T value, boolean reportCollision) {
        if (!this.containsKey(ident3)) {
            this.put(ident3, new Level2Container<T>());
        }
        return this.get(ident3).put(ident2, ident1, value, reportCollision);
    }

    public List<T> getList() {
        ArrayList<T> list = new ArrayList<T>();
        for (String key : this.keySet()) {
            list.addAll(this.get(key).getList());
        }
        return list;
    }

    public List<T> getList(String ident3) {
        ArrayList<T> list = new ArrayList<T>();
        list.addAll(this.get(ident3).getList());
        return list;
    }

    public List<T> getList(String ident3, String ident2) {
        ArrayList<T> list = new ArrayList<T>();
        list.addAll(this.get(ident3).getList(ident2));
        return list;
    }

    public int totalSize() {
        int count = 0;
        for (String key : this.keySet()) {
            count += this.get(key).totalSize();
        }
        return count;
    }
}