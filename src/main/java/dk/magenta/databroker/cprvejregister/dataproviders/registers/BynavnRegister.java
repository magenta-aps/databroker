package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.CprRecord;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.RawVej;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.objectcontainers.Level2Container;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.util.objectcontainers.ModelUpdateCounter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashSet;

/**
 * Created by lars on 04-11-14.
 */
@Component
public class BynavnRegister extends CprSubRegister {

    public class ByNavn extends CprRecord {
        public static final String RECORDTYPE_BYNAVN = "001";
        public String getRecordType() {
            return RECORDTYPE_BYNAVN;
        }
        public ByNavn(String line) throws ParseException {
            super(line);
            this.obtain("kommuneKode", 4, 4);
            this.obtain("vejKode", 8, 4);
            this.obtain("myndighedsNavn", 12, 20);
            this.obtain("vejadresseringsNavn", 32, 20);
            this.obtain("husNrFra", 52, 4);
            this.obtain("husNrTil", 56, 4);
            this.obtain("ligeUlige", 60, 1);
            this.obtain("byNavn", 61, 34);
        }
    }

    /*
    * Constructors
    * */
    public BynavnRegister() {
    }

    /*
    * Data source spec
    * */
    @Autowired
    private ConfigurableApplicationContext ctx;


    /*public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152120/a370713.txt");
    }*/
    @Override
    public Resource getRecordResource() {
        return this.ctx.getResource("classpath:/data/cprBynavneregister.zip");
    }

    protected CprRecord parseTrimmedLine(String recordType, String line) {
        CprRecord r = super.parseTrimmedLine(recordType, line);
        if (r != null) {
            return r;
        }
        try {
            if (recordType.equals(ByNavn.RECORDTYPE_BYNAVN)) {
                return new ByNavn(line);
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

    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {

        System.out.println("Preparatory linking");
        long time = this.indepTic();
        ModelUpdateCounter counter = new ModelUpdateCounter();

        Level2Container<HashSet<RawVej>> lokalitetData = new Level2Container<HashSet<RawVej>>();

        for (Record record : run) {
            if (record.getClass() == ByNavn.class) {
                ByNavn by = (ByNavn) record;
                int kommuneKode = by.getInt("kommuneKode");
                int vejKode = by.getInt("vejKode");
                String byNavn = by.get("byNavn");
                HashSet<RawVej> veje = lokalitetData.get(kommuneKode, byNavn);
                if (veje == null) {
                    veje = new HashSet<RawVej>();
                    lokalitetData.put(kommuneKode, byNavn, veje);
                }
                RawVej vej = new RawVej(kommuneKode, vejKode);

                // HashSet.contains just isn't enough here, and HashSet.add fails to find equal elements too
                boolean contains = false;
                for (RawVej v : veje) {
                    if (vej.equals(v)) {
                        contains = true;
                    }
                }
                if (!contains) {
                    veje.add(vej);
                }
                counter.printEntryProcessed();
            }
        }
        counter.printFinalEntriesProcessed();
        System.out.println("Links created in " + this.toc(time) + " ms");

        System.out.println("Storing LokalitetEntities in database");
        time = this.indepTic();
        counter.reset();
        for (int kommuneKode : lokalitetData.intKeySet()) {
            for (String lokalitetsNavn : lokalitetData.get(kommuneKode).keySet()) {
                HashSet<RawVej> veje = lokalitetData.get(kommuneKode, lokalitetsNavn);
                this.model.setLokalitet(
                        kommuneKode, lokalitetsNavn, veje,
                        this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity)
                );
            }
            counter.printEntryProcessed();
        }
        counter.printFinalEntriesProcessed();
        System.out.println("LokalitetEntities stored in "+this.toc(time)+" ms");
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getUploadPartName() {
        return "bynavnSourceUpload";
    }

    @Override
    public String getSourceTypeFieldName() {
        return "bynavnSourceType";
    }

    @Override
    public String getSourceUrlFieldName() {
        return "bynavnSourceUrl";
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        JSONObject config = new JSONObject();
        config.put(this.getSourceUrlFieldName(),"https://cpr.dk/media/152120/a370713.txt");
        return new DataProviderConfiguration(config);
    }
}
