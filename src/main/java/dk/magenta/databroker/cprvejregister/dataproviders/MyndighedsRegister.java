package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.Application;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.Level2Container;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneVersionEntity;
import org.springframework.boot.test.SpringApplicationConfiguration;
//import dk.magenta.databroker.models.adresser.Kommune;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

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
            if (record.getRecordType().equals(MyndighedsDataRecord.RECORDTYPE_MYNDIGHED)) {
                Myndighed myndighed = (Myndighed) record;
                String myndighedsType = myndighed.get("myndighedsType");
                String myndighedsKode = myndighed.get("myndighedsKode");
                if (!myndigheder.put(myndighedsType, myndighedsKode, myndighed, true)) {
                    System.out.println("Collision on myndighedsType "+myndighedsType+", myndighedsKode "+myndighedsKode+" ("+myndigheder.get(myndighedsType, myndighedsKode).get("myndighedsNavn")+" vs "+myndighed.get("myndighedsNavn")+")");
                }
                return super.add(myndighed);
            }
            return false;
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

    protected String getEncoding() {
        return "ISO-8859-1";
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

    protected void saveRunToDatabase(RegisterRun run, RepositoryCollection repositories) {

        KommuneRepository kommuneRepository = repositories.kommuneRepository;

        RegistreringRepository registreringRepository = repositories.registreringRepository;
        RegistreringEntity createRegistrering = registreringRepository.createNew(this);
        RegistreringEntity updateRegistrering = registreringRepository.createUpdate(this);


        if (kommuneRepository == null) {
            System.err.println("Insufficient repositories");
            return;
        }
        System.out.println("Storing KommuneEntities in database");
        MyndighedsRegisterRun mrun = (MyndighedsRegisterRun) run;
        List<Myndighed> kommuner = mrun.getMyndigheder("5");
        EntityModificationCounter counter = new EntityModificationCounter();

        for (Myndighed kommune : kommuner) {
            int kommuneKode = kommune.getInt("myndighedsKode");
            String kommuneNavn = kommune.get("myndighedsNavn");
            KommuneEntity kommuneEntity = kommuneRepository.findByKommunekode(kommuneKode);

            List<VirkningEntity> virkninger = new ArrayList<VirkningEntity>(); // TODO: Populate this list

            KommuneVersionEntity kommuneVersion = null;
            if (kommuneEntity == null) {
                kommuneEntity = KommuneEntity.create();
                kommuneEntity.setKommunekode(kommuneKode);
                kommuneVersion = kommuneEntity.addVersion(createRegistrering, virkninger);
                counter.countCreatedItem();
            } else if (!kommuneEntity.getLatestVersion().getNavn().equals(kommuneNavn)) {
                kommuneVersion = kommuneEntity.addVersion(updateRegistrering, virkninger);
                counter.countUpdatedItem();
            }

            if (kommuneVersion != null) {
                kommuneVersion.setNavn(kommuneNavn);
                kommuneRepository.save(kommuneEntity);
            }


            this.printInputProcessed();
        }
        this.printFinalInputsProcessed();
        kommuneRepository.flush();
        System.out.println("Stored KommuneEntities in database:");
        counter.printModifications();
    }

    public static void main(String[] args) {
        MyndighedsRegister register = new MyndighedsRegister(null);
        register.pull();
    }

}
