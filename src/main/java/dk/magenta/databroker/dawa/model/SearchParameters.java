package dk.magenta.databroker.dawa.model;

import dk.magenta.databroker.register.conditions.GlobalCondition;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lars on 13-01-15.
 */
public class SearchParameters extends HashMap<String, String[]> {

    public enum Key {
        LAND,
        KOMMUNE,
        POST,
        LOKALITET,
        VEJ,
        HUSNR,
        BNR,
        ETAGE,
        DOER,
        VIRKSOMHED,
        CVR,
        EMAIL,
        PHONE,
        FAX,
        PRIMARYINDUSTRY,
        SECONDARYINDUSTRY,
        ANYINDUSTRY,
        UPDATEDATE
    }

    private GlobalCondition globalCondition = null;


    public SearchParameters() {
    }

    public SearchParameters(String[] land, String[] kommune, String[] post, String[] lokalitet, String[] vej, GlobalCondition globalCondition) {
        this(land,kommune,post,lokalitet,vej,null,null,globalCondition);
    }
    public SearchParameters(String[] land, String[] kommune, String[] post, String[] lokalitet, String[] vej, String[] husnr, String[] bnr, GlobalCondition globalCondition) {
        this(land,kommune,post,lokalitet,vej,husnr,bnr,null,null,globalCondition);
    }
    public SearchParameters(String[] land, String[] kommune, String[] post, String[] lokalitet, String[] vej, String[] husnr, String[] bnr, String[] etage, String[] doer) {
        this(land,kommune,post,lokalitet,vej,husnr,bnr,etage,doer,null);
    }
    public SearchParameters(String[] land, String[] kommune, String[] post, String[] lokalitet, String[] vej, String[] husnr, String[] bnr, String[] etage, String[] doer, GlobalCondition globalCondition) {
        this.put(Key.LAND, land);
        this.put(Key.KOMMUNE, kommune);
        this.put(Key.POST, post);
        this.put(Key.LOKALITET, lokalitet);
        this.put(Key.VEJ, vej);
        this.put(Key.HUSNR, husnr);
        this.put(Key.BNR, bnr);
        this.put(Key.ETAGE, etage);
        this.put(Key.DOER, doer);
        this.globalCondition = globalCondition;
    }









    public String[] put(Key key, int value) {
        return this.put(key.name(), ""+value);
    }
    public String[] put(Key key, String value) {
        return this.put(key.name(), this.cleanInput(value));
    }
    public String[] put(Key key, String[] values) {
        return this.put(key.name(), this.cleanInput(values));
    }

    public String[] put(String key, String[] values) {
        values = this.cleanInput(values);
        if (values != null && values.length > 0) {
            return super.put(key.toString(), values);
        }
        return null;
    }
    private String[] put(String key, String value) {
        value = this.cleanInput(value);
        if (value != null) {
            return super.put(key.toString(), new String[]{value});
        }
        return null;
    }

    public String[] get(Key key) {
        return this.get(key.name());
    }

    public boolean has(Key key) {
        return this.keySet().contains(key.name());
    }
    public boolean hasAny(Key... keys) {
        for (Key key : keys) {
            if (this.has(key)) {
                return true;
            }
        }
        return false;
    }
    public boolean hasAll(Key... keys) {
        for (Key key : keys) {
            if (!this.has(key)) {
                return false;
            }
        }
        return true;
    }

    public GlobalCondition getGlobalCondition() {
        return globalCondition;
    }

    public void setGlobalCondition(GlobalCondition globalCondition) {
        this.globalCondition = globalCondition;
    }

    public boolean hasGlobalCondition() {
        return this.globalCondition != null;
    }



    private String cleanInput(String s) {
        return (s == null || s.equals("*") || s.isEmpty()) ? null : s;
    }

    private String[] cleanInput(String[] s) {
        if (s != null) {
            ArrayList<String> list = new ArrayList<String>();
            for (String part : s) {
                part = this.cleanInput(part);
                if (part != null) {
                    list.add(part);
                }
            }
            return list.isEmpty() ? null : list.toArray(new String[list.size()]);
        }
        return null;
    }
}
