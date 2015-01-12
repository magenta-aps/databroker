package dk.magenta.databroker.core.model.oio;

import dk.magenta.databroker.core.model.OutputFormattable;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import java.util.UUID;

/**
 * Created by jubk on 11/25/14.
 */
@MappedSuperclass
public abstract class UniqueBase implements OutputFormattable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = true, updatable = true)
    private Long id;

    @Column(nullable = false, insertable = true, updatable = true, unique = true)
    private String uuid;

    protected UniqueBase() {
    }

    public UniqueBase(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void generateNewUUID() {
        this.uuid = UUID.randomUUID().toString();
    }









    public abstract String getTypeName();

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("__type__", this.getTypeName());
        return obj;
    };

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
                    SOAPElement node = parent.addChildElement(envelope.createName(obj.has("__type__") ? (String)obj.get("__type__") : name));
                    for (Object oKey : obj.keySet()) {
                        String key = (String) oKey;
                        if (!key.equals("__type__")) {
                            toXML(key, obj.get(key), node, envelope);
                        }
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
