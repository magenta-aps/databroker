package dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers;

/**
 * Created by lars on 17-11-14.
 */
public class DataPoint {
    private String stringPart = null;
    private Integer intPart = null;

    public DataPoint(String rawData) {
        try {
            this.intPart = Integer.parseInt(rawData, 10);
        } catch (NumberFormatException e) {
            this.stringPart = rawData;
        }
    }

    public String toString() {
        return this.stringPart != null ? this.stringPart : ""+this.intPart;
    }

    public int toInt() {
        return this.intPart != null ? this.intPart.intValue() : 0;
    }
}
