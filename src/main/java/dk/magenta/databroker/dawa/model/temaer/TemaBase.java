package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.model.oio.UniqueBase;
import dk.magenta.databroker.register.objectcontainers.Level1Container;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.*;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jubk on 18-12-2014.
 */

@MappedSuperclass
public abstract class TemaBase implements OutputFormattable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @Column
    private String navn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public abstract TemaType getTemaType();

    // Static method for easy access to the different implementation classes
    public static final Class getClassForTemaType(TemaType type) {
        switch (type) {
            case KOMMUNE:
                break;
            case REGION:
                break;
            case SOGN:
            case OPSTILLINGSKREDS:
            case POLITIKREDS:
            case RETSKREDS:
            case AFSTEMNINGSOMRAADE:
            case POSTNUMMER:
            case DANMARK:
            case MENIGHEDSRAADSAFSTEMNINGSOMRAADE:
            case SAMLEPOSTNUMMER:
            case STORKREDS:
            case SUPPLERENDEBYNAVN:
            case VALGLANDSDEL:
            case ZONE:
            default:
                throw new NotImplementedException();
        }
        return null;
    }







    public abstract String getTypeName();

    public abstract JSONObject toJSON();

    public JSONObject toFullJSON() {
        return this.toJSON();
    }

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope) {
        return toXML(this.getTypeName(), this.toJSON(), parent, envelope);
    }
    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope) {
        return toXML(this.getTypeName(), this.toFullJSON(), parent, envelope);
    }


    private static SOAPElement toXML(String name, Object value, SOAPElement parent, SOAPEnvelope envelope) {
        try {
            if (value instanceof String || value instanceof Integer || value instanceof Long || value instanceof Boolean) {
                parent.addAttribute(envelope.createName(name), value.toString());
                return null;
            } else if (value instanceof JSONObject) {
                JSONObject obj = (JSONObject) value;
                if (obj.length() > 0) {
                    SOAPElement node = parent.addChildElement(envelope.createName(name));
                    for (Object oKey : obj.keySet()) {
                        String key = (String) oKey;
                        toXML(key, obj.get(key), node, envelope);
                    }
                    return node;
                }
            } else if (value instanceof JSONArray) {
                JSONArray list = (JSONArray) value;
                if (list.length() > 0) {
                    SOAPElement node = parent.addChildElement(envelope.createName(name));
                    for (int i = 0; i < list.length(); i++) {
                        toXML(name, list.get(i), node, envelope);
                    }
                    return node;
                }
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return null;
    }

}
