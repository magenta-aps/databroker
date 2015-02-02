package dk.magenta.databroker.util.objectcontainers;

import java.util.ArrayList;

/**
 * Created by lars on 29-01-15.
 */
public class ListHash<T> extends Level1Container<ArrayList<T>> {

    public boolean put(String key, T value) {
        if (!this.containsKey(key)) {
            super.put(key, new ArrayList<T>());
        }
        super.get(key).add(value);
        return true;
    }

    public T[] getArray(String key) {
        ArrayList<T> list = this.get(key);
        if (list != null) {
            return list.toArray((T[]) new Object[list.size()]);
        }
        return null;
    }

    public T getFirst(String key) {
        ArrayList<T> list = this.get(key);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

}
