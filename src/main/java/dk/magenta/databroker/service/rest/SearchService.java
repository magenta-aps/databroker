package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneRepository;
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
    private CprKommuneRepository cprKommuneRepository;

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
    @Path("kommune/{kommune}")
    @Transactional
    public String kommune(@PathParam("vej") String kommune,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);
        try {
            GlobalCondition condition = new GlobalCondition(includeBefore, includeAfter);
            return this.format("kommuner", new ArrayList<OutputFormattable>(this.getKommuner(kommune, condition)), fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }

    private List<CprKommuneEntity> getKommuner(String kommune, GlobalCondition globalCondition) throws InputError {
        return new ArrayList<CprKommuneEntity>(
                this.cprKommuneRepository.search(
                        this.cleanInput(kommune),
                        globalCondition
                )
        );
    }

    //------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("vej/{kommune}")
    @Transactional
    public String vej(@PathParam("kommune") String kommune,
                      @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        return this.vej(kommune, null, formatStr, includeBefore, includeAfter);
    }

    @GET
    @Path("vej/{kommune}/{vej}")
    @Transactional
    public String vej(@PathParam("kommune") String kommune, @PathParam("vej") String vej,
                      @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);
        try {
            GlobalCondition condition = new GlobalCondition(includeBefore, includeAfter);
            return this.format("veje", new ArrayList<OutputFormattable>(this.getVeje(kommune, vej, condition)), fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }

    private List<NavngivenVejEntity> getVeje(String kommune, String vej, GlobalCondition globalCondition) throws InputError {
        return new ArrayList<NavngivenVejEntity>(
                this.navngivenVejRepository.search(
                        this.cleanInput(kommune),
                        this.cleanInput(vej),
                        globalCondition
                )
        );
    }



    //------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("postnr/{post}")
    @Transactional
    public String postnummer(@PathParam("post") String post,
                             @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);
        try {
            GlobalCondition condition = new GlobalCondition(includeBefore, includeAfter);
            return this.format("postnummer", new ArrayList<OutputFormattable>(this.getPostnumre(post, condition)), fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }

    private List<PostnummerEntity> getPostnumre(String post, GlobalCondition globalCondition) throws InputError {
        return new ArrayList<PostnummerEntity>(
                this.postnummerRepository.search(
                        this.cleanInput(post),
                        globalCondition
                )
        );
    }


    //------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("husnr/{kommune}")
    @Transactional
    public String husnummer(@PathParam("kommune") String kommune,
                            @QueryParam("format") String formatStr) {
        return this.husnummer(kommune, null, formatStr);
    }

    @GET
    @Path("husnr/{kommune}/{vej}")
    @Transactional
    public String husnummer(@PathParam("kommune") String kommune, @PathParam("vej") String vej,
                            @QueryParam("format") String formatStr) {
        return this.husnummer(kommune, vej, null, formatStr);
    }

    @GET
    @Path("husnr/{kommune}/{vej}/{postnr}")
    @Transactional
    public String husnummer(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr,
                            @QueryParam("format") String formatStr) {
        return this.husnummer(kommune, vej, postnr, null, formatStr);
    }

    @GET
    @Path("husnr/{kommune}/{vej}/{postnr}/{husnr}")
    @Transactional
    public String husnummer(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr, @PathParam("husnr") String husnr,
                            @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        try {
            return this.format("husnumre", new ArrayList<OutputFormattable>(this.getHusnumre(kommune, vej, postnr, husnr)), fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }

    private List<HusnummerEntity> getHusnumre(String kommune, String vej, String postnr, String husnr) throws InputError {
        return new ArrayList<HusnummerEntity>(
                this.husnummerRepository.search(
                        this.cleanInput(kommune),
                        this.cleanInput(vej),
                        this.cleanInput(postnr),
                        this.cleanInput(husnr)
                )
        );
    }

//------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("adresse/{kommune}")
    @Transactional
    public String adresse(@PathParam("kommune") String kommune,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        return this.adresse(kommune, null, formatStr, includeBefore, includeAfter);
    }

    @GET
    @Path("adresse/{kommune}/{vej}")
    @Transactional
    public String adresse(@PathParam("kommune") String kommune, @PathParam("vej") String vej,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        return this.adresse(kommune, vej, null, formatStr, includeBefore, includeAfter);
    }

    @GET
    @Path("adresse/{kommune}/{vej}/{postnr}")
    @Transactional
    public String adresse(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        return this.adresse(kommune, vej, postnr, null, formatStr, includeBefore, includeAfter);
    }

    @GET
    @Path("adresse/{kommune}/{vej}/{postnr}/{husnr}")
    @Transactional
    public String adresse(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr, @PathParam("husnr") String husnr,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        return this.adresse(kommune, vej, postnr, husnr, null, formatStr, includeBefore, includeAfter);
    }

    @GET
    @Path("adresse/{kommune}/{vej}/{postnr}/{husnr}/{etage}")
    @Transactional
    public String adresse(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr, @PathParam("husnr") String husnr, @PathParam("etage") String etage,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        return this.adresse(kommune, vej, postnr, husnr, etage, null, formatStr, includeBefore, includeAfter);
    }


    @GET
    @Path("adresse/{kommune}/{vej}/{postnr}/{husnr}/{etage}/{doer}")
    @Transactional
    public String adresse(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr, @PathParam("husnr") String husnr, @PathParam("etage") String etage, @PathParam("doer") String doer,
                          @QueryParam("format") String formatStr, @QueryParam("includeBefore") String includeBefore, @QueryParam("includeAfter") String includeAfter) {
        Format fmt = this.getFormat(formatStr);
        try {
            GlobalCondition condition = new GlobalCondition(includeBefore, includeAfter);
            return this.format("husnumre", new ArrayList<OutputFormattable>(this.getAdresser(kommune, vej, postnr, husnr, etage, doer, condition)), fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }

    private List<AdresseEntity> getAdresser(String kommune, String vej, String postnr, String husnr, String etage, String doer, GlobalCondition globalCondition) throws InputError {
        return new ArrayList<AdresseEntity>(
                this.adresseRepository.search(
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
            list.put(item.toJSON());
        }

        // Export JSON structure as string
        return object.toString();
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
                list.appendChild(item.toXML(soapBody, soapEnvelope));
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

}
