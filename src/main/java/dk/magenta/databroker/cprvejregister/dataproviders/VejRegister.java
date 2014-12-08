package dk.magenta.databroker.cprvejregister.dataproviders;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.*;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejVersionEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import org.hibernate.dialect.pagination.LegacyLimitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;


/**
 * Created by lars on 04-11-14.
 */
@Component
public class VejRegister extends CprRegister {


    /*
    * Inner classes for parsed data
    * */

    public abstract class VejDataRecord extends Record {
        public static final String RECORDTYPE_AKTVEJ = "001";
        public static final String RECORDTYPE_BOLIG = "002";
        public static final String RECORDTYPE_BYDISTRIKT = "003";
        public static final String RECORDTYPE_POSTDIST = "004";
        public static final String RECORDTYPE_NOTATVEJ = "005";
        public static final String RECORDTYPE_BYFORNYDIST = "006";
        public static final String RECORDTYPE_DIVDIST = "007";
        public static final String RECORDTYPE_EVAKUERDIST = "008";
        public static final String RECORDTYPE_KIRKEDIST = "009";
        public static final String RECORDTYPE_SKOLEDIST = "010";
        public static final String RECORDTYPE_BEFOLKDIST = "011";
        public static final String RECORDTYPE_SOCIALDIST = "012";
        public static final String RECORDTYPE_SOGNEDIST = "013";
        public static final String RECORDTYPE_VALGDIST = "014";
        public static final String RECORDTYPE_VARMEDIST = "015";
        public static final String RECORDTYPE_HISTORISKVEJ = "016";
        protected int getTimestampStart() {
            return 21;
        }

        public VejDataRecord(String line) throws ParseException {
            super(line);
            this.obtain("kommuneKode", 4, 4);
            this.obtain("vejKode", 8, 4);
            this.obtain("timestamp", this.getTimestampStart(), 12);
        }
    }

    public abstract class Distrikt extends VejDataRecord {
        protected int getDistriktsTekstStart() { return 35; }
        protected int getDistriktsTekstLength() {
            return 30;
        }

        public Distrikt(String line) throws ParseException {
            super(line);
            this.obtain("husNrFra", 12, 4);
            this.obtain("husNrTil", 16, 4);
            this.obtain("ligeUlige", 20, 1);
            this.obtain("distriktsTekst", this.getDistriktsTekstStart(), this.getDistriktsTekstLength());
        }
    }

    public class AktivVej extends VejDataRecord {
        public String getRecordType() {
            return RECORDTYPE_AKTVEJ;
        }
        protected int getTimestampStart() {
            return 12;
        }

        private ArrayList<AktivVej> connections;
        public void addConnection(AktivVej connection) {
            this.connections.add(connection);
        }
        public List<AktivVej> getConnections() {
            return (List<AktivVej>) this.connections;
        }

        public AktivVej(String line) throws ParseException {
            super(line);
            this.obtain("tilKommuneKode", 24, 4);
            this.obtain("tilVejKode", 28, 4);
            this.obtain("fraKommuneKode", 32, 4);
            this.obtain("fraVejKode", 36, 4);
            this.obtain("startDato", 40, 12);
            this.obtain("vejAdresseringsnavn", 52, 20);
            this.obtain("vejNavn", 72, 40);
            this.connections = new ArrayList<AktivVej>();
        }
    }

    public class Bolig extends VejDataRecord {
        public String getRecordType() {
            return RECORDTYPE_BOLIG;
        }
        protected int getTimestampStart() {
            return 22;
        }

        public Bolig(String line) throws ParseException {
            super(line);
            this.obtain("husNr", 12, 4);
            this.obtain("etage", 16, 2);
            this.obtain("sidedoer", 18, 4);
            this.obtain("startDato", 35, 12);
            this.obtain("lokalitet", 59, 34);

        }
    }

