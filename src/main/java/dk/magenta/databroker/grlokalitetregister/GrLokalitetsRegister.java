package dk.magenta.databroker.grlokalitetregister;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.RawVej;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.Level2Container;
import dk.magenta.databroker.register.records.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by lars on 12-12-14.
 */
@Component
public class GrLokalitetsRegister extends Register {

    /*
    * RegisterRun inner class
    * */

    private class GrLokalitetRecord extends Record {
    }

    private class GrRegisterRun extends RegisterRun {
    }

    @Override
    protected RegisterRun createRun() {
        return new GrRegisterRun();
    }

    //------------------------------------------------------------------------------------------------------------------

    /*
    * Constructors
    * */

    public GrLokalitetsRegister() {
    }

    /*
    * Data source spec
    * */

    @Override
    public File getRecordFile() {
        return new File("src/test/resources/grønlandLokaliteter.csv");
    }

    @Override
    protected String getEncoding() {
        return "UTF-8";
    }


    /*
    * Parse definition
    * */

    protected GrLokalitetRecord parseTrimmedLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {
            GrLokalitetRecord record = new GrLokalitetRecord();
            record.put("kommuneKode", parts[0]);
            record.put("vejKode", parts[1]);
            record.put("lokalitetsNavn", parts[2]);
            record.put("lokalitetsKode", parts[3]);
            return record;
        }
        return null;
    }


    @Transactional
    public void pull(boolean forceFetch, boolean forceParse, DataProviderEntity dataProviderEntity) {
        super.pull(forceFetch, forceParse, dataProviderEntity);
    }


    /*
    * Database save
    * */

    @Autowired
    private DawaModel model;

     protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {
         GrRegisterRun grun = (GrRegisterRun) run;
         this.model.resetVejstykkeCache();
         this.model.resetLokalitetCache();

         Level2Container<HashSet<RawVej>> lokalitetData = new Level2Container<HashSet<RawVej>>();
         for (Record record : grun) {
             GrLokalitetRecord gRecord = (GrLokalitetRecord) record;
             int lokalitetsKode = gRecord.getInt("lokalitetsKode");
             String lokalitetsNavn = gRecord.get("lokalitetsNavn");
             int kommuneKode = gRecord.getInt("kommuneKode");
             int vejKode = gRecord.getInt("vejKode");
             HashSet<RawVej> veje = lokalitetData.get(kommuneKode, lokalitetsNavn);
             if (veje == null) {
                 veje = new HashSet<RawVej>();
                 lokalitetData.put(kommuneKode, lokalitetsNavn, veje);
             }
             RawVej vej = new RawVej(kommuneKode, vejKode);
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
         for (int kommuneKode : lokalitetData.intKeySet()) {
             for (String lokalitetsNavn : lokalitetData.get(kommuneKode).keySet()) {
                 HashSet<RawVej> veje = lokalitetData.get(kommuneKode, lokalitetsNavn);
                 this.model.setLokalitet(kommuneKode, lokalitetsNavn, veje,
                         this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity)
                 );
             }
         }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getTemplatePath() {
        return "/fragments/GrLokalitetsRegisterForm.txt";
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        return new DataProviderConfiguration("{\"sourceType\":\"upload\"}");
    }
}
