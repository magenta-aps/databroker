package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.core.model.OutputFormattable;
import org.json.JSONObject;
import org.json.XML;

import javax.xml.soap.*;

/**
 * Created by lars on 08-12-14.
 */
public class InputError extends Exception implements OutputFormattable {

    public InputError(String message) {
        super(message);
    }

    public JSONObject toJSON() {
        JSONObject error = new JSONObject();
        error.put("message", this.getMessage());
        return error;
    }

    public Node toXML(SOAPElement parent, SOAPEnvelope envelope) {
        try {
            SOAPElement node = parent.addChildElement("error");
            node.addAttribute(envelope.createName("message"), this.getMessage());
            return node;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

}
