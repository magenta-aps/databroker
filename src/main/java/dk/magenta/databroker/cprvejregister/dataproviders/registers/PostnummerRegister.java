package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.RawVej;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.*;
import dk.magenta.databroker.cprvejregister.dataproviders.records.CprRecord;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.cprvejregister.model.PostnummerModel;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktVersionEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * Created by lars on 04-11-14.
 */
@Component
public class PostnummerRegister extends CprSubRegister {


    /*
    * Inner classes for parsed data
    * */

     public class PostNummer extends CprRecord {
        public static final String RECORDTYPE_POSTNUMMER = "001";
        public String getRecordType() {
            return RECORDTYPE_POSTNUMMER;
        }
        public PostNummer(String line) throws ParseException {
            super(line);
            this.obtain("kommuneKode", 4, 4);
            this.obtain("vejKode", 8, 4);
            this.obtain("myndighedsNavn", 12, 20);
            this.obtain("vejadresseringsNavn", 32, 20);
            this.obtain("husNrFra", 52, 4);
            this.obtain("husNrTil", 56, 4);
            this.obtain("ligeUlige", 60, 1);
            this.obtain("postNr", 61, 4);
            this.obtain("postDistriktTekst", 65, 20);
        }
    }

    /*
    * RegisterRun inner class
    * */

    public class PostnummerRegisterRun extends RegisterRun {

        private Level1Container<ArrayList<PostNummer>> postnumre;

        public PostnummerRegisterRun() {
            this.postnumre = new Level1Container<ArrayList<PostNummer>>();
        }

        public boolean add(Record record) {
            if (record.getClass() == PostNummer.class) {
                PostNummer postNummer = (PostNummer) record;
                int postnr = record.getInt("postNr");
                ArrayList<PostNummer> list = this.postnumre.get(postnr);
                if (!this.postnumre.containsKey(""+postnr)) {
                    list = new ArrayList<PostNummer>();
                    this.postnumre.put(postnr, list);
                }
                list.add(postNummer);
                return super.add(record);
            }
            return false;
        }

        public Level1Container<ArrayList<PostNummer>> getPostnumre() {
            return this.postnumre;
        }
    }

    protected RegisterRun createRun() {
        return new PostnummerRegisterRun();
    }

    //------------------------------------------------------------------------------------------------------------------

    /*
    * Constructors
    * */

    public PostnummerRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public PostnummerRegister() {
    }


    /*
    * Data source spec
    * */

    @Override
    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152114/a370712.txt");
    }

    @Override
    protected String getEncoding() {
        return "ISO-8859-1";
    }


    /*
    * Parse definition
    * */

    protected CprRecord parseTrimmedLine(String recordType, String line) {
        CprRecord r = super.parseTrimmedLine(recordType, line);
        if (r != null) {
            return r;
        }
        try {
            if (recordType.equals(PostNummer.RECORDTYPE_POSTNUMMER)) {
                return new PostNummer(line);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
    * Repositories
    * */

    @Autowired
    private DawaModel model;

    /*
    * Database save
    * */

    protected void saveRunToDatabase(RegisterRun run) {
        this.createRegistreringEntities();

        PostnummerRegisterRun prun = (PostnummerRegisterRun) run;

        System.out.println("Storing PostnummerEntities in database");

        InputProcessingCounter postCounter = new InputProcessingCounter();
        for (String nummer : prun.getPostnumre().keySet()) {
            ArrayList<PostNummer> postnummerRecords = prun.getPostnumre().get(nummer);
            int nr = Integer.parseInt(nummer, 10);
            if (postnummerRecords.size() > 0 && nr != 9999) {
                String navn = postnummerRecords.get(0).get("postDistriktTekst");
                HashSet<RawVej> veje = new HashSet<RawVej>();

                for (PostNummer postNummer : postnummerRecords) {
                    int kommuneKode = postNummer.getInt("kommuneKode");
                    int vejKode = postNummer.getInt("vejKode");
                    int husnrFra = postNummer.getInt("husNrFra", true);
                    int husnrTil = postNummer.getInt("husNrTil", true);
                    RawVej rawVej = new RawVej(kommuneKode, vejKode);
                    rawVej.setRange(husnrFra, husnrTil);
                    veje.add(rawVej);

                    /*for (String nummer2 : prun.getPostnumre().keySet()) {
                        if (!nummer.equals(nummer2)) {
                            ArrayList<PostNummer> postnummerRecords2 = prun.getPostnumre().get(nummer2);
                            for (PostNummer postNummer2 : postnummerRecords2) {
                                int kommuneKode2 = postNummer2.getInt("kommuneKode");
                                int vejKode2 = postNummer2.getInt("vejKode");
                                if (kommuneKode == kommuneKode2 && vejKode == vejKode2) {
                                    System.out.println("Collision! Both postnummer "+nummer+"/"+postNummer.getInt("husNrFra")+"-"+postNummer.getInt("husNrTil")+" and "+nummer2+"/"+postNummer2.getInt("husNrFra")+"-"+postNummer2.getInt("husNrTil")+" claim road "+kommuneKode+":"+vejKode+" ("+this.model.getVejstykke(kommuneKode, vejKode).getLatestVersion().getVejnavn()+")");
                                }
                            }

                        }
                    }*/

                }

                model.setPostNummer(nr, navn, veje, this.getCreateRegistrering(), this.getUpdateRegistrering());
                postCounter.printInputProcessed();
            }
        }
        postCounter.printFinalInputsProcessed();
    }
}
