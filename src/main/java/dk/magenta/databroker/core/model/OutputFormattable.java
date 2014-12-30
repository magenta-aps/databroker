package dk.magenta.databroker.core.model;

import org.json.JSONObject;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;

/**
 * Created by lars on 08-12-14.
 */
public interface OutputFormattable {

    public JSONObject toJSON();
    public JSONObject toFullJSON();

    public SOAPElement toXML(SOAPElement parent, SOAPEnvelope envelope);
    public SOAPElement toFullXML(SOAPElement parent, SOAPEnvelope envelope);


}
