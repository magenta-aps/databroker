package dk.magenta.databroker.cvr.model.industry;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.util.Util;
import org.hibernate.annotations.Index;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_industry")
public class IndustryEntity implements OutputFormattable {

    public IndustryEntity() {
    }

    //----------------------------------------------------

    @Column(nullable = true, unique = true)
    @Index(name = "nameIndex")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------------------------

    @Id
    @Column(nullable = false, unique = true)
    @Index(name = "codeIndex")
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    //----------------------------------------------------

    public String getTypeName() {
        return "industry";
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("name", this.getName());
        obj.put("code", this.getCode());
        //obj.put("href", SearchService.getLokalitetBaseUrl() + "/" + this.getCode());
        return obj;
    }

    @Override
    public JSONObject toFullJSON() {
        return this.toJSON();
    }

    @Override
    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        return null;
    }

    @Override
    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        return null;
    }


    public boolean equals(Object otherObject) {
        if (otherObject == null || otherObject.getClass() != IndustryEntity.class) {
            return false;
        }
        return this.equals((IndustryEntity) otherObject);
    }

    public boolean equals(IndustryEntity otherIndustryEntity) {
        return this.code == otherIndustryEntity.getCode() &&
                Util.compare(this.name, otherIndustryEntity.getName());
    }


    public static Condition industryCondition(String[] value, String pathPrefix) {
        return RepositoryUtil.whereField(value, pathPrefix + ".code", pathPrefix + ".name");
    }
}
