package dk.magenta.databroker.cprvejregister.dataproviders.registers;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneRepository;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.*;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.CprRecord;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.cprvejregister.model.AdresseModel;
import dk.magenta.databroker.cprvejregister.model.VejModel;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;


/**
 * Created by lars on 04-11-14.
 */
@Component
public class VejRegister extends CprSubRegister {


    /*
    * Inner classes for parsed data
    * */

    public abstract class VejDataRecord extends CprRecord {
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
            this.clean();
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
            this.clean();

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
            this.clean();
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
            this.clean();
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
            this.clean();
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
            this.clean();
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
            this.clean();
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
            this.clean();
        }
    }

    public class KirkeDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_KIRKEDIST;
        }
        public KirkeDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("kirkeKode", 33, 2);
            this.clean();
        }
    }

    public class SkoleDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_SKOLEDIST;
        }
        public SkoleDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("skoleKode", 33, 2);
            this.clean();
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
            this.clean();
        }
    }

    public class SocialDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_SOCIALDIST;
        }
        public SocialDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("socialKode", 33, 2);
            this.clean();
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
            this.clean();
        }
    }

    public class ValgDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_VALGDIST;
        }
        public ValgDistrikt(String line) throws ParseException {
            super(line);
            this.obtain("valgKode", 33, 2);
            this.clean();
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
        private EntityModificationCounter delvejCounter;
        private EntityModificationCounter navngivenvejCounter;
        private Level2Container<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVejCache = null;
        private Level1Container<CprKommuneEntity> kommuneCache = null;
        private ArrayList<PostDistrikt> postDistrikter;

        public ArrayList<Bolig> boliger;

        public VejRegisterRun() {
            super();
            this.aktiveVeje = new Level2Container<AktivVej>();
            this.delvejCounter = new EntityModificationCounter();
            this.navngivenvejCounter = new EntityModificationCounter();

            this.boliger = new ArrayList<Bolig>();
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

            if (record.getRecordType().equals(VejDataRecord.RECORDTYPE_BOLIG)) {
                this.boliger.add((Bolig)record);
            }

            return false;
        }

        public Level2Container<AktivVej> getAktiveVeje() {
            return aktiveVeje;
        }

        public ArrayList<PostDistrikt> getPostDistrikter() {
            return postDistrikter;
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

        private Level1Container<CprKommuneEntity> getKommuneCache() {
            if (this.kommuneCache == null) {
                this.kommuneCache = new Level1Container<CprKommuneEntity>();
                Collection<CprKommuneEntity> kommuneListe = kommuneRepository.findAll();
                for (CprKommuneEntity kommune : kommuneListe) {
                    this.kommuneCache.put(kommune.getKommunekode(), kommune);
                }
            }
            return this.kommuneCache;
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



    /*
    * Data source spec
    * */

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152096/vejregister_hele_landet_pr_141201.zip");
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
            if (recordType.equals(VejDataRecord.RECORDTYPE_AKTVEJ)) {
                return new AktivVej(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BOLIG)) {
                return new Bolig(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BYDISTRIKT)) {
                //return new ByDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_POSTDIST)) {
                //return new PostDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_NOTATVEJ)) {
                //return new NotatVej(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BYFORNYDIST)) {
                //return new ByfornyelsesDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_DIVDIST)) {
                //return new DiverseDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_EVAKUERDIST)) {
                //return new EvakueringsDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_KIRKEDIST)) {
                //return new KirkeDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SKOLEDIST)) {
                //return new SkoleDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BEFOLKDIST)) {
                //return new BefolkningsDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SOCIALDIST)) {
                //return new SocialDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SOGNEDIST)) {
                //return new SogneDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_VALGDIST)) {
                //return new ValgDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_VARMEDIST)) {
                //return new VarmeDistrikt(line);
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
    private CprKommuneRepository kommuneRepository;

    @Autowired
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;

    @Autowired
    private NavngivenVejRepository navngivenVejRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private HusnummerRepository husnummerRepository;


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

        System.out.println("Updating entries");
        time = this.indepTic();
        VejModel vejModel = new VejModel(this.kommuneRepository, this.kommunedelAfNavngivenVejRepository, this.navngivenVejRepository, this.getCreateRegistrering(), this.getUpdateRegistrering());
        vejModel.createVeje(new ArrayList<Record>(aktiveVeje.getList()));
        System.out.println("Entry update took "+this.toc(time)+"ms");
        vejModel.cleanNavngivneVeje();




        AdresseModel adresseModel = new AdresseModel(this.adresseRepository, this.navngivenVejRepository, this.husnummerRepository, this.getCreateRegistrering(), this.getUpdateRegistrering());
        adresseModel.createAddresses(new ArrayList<Record>(vrun.boliger));

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
                    CprKommuneEntity kommune = del.getKommune();
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
