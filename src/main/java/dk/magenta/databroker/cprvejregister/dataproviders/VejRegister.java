package dk.magenta.databroker.cprvejregister.dataproviders;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.*;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;


/**
 * Created by lars on 04-11-14.
 */
@Component
public class VejRegister extends CprRegister {


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

        public AktivVej(String line) throws ParseException {
            super(line);
            this.obtain("tilKommuneKode", 24, 4);
            this.obtain("tilVejKode", 28, 4);
            this.obtain("fraKommuneKode", 32, 4);
            this.obtain("fraVejKode", 36, 4);
            this.obtain("startDato", 40, 12);
            this.obtain("vejAdresseringsnavn", 52, 20);
            this.obtain("vejNavn", 72, 40);
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

    public class VejRegisterRun extends RegisterRun {

        private Level2Container<AktivVej> aktiveVeje;
        public VejRegisterRun() {
            super();
            this.aktiveVeje = new Level2Container<AktivVej>();
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
    }

    //------------------------------------------------------------------------------------------------------------------

    private class KommuneContainer extends Level2Container<KommunedelAfNavngivenVejEntity> {
    }

    //------------------------------------------------------------------------------------------------------------------
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

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152096/vejregister_hele_landet_pr_141101.zip");
    }

    protected RegisterRun createRun() {
        return new VejRegisterRun();
    }

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



    private NavngivenVejEntity findNavngivenVejByContainer(Level2Container<AktivVej> aktiveVeje, int kommuneKode, int vejKode, KommunedelAfNavngivenVejRepository repository, String vejNavn) {
        if (kommuneKode != 0 && vejKode != 0) {
            AktivVej other = aktiveVeje.get(kommuneKode, vejKode);
            if (other != null) {
                KommunedelAfNavngivenVejEntity andenVejEntity = repository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
                if (andenVejEntity != null && vejNavn.equals(andenVejEntity.getNavngivenVejVersion().getVejnavn())) {
                    return andenVejEntity.getNavngivenVejVersion().getEntity();
                }
            }
        }
        return null;
    }


    @Autowired
    private KommuneRepository kommuneRepository;

    public KommuneRepository getKommuneRepository() {
        return kommuneRepository;
    }

    @Autowired
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;

    public KommunedelAfNavngivenVejRepository getKommunedelAfNavngivenVejRepository() {
        return kommunedelAfNavngivenVejRepository;
    }

    @Autowired
    private NavngivenVejRepository navngivenVejRepository;


    public NavngivenVejRepository getNavngivenVejRepository() {
        return navngivenVejRepository;
    }

    @Autowired
    private HusnummerRepository husnummerRepository;

    public HusnummerRepository getHusnummerRepository() {
        return husnummerRepository;
    }

    @Autowired
    private AdresseRepository adresseRepository;

    public AdresseRepository getAdresseRepository() {
        return adresseRepository;
    }

    @Autowired
    private PostnummerRepository postnummerRepository;

    public PostnummerRepository getPostnummerRepository() {
        return postnummerRepository;
    }


    @Autowired
    private RegistreringRepository registreringRepository;

    protected void saveRunToDatabase(RegisterRun run) {

            VejRegisterRun vrun = (VejRegisterRun) run;

            RegistreringEntity createRegistrering = registreringRepository.createNew(this);
            RegistreringEntity updateRegistrering = registreringRepository.createUpdate(this);

            System.out.println("Storing NavngivenvejEntities and KommunedelAfNavngivenvejEntries in database");
            Level2Container<AktivVej> aktiveVeje = vrun.getAktiveVeje();
            EntityModificationCounter delvejCounter = new EntityModificationCounter();
            EntityModificationCounter navngivenvejCounter = new EntityModificationCounter();
            this.startInputProcessing();

            int counter = 0;

            for (int kommuneKode : aktiveVeje.intKeySet()) {
                if (counter >= 5000) {
                    break;
                }

                for (int vejKode : aktiveVeje.get(kommuneKode).intKeySet()) {
                    if (counter >= 5000) {
                        break;
                    }
                    counter++;
                    System.out.println("counter: "+counter);

                    AktivVej aktivVej = aktiveVeje.get(kommuneKode, vejKode);
                    if (aktivVej != null) {
                        KommuneEntity kommune = kommuneRepository.getByKommunekode(kommuneKode);

                        if (kommune == null) {
                            System.out.println("Kommune with id "+kommuneKode+" not found for vej "+aktivVej.get("vejNavn"));
                        } else {

                            this.tic();

                            KommunedelAfNavngivenVejEntity delvejEntity = kommunedelAfNavngivenVejRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);

                            System.out.println("fetching delvej took " + this.toc());

                            String vejNavn = aktivVej.get("vejNavn");
                            String vejAdresseringsnavn = aktivVej.get("vejAdresseringsnavn");

                            boolean createdDelvej = false;
                            boolean updatedDelvej = false;
                            NavngivenVejEntity navngivenVej = null;
                            NavngivenVejVersionEntity navngivenVejVersion = null;

                            // If we don't have a KommunedelAfNavngivenVejEntity for this vejKode and kommune, create one
                            if (delvejEntity == null) {
                                System.out.println("We have to create a delvej. Vejkode: " + vejKode + " - kommune: " + kommune.getKommunekode());
                                delvejEntity = KommunedelAfNavngivenVejEntity.create();
                                delvejEntity.setVejkode(vejKode);
                                delvejEntity.setKommune(kommune);
                                delvejCounter.countCreatedItem();
                                updatedDelvej = createdDelvej = true;
                            } else {
                                // Try to find an existing navngivenvej by looking in our KommunedelAfNavngivenVejEntity
                                navngivenVejVersion = delvejEntity.getNavngivenVejVersion();
                                if (navngivenVejVersion != null) {
                                    NavngivenVejEntity navngivenVejEntity = navngivenVejVersion.getEntity();
                                    if (navngivenVejEntity != null && vejNavn.equals(navngivenVejEntity.getLatestVersion().getVejnavn())) {
                                        navngivenVej = navngivenVejEntity;
                                    }
                                }
                                navngivenVejVersion = null;
                            }

                            // Try to find an existing navngivenvej by looking in references forward and backward
                            if (navngivenVej == null) {
                                navngivenVej = findNavngivenVejByContainer(aktiveVeje, aktivVej.getInt("fraKommuneKode"), aktivVej.getInt("fraVejKode"), kommunedelAfNavngivenVejRepository, vejNavn);
                            }

                            if (navngivenVej == null) {
                                navngivenVej = findNavngivenVejByContainer(aktiveVeje, aktivVej.getInt("tilKommuneKode"), aktivVej.getInt("tilVejKode"), kommunedelAfNavngivenVejRepository, vejNavn);
                            }

                            // No navngivenvej instances found - create one
                            if (navngivenVej == null) {
                                navngivenVej = NavngivenVejEntity.create();
                                navngivenVejVersion = navngivenVej.addVersion(createRegistrering);
                                navngivenvejCounter.countCreatedItem();
                            } else {
                                // Put a version on our existing navngivenvej
                                NavngivenVejVersionEntity latestVersion = navngivenVej.getLatestVersion();
                                if (!(
                                        vejNavn.equals(latestVersion.getVejnavn()) &&
                                        vejAdresseringsnavn.equals(latestVersion.getVejaddresseringsnavn()) &&
                                        kommune.equals(latestVersion.getAnsvarligKommune())
                                )) {
                                    navngivenVejVersion = navngivenVej.addVersion(updateRegistrering);
                                }
                            }

                            // Update and save navngivenvejversion
                            if (navngivenVejVersion != null) {
                                navngivenVejVersion.setAnsvarligKommune(kommune);
                                navngivenVejVersion.setVejnavn(vejNavn);
                                navngivenVejVersion.setVejaddresseringsnavn(vejAdresseringsnavn);
                                navngivenVejRepository.save(navngivenVej);
                            }





                            if (delvejEntity.getNavngivenVejVersion() == null || !delvejEntity.getNavngivenVejVersion().equals(navngivenVej.getLatestVersion())) {
                                delvejEntity.setNavngivenVejVersion(navngivenVej.getLatestVersion());
                                if (!updatedDelvej) {
                                    delvejCounter.countUpdatedItem();
                                    updatedDelvej = true;
                                }
                            }

                            if (createdDelvej || updatedDelvej) {
                                kommunedelAfNavngivenVejRepository.save(delvejEntity);
                            }
                            System.out.println("processed one item ("+vejNavn+")");

                            
                        }
                        this.printInputProcessed();
                    } else {
                        System.out.println("NULL road at kommuneKode: "+kommuneKode+", vejKode: "+vejKode);
                    }
                }
            }

            this.printFinalInputsProcessed();
            System.out.println("Stored KommunedelAfNavngivenVejEntities to database:");
            delvejCounter.printModifications();
            System.out.println("Stored NavngivenVejEntities to database:");
            navngivenvejCounter.printModifications();
    }

    public static void main(String[] args) {
        new VejRegister(null).pull();
        System.out.println("Finished");
    }
}
