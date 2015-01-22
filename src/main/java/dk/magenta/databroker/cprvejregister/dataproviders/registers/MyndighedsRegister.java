package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.DataProviderConfiguration;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.EntityModificationCounter;
import dk.magenta.databroker.register.objectcontainers.Level2Container;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.register.records.Record;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * Created by lars on 04-11-14.
 */
@Component
public class MyndighedsRegister extends CprSubRegister {

    public abstract class MyndighedsDataRecord extends CprRecord {
        public static final String RECORDTYPE_MYNDIGHED = "001";
        public static final String RECORDTYPE_KOMMUNEREL = "002";
        protected int getTimestampStart() {
            return 11;
        }
        public MyndighedsDataRecord(String line) throws ParseException {
            super(line);
            this.obtain("myndighedsKode", 4, 4);
            this.obtain("myndighedsType", 8, 2);
            this.obtain("timestamp", this.getTimestampStart(), 12);
        }
    }

    public class Myndighed extends MyndighedsDataRecord {
        public String getRecordType() {
            return RECORDTYPE_MYNDIGHED;
        }
        protected int getTimestampStart() {
            return 11;
        }
        public Myndighed(String line) throws ParseException {
            super(line);
            this.obtain("myndighedsGruppe", 10, 1);
            this.obtain("telefon", 23, 8);
            this.obtain("startDato", 31, 12);
            this.obtain("slutDato", 43, 12);
            this.obtain("myndighedsNavn", 55, 20);
            this.obtain("myndighedsAdressat", 75, 34);
            this.obtain("adresselinie1", 109, 34);
            this.obtain("adresselinie2", 143, 34);
            this.obtain("adresselinie3", 177, 34);
            this.obtain("adresselinie4", 211, 34);
            this.obtain("telefax", 245, 8);
            this.obtain("myndighedsNavnFull", 253, 60);
            this.obtain("email", 313, 100);
            this.obtain("landekodeA2", 413, 2);
            this.obtain("landekodeA3", 415, 3);
            this.obtain("landekodeN", 418, 3);
        }
    }

    public class KommuneRelation extends MyndighedsDataRecord {
        public String getRecordType() {
            return RECORDTYPE_KOMMUNEREL;
        }
        protected int getTimestampStart() {
            return 15;
        }
        public KommuneRelation(String line) throws ParseException {
            super(line);
            this.obtain("kommuneKode", 11, 4);
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public class MyndighedsRegisterRun extends RegisterRun {

        private Level2Container<Myndighed> myndigheder;
        public MyndighedsRegisterRun() {
            super();
            this.myndigheder = new Level2Container<Myndighed>();
        }
        public boolean add(Record record) {
            if (record.getClass() == Myndighed.class) {
                return this.add((Myndighed) record);
            }
            return false;
        }
        public boolean add(Myndighed myndighed) {
            String myndighedsType = myndighed.get("myndighedsType");
            String myndighedsKode = myndighed.get("myndighedsKode");
            if (!this.myndigheder.put(myndighedsType, myndighedsKode, myndighed, true)) {
                System.err.println("Collision on myndighedsType "+myndighedsType+", myndighedsKode "+myndighedsKode+" ("+myndigheder.get(myndighedsType, myndighedsKode).get("myndighedsNavn")+" vs "+myndighed.get("myndighedsNavn")+")");
            }
            return super.add(myndighed);
        }

        public Myndighed getMyndighed(String myndighedsType, String myndighedsKode) {
            return this.myndigheder.get(myndighedsType, myndighedsKode);
        }
        public List<Myndighed> getMyndigheder(String myndighedsType) {
            return this.myndigheder.getList(myndighedsType);
        }
        public Set<String> getMyndighedsTyper() {
            return this.myndigheder.keySet();
        }
        public Set<String> getMyndighedsKoder(String myndighedsType) {
            if (this.myndigheder.containsKey(myndighedsType)) {
                return this.myndigheder.get(myndighedsType).keySet();
            }
            return null;
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public MyndighedsRegister() {
    }

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/219468/a370716.txt");
    }

    protected String getEncoding() {
        return "ISO-8859-1";
    }

    protected RegisterRun createRun() {
        return new MyndighedsRegisterRun();
    }

    protected CprRecord parseTrimmedLine(String recordType, String line) {
        CprRecord r = super.parseTrimmedLine(recordType, line);
        if (r != null) {
            return r;
        }
        try {
            if (recordType.equals(MyndighedsDataRecord.RECORDTYPE_MYNDIGHED)) {
                return new Myndighed(line);
            }
            if (recordType.equals(MyndighedsDataRecord.RECORDTYPE_KOMMUNEREL)) {
                return new KommuneRelation(line);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    //@Autowired
    //private RegistreringRepository registreringRepository;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DawaModel model;

    protected void saveRunToDatabase(RegisterRun run, DataProviderEntity dataProviderEntity) {
        System.out.println("Storing KommuneEntities in database");
        MyndighedsRegisterRun mrun = (MyndighedsRegisterRun) run;
        List<Myndighed> kommuner = mrun.getMyndigheder("5");

        for (Myndighed kommune : kommuner) {
            int kommuneKode = kommune.getInt("myndighedsKode");
            String kommuneNavn = kommune.get("myndighedsNavn");
            model.setKommune(
                    kommuneKode, kommuneNavn,
                    this.getCreateRegistrering(dataProviderEntity), this.getUpdateRegistrering(dataProviderEntity)
            );
            mrun.printInputProcessed();
        }
        mrun.printFinalInputsProcessed();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getUploadPartName() {
        return "myndighedSourceUpload";
    }

    @Override
    public String getSourceUrlFieldName() {
        return "myndighedSourceUrl";
    }

    @Override
    public DataProviderConfiguration getDefaultConfiguration() {
        JSONObject config = new JSONObject();
        config.put(this.getSourceUrlFieldName(), "https://cpr.dk/media/219468/a370716.txt");
        return new DataProviderConfiguration(config);
    }


}
