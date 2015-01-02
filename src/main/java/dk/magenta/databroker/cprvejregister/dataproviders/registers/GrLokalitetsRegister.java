package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.RawVej;
import dk.magenta.databroker.register.Register;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.Pair;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.cprvejregister.model.LokalitetModel;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

    public GrLokalitetsRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

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


    /*
    * Database save
    * */

    @Autowired
    private DawaModel model;

     protected void saveRunToDatabase(RegisterRun run) {
         GrRegisterRun grun = (GrRegisterRun) run;
         this.createRegistreringEntities();

         HashMap<String, HashSet<RawVej>> lokalitetData = new HashMap<String, HashSet<RawVej>>();
         for (Record record : grun) {
             GrLokalitetRecord gRecord = (GrLokalitetRecord) record;
             int lokalitetsKode = gRecord.getInt("lokalitetsKode");
             String lokalitetsNavn = gRecord.get("lokalitetsNavn");
             int kommuneKode = gRecord.getInt("kommuneKode");
             int vejKode = gRecord.getInt("vejKode");
             HashSet<RawVej> veje = lokalitetData.get(lokalitetsNavn);
             if (veje == null) {
                 veje = new HashSet<RawVej>();
                 lokalitetData.put(lokalitetsNavn, veje);
             }
             RawVej vej = new RawVej(kommuneKode, vejKode);
             veje.add(vej);
         }
         for (String lokalitetsNavn : lokalitetData.keySet()) {
             HashSet<RawVej> veje = lokalitetData.get(lokalitetsNavn);
             this.model.setLokalitet(lokalitetsNavn, veje, this.getCreateRegistrering(), this.getUpdateRegistrering());
         }
    }
}
