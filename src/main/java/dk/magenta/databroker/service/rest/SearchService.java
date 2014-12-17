package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.soap.*;
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
    private KommuneRepository kommuneRepository;

    @Autowired
    private NavngivenVejRepository navngivenVejRepository;

    @Autowired
    private HusnummerRepository husnummerRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostnummerRepository postnummerRepository;


    @GET
    public String getList() {
        return "getList()";
    } // TODO: return index of options

    @GET
    @Path("{path}")
    public String catcher(@PathParam("path") String path) {
        return "{\"msg\":\"catcher("+path+")\"}";
    }

    //------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("kommune")
    @Transactional
    public String kommune(@QueryParam("land") String land, @QueryParam("kommune") String[] kommune,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);
        GlobalCondition condition = new GlobalCondition(includeBefore, includeAfter);
        return this.format("kommuner", new ArrayList<OutputFormattable>(this.getKommuner(land, kommune, condition)), fmt);
    }

    private List<KommuneEntity> getKommuner(String land, String[] kommune, GlobalCondition globalCondition) {
        return new ArrayList<KommuneEntity>(
            this.kommuneRepository.search(
                this.cleanInput(land),
                this.cleanInput(kommune),
                globalCondition
            )
        );
    }

    //------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("vej")
    @Transactional
    public String vej(@QueryParam("land") String land, @QueryParam("kommune") String[] kommune, @QueryParam("vej") String[] vej,
                      @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);
        GlobalCondition condition = new GlobalCondition(includeBefore, includeAfter);
        return this.format("veje", new ArrayList<OutputFormattable>(this.getVeje(land, kommune, vej, condition)), fmt);
    }

    private List<NavngivenVejEntity> getVeje(String land, String[] kommune, String[] vej, GlobalCondition globalCondition) {
        return new ArrayList<NavngivenVejEntity>(
            this.navngivenVejRepository.search(
                this.cleanInput(land),
                this.cleanInput(kommune),
                this.cleanInput(vej),
                globalCondition
            )
        );
    }



    //------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("postnr")
    @Transactional
    public String postnummer(@QueryParam("post") String[] post,
                             @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);
        GlobalCondition condition = new GlobalCondition(includeBefore, includeAfter);
        return this.format("postnumre", new ArrayList<OutputFormattable>(this.getPostnumre(post, condition)), fmt);
    }

    private List<PostnummerEntity> getPostnumre(String[] post, GlobalCondition globalCondition) {
        return new ArrayList<PostnummerEntity>(
            this.postnummerRepository.search(
                this.cleanInput(post),
                globalCondition
            )
        );
    }


    //------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("husnr")
    @Transactional
    public String husnummer(@QueryParam("land") String land, @QueryParam("kommune") String[] kommune, @QueryParam("vej") String[] vej, @QueryParam("postnr") String[] postnr, @QueryParam("husnr") String[] husnr,
                            @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        return this.format("husnumre", new ArrayList<OutputFormattable>(this.getHusnumre(land, kommune, vej, postnr, husnr)), fmt);
    }

    private List<HusnummerEntity> getHusnumre(String land, String[] kommune, String[] vej, String[] postnr, String[] husnr) {
        return new ArrayList<HusnummerEntity>(
            this.husnummerRepository.search(
                this.cleanInput(land),
                this.cleanInput(kommune),
                this.cleanInput(vej),
                this.cleanInput(postnr),
                this.cleanInput(husnr)
            )
        );
    }

//------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("adresse")
    @Transactional
    public String adresse(@QueryParam("land") String land, @QueryParam("kommune") String[] kommune, @QueryParam("vej") String[] vej, @QueryParam("postnr") String[] postnr, @QueryParam("husnr") String[] husnr, @QueryParam("etage") String[] etage, @QueryParam("doer") String[] doer,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);
        GlobalCondition condition = new GlobalCondition(includeBefore, includeAfter);
        return this.format("husnumre", new ArrayList<OutputFormattable>(this.getAdresser(land, kommune, vej, postnr, husnr, etage, doer, condition)), fmt);
    }

    private List<AdresseEntity> getAdresser(String land, String[] kommune, String[] vej, String[] postnr, String[] husnr, String[] etage, String[] doer, GlobalCondition globalCondition) {
        return new ArrayList<AdresseEntity>(
            this.adresseRepository.search(
                this.cleanInput(land),
                this.cleanInput(kommune),
                this.cleanInput(vej),
                this.cleanInput(postnr),
                this.cleanInput(husnr),
                this.cleanInput(etage),
                this.cleanInput(doer),
                globalCondition
            )
        );
    }

    @GET
    @Path("adresse/{uuid}")
    @Transactional
    public String adresse(@PathParam("uuid") String uuid,
                          @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        AdresseEntity adresseEntity = this.getAdresse(uuid);
        if (adresseEntity != null) {
            return this.format("husnumre", adresseEntity, fmt);
        } else {
            throw new NotFoundException();
        }
    }

    private AdresseEntity getAdresse(String uuid) {
        return this.adresseRepository.findByUuid(uuid);
    }

    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------

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

    private String formatJSON(String key, List<OutputFormattable> output) {
        // Setup JSON structure
        JSONArray list = new JSONArray();
        JSONObject object = new JSONObject();
        object.put(key, list);

        // Insert items in JSON structure
        for (OutputFormattable item : output) {
            list.put(item.toFullJSON());
        }

        // Export JSON structure as string
        return object.toString(4);
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

    //------------------------------------------------------------------------------------------------------------------

    private String cleanInput(String s) {
        if ("*".equals(s)) {
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
