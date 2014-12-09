package dk.magenta.databroker.service.rest;

import dk.magenta.databroker.component.DataBean;
import dk.magenta.databroker.core.model.OutputFormattable;
import dk.magenta.databroker.core.testmodel.TestAddressRepository;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
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
import java.util.regex.Pattern;


@Component
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

    private Pattern onlyDigits = Pattern.compile("^\\d+$");

    @Autowired
    private DataBean db;

    @Autowired
    private TestAddressRepository testAddressRepository;

    @Autowired
    private KommuneRepository kommuneRepository;

    @Autowired
    private NavngivenVejRepository navngivenVejRepository;

    @Autowired
    private HusnummerRepository husnummerRepository;


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

    @GET
    @Path("kommune/{search}")
    @Transactional
    public String kommune(@PathParam("search") String search, @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        try {
            return this.format("kommuner", new ArrayList<OutputFormattable>(this.getKommuner(search)), fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }

    private List<KommuneEntity> getKommuner(String search) throws InputError {
        if (search == null) {
            throw new InputError("Invalid kommune id \""+search+"\"");
        }
        ArrayList<KommuneEntity> matches = new ArrayList<KommuneEntity>();
        if (this.onlyDigits.matcher(search).matches()) {
            KommuneEntity match = this.kommuneRepository.getByKommunekode(Integer.parseInt(search, 10));
            if (match != null) {
                matches.add(match);
            }
        } else {
            matches.addAll(this.kommuneRepository.findByName("%"+search+"%"));
        }
        return matches;
    }

    //------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("vej/{kommune}")
    @Transactional
    public String vej(@PathParam("kommune") String kommune, @QueryParam("format") String formatStr) {
        return this.vej(kommune, null, formatStr);
    }

    @GET
    @Path("vej/{kommune}/{search}")
    @Transactional
    public String vej(@PathParam("kommune") String kommune, @PathParam("search") String search, @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        try {
            return this.format("veje", new ArrayList<OutputFormattable>(this.getVeje(kommune, search)), fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }

    private List<NavngivenVejEntity> getVeje(String kommune, String search) throws InputError {
        List<KommuneEntity> matchingKommuner = this.getKommuner(kommune);
        ArrayList<NavngivenVejEntity> matches = new ArrayList<NavngivenVejEntity>();

        if (search == null) {
            for (KommuneEntity matchingKommune : matchingKommuner) {
                matches.addAll(this.navngivenVejRepository.getByKommunekode(matchingKommune.getKommunekode()));
            }
        } else if (this.onlyDigits.matcher(search).matches()) {
            for (KommuneEntity matchingKommune : matchingKommuner) {
                matches.addAll(this.navngivenVejRepository.getByKommunekodeAndVejkode(matchingKommune.getKommunekode(), Integer.parseInt(search,10)));
            }
        } else {
            for (KommuneEntity matchingKommune : matchingKommuner) {
                matches.addAll(this.navngivenVejRepository.getByKommunekodeAndVejnavn(matchingKommune.getKommunekode(), "%" + search + "%"));
            }
        }
        return matches;
    }



    //------------------------------------------------------------------------------------------------------------------






    @GET
    @Path("address/{kommune}")
    @Transactional
    public String husnummer(@PathParam("kommune") String kommune, @QueryParam("format") String formatStr) {
        return this.husnummer(kommune, null, null, null, formatStr);
    }

    @GET
    @Path("address/{kommune}/{vej}")
    @Transactional
    public String husnummer(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @QueryParam("format") String formatStr) {
        return this.husnummer(kommune, vej, null, null, formatStr);
    }

    @GET
    @Path("address/{kommune}/{vej}/{postnr}")
    @Transactional
    public String husnummer(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr, @QueryParam("format") String formatStr) {
        return this.husnummer(kommune, vej, postnr, null, formatStr);
    }

    @GET
    @Path("address/{kommune}/{vej}/{postnr}/{husnr}")
    @Transactional
    public String husnummer(@PathParam("kommune") String kommune, @PathParam("vej") String vej, @PathParam("postnr") String postnr, @PathParam("husnr") String husnr, @QueryParam("format") String formatStr) {
        Format fmt = this.getFormat(formatStr);
        try {
            return this.format("veje", new ArrayList<OutputFormattable>(this.getHusnumre(kommune, vej, postnr, husnr)), fmt);
        } catch (InputError error) {
            return this.format("error", error, fmt);
        }
    }

    private List<HusnummerEntity> getHusnumre(String kommune, String vej, String postnr, String husnr) throws InputError {
        List<NavngivenVejEntity> matchingVeje = this.getVeje(kommune, vej);
        ArrayList<HusnummerEntity> matches = new ArrayList<HusnummerEntity>();

        if (postnr == null && husnr != null) {
            for (NavngivenVejEntity navngivenVejEntity : matchingVeje) {
                matches.addAll(this.husnummerRepository.getByNavngivenvejAndHusnr(navngivenVejEntity, "%" + husnr + "%"));
            }
        } else if (postnr != null && husnr == null) {
            for (NavngivenVejEntity navngivenVejEntity : matchingVeje) {
                matches.addAll(this.husnummerRepository.getByNavngivenvejAndPostnr(navngivenVejEntity, Integer.parseInt(postnr, 10)));
            }
        } else if (postnr != null && husnr != null) {
            for (NavngivenVejEntity navngivenVejEntity : matchingVeje) {
                matches.addAll(this.husnummerRepository.getByNavngivenvejAndPostnrAndHusnr(navngivenVejEntity, Integer.parseInt(postnr, 10), "%" + husnr + "%"));
            }
        }
        return matches;
    }




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



}
