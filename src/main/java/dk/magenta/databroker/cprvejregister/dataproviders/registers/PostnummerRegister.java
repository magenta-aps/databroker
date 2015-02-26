package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.RawVej;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.objectcontainers.*;
import dk.magenta.databroker.cprvejregister.dataproviders.records.CprRecord;
import dk.magenta.databroker.register.records.Record;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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

    public PostnummerRegister() {
    }

    /*
    * Data source spec
    * */

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public Resource getRecordResource() {
        return this.ctx.getResource("classpath:/data/cprPostregister.zip");
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
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel model;

    /*
    * Database save
    * */

    protected void saveRunToDatabase(RegisterRun run, RegistreringInfo registreringInfo) {
        this.log.info("Storing PostnummerEntities in database");
        long time = this.indepTic();
        ModelUpdateCounter counter = new ModelUpdateCounter();
        counter.setLog(this.log);
        PostnummerRegisterRun prun = (PostnummerRegisterRun) run;

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
                }

                model.setPostNummer(nr, navn, veje, registreringInfo);
                counter.countEntryProcessed();
            }
        }
        counter.printFinalEntriesProcessed();
        int count = counter.getCount();
        this.log.info(count + " PostnummerEntities stored in " + this.toc(time) + " ms (avg " + ((double)time / (double)count) + " ms)");
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getUploadPartName() {
        return "postnummerSourceUpload";
    }

    @Override
    public String getSourceUrlFieldName() {
        return "postnummerSourceUrl";
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        JSONObject config = new JSONObject();
        config.put(this.getSourceUrlFieldName(),"https://cpr.dk/media/152114/a370712.txt");
        return new DataProviderConfiguration(config);
    }
}
