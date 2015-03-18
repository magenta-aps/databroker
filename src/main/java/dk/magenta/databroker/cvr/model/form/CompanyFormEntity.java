package dk.magenta.databroker.cvr.model.form;

import dk.magenta.databroker.core.model.OutputFormattable;
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
@Table(name = "cvr_form")
public class CompanyFormEntity implements OutputFormattable {

    public CompanyFormEntity() {
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

    @Column
    private String responsibleDatesource;

    public String getResponsibleDatesource() {
        return responsibleDatesource;
    }

    public void setResponsibleDatesource(String responsibleDatesource) {
        this.responsibleDatesource = responsibleDatesource;
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
        if (otherObject == null || otherObject.getClass() != CompanyFormEntity.class) {
            return false;
        }
        return this.equals((CompanyFormEntity) otherObject);
    }

    public boolean equals(CompanyFormEntity otherCompanyFormEntity) {
        return this.code == otherCompanyFormEntity.getCode() &&
                Util.compare(this.name, otherCompanyFormEntity.getName()) &&
                Util.compare(this.responsibleDatesource, otherCompanyFormEntity.getResponsibleDatesource());
    }
}
