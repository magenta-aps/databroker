package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.CprRecord;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.RawVej;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.InputProcessingCounter;
import dk.magenta.databroker.register.objectcontainers.Level2Container;
import dk.magenta.databroker.register.records.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.UUID;
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

    public BynavnRegister() {
    }

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152120/a370713.txt");
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
    private DawaModel model;

    /*
    * Database save
    * */

    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {

        System.out.println("Storing Bynavne in database");

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
            }
        }

        for (int kommuneKode : lokalitetData.intKeySet()) {
            for (String lokalitetsNavn : lokalitetData.get(kommuneKode).keySet()) {
                HashSet<RawVej> veje = lokalitetData.get(kommuneKode, lokalitetsNavn);
                this.model.setLokalitet(
                        kommuneKode, lokalitetsNavn, veje,
                        this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity)
                );
            }
        }

        System.out.println("Save complete");

    }




    @Override
    protected String getUploadPartName() {
        return "bynavnSourceUpload";
    }
}
