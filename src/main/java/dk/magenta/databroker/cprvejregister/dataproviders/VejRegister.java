package dk.magenta.databroker.cprvejregister.dataproviders;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringLivscyklusStatus;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.*;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;


/**
 * Created by lars on 04-11-14.
 */
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

    protected void saveRunToDatabase(RegisterRun run, RepositoryCollection repositories) {
        try {
            VejRegisterRun vrun = (VejRegisterRun) run;

            KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository = repositories.kommunedelAfNavngivenVejRepository;
            NavngivenVejRepository navngivenVejRepository = repositories.navngivenVejRepository;
            KommuneRepository kommuneRepository = repositories.kommuneRepository;

            RegistreringRepository registreringRepository = repositories.registreringRepository;
            RegistreringEntity createRegistrering = registreringRepository.createNew(this);
            RegistreringEntity updateRegistrering = registreringRepository.createUpdate(this);

            if (kommunedelAfNavngivenVejRepository == null || navngivenVejRepository == null || kommuneRepository == null) {
                System.err.println("Insufficient repositories");
                return;
            }
            System.out.println("Storing NavngivenvejEntities and KommunedelAfNavngivenvejEntries in database");

            //SegmentedList<KommunedelAfNavngivenVejEntity> delvejEntities = new SegmentedList<KommunedelAfNavngivenVejEntity>(10000);
            //SegmentedList<NavngivenVejEntity> navngivenVejEntities = new SegmentedList<NavngivenVejEntity>(10000);

            Level2Container<AktivVej> aktiveVeje = vrun.getAktiveVeje();
            EntityModificationCounter delvejCounter = new EntityModificationCounter();
            EntityModificationCounter navngivenvejCounter = new EntityModificationCounter();
            this.startInputProcessing();

            for (int kommuneKode : aktiveVeje.intKeySet()) {
                for (int vejKode : aktiveVeje.get(kommuneKode).intKeySet()) {
                    AktivVej aktivVej = aktiveVeje.get(kommuneKode, vejKode);
                    if (aktivVej != null) {
                        KommuneEntity kommune = kommuneRepository.findByKommunekode(kommuneKode);

                        if (kommune == null) {
                            //System.out.println("Kommune with id "+kommuneKode+" not found for vej "+aktivVej.get("vejNavn"));
                        } else {

                            KommunedelAfNavngivenVejEntity delvejEntity = kommunedelAfNavngivenVejRepository.findByKommunekodeAndVejkode(kommuneKode, vejKode);

                            String vejNavn = aktivVej.get("vejNavn");
                            String vejAdresseringsnavn = aktivVej.get("vejAdresseringsnavn");

                            boolean createdDelvej = false;
                            boolean updatedDelvej = false;
                            if (delvejEntity == null) {
                                delvejEntity = KommunedelAfNavngivenVejEntity.create();
                                delvejEntity.setVejkode(vejKode);
                                delvejEntity.setKommune(kommune);
                                delvejCounter.countCreatedItem();
                                updatedDelvej = createdDelvej = true;
                            }

                            NavngivenVejEntity navngivenVejEntity = delvejEntity.getNavngivenVej();

                            if (navngivenVejEntity == null) {
                                int fraKommuneKode = aktivVej.getInt("fraKommuneKode");
                                if (fraKommuneKode != 0) {
                                    int fraVejKode = aktivVej.getInt("fraVejKode");
                                    if (fraVejKode != 0) {
                                        AktivVej other = aktiveVeje.get(fraKommuneKode, fraVejKode);
                                        if (other != null) {
                                            KommunedelAfNavngivenVejEntity andenVejEntity = kommunedelAfNavngivenVejRepository.findByKommunekodeAndVejkode(fraKommuneKode, fraVejKode);
                                            if (andenVejEntity != null) {
                                                navngivenVejEntity = andenVejEntity.getNavngivenVej();
                                            }
                                        } else {
                                            //System.out.println("    fra vej: (" + fraKommuneKode + ", " + fraVejKode + ") doesn't exist");
                                        }
                                    }
                                }
                            }

                            if (navngivenVejEntity == null) {
                                int tilKommuneKode = aktivVej.getInt("tilKommuneKode");
                                if (tilKommuneKode != 0) {
                                    int tilVejKode = aktivVej.getInt("tilVejKode");
                                    if (tilVejKode != 0) {
                                        AktivVej other = aktiveVeje.get(tilKommuneKode, tilVejKode);
                                        if (other != null) {
                                            KommunedelAfNavngivenVejEntity andenVejEntity = kommunedelAfNavngivenVejRepository.findByKommunekodeAndVejkode(tilKommuneKode, tilVejKode);
                                            if (andenVejEntity != null) {
                                                navngivenVejEntity = andenVejEntity.getNavngivenVej();
                                            }
                                        } else {
                                            //System.out.println("    til vej: (" + tilKommuneKode + ", " + tilVejKode + ") doesn't exist");
                                        }
                                    }
                                }
                            }


                            boolean updatedNavngivenVej = false;
                            boolean createdNavngivenVej = false;
                            if (navngivenVejEntity == null) {
                                navngivenVejEntity = NavngivenVejEntity.create();
                                navngivenVejEntity.setUuid(UUID.randomUUID().toString());
                                navngivenvejCounter.countCreatedItem();
                                updatedNavngivenVej = createdNavngivenVej = true;
                            }


                            if (!vejNavn.equals(navngivenVejEntity.getVejnavn())) {
                                navngivenVejEntity.setVejnavn(vejNavn);
                                if (!updatedNavngivenVej) {
                                    navngivenvejCounter.countUpdatedItem();
                                    updatedNavngivenVej = true;
                                }
                            }
                            if (!vejAdresseringsnavn.equals(navngivenVejEntity.getVejaddresseringsnavn())) {
                                navngivenVejEntity.setVejaddresseringsnavn(vejAdresseringsnavn);
                                if (!updatedNavngivenVej) {
                                    navngivenvejCounter.countUpdatedItem();
                                    updatedNavngivenVej = true;
                                }
                            }
                            if (!kommune.equals(navngivenVejEntity.getAnsvarligKommune())) {
                                navngivenVejEntity.setAnsvarligKommune(kommune);
                                if (!updatedNavngivenVej) {
                                    navngivenvejCounter.countUpdatedItem();
                                    updatedNavngivenVej = true;
                                }
                            }


                            if (createdNavngivenVej) {
                                navngivenVejEntity.save(repositories, createRegistrering);
                            } else if (updatedNavngivenVej) {
                                navngivenVejEntity.save(repositories, updateRegistrering);
                            }




                            if (delvejEntity.getNavngivenVej() == null || !delvejEntity.getNavngivenVej().equals(navngivenVejEntity)) {
                                delvejEntity.setNavngivenVej(navngivenVejEntity);
                                if (!updatedDelvej) {
                                    delvejCounter.countUpdatedItem();
                                    updatedDelvej = true;
                                }
                            }

                            if (createdDelvej) {
                                delvejEntity.save(repositories, createRegistrering);
                            } else if (updatedDelvej) {
                                delvejEntity.save(repositories, updateRegistrering);
                            }
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

        } catch (ClassCastException e) {}
    }

    public static void main(String[] args) {
        new VejRegister(null).pull();
        System.out.println("Finished");
    }
}
