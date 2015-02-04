package dk.magenta.databroker.util.objectcontainers;

import java.util.*;

/**
 * Created by lars on 11-11-14.
 */

// One level further up
// e.g. { a: { b: { c: 42 }, d: { e: 23, f: 0 } } }
public class Level4Container<T> extends HashMap<String, Level3Container<T>> {
    public T get(String ident4, String ident3, String ident2, String ident1) {
        if (this.containsKey(ident4)) {
            return this.get(ident4).get(ident3, ident2, ident1);
        }
        return null;
    }

    public Level3Container<T> get(int ident4) {
        return this.get(""+ident4);
    }

    public void put(String ident4, String ident3, String ident2, String ident1, T value) {
        if (!this.containsKey(ident4)) {
            this.put(ident4, new Level3Container<T>());
        }
        this.get(ident4).put(ident3, ident2, ident1, value);
    }

    public boolean put(String ident4, String ident3, String ident2, String ident1, T value, boolean reportCollision) {
        if (!this.containsKey(ident4)) {
            this.put(ident4, new Level3Container<T>());
        }
        return this.get(ident4).put(ident3, ident2, ident1, value, reportCollision);
    }

    public List<T> getList() {
        ArrayList<T> list = new ArrayList<T>();
        for (String key : this.keySet()) {
            list.addAll(this.get(key).getList());
        }
        return list;
    }

    public List<T> getList(String ident4) {
        ArrayList<T> list = new ArrayList<T>();
        if (this.containsKey(ident4)) {
            list.addAll(this.get(ident4).getList());
        }
        return list;
    }

    public List<T> getList(String ident4, String ident3) {
        ArrayList<T> list = new ArrayList<T>();
        if (this.containsKey(ident4)) {
            list.addAll(this.get(ident4).getList(ident3));
        }
        return list;
    }

