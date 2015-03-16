package dk.magenta.databroker.util;

import dk.magenta.databroker.util.objectcontainers.StringList;

import java.util.ArrayList;

/**
 * Created by lars on 20-02-15.
 */
public class TimeRecorder {
    private double time;
    private ArrayList<Double> record;
    private int added = 1;

    public TimeRecorder() {
        this.record = new ArrayList<Double>();
        this.time = this.getTime();
    }

    public void record() {
        double t = this.getTime();
        this.record.add(t - this.time);
        this.time = t;
    }

    public String toString() {
        StringList s = new StringList();
        for (double time : this.record) {
            s.append(String.format("%.2f", time));
        }
        return "["+s.join("; ")+"]";
    }

    public void add(TimeRecorder otherRecord) {
        int thisSize = this.record.size();
        int otherSize = otherRecord.record.size();
        int count = Math.max(thisSize, otherSize);
        for (int i=0; i<count; i++) {
            double sum = (i<thisSize ? this.record.get(i) : 0) + (i<otherSize ? otherRecord.record.get(i) : 0);
            if (i < thisSize) {
                this.record.set(i, sum);
            } else {
                this.record.add(sum);
            }
        }
        this.added += otherRecord.added;
    }

    private double getTime() {
        return Util.getTime();
    }

    public int getAdded() {
        return added;
    }

    public double sum() {
        double sum = 0;
        for (Double item : this.record) {
            sum += item;
        }
        return sum;
    }

    public void reset() {
        this.record.clear();
        this.added = 0;
    }
}
