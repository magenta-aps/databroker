package dk.magenta.databroker.cvr.model.embeddable;

import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.util.Util;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class ValidFromField {
    @Column
    private Date validFrom;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    @Column
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public ValidFromField() {
    }

    public ValidFromField(String text, Date validFrom) {
        this.text = text;
        this.validFrom = validFrom;
    }


    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("text", this.text);
        obj.put("validFrom", this.validFrom);
        return obj;
    }

    public String toString() {
        return this.toJSON().toString();
    }


    public boolean equals(Object otherObject) {
        if (otherObject == null || otherObject.getClass() != ValidFromField.class) {
            return false;
        }
        return this.equals((ValidFromField) otherObject);
    }

    public boolean equals(ValidFromField otherValidFromField) {
        return Util.compare(this.text, otherValidFromField.getText()) &&
                Util.compare(this.validFrom, otherValidFromField.getValidFrom());
    }

    public static boolean compare(ValidFromField a, ValidFromField b) {
        // Allow an empty ValidFromField to equal null
        if (a != null && a.getText() == null && a.getValidFrom() == null && b == null) {
            return true;
        }
        if (b != null && b.getText() == null && b.getValidFrom() == null && a == null) {
            return true;
        }
        return Util.compare(a, b);
    }

    public static Condition fromCondition(String[] value, String pathPrefix) {
        return RepositoryUtil.whereField(value, null, pathPrefix+".text");
    }
}