    public class ByDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_BYDISTRIKT;
        }
        protected int getDistriktsTekstStart() {
            return 33;
        }
        protected int getDistriktsTekstLength() {
            return 34;
        }
        public ByDistrikt(String line) throws ParseException {
            super(line);
            this.put("bynavn", this.get("distriktsTekst"));
        }
    }

    public class PostDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_POSTDIST;
        }
        protected int getDistriktsTekstStart() {
            return 37;
        }
        protected int getDistriktsTekstLength() {
            return 20;
        }
        public PostDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("postNr", 33, 4);
        }
    }

    public class NotatVej extends VejDataRecord {
        public String getRecordType() {
            return RECORDTYPE_NOTATVEJ;
        }
        protected int getTimestampStart() {
            return 54;
        }
        public NotatVej(String line) throws ParseException {
            super(line);
            this.obtain("notatNr", 12, 2);
            this.obtain("notatLinie", 14, 40);
            this.obtain("startDato", 66, 12);
        }
    }

    public class ByfornyelsesDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_BYFORNYDIST;
        }
        protected int getDistriktsTekstStart() {
            return 39;
        }
        public ByfornyelsesDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("byfornyKode", 33, 6);
        }
    }

    public class DiverseDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_DIVDIST;
        }
        protected int getDistriktsTekstStart() {
            return 39;
        }
        public DiverseDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("distriktType", 33, 2);
            this.obtain("diverseDistriktsKode", 35, 4);
        }
    }

    public class EvakueringsDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_EVAKUERDIST;
        }
        protected int getDistriktsTekstStart() {
            return 34;
        }
        public EvakueringsDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("evakueringsKode", 33, 1);
        }
    }

    public class KirkeDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_KIRKEDIST;
        }
        public KirkeDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("kirkeKode", 33, 2);
        }
    }

    public class SkoleDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_SKOLEDIST;
        }
        public SkoleDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("skoleKode", 33, 2);
        }
    }

    public class BefolkningsDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_BEFOLKDIST;
        }
        protected int getDistriktsTekstStart() {
            return 37;
        }
        public BefolkningsDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("befolkningsKode", 33, 4);
        }
    }

    public class SocialDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_SOCIALDIST;
        }
        public SocialDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("socialKode", 33, 2);
        }
    }

    public class SogneDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_SOGNEDIST;
        }
        protected int getDistriktsTekstStart() {
            return 37;
        }
        protected int getDistriktsTekstLength() {
            return 20;
        }
        public SogneDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("myndighedsKode", 33, 4);
            this.put("myndighedsNavn", this.get("distriktsTekst"));
        }
    }

    public class ValgDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_VALGDIST;
        }
        public ValgDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("valgKode", 33, 2);
        }
    }

    public class VarmeDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_VARMEDIST;
        }
        protected int getDistriktsTekstStart() {
            return 37;
        }
        public VarmeDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("varmeKode", 33, 4);
        }
    }

    public class HistoriskVej extends VejDataRecord {
        public String getRecordType() {
            return RECORDTYPE_HISTORISKVEJ;
        }
        protected int getTimestampStart() {
            return 12;
        }
        public HistoriskVej(String line) throws ParseException {
            super(line);
            this.obtain("startDato", 24, 12);
            this.obtain("slutDato", 36, 12);
            this.obtain("vejAdresseringsnavn", 48, 20);
            this.obtain("vejNavn", 68, 40);
        }
    }


    /*
    * RegisterRun inner class
    * */

    public class VejRegisterRun extends RegisterRun {

        private Level2Container<AktivVej> aktiveVeje;
        EntityModificationCounter delvejCounter;
        EntityModificationCounter navngivenvejCounter;
        Level2Container<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVejCache = null;
        Level1Container<KommuneEntity> kommuneCache = null;

        public VejRegisterRun() {
            super();
            this.aktiveVeje = new Level2Container<AktivVej>();
            this.delvejCounter = new EntityModificationCounter();
            this.navngivenvejCounter = new EntityModificationCounter();
        }

        public boolean add(Record record) {
            if (record.getRecordType().equals(VejDataRecord.RECORDTYPE_AKTVEJ)) {
                AktivVej vej = (AktivVej) record;
                int vejKode = vej.getInt("vejKode");
                int kommuneKode = vej.getInt("kommuneKode");
                if (!aktiveVeje.put(kommuneKode, vejKode, vej, true)) {
                    System.out.println("Collision on kommuneKode "+kommuneKode+", vejKode "+vejKode+" ("+aktiveVeje.get(kommuneKode, vejKode).get("vejNavn")+" vs "+vej.get("vejNavn")+")");
                }
                super.add(vej);
            }
            return false;
        }

        public Level2Container<AktivVej> getAktiveVeje() {
            return aktiveVeje;
        }


        // Process an AktivVej item, adding relevant database entries
        public void processAktivVej(AktivVej aktivVej) {
            ArrayList<Long> time = new ArrayList<Long>();

            int kommuneKode = aktivVej.getInt("kommuneKode");
            int vejKode = aktivVej.getInt("vejKode");
            String vejNavn = aktivVej.get("vejNavn");
            String vejAdresseringsnavn = aktivVej.get("vejAdresseringsnavn");

            if (createRegistrering == null || updateRegistrering == null) {
                createRegistreringEntities();
            }

            //KommuneEntity kommune = kommuneRepository.getByKommunekode(kommuneKode);
            KommuneEntity kommune = this.getKommuneCache().get(kommuneKode);

            if (kommune == null) {
                //System.out.println("Kommune with id "+kommuneKode+" not found for vej "+aktivVej.get("vejNavn"));
            } else {
                Level2Container<KommunedelAfNavngivenVejEntity> delveje = this.getKommuneDelAfNavngivenVejCache();

                KommunedelAfNavngivenVejEntity delvejEntity = delveje.get(kommuneKode, vejKode);

                boolean createdDelvej = false;
                boolean updatedDelvej = false;

                NavngivenVejEntity navngivenVej = null;
                NavngivenVejVersionEntity navngivenVejVersion;

                // If we don't have a KommunedelAfNavngivenVejEntity for this vejKode and kommune, create one
                if (delvejEntity == null) {
                    delvejEntity = KommunedelAfNavngivenVejEntity.create();
                    delvejEntity.setVejkode(vejKode);
                    delvejEntity.setKommune(kommune);
                    this.delvejCounter.countCreatedItem();
                    updatedDelvej = createdDelvej = true;
                } else {
                    // Since we do have a delvej, try to find an existing navngivenvej by looking in it
                    NavngivenVejVersionEntity delvejRefVersion = delvejEntity.getNavngivenVejVersion();
                    if (delvejRefVersion != null) {
                        NavngivenVejEntity navngivenVejEntity = delvejRefVersion.getEntity();
                        if (navngivenVejEntity != null && vejNavn.equals(navngivenVejEntity.getLatestVersion().getVejnavn())) {
                            navngivenVej = navngivenVejEntity;
                        }
                    }
                }

                if (navngivenVej == null) {
                    for (AktivVej otherVej : aktivVej.getConnections()) {
                        navngivenVej = findNavngivenVejByAktivVej(otherVej, vejNavn);
                        if (navngivenVej != null) {
                            break;
                        }
                    }
                }

                // No navngivenvej instances found - create one
                // Also create a new vejversion to put data in
                if (navngivenVej == null) {
                    navngivenVej = NavngivenVejEntity.create();
                    navngivenVejVersion = navngivenVej.addVersion(createRegistrering);
                    this.navngivenvejCounter.countCreatedItem();
                } else {
                    // Put a version on our existing navngivenvej
                    NavngivenVejVersionEntity latestVersion = navngivenVej.getLatestVersion();
                    if (latestVersion.getRegistrering().equals(updateRegistrering) || latestVersion.getRegistrering().equals(createRegistrering)) {
                        // If we happen to have created a version for an existing vej in this very run, reuse that version
                        navngivenVejVersion = latestVersion;
                    } else {
                        // Otherwise append a new version
                        navngivenVejVersion = navngivenVej.addVersion(updateRegistrering);
                    }
                }

                // Update and save navngivenVejVersion
                navngivenVejVersion.setAnsvarligKommune(kommune);
                navngivenVejVersion.setVejnavn(vejNavn);
                navngivenVejVersion.setVejaddresseringsnavn(vejAdresseringsnavn);
                navngivenVejVersion.addKommunedelAfNavngivenVej(delvejEntity);
                navngivenVejRepository.save(navngivenVej);

                // Update and save delvejEntity
                delvejEntity.setNavngivenVejVersion(navngivenVejVersion);
                if (!updatedDelvej) {
                    this.delvejCounter.countUpdatedItem();
                    updatedDelvej = true;
                }
                if (createdDelvej || updatedDelvej) {
                    this.getKommuneDelAfNavngivenVejCache().put(kommuneKode, vejKode, delvejEntity);
                    kommunedelAfNavngivenVejRepository.save(delvejEntity);
                }

                // Process any linked items
                aktivVej.setVisited();
                for (AktivVej otherVej : aktivVej.getConnections()) {
                    if (!otherVej.getVisited()) {
                        this.processAktivVej(otherVej);
                    }
                }


                StringBuilder timeStr = new StringBuilder();
                boolean exceed = false;
                long total = 0;
                for (Long t : time) {
                    timeStr.append(t);
                    timeStr.append(",");
                    if (t > 20) {
                        exceed = true;
                    }
                    total += t;
                }
                if (exceed) {
                    System.out.println(vejNavn + " i " + kommune.getLatestVersion().getNavn() + " processed (" + timeStr.toString() + ") = "+total);
                }

            }
            this.printInputProcessed();
        }

        private Level2Container<KommunedelAfNavngivenVejEntity> getKommuneDelAfNavngivenVejCache() {
            if (this.kommunedelAfNavngivenVejCache == null) {
                this.kommunedelAfNavngivenVejCache = new Level2Container<KommunedelAfNavngivenVejEntity>();
                Collection<KommunedelAfNavngivenVejEntity> delvejListe = kommunedelAfNavngivenVejRepository.getAllLatest();
                for (KommunedelAfNavngivenVejEntity delvej : delvejListe) {
                    kommunedelAfNavngivenVejCache.put(delvej.getKommune().getKommunekode(), delvej.getVejkode(), delvej);
                }
                System.out.println("    Cache size: "+kommunedelAfNavngivenVejCache.totalSize());
            }
            return this.kommunedelAfNavngivenVejCache;
        }

        private Level1Container<KommuneEntity> getKommuneCache() {
            if (this.kommuneCache == null) {
                this.kommuneCache = new Level1Container<KommuneEntity>();
                Collection<KommuneEntity> kommuneListe = kommuneRepository.findAll();
                for (KommuneEntity kommune : kommuneListe) {
                    this.kommuneCache.put(kommune.getKommunekode(), kommune);
                }
            }
            return this.kommuneCache;
        }

        public void printStatus() {
            this.printFinalInputsProcessed();
            System.out.println("Stored KommunedelAfNavngivenVejEntities to database:");
            this.delvejCounter.printModifications();
            System.out.println("Stored NavngivenVejEntities to database:");
            navngivenvejCounter.printModifications();
        }

        private NavngivenVejEntity findNavngivenVejByAktivVej(AktivVej aktivVej) {
            return this.findNavngivenVejByAktivVej(aktivVej, null);
        }
        private NavngivenVejEntity findNavngivenVejByAktivVej(AktivVej aktivVej, String vejNavn) {
            if (aktivVej != null) {
                int kommuneKode = aktivVej.getInt("kommuneKode");
                int vejKode = aktivVej.getInt("vejKode");
                try {
                    //KommunedelAfNavngivenVejEntity andenVejEntity = kommunedelAfNavngivenVejRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
                    KommunedelAfNavngivenVejEntity andenVejEntity = this.getKommuneDelAfNavngivenVejCache().get(kommuneKode, vejKode);
                    if (andenVejEntity != null && (vejNavn == null || vejNavn.equals(andenVejEntity.getNavngivenVejVersion().getVejnavn()))) {
                        return andenVejEntity.getNavngivenVejVersion().getEntity();
                    }
                } catch (Exception e) {
                    System.out.println("Failed on "+kommuneKode+":"+vejKode);
                }
            }
            return null;
        }


    }

    protected RegisterRun createRun() {
        return new VejRegisterRun();
    }

    //------------------------------------------------------------------------------------------------------------------

    /*
    * Constructors
    * */

    public VejRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public VejRegister() {
    }

    @PostConstruct
    public void PostConstructVejRegister() {
        DataProviderEntity vejProvider = new DataProviderEntity();
        vejProvider.setUuid(UUID.randomUUID().toString());

        this.setDataProviderEntity(vejProvider);
    }


    /*
    * Data source spec
    * */

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152096/vejregister_hele_landet_pr_141101.zip");
    }


    /*
    * Parse definition
    * */

    protected Record parseTrimmedLine(String recordType, String line) {
        Record r = super.parseTrimmedLine(recordType, line);
        if (r != null) {
            return r;
        }
        try {
            if (recordType.equals(VejDataRecord.RECORDTYPE_AKTVEJ)) {
                return new AktivVej(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BOLIG)) {
                return new Bolig(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BYDISTRIKT)) {
                return new ByDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_POSTDIST)) {
                return new PostDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_NOTATVEJ)) {
                return new NotatVej(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BYFORNYDIST)) {
                return new ByfornyelsesDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_DIVDIST)) {
                return new DiverseDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_EVAKUERDIST)) {
                return new EvakueringsDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_KIRKEDIST)) {
                return new KirkeDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SKOLEDIST)) {
                return new SkoleDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BEFOLKDIST)) {
                return new BefolkningsDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SOCIALDIST)) {
                return new SocialDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SOGNEDIST)) {
                return new SogneDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_VALGDIST)) {
                return new ValgDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_VARMEDIST)) {
                return new VarmeDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_HISTORISKVEJ)) {
                return new HistoriskVej(line);
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
    private KommuneRepository kommuneRepository;

    @Autowired
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;

    @Autowired
    private NavngivenVejRepository navngivenVejRepository;

    @Autowired
    private HusnummerRepository husnummerRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostnummerRepository postnummerRepository;

    @Autowired
    private RegistreringRepository registreringRepository;


    /*
    * Registration
    * */

    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegistrering;

    private void createRegistreringEntities() {
        this.createRegistrering = registreringRepository.createNew(this);
        this.updateRegistrering = registreringRepository.createUpdate(this);
    }


    /*
    * Database save
    * */

    protected void saveRunToDatabase(RegisterRun run) {
        long time;
        this.createRegistreringEntities();
        VejRegisterRun vrun = (VejRegisterRun) run;


        System.out.println("Storing NavngivenvejEntities and KommunedelAfNavngivenvejEntries in database");

        System.out.println("Preparatory caching");
        time = this.indepTic();
        Level2Container<AktivVej> aktiveVeje = vrun.getAktiveVeje();
        vrun.startInputProcessing();

        // Add connections to AktivVej objects
        for (AktivVej vej : aktiveVeje.getList()) {
            String[] dir = {"fra","til"};
            for (String d : dir) {
                int otherKommuneKode = vej.getInt(d + "KommuneKode");
                int otherVejKode = vej.getInt(d + "VejKode");
                if (otherKommuneKode > 0 && otherVejKode > 0) {
                    AktivVej otherVej = aktiveVeje.get(otherKommuneKode, otherVejKode);
                    if (otherVej != null) {
                        vej.addConnection(otherVej);
                        otherVej.addConnection(vej);
                    }
                }
            }
        }
        System.out.println("    Internal references set");

        // Process each AktivVej object, creating database entries
        // We do this in the VejRegisterRun instance because there is some state information
        // that we don't want to pollute our VejRegister instance with

        //int counter = 0;
        vrun.getKommuneCache();
        System.out.println("    Kommuner loaded into cache");
        vrun.getKommuneDelAfNavngivenVejCache();
        System.out.println("    KommunedelAfNavngivenVej entries loaded into cache");

        System.out.println("Preparatory caching took "+this.toc(time)+"ms");

        System.out.println("Updating entries");
        time = this.indepTic();
        for (AktivVej aktivVej : aktiveVeje.getList()) {
            /*if (counter >= 2000) {
                break;
            }
            counter++;*/
            if (aktivVej != null) {
                vrun.processAktivVej(aktivVej);
            }
        }
        System.out.println("Entry update took "+this.toc(time)+"ms");

        System.out.println("Cleaning versions");
        time = this.indepTic();
        // Clean up redundant versions on NavngivenVej entities
        for (NavngivenVejEntity navngivenVejEntity : navngivenVejRepository.findAll()) {
            navngivenVejEntity.cleanLatestVersion(kommunedelAfNavngivenVejRepository);
            navngivenVejRepository.save(navngivenVejEntity);
        }
        System.out.println("Version cleaning took "+toc(time)+"ms");

        vrun.printStatus();

        System.out.println("Save complete");
    }

    @Transactional
    public void checkNavngivenvejIntegrity() {
        System.out.println("Integrity check");
        long time = this.indepTic();
        for (NavngivenVejEntity navngivenVejEntity : navngivenVejRepository.findAll()) {
            if (navngivenVejEntity.getLatestVersion().getKommunedeleAfNavngivenVej().size() > 1) {
                System.out.println("    Navngiven vej "+navngivenVejEntity.getLatestVersion().getVejnavn()+" bruges af følgende delveje:");
                Collection<KommunedelAfNavngivenVejEntity> delveje2 = navngivenVejEntity.getLatestVersion().getKommunedeleAfNavngivenVej();
                for (KommunedelAfNavngivenVejEntity del : delveje2) {
                    KommuneEntity kommune = del.getKommune();
                    if (kommune == null) {
                        System.err.println("        Kommune not found for delvej "+del.getId());
                        System.err.println("        This should not happen");
                    } else {
                        System.out.println("        " + del.getKommune().getKommunekode()+":"+del.getVejkode() + ": " + del.getNavngivenVejVersion().getVejnavn() + " i " + kommune.getLatestVersion().getNavn()+" kommune");
                    }
                }
            }
        }
        System.out.println("Integrity check complete in "+this.toc(time)+"ms");
    }

}