    public int totalSize() {
        int count = 0;
        for (String key : this.keySet()) {
            count += this.get(key).totalSize();
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

    //------------------------------------------------------------------------------------------------------------------


    public T get(String ident4, String ident3, String ident2, int ident1) {
        return this.get(ident4, ident3, ident2, ""+ident1);
    }
    public T get(String ident4, String ident3, int ident2, int ident1) {
        return this.get(ident4, ident3, ""+ident2, ""+ident1);
    }
    public T get(String ident4, String ident3, int ident2, String ident1) {
        return this.get(ident4, ident3, ""+ident2, ident1);
    }
    public T get(String ident4, int ident3, String ident2, String ident1) {
        return this.get(ident4, ""+ident3, ident2, ident1);
    }
    public T get(String ident4, int ident3, int ident2, String ident1) {
        return this.get(ident4, ""+ident3, ""+ident2, ident1);
    }
    public T get(String ident4, int ident3, String ident2, int ident1) {
        return this.get(ident4, ""+ident3, ident2, ""+ident1);
    }
    public T get(String ident4, int ident3, int ident2, int ident1) {
        return this.get(ident4, ""+ident3, ""+ident2, ""+ident1);
    }
    public T get(int ident4, String ident3, String ident2, int ident1) {
        return this.get(""+ident4, ident3, ident2, ""+ident1);
    }
    public T get(int ident4, String ident3, int ident2, int ident1) {
        return this.get(""+ident4, ident3, ""+ident2, ""+ident1);
    }
    public T get(int ident4, String ident3, int ident2, String ident1) {
        return this.get(""+ident4, ident3, ""+ident2, ident1);
    }
    public T get(int ident4, int ident3, String ident2, String ident1) {
        return this.get(""+ident4, ""+ident3, ident2, ident1);
    }
    public T get(int ident4, int ident3, int ident2, String ident1) {
        return this.get(""+ident4, ""+ident3, ""+ident2, ident1);
    }
    public T get(int ident4, int ident3, String ident2, int ident1) {
        return this.get(""+ident4, ""+ident3, ident2, ""+ident1);
    }
    public T get(int ident4, int ident3, int ident2, int ident1) {
        return this.get(""+ident4, ""+ident3, ""+ident2, ""+ident1);
    }


    //------------------------------------------------------------------------------------------------------------------

    public void put(String ident4, int ident3, int ident2, int ident1, T value) {
        this.put(ident4, ""+ident3, ""+ident2, ""+ident1, value);
    }

    public boolean put(String ident4, int ident3, int ident2, int ident1, T value, boolean reportCollision) {
        return this.put(ident4, ""+ident3, ""+ident2, ""+ident1, value, reportCollision);
    }


    public void put(String ident4, int ident3, int ident2, String ident1, T value) {
        this.put(ident4, ""+ident3, ""+ident2, ident1, value);
    }
    public boolean put(String ident4, int ident3, int ident2, String ident1, T value, boolean reportCollision) {
        return this.put(ident4, ""+ident3, ""+ident2, ident1, value, reportCollision);
    }


    public void put(String ident4, int ident3, String ident2, String ident1, T value) {
        this.put(ident4, ""+ident3, ident2, ident1, value);
    }
    public boolean put(String ident4, int ident3, String ident2, String ident1, T value, boolean reportCollision) {
        return this.put(ident4, ""+ident3, ident2, ident1, value, reportCollision);
    }


    public void put(String ident4, String ident3, String ident2, int ident1, T value) {
        this.put(ident4, ident3, ident2, ""+ident1, value);
    }
    public boolean put(String ident4, String ident3, String ident2, int ident1, T value, boolean reportCollision) {
        return this.put(ident4, ident3, ident2, ""+ident1, value, reportCollision);
    }


    public void put(String ident4, String ident3, int ident2, int ident1, T value) {
        this.put(ident4, ident3, ""+ident2, ""+ident1, value);
    }
    public boolean put(String ident4, String ident3, int ident2, int ident1, T value, boolean reportCollision) {
        return this.put(ident4, ident3, ""+ident2, ""+ident1, value, reportCollision);
    }


    public void put(String ident4, String ident3, int ident2, String ident1, T value) {
        this.put(ident4, ident3, ""+ident2, ident1, value);
    }
    public boolean put(String ident4, String ident3, int ident2, String ident1, T value, boolean reportCollision) {
        return this.put(ident4, ident3, ""+ident2, ident1, value, reportCollision);
    }


    public void put(String ident4, int ident3, String ident2, int ident1, T value) {
        this.put(ident4, ""+ident3, ident2, ""+ident1, value);
    }
    public boolean put(String ident4, int ident3, String ident2, int ident1, T value, boolean reportCollision) {
        return this.put(ident4, ""+ident3, ident2, ""+ident1, value, reportCollision);
    }











    public void put(int ident4, int ident3, int ident2, int ident1, T value) {
        this.put(""+ident4, ""+ident3, ""+ident2, ""+ident1, value);
    }

    public boolean put(int ident4, int ident3, int ident2, int ident1, T value, boolean reportCollision) {
        return this.put(""+ident4, ""+ident3, ""+ident2, ""+ident1, value, reportCollision);
    }


    public void put(int ident4, int ident3, int ident2, String ident1, T value) {
        this.put(""+ident4, ""+ident3, ""+ident2, ident1, value);
    }
    public boolean put(int ident4, int ident3, int ident2, String ident1, T value, boolean reportCollision) {
        return this.put(""+ident4, ""+ident3, ""+ident2, ident1, value, reportCollision);
    }


    public void put(int ident4, int ident3, String ident2, String ident1, T value) {
        this.put(""+ident4, ""+ident3, ident2, ident1, value);
    }
    public boolean put(int ident4, int ident3, String ident2, String ident1, T value, boolean reportCollision) {
        return this.put(""+ident4, ""+ident3, ident2, ident1, value, reportCollision);
    }


    public void put(int ident4, String ident3, String ident2, int ident1, T value) {
        this.put(""+ident4, ident3, ident2, ""+ident1, value);
    }
    public boolean put(int ident4, String ident3, String ident2, int ident1, T value, boolean reportCollision) {
        return this.put(""+ident4, ident3, ident2, ""+ident1, value, reportCollision);
    }


    public void put(int ident4, String ident3, int ident2, int ident1, T value) {
        this.put(""+ident4, ident3, ""+ident2, ""+ident1, value);
    }
    public boolean put(int ident4, String ident3, int ident2, int ident1, T value, boolean reportCollision) {
        return this.put(""+ident4, ident3, ""+ident2, ""+ident1, value, reportCollision);
    }


    public void put(int ident4, String ident3, int ident2, String ident1, T value) {
        this.put(""+ident4, ident3, ""+ident2, ident1, value);
    }
    public boolean put(int ident4, String ident3, int ident2, String ident1, T value, boolean reportCollision) {
        return this.put(""+ident4, ident3, ""+ident2, ident1, value, reportCollision);
    }


    public void put(int ident4, int ident3, String ident2, int ident1, T value) {
        this.put(""+ident4, ""+ident3, ident2, ""+ident1, value);
    }
    public boolean put(int ident4, int ident3, String ident2, int ident1, T value, boolean reportCollision) {
        return this.put(""+ident4, ""+ident3, ident2, ""+ident1, value, reportCollision);
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean containsKey(int ident3) {
        return this.containsKey(""+ident3);
    }

}