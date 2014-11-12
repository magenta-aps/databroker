package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.Level2Container;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.cprvejregister.model.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.KommuneRepository;
import org.apache.cxf.service.invoker.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
//import dk.magenta.databroker.models.adresser.Kommune;

import javax.persistence.EntityManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * Created by lars on 04-11-14.
 */
@SpringApplicationConfiguration(classes = Application.class)
public class MyndighedsRegister extends CprRegister {

    public abstract class MyndighedsDataRecord extends Record {
        public static final String RECORDTYPE_MYNDIGHED = "001";
        public static final String RECORDTYPE_KOMMUNEREL = "002";
        protected int getTimestampStart() {
            return 11;
        }
        public MyndighedsDataRecord(String line) throws ParseException {
            super(line);
            this.put("myndighedsKode", substr(line, 4, 4));
            this.put("myndighedsType", substr(line, 8, 2));
            this.put("timestamp", substr(line, this.getTimestampStart(), 12));
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
            this.put("myndighedsGruppe", substr(line, 10, 1));
            this.put("telefon", substr(line, 23, 8));
            this.put("startDato", substr(line, 31, 12));
            this.put("slutDato", substr(line, 43, 12));
            this.put("myndighedsNavn", substr(line, 55, 20));
            this.put("myndighedsAdressat", substr(line, 75, 34));
            this.put("adresselinie1", substr(line, 109, 34));
            this.put("adresselinie2", substr(line, 143, 34));
            this.put("adresselinie3", substr(line, 177, 34));
            this.put("adresselinie4", substr(line, 211, 34));
            this.put("telefax", substr(line, 245, 8));
            this.put("myndighedsNavnFull", substr(line, 253, 60));
            this.put("email", substr(line, 313, 100));
            this.put("landekodeA2", substr(line, 413, 2));
            this.put("landekodeA3", substr(line, 415, 3));
            this.put("landekodeN", substr(line, 418, 3));
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
            this.put("kommuneKode", substr(line, 11, 4));
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public class MyndighedsRegisterRun extends RegisterRun {

        private Level2Container<Myndighed> myndigheder;
        public MyndighedsRegisterRun() {
            super();
            this.myndigheder = new Level2Container<Myndighed>();
        }
        public void saveRecord(Record record) {
            if (record.getRecordType().equals(MyndighedsDataRecord.RECORDTYPE_MYNDIGHED)) {
                this.saveRecord((Myndighed) record);
            }
        }
        public void saveRecord(Myndighed myndighed) {
            super.saveRecord(myndighed);
            String myndighedsType = myndighed.get("myndighedsType");
            String myndighedsKode = myndighed.get("myndighedsKode");
            if (!myndigheder.put(myndighedsType, myndighedsKode, myndighed, true)) {
                System.out.println("Collision on myndighedsType "+myndighedsType+", myndighedsKode "+myndighedsKode+" ("+myndigheder.get(myndighedsType, myndighedsKode).get("myndighedsNavn")+" vs "+myndighed.get("myndighedsNavn")+")");
            }
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
    //@SuppressWarnings("SpringJavaAutowiringInspection")
    //@Autowired
    //private KommuneRepository kommuneRepository;

    public MyndighedsRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/219468/a370716.txt");
    }
    protected RegisterRun createRun() {
        return new MyndighedsRegisterRun();
    }

    protected Record parseTrimmedLine(String recordType, String line) {
        Record r = super.parseTrimmedLine(recordType, line);
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

    protected void saveRunToDatabase(RegisterRun run) {
        MyndighedsRegisterRun mrun = (MyndighedsRegisterRun) run;
        List<Myndighed> kommuner = mrun.getMyndigheder("05");
        for (Myndighed kommune : kommuner) {
            KommuneEntity kommuneEntity = new KommuneEntity();
            kommuneEntity.setNavn(kommune.get("myndighedsNavn"));
            kommuneEntity.setKommunekode(Integer.parseInt(kommune.get("myndighedsKode")));
            // Vi har nu skabt en Kommune-instans. Gem den somehow
            //this.kommuneRepository.save(kommuneEntity);
        }
    }

    public static void main(String[] args) {
        MyndighedsRegister register = new MyndighedsRegister(null);
        register.pull();
    }

}
