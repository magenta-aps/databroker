package dk.magenta.databroker.util;

import dk.magenta.databroker.util.objectcontainers.StringList;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lars on 20-02-15.
 */
public class TimeRecorder {
    private long time;
    private ArrayList<Long> record;

    public TimeRecorder() {
        this.record = new ArrayList<Long>();
        this.time = this.getTime();
    }

    public void record() {
        long t = this.getTime();
        this.record.add(t - this.time);
        this.time = t;
    }

    public String toString() {
        StringList s = new StringList();
        for (long time : this.record) {
            s.append(""+time);
        }
        return "["+s.join(",")+"]";
    }

    public void add(TimeRecorder otherRecord) {
        int thisSize = this.record.size();
        int otherSize = otherRecord.record.size();
        int count = Math.max(thisSize, otherSize);
        for (int i=0; i<count; i++) {
            long sum = (i<thisSize ? this.record.get(i) : 0) + (i<otherSize ? otherRecord.record.get(i) : 0);
            if (i < thisSize) {
                this.record.set(i, sum);
            } else {
                this.record.add(sum);
            }
        }
    }

    private long getTime() {
        return new Date().getTime();
    }
}
