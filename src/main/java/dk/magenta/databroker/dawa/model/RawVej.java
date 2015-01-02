package dk.magenta.databroker.dawa.model;

import dk.magenta.databroker.register.objectcontainers.Pair;

/**
 * Created by lars on 02-01-15.
 */

public class RawVej extends Pair<Integer,Integer> {
    public RawVej(int kommuneKode, int vejKode) {
        super(kommuneKode, vejKode);
    }
    public int getKommuneKode() {
        return this.getLeft();
    }
    public int getVejKode() {
        return this.getRight();
    }
    public String toString() {
        return "RawVej("+this.getKommuneKode()+","+this.getVejKode()+")";
    }
}
