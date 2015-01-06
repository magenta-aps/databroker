package dk.magenta.databroker.dawa.model;

import java.util.Objects;

/**
 * Created by lars on 02-01-15.
 */

public class RawVej {
    public RawVej(int kommuneKode, int vejKode) {
        this.kommuneKode = kommuneKode;
        this.vejKode = vejKode;
        this.husnrFra = 0;
        this.husnrTil = 0;
    }

    private int kommuneKode;
    private int vejKode;
    private int husnrFra;
    private int husnrTil;

    public int getKommuneKode() {
        return this.kommuneKode;
    }
    public int getVejKode() {
        return this.vejKode;
    }

    public void setRange(int husnrFra, int husnrTil) {
        this.husnrFra = husnrFra;
        this.husnrTil = husnrTil;
    }
    public int getHusnrFra() {
        return this.husnrFra;
    }
    public int getHusnrTil() {
        return this.husnrTil;
    }


    public String toString() {
        return "RawVej("+this.getKommuneKode()+","+this.getVejKode()+"" + (this.husnrFra!=0 || this.husnrTil!=0 ? (" "+this.husnrFra+"-"+this.husnrTil) : "") + ")";
    }

    public boolean equals(RawVej otherObject) {
        return this.equals((Object) otherObject);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) return false;
        if (otherObject.getClass() != RawVej.class) return false;
        RawVej otherVej = (RawVej) otherObject;
        if (otherVej.getKommuneKode() != this.kommuneKode) return false;
        if (otherVej.getVejKode() != this.vejKode) return false;
        /*if (otherVej.getHusnrFra() != this.husnrFra) return false;
        if (otherVej.getHusnrTil() != this.husnrTil) return false;*/
        return true;
    }
}
