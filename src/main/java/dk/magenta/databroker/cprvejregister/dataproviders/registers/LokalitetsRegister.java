package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.register.objectcontainers.EntityModificationCounter;
import dk.magenta.databroker.register.objectcontainers.InputProcessingCounter;
import dk.magenta.databroker.register.records.Record;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
@Component
public class LokalitetsRegister extends CprSubRegister {


    /*
    * Inner classes for parsed data
    * */
    public class Lokalitet extends CprRecord {
        public static final String RECORDTYPE_LOKALITET = "001";
        public String getRecordType() {
            return RECORDTYPE_LOKALITET;
        }
        public Lokalitet(String line) throws ParseException {
            super(line);
            this.obtain("kommuneKode", 4, 4);
            this.obtain("vejKode", 8, 4);
            this.obtain("myndighedsNavn", 12, 20);
            this.obtain("vejadresseringsNavn", 32, 20);
            this.obtain("husNr", 52, 4);
            this.obtain("etage", 56, 2);
            this.obtain("sidedoer", 58, 4);
            this.obtain("lokalitet", 62, 34);
        }
    }

    private class LokalitetRegisterRun extends RegisterRun {
        public boolean add(Record record) {
            if (record.getClass() == Lokalitet.class) {
                return super.add(record);
            }
            return false;
        }
    }


    @Override
    protected RegisterRun createRun() {
        return new LokalitetRegisterRun();
    }


    //------------------------------------------------------------------------------------------------------------------

    /*
    * Constructors
    * */

    public LokalitetsRegister() {
    }

    /*
    * Data source spec
    * */

    @Autowired
    private ConfigurableApplicationContext ctx;

    /*@Override
    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152108/a370714.txt");
    }*/
    @Override
    public Resource getRecordResource() {
        return this.ctx.getResource("classpath:/data/cprLokalitetsregister.zip");
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
            if (recordType.equals(Lokalitet.RECORDTYPE_LOKALITET)) {
                return new Lokalitet(line);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
    * Database save
    * */


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel model;

    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {

        System.out.println("Storing KommuneEntities in database");
        LokalitetRegisterRun lrun = (LokalitetRegisterRun) run;
        EntityModificationCounter counter = new EntityModificationCounter();
        InputProcessingCounter inputCounter = new InputProcessingCounter(true);


        for (Record record : lrun) {
            if (record.getClass() == Lokalitet.class) {
                Lokalitet lokalitet = (Lokalitet) record;
                int kommuneKode = lokalitet.getInt("kommuneKode");
                int vejKode = lokalitet.getInt("vejKode");
                String lokalitetsNavn = lokalitet.get("myndighedsNavn");
                String husnr = lokalitet.get("husNr");
                String etage = lokalitet.get("etage");
                String sidedoer = lokalitet.get("sidedoer");
                model.setAdresse(kommuneKode, vejKode, husnr, null, etage, sidedoer,
                        this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity)
                );
                inputCounter.printInputProcessed();
            }
        }
        inputCounter.printFinalInputsProcessed();

        System.out.println("Stored LokalitetEntities in database:");
        counter.printModifications();
    }



    @Override
    protected String getUploadPartName() {
        return "lokalitetSourceUpload";
    }

    public String getSourceTypeFieldName() {
        return "lokalitetSourceType";
    }
    public String getSourceUrlFieldName() {
        return "lokalitetSourceUrl";
    }
    public String getSourceCronFieldName() {
        return "lokalitetCron";
    }


    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        JSONObject config = new JSONObject();
        config.put(this.getSourceTypeFieldName(),"url");
        config.put(this.getSourceUrlFieldName(),"https://cpr.dk/media/152108/a370714.txt");
        config.put(this.getSourceCronFieldName(),"* * 1 * *");
        return new DataProviderConfiguration(config);
    }
}
