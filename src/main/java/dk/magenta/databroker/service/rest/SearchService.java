package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;


@Component
@Path("search")
//@Produces({ "application/json", "application/xml" })
//@Produces({ MediaType.APPLICATION_JSON + "; " + MediaType.CHARSET_PARAMETER +"=UTF-8,"+ MediaType.APPLICATION_XML + "; " + MediaType.CHARSET_PARAMETER +"=UTF-8" })
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
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel model;


    @GET
    public String getList() {
        return "getList()";
    } // TODO: return index of options

    @GET
    @Path("{path}")
    public String catcher(@PathParam("path") String path) {
        throw new NotFoundException();
    }

    // Base url to create hrefs in output objects
    // Be sure to modify this when the webservice changes
    private static String getBaseUrl() {
        return /*"http://localhost:8181" + */
                "/services/search";
    }

    //------------------------------------------------------------------------------------------------------------------


    public static String getKommuneBaseUrl() {
        return SearchService.getBaseUrl() + "/kommune";
    }

    @GET
    @Path("kommune")
    @Transactional
    public String kommune(@QueryParam("land") String[] land, @QueryParam("kommune") String[] kommune, @QueryParam("post") String[] post, @QueryParam("lokalitet") String[] lokalitet, @QueryParam("vej") String[] vej,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);

        SearchParameters parameters = new SearchParameters(land, kommune, post, lokalitet, vej, new GlobalCondition(includeBefore, includeAfter));

        List<OutputFormattable> kommuner = new ArrayList<OutputFormattable>(
                this.model.getKommune(parameters)
        );

        return this.format("kommuner", kommuner, fmt);
    }

    @GET
    @Path("kommune/{id}")
    @Transactional
    public String kommune(@PathParam("id") String id,
                      @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);

        KommuneEntity kommuneEntity = null;

        try {
            int kommuneKode = Integer.parseInt(id, 10);
            kommuneEntity = this.model.getKommune(kommuneKode);
        } catch (NumberFormatException e) {
        }


        if (kommuneEntity != null) {
            return this.format(kommuneEntity, fmt);
        } else {
            throw new NotFoundException();
        }
    }

    //------------------------------------------------------------------------------------------------------------------


    public static String getVejBaseUrl() {
        return SearchService.getBaseUrl() + "/vej";
    }

    @GET
    @Path("vej")
    @Transactional
    public String vej(@QueryParam("land") String[] land, @QueryParam("kommune") String[] kommune, @QueryParam("vej") String[] vej, @QueryParam("lokalitet") String[] lokalitet, @QueryParam("post") String[] post,
                      @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);

        SearchParameters parameters = new SearchParameters(land, kommune, post, lokalitet, vej, new GlobalCondition(includeBefore, includeAfter));

        List<OutputFormattable> veje = new ArrayList<OutputFormattable>(
            this.model.getVejstykke(parameters)
        );

        return this.format("veje", veje, fmt);
    }

    @GET
    @Path("vej/{uuid}")
    @Transactional
    public String vej(@PathParam("uuid") String uuid,
                        @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        OutputFormattable vejEntity = this.model.getVejstykke(uuid);
        if (vejEntity != null) {
            return this.format(vejEntity, fmt);
        } else {
            throw new NotFoundException();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public static String getPostnummerBaseUrl() {
        return SearchService.getBaseUrl() + "/postnr";
    }

    @GET
    @Path("postnr")
    @Transactional
    public String postnummer(@QueryParam("land") String[] land, @QueryParam("post") String[] post, @QueryParam("kommune") String[] kommune, @QueryParam("vej") String[] vej,
                             @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);

        SearchParameters parameters = new SearchParameters(land, kommune, post, null, vej, new GlobalCondition(includeBefore, includeAfter));

        ArrayList<OutputFormattable> postnumre = new ArrayList<OutputFormattable>(
            this.model.getPostnummer(parameters)
        );

        return this.format("postnumre", postnumre, fmt);
    }

    @GET
    @Path("postnr/{id}")
    @Transactional
    public String postnummer(@PathParam("id") String id,
                        @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);

        OutputFormattable postnummerEntity = null;
        if (id.length() < 5) {
            try {
                int postnr = Integer.parseInt(id,10);
                postnummerEntity = this.model.getPostnummer(postnr);
            } catch (NumberFormatException e) {
            }
        }

        if (postnummerEntity != null) {
            return this.format(postnummerEntity, fmt);
        } else {
            throw new NotFoundException();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public static String getAdgangsAdresseBaseUrl() {
        return SearchService.getBaseUrl() + "/adgangsadresse";
    }

    @GET
    @Path("adgangsadresse")
    @Transactional
    public String adgangsadresse(@QueryParam("land") String[] land, @QueryParam("kommune") String[] kommune, @QueryParam("vej") String[] vej, @QueryParam("post") String[] post, @QueryParam("husnr") String[] husnr, @QueryParam("bnr") String[] bnr,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);

        SearchParameters parameters = new SearchParameters(land, kommune, post, null, vej, husnr, bnr, new GlobalCondition(includeBefore, includeAfter));

        ArrayList<OutputFormattable> adresser = new ArrayList<OutputFormattable>(
                this.model.getAdgangsAdresse(parameters)
        );

        return this.format("adgangsadresser", adresser, fmt);
    }

    @GET
    @Path("adgangsadresse/{uuid}")
    @Transactional
    public String adgangsadresse(@PathParam("uuid") String uuid,
                          @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        OutputFormattable adresseEntity = this.model.getAdgangsAdresse(uuid);
        if (adresseEntity != null) {
            return this.format(adresseEntity, fmt);
        } else {
            throw new NotFoundException();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public static String getEnhedsAdresseBaseUrl() {
        return SearchService.getBaseUrl() + "/adresse";
    }

    @GET
    @Path("adresse")
    @Transactional
    public String adresse(@QueryParam("land") String[] land, @QueryParam("kommune") String[] kommune, @QueryParam("vej") String[] vej, @QueryParam("post") String[] post, @QueryParam("husnr") String[] husnr, @QueryParam("bnr") String[] bnr, @QueryParam("etage") String[] etage, @QueryParam("doer") String[] doer,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);

        SearchParameters parameters = new SearchParameters(land, kommune, post, null, vej, husnr, bnr, etage, doer, new GlobalCondition(includeBefore, includeAfter));

        ArrayList<OutputFormattable> adresser = new ArrayList<OutputFormattable>(
                this.model.getEnhedsAdresse(parameters)
        );

        return this.format("adresser", adresser, fmt);
    }

    @GET
    @Path("adresse/{uuid}")
    @Transactional
    public String adresse(@PathParam("uuid") String uuid,
                          @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        OutputFormattable adresseEntity = this.model.getEnhedsAdresse(uuid);
        if (adresseEntity != null) {
            return this.format(adresseEntity, fmt);
        } else {
            throw new NotFoundException();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public static String getLokalitetBaseUrl() {
        return SearchService.getBaseUrl() + "/lokalitet";
    }


    @GET
    @Path("lokalitet")
    @Transactional
    public String lokalitet(@QueryParam("land") String[] land, @QueryParam("kommune") String[] kommune, @QueryParam("vej") String[] vej, @QueryParam("post") String[] post, @QueryParam("lokalitet") String[] lokalitet,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);

        SearchParameters parameters = new SearchParameters(land, kommune, post, lokalitet, vej, new GlobalCondition(includeBefore, includeAfter));

        ArrayList<OutputFormattable> adresser = new ArrayList<OutputFormattable>(
                this.model.getLokalitet(parameters)
        );

        return this.format("lokaliteter", adresser, fmt);
    }


    @GET
    @Path("lokalitet/{uuid}")
    @Transactional
    public String lokalitet(@PathParam("uuid") String uuid,
                          @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        OutputFormattable lokalitetEntity = this.model.getLokalitet(uuid);
        if (lokalitetEntity != null) {
            return this.format(lokalitetEntity, fmt);
        } else {
            throw new NotFoundException();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private static int indent = 4;

    private String format(OutputFormattable output, Format format) {
        switch (format) {
            case json:
                return this.formatJSON(output);
            case xml:
                return this.formatXML(output);
        }
        return null;
    }

    private String format(String key, OutputFormattable output, Format format) {
        ArrayList<OutputFormattable> outputList = new ArrayList<OutputFormattable>();
        outputList.add(output);
        return this.format(key, outputList, format);
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

    private String formatJSON(OutputFormattable output) {
        return this.postProcessJSON(output.toFullJSON()).toString(4);
    }

    private JSONObject postProcessJSON(JSONObject obj) {
        if (obj.keySet().contains("__type__")) {
            obj.remove("__type__");
        }
        for (Object key : obj.keySet()) {
            String sKey = (String) key;
            Object value = obj.get(sKey);
            if (value instanceof JSONObject) {
                obj.put(sKey, this.postProcessJSON((JSONObject)value));
            }
            if (value instanceof JSONArray) {
                obj.put(sKey, this.postProcessJSON((JSONArray) value));
            }
        }
        return obj;
    }
    private JSONArray postProcessJSON(JSONArray arr) {
        for (int i=0; i<arr.length(); i++) {
            Object value = arr.get(i);
            if (value instanceof JSONObject) {
                arr.put(i, this.postProcessJSON((JSONObject) value));
            }
            if (value instanceof JSONArray) {
                arr.put(i, this.postProcessJSON((JSONArray) value));
            }
        }
        return arr;
    }

    private String formatJSON(String key, List<OutputFormattable> output) {
        // Setup JSON structure
        JSONArray list = new JSONArray();
        JSONObject object = new JSONObject();
        object.put(key, list);

        // Insert items in JSON structure
        for (OutputFormattable item : output) {
            list.put(this.postProcessJSON(item.toFullJSON()));
        }

        // Export JSON structure as string
        return object.toString(indent);
    }

    private String formatXML(OutputFormattable output) {
        try {
            // Setup XML structure
            final MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
            //soapEnvelope.setPrefix("magenta");
            SOAPBody soapBody = soapEnvelope.getBody();

            // Insert items in XML structure
            soapBody.appendChild(output.toFullXML(soapBody, soapEnvelope));

            // Export XML structure as string
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

    private String formatXML(String key, List<OutputFormattable> output) {
        try {
            // Setup XML structure
            final MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
            //soapEnvelope.setPrefix("magenta");
            SOAPBody soapBody = soapEnvelope.getBody();

            // Insert items in XML structure
            Node list = soapBody.addChildElement(key);
            for (OutputFormattable item : output) {
                list.appendChild(item.toFullXML(soapBody, soapEnvelope));
            }

            // Export XML structure as string
            final StringWriter sw = new StringWriter();
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "" + indent);
                transformer.transform(new DOMSource(soapMessage.getSOAPPart()), new StreamResult(sw));
                return sw.toString();
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------

    private String cleanInput(String s) {
        if (s == null || s.equals("*") || s.isEmpty()) {
            return null;
        }
        return s;
    }
    private String[] cleanInput(String[] s) {
        ArrayList<String> list = new ArrayList<String>();
        for (String part : s) {
            part = this.cleanInput(part);
            if (part != null) {
                list.add(part);
            }
        }
        return list.isEmpty() ? null : list.toArray(new String[list.size()]);
    }

}
