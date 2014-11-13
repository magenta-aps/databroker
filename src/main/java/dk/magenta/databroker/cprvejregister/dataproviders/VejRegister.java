package dk.magenta.databroker.cprvejregister.dataproviders;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.*;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import dk.magenta.databroker.cprvejregister.model.*;
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
            this.put("kommuneKode", substr(line, 4, 4));
            this.put("vejKode", substr(line, 8, 4));
            this.put("timestamp", substr(line, this.getTimestampStart(), 12));
        }
    }

    public abstract class Distrikt extends VejDataRecord {
        protected int getDistriktsTekstStart() { return 35; }
        protected int getDistriktsTekstLength() {
            return 30;
        }

        public Distrikt(String line) throws ParseException {
            super(line);
            this.put("husNrFra", substr(line, 12, 4));
            this.put("husNrTil", substr(line, 16, 4));
            this.put("ligeUlige", substr(line, 20, 1));
            this.put("distriktsTekst", substr(line, this.getDistriktsTekstStart(), this.getDistriktsTekstLength()));
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
            this.put("tilKommuneKode", substr(line, 24, 4));
            this.put("tilVejKode", substr(line, 28, 4));
            this.put("fraKommuneKode", substr(line, 32, 4));
            this.put("fraVejKode", substr(line, 36, 4));
            this.put("startDato", substr(line, 40, 12));
            this.put("vejAdresseringsnavn", substr(line, 52, 20));
            this.put("vejNavn", substr(line, 72, 40));
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
            this.put("husNr", substr(line, 12, 4));
            this.put("etage", substr(line, 16, 2));
            this.put("sidedoer", substr(line, 18, 4));
            this.put("startDato", substr(line, 35, 12));
            this.put("lokalitet", substr(line, 59, 34));

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
            this.put("postNr", substr(line, 33, 4));
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
            this.put("notatNr", substr(line, 12, 2));
            this.put("notatLinie", substr(line, 14, 40));
            this.put("startDato", substr(line, 66, 12));
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
            this.put("byfornyKode", substr(line, 33, 6));
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
            this.put("distriktType", substr(line, 33, 2));
            this.put("diverseDistriktsKode", substr(line, 35, 4));
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
            this.put("evakueringsKode", substr(line, 33, 1));
        }
    }

    public class KirkeDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_KIRKEDIST;
        }
        public KirkeDistrikt(String line) throws ParseException {
            super(line);
            this.put("kirkeKode", substr(line, 33, 2));
        }
    }

    public class SkoleDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_SKOLEDIST;
        }
        public SkoleDistrikt(String line) throws ParseException {
            super(line);
            this.put("skoleKode", substr(line, 33, 2));
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
            this.put("befolkningsKode", substr(line, 33, 4));
        }
    }

    public class SocialDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_SOCIALDIST;
        }
        public SocialDistrikt(String line) throws ParseException {
            super(line);
            this.put("socialKode", substr(line, 33, 2));
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
            this.put("myndighedsKode", substr(line, 33, 4));
            this.put("myndighedsNavn", this.get("distriktsTekst"));
        }
    }

    public class ValgDistrikt extends Distrikt {
        public String getRecordType() {
            return RECORDTYPE_VALGDIST;
        }
        public ValgDistrikt(String line) throws ParseException {
            super(line);
            this.put("valgKode", substr(line, 33, 2));
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
            this.put("varmeKode", substr(line, 33, 4));
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
            this.put("startDato", substr(line, 24, 12));
            this.put("slutDato", substr(line, 36, 12));
            this.put("vejAdresseringsnavn", substr(line, 48, 20));
            this.put("vejNavn", substr(line, 68, 40));
        }
    }

    public class VejRegisterRun extends RegisterRun {

        private Level2Container<AktivVej> aktiveVeje;
        public VejRegisterRun() {
            super();
            this.aktiveVeje = new Level2Container<AktivVej>();
        }
        public void saveRecord(Record record) {
            if (record.getRecordType().equals(VejDataRecord.RECORDTYPE_AKTVEJ)) {
                this.saveRecord((AktivVej) record);
            }
        }
        public void saveRecord(AktivVej vej) {
            super.saveRecord(vej);
            String vejKode = vej.get("vejKode");
            String kommuneKode = vej.get("kommuneKode");
            if (!aktiveVeje.put(kommuneKode, vejKode, vej, true)) {
                System.out.println("Collision on kommuneKode "+kommuneKode+", vejKode "+vejKode+" ("+aktiveVeje.get(kommuneKode, vejKode).get("vejNavn")+" vs "+vej.get("vejNavn")+")");
            }
        }

        public Level2Container<AktivVej> getAktiveVeje() {
            return aktiveVeje;
        }

        public void setAktiveVeje(Level2Container<AktivVej> aktiveVeje) {
            this.aktiveVeje = aktiveVeje;
        }
/*
        public VejRegister.AktivVej getAktivVej(String kommuneKode, String vejKode) {
            return this.aktiveVeje.get(kommuneKode, vejKode);
        }
        public Set<Integer> getKommuneKoder() {
            return (Set<String>) this.aktiveVeje.keySet();
        }
        public Set<String> getVejKoder(String kommuneKode) {
            if (this.aktiveVeje.containsKey(kommuneKode)) {
                return this.aktiveVeje.get(kommuneKode).keySet();
            }
            return null;
        }*/
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

    protected void saveRunToDatabase(RegisterRun run, List<JpaRepository> repositories) {
        try {
            VejRegisterRun vrun = (VejRegisterRun) run;
            KommuneContainer created = new KommuneContainer();


            KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository = null;
            NavngivenVejRepository navngivenVejRepository = null;
            KommuneRepository kommuneRepository = null;

            for (JpaRepository repository : repositories) {
                try {
                    kommunedelAfNavngivenVejRepository = (KommunedelAfNavngivenVejRepository) repository;
                    continue;
                } catch (ClassCastException e) {}
                try {
                    navngivenVejRepository = (NavngivenVejRepository) repository;
                    continue;
                } catch (ClassCastException e) {}
                try {
                    kommuneRepository = (KommuneRepository) repository;
                    continue;
                } catch (ClassCastException e) {}
            }
            if (kommunedelAfNavngivenVejRepository == null || navngivenVejRepository == null || kommuneRepository == null) {
                System.err.println("Insufficient repositories");
                return;
            }

            List<KommuneEntity> existingList = kommuneRepository.findAll();
            HashMap<Integer, KommuneEntity> kommuneMap = new HashMap<Integer,KommuneEntity>();
            for (KommuneEntity entity : existingList) {
                kommuneMap.put(entity.getKommunekode(), entity);
            }

            List<KommunedelAfNavngivenVejEntity> kmdVejEntities = kommunedelAfNavngivenVejRepository.findAll();
            Level2Container<KommunedelAfNavngivenVejEntity> existingKmdVejMap = new Level2Container<KommunedelAfNavngivenVejEntity>();
            for (KommunedelAfNavngivenVejEntity entity : kmdVejEntities) {
                existingKmdVejMap.put(""+entity.getKommune().getKommunekode(), ""+entity.getVejkode(), entity);
            }


            ArrayList<ArrayList<KommunedelAfNavngivenVejEntity>> delvejEntities = new ArrayList<ArrayList<KommunedelAfNavngivenVejEntity>>();
            ArrayList<ArrayList<NavngivenVejEntity>> navngivenVejEntities = new ArrayList<ArrayList<NavngivenVejEntity>>();

            ArrayList<KommunedelAfNavngivenVejEntity> currentDelvejEntities = new ArrayList<KommunedelAfNavngivenVejEntity>();
            delvejEntities.add(currentDelvejEntities);

            ArrayList<NavngivenVejEntity> currentVejEntities = new ArrayList<NavngivenVejEntity>();
            navngivenVejEntities.add(currentVejEntities);

            Level2Container<AktivVej> aktiveVeje = vrun.getAktiveVeje();
            for (String kommuneKode : aktiveVeje.keySet()) {
                for (String vejKode : aktiveVeje.get(kommuneKode).keySet()) {
                    AktivVej aktivVej = aktiveVeje.get(kommuneKode, vejKode);
                    if (aktivVej != null) {
                        int vKode = Integer.parseInt(vejKode, 10);
                        int kKode = Integer.parseInt(kommuneKode, 10);
                        KommunedelAfNavngivenVejEntity delvejEntity = existingKmdVejMap.get("" + kKode, "" + vKode);
                        boolean changedDelvej = false;

                        if (delvejEntity == null) {
                            delvejEntity = new KommunedelAfNavngivenVejEntity();
                            delvejEntity.setVejkode(vKode);
                            delvejEntity.setKommune(kommuneMap.get(kKode));
                            changedDelvej = true;
                        }

                        created.put(kommuneKode, vejKode, delvejEntity);


                   /* }
                }
            }

            System.out.println("hephey 2");

            for (String kommuneKode : aktiveVeje.keySet()) {
                for (String vejKode : aktiveVeje.get(kommuneKode).keySet()) {
                    AktivVej aktivVej = aktiveVeje.get(kommuneKode, vejKode);
                    if (aktivVej != null) {*/
                        //int kKode = Integer.parseInt(kommuneKode, 10);


                        KommuneEntity kommune = kommuneMap.get(kKode);
                        if (kommune == null) {
                            System.out.println("Kommune with id "+kKode+" ("+kommuneKode+") not found for vej "+aktivVej.get("vejNavn"));
                        } else {

                            //KommunedelAfNavngivenVejEntity delvejEntity = created.get(kommuneKode, vejKode);

                            NavngivenVejEntity navngivenVejEntity = delvejEntity.getNavngivenVej();

                            if (navngivenVejEntity == null) {
                                String fraKommuneKode = aktivVej.get("fraKommuneKode");
                                if (!fraKommuneKode.isEmpty() && !fraKommuneKode.equals("0000")) {
                                    String fraVejKode = aktivVej.get("fraVejKode");
                                    if (!fraVejKode.isEmpty() && !fraVejKode.equals("0000")) {
                                        AktivVej other = aktiveVeje.get(fraKommuneKode, fraVejKode);
                                        if (other != null) {
                                            KommunedelAfNavngivenVejEntity andenVejEntity = created.get(fraKommuneKode, fraVejKode);
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
                                String tilKommuneKode = aktivVej.get("tilKommuneKode");
                                if (!tilKommuneKode.isEmpty() && !tilKommuneKode.equals("0000")) {
                                    String tilVejKode = aktivVej.get("tilVejKode");
                                    if (!tilVejKode.isEmpty() && !tilVejKode.equals("0000")) {
                                        AktivVej other = aktiveVeje.get(tilKommuneKode, tilVejKode);
                                        if (other != null) {
                                            KommunedelAfNavngivenVejEntity andenVejEntity = created.get(tilKommuneKode, tilVejKode);
                                            if (andenVejEntity != null) {
                                                navngivenVejEntity = andenVejEntity.getNavngivenVej();
                                            }
                                        } else {
                                            //System.out.println("    til vej: (" + tilKommuneKode + ", " + tilVejKode + ") doesn't exist");
                                        }
                                    }
                                }
                            }

                            if (navngivenVejEntity == null) {
                                navngivenVejEntity = new NavngivenVejEntity();
                                navngivenVejEntity.setVejnavn(aktivVej.get("vejNavn"));
                                navngivenVejEntity.setVejaddresseringsnavn(aktivVej.get("vejAdresseringsnavn"));
                                navngivenVejEntity.setAnsvarligKommune(kommune);
                                navngivenVejEntity.setNavngivenVejUuid(UUID.randomUUID().toString());

                                if (currentVejEntities.size() >= 10000) {
                                    currentVejEntities = new ArrayList<NavngivenVejEntity>();
                                    navngivenVejEntities.add(currentVejEntities);
                                }

                                currentVejEntities.add(navngivenVejEntity);
                            }


                            if (currentDelvejEntities.size() >= 10000) {
                                currentDelvejEntities = new ArrayList<KommunedelAfNavngivenVejEntity>();
                                delvejEntities.add(currentDelvejEntities);
                            }

                            if (delvejEntity.getNavngivenVej() == null || !delvejEntity.getNavngivenVej().equals(navngivenVejEntity)) {
                                delvejEntity.setNavngivenVej(navngivenVejEntity);
                                changedDelvej = true;
                            }
                            if (changedDelvej) {
                                currentDelvejEntities.add(delvejEntity);
                            }
                        }
                    } else {
                        System.out.println("NULL road at kommuneKode: "+kommuneKode+", vejKode: "+vejKode);
                    }
                }
            }

            System.out.println("Saving NavngivenvejEntities to database");
            int i = 0;
            for (List<NavngivenVejEntity> current : navngivenVejEntities) {
                navngivenVejRepository.save(current);
                navngivenVejRepository.flush();
                i += current.size();
                System.out.println("    saved "+i+" entities");
            }

            System.out.println("Saving KommunedelAfNavngivenVejEntities to database");
            i = 0;
            for (List<KommunedelAfNavngivenVejEntity> current : delvejEntities) {
                kommunedelAfNavngivenVejRepository.save(current);
                kommunedelAfNavngivenVejRepository.flush();
                i += current.size();
                System.out.println("    saved "+i+" entities");
            }

        } catch (ClassCastException e) {}
    }

    protected void processRecord(Record record) {
        if (record.getRecordType() == VejDataRecord.RECORDTYPE_AKTVEJ) {
            AktivVej vej = (AktivVej) record;
            //System.out.println("AktivVej("+vej.get("vejNavn")+")");
            //Registrering registrering = new Registrering();
            //Virkning virkning = new Virkning();
            //Vejnavneomraade vejnavneomraade = new Vejnavneomraade();
            //String navngivenVejUuid = UUID.randomUUID().toString();
            //Kommune kommune = this.findKommune(1234);
            //System.out.println("NavngivenVej("+registrering+", "+virkning+", "+kommune+", "+vejnavneomraade+", "+navngivenVejUuid+")");
            //NavngivenVej vej = new NavngivenVej(registrering, virkning, kommune, vejnavneomraade, navngivenVejUuid);
            // Vi har nu skabt en NavngivenVej-instans. Gem den somehow
        }
    }

    /*private Kommune findKommune(int kommuneKode) {
        //this.getDbObject().
        return null;
    }*/

    public static void main(String[] args) {
        new VejRegister(null).pull();
        System.out.println("Finished");
    }
}
