package dk.magenta.databroker.grlokalitetregister;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.dawa.model.RawVej;
import dk.magenta.databroker.register.LineRegister;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.util.objectcontainers.Level2Container;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.util.objectcontainers.ModelUpdateCounter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.core.io.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 12-12-14.
 */
@Component
public class GrLokalitetsRegister extends LineRegister {

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

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public Resource getRecordResource() {
        return this.ctx.getResource("classpath:/data/groenlandLokaliteter.csv");
    }

    @Override
    protected String getEncoding() {
        return "UTF-8";
    }


    /*
    * Logging
     */
    private Logger log = Logger.getLogger(GrLokalitetsRegister.class);


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
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel model;

     protected void saveRunToDatabase(RegisterRun run, RegistreringInfo registreringInfo) {

         this.log.info("Preparatory linking");
         double time = this.indepTic();
         ModelUpdateCounter counter = new ModelUpdateCounter();
         counter.setLog(this.log);
         GrRegisterRun grun = (GrRegisterRun) run;

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
             counter.countEntryProcessed();
         }
         counter.printFinalEntriesProcessed();
         this.log.info("Links created in " + this.toc(time) + " ns");


         this.log.info("Storing LokalitetEntities in database");
         counter.reset();
         for (int kommuneKode : lokalitetData.intKeySet()) {
             for (String lokalitetsNavn : lokalitetData.get(kommuneKode).keySet()) {
                 HashSet<RawVej> veje = lokalitetData.get(kommuneKode, lokalitetsNavn);
                 this.model.setLokalitet(kommuneKode, lokalitetsNavn, veje, registreringInfo);
                 counter.countEntryProcessed();
             }
         }
         counter.printFinalEntriesProcessed();
         time = this.toc(time);
         int count = counter.getCount();
         this.log.info(count + " LokalitetEntities stored in " + time + " ms (avg " + (time / (double) count) + " ms)");
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTemplatePath() {
        return "GrLokalitetsRegisterForm";
    }

    public DataProviderConfiguration getDefaultConfiguration() {
        return new DataProviderConfiguration("{\"sourceType\":\"upload\"}");
    }

    @Override
    public List<String> getUploadFields() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("sourceUpload");
        return list;
    }

    public boolean wantUpload(DataProviderConfiguration configuration) {
        List<String> sourceType = configuration.get("sourceType");
        return sourceType != null && sourceType.contains("upload");
    }

}
