package dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers;

import java.util.ArrayList;

/**
 * Created by lars on 14-11-14.
 */
public class SegmentedList<T> extends ArrayList<ArrayList<T>> {
    private int segmentSize;
    private ArrayList current;
    public SegmentedList(int segmentSize) {
        this.segmentSize = segmentSize;
        this.current = new ArrayList<T>();
        this.add(this.current);
    }
    public boolean addItem(T item) {
        boolean increased = false;
        if (this.current.size() >= this.segmentSize) {
            this.current = new ArrayList<T>();
            this.add(this.current);
            increased = true;
        }
        this.current.add(item);
        return increased;
    }

    public int totalSize() {
        int count = 0;
        for (ArrayList<T> sublist : this) {
            count += sublist.size();
        }
        return count;
    }
}
