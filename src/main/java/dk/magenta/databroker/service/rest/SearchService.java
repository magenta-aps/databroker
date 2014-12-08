package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.component.DataBean;
import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.testmodel.TestAddressRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.soap.*;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Pattern;


@Path("search")
//@Produces({ "application/json", "application/xml" })
@Produces({ MediaType.APPLICATION_JSON + "; " + MediaType.CHARSET_PARAMETER +"=UTF-8" })
public class SearchService {

    private enum Format {
        json,
        xml
    }

    private Format getFormat(String formatStr) {
        if (formatStr != null) {
            try {
                return Format.valueOf(formatStr.toLowerCase());
            } catch (IllegalArgumentException e) {
            }
        }
        return Format.json;

    }

    @Autowired
    private DataBean db;

    @Autowired
    private TestAddressRepository testAddressRepository;

    @Autowired
    private KommuneRepository kommuneRepository;


    @GET
    public String getList() {
        return "getList()";
    }

    @GET
    @Path("by-street")
    /*
     Path: /services/address/search/by-street/
     Given a street name
      */
    public String byStreet() {

        return db.getSomeData();
    }

    @GET
    @Path("{path}")
    public String catcher(@PathParam("path") String path) {
        return "{\"msg\":\"catcher("+path+")\"}";
    }

    //------------------------------------------------------------------------------------------------------------------

    private Pattern onlyDigits = Pattern.compile("^\\d+$");

    @GET
    @Path("kommune/{id}")
    public String kommune(@PathParam("id") String id, @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        try {
            if (id == null) {
                throw new InputError("Invalid kommune id");
            }
            List<OutputFormattable> matches = new ArrayList<OutputFormattable>();
            if (this.onlyDigits.matcher(id).matches()) {
                KommuneEntity match = this.kommuneRepository.getByKommunekode(Integer.parseInt(id, 10));
                if (match != null) {
                    matches.add(match);
                }
            } else {
                matches.addAll(this.kommuneRepository.findByName("%"+id+"%"));
            }

            return this.format("kommuner", matches, fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }








    //------------------------------------------------------------------------------------------------------------------

/*

    @GET
    @Path("address/{kommune}")
    public String address(@PathParam("kommune") String kommune) {
        return this.address(kommune, null, null);
    }

    @GET
    @Path("address/{kommune}/{vej}")
    public String address(@PathParam("kommune") String kommune, @PathParam("vej") String vej) {
        return this.address(kommune, vej, null);
    }

    @GET
    @Path("address/{kommune}/{vej}/{postnr}")
    public String address(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("kommune",kommune);
        jsonObject.put("vej",vej);
        jsonObject.put("postnr",postnr);

        return jsonObject.toString();
    }

*/


    //------------------------------------------------------------------------------------------------------------------

    private String format(String key, OutputFormattable output, Format format) {
        ArrayList<OutputFormattable> outputList = new ArrayList<OutputFormattable>();
        outputList.add(output);
        return this.format(key, output, format);
    }

    private String format(String key, List<OutputFormattable> output, Format format) {
        switch (format) {
            case json:
                return this.formatJSON(key, output);
            case xml:
                return this.formatXML(key, output);
        }
        return null;
    }

    private String formatJSON(String key, List<OutputFormattable> output) {
        JSONArray list = new JSONArray();
        for (OutputFormattable item : output) {
            list.put(item.toJSON());
        }
        JSONObject object = new JSONObject();
        object.put(key, list);
        return object.toString();
    }

    private String formatXML(String key, List<OutputFormattable> output) {
        try {
            final MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();


            Node list = soapBody.addChildElement(key);
            for (OutputFormattable item : output) {
                list.appendChild(item.toXML(soapBody, soapEnvelope));
            }



            final StringWriter sw = new StringWriter();
            try {
                TransformerFactory.newInstance().newTransformer().transform(
                        new DOMSource(soapMessage.getSOAPPart()),
                        new StreamResult(sw));
                return sw.toString();
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return null;
    }



}
