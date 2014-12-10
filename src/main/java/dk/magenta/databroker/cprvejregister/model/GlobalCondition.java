package dk.magenta.databroker.cprvejregister.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lars on 10-12-14.
 */
public class GlobalCondition {

    private static final String[] parseFormats = {
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd",
            "yyyy.MM.dd",
            "yyyy:MM:dd"
    };

    private Date includeOnlyBefore;
    private Date includeOnlyAfter;

    public GlobalCondition(){}


    public GlobalCondition(String includeOnlyBefore, String includeOnlyAfter){
        this.setIncludeOnlyBefore(includeOnlyBefore);
        this.setIncludeOnlyAfter(includeOnlyAfter);
        if (this.includeOnlyAfter != null && this.includeOnlyBefore != null) {
            if (this.includeOnlyAfter.compareTo(this.includeOnlyBefore) > 0) {
                throw new IllegalArgumentException("includeOnlyAfter argument must be less than includeOnlyBefore argument");
            }
        }
    }

    public Date getIncludeOnlyBefore() {
        return this.includeOnlyBefore;
    }
    public void setIncludeOnlyBefore(Date includeOnlyBefore) {
        this.includeOnlyBefore = includeOnlyBefore;
    }
    public void setIncludeOnlyBefore(String includeOnlyBefore) {
        this.setIncludeOnlyBefore(this.parseDate(includeOnlyBefore));
    }


    public Date getIncludeOnlyAfter() {
        return this.includeOnlyAfter;
    }
    public void setIncludeOnlyAfter(Date includeOnlyAfter) {
        this.includeOnlyAfter = includeOnlyAfter;
    }
    public void setIncludeOnlyAfter(String includeOnlyAfter) {
        this.setIncludeOnlyAfter(this.parseDate(includeOnlyAfter));
    }


    private Date parseDate(String dateStr) {
        if (dateStr != null) {
            for (String format : parseFormats) {
                try {
                    return (new SimpleDateFormat(format)).parse(dateStr);
                } catch (ParseException e) {
                    // Try next format
                }
            }
        }
        return null;
    }

    public ArrayList<Condition> whereField(String baseEntityName) {
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        if (this.includeOnlyAfter != null) {
            conditions.add(new Condition(baseEntityName+"Version.registrering.registreringFra", ">", this.includeOnlyAfter, baseEntityName+".versioner "+baseEntityName+"Version"));
        }
        if (this.includeOnlyBefore != null) {
            conditions.add(new Condition(baseEntityName+"Version.registrering.registreringFra", "<", this.includeOnlyBefore, this.includeOnlyAfter == null ? baseEntityName+".versioner "+baseEntityName+"Version" : null));
        }
        return conditions;
    }
}
