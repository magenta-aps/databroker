package dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers;

import java.util.*;

/**
 * Created by lars on 11-11-14.
 */
// One level up from Level1Container, contains Level1Containers as keys
// e.g. { a: { b: 42, c: 23 } }
public class Level2Container<T> extends HashMap<String, Level1Container<T>> {
    public T get(String ident2, String ident1) {
        if (this.containsKey(ident2)) {
            return this.get(ident2).get(ident1);
        }
        return null;
    }

    public T get(int ident2, int ident1) {
        return this.get(""+ident2, ""+ident1);
    }

    public Level1Container<T> get(int ident2) {
        return this.get(""+ident2);
    }

    public void put(String ident2, String ident1, T value) {
        if (!this.containsKey(ident2)) {
            this.put(ident2, new Level1Container<T>());
        }
        this.get(ident2).put(ident1, value);
    }

    public boolean put(String ident2, String ident1, T value, boolean reportCollision) {
        if (!this.containsKey(ident2)) {
            this.put(ident2, new Level1Container<T>());
        }
        return this.get(ident2).put(ident1, value, reportCollision);
    }

    public void put(int ident2, int ident1, T value) {
        this.put(""+ident2, ""+ident1, value);
    }

    public boolean put(int ident2, int ident1, T value, boolean reportCollision) {
        return this.put(""+ident2, ""+ident1, value, reportCollision);
    }

    public List<T> getList() {
        ArrayList<T> list = new ArrayList<T>();
        for (String key : this.keySet()) {
            list.addAll(this.get(key).getList());
        }
        return list;
    }

    public List<T> getList(String ident2) {
        ArrayList<T> list = new ArrayList<T>();
        list.addAll(this.get(ident2).getList());
        return list;
    }

    public int totalSize() {
        int count = 0;
        for (String key : this.keySet()) {
            count += this.get(key).size();
        }
        return count;
    }

    public Set<Integer> intKeySet() {
        HashSet<Integer> intSet = new HashSet<Integer>();
        for (String key : this.keySet()) {
            intSet.add(Integer.parseInt(key, 10));
        }
        return intSet;
    }
}