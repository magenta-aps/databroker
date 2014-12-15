package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.cprvejregister.dataproviders.RegisterRun;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.Level1Container;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.Level2Container;
import dk.magenta.databroker.cprvejregister.dataproviders.records.CprRecord;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktVersionEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerVersionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * Created by lars on 04-11-14.
 */
@Component
public class PostnummerRegister extends CprRegister {


    /*
    * Inner classes for parsed data
    * */

     public class PostNummer extends CprRecord {
        public static final String RECORDTYPE_POSTNUMMER = "001";
        public String getRecordType() {
            return RECORDTYPE_POSTNUMMER;
        }
        public PostNummer(String line) throws ParseException {
            super(line);
            this.obtain("kommuneKode", 4, 4);
            this.obtain("vejKode", 8, 4);
            this.obtain("myndighedsNavn", 12, 20);
            this.obtain("vejadresseringsNavn", 32, 20);
            this.obtain("husNrFra", 52, 4);
            this.obtain("husNrTil", 56, 4);
            this.obtain("ligeUlige", 60, 1);
            this.obtain("postNr", 61, 4);
            this.obtain("postDistriktTekst", 65, 20);
        }
    }

    /*
    * RegisterRun inner class
    * */

    public class PostnummerRegisterRun extends RegisterRun {

        private HashMap<String, String> postdistrikter;

        public PostnummerRegisterRun() {
            super();
            this.postdistrikter = new HashMap<String, String>();
        }

        public boolean add(CprRecord record) {
            if (record.getRecordType().equals(PostNummer.RECORDTYPE_POSTNUMMER)) {
                PostNummer postnummer = (PostNummer) record;
                String nummer = postnummer.get("postNr");
                if (nummer != null) {
                    this.postdistrikter.put(nummer, postnummer.get("postDistriktTekst"));
                }
                return super.add(postnummer);
            }
            return false;
        }

        public HashMap<String, String> getPostdistrikter() {
            return this.postdistrikter;
        }
    }

    protected RegisterRun createRun() {
        return new PostnummerRegisterRun();
    }

    //------------------------------------------------------------------------------------------------------------------

    /*
    * Constructors
    * */

    public PostnummerRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public PostnummerRegister() {
    }

    @PostConstruct
    public void PostConstructPostnummerRegister() {
        DataProviderEntity postProvider = new DataProviderEntity();
        postProvider.setUuid(UUID.randomUUID().toString());

        this.setDataProviderEntity(postProvider);
    }


    /*
    * Data source spec
    * */

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152114/a370712.txt");
    }

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
            if (recordType.equals(PostNummer.RECORDTYPE_POSTNUMMER)) {
                return new PostNummer(line);
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
    private PostnummerRepository postnummerRepository;
    public PostnummerRepository getPostnummerRepository() {
        return postnummerRepository;
    }

    @Autowired
    private RegistreringRepository registreringRepository;

    @Autowired
    private NavngivenVejRepository navngivenVejRepository;


    /*
    * Registration
    * */

    // RegistreringEntities that must be attached to all versioned data entities
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
        this.createRegistreringEntities();

        PostnummerRegisterRun prun = (PostnummerRegisterRun) run;

        EntityModificationCounter counter = new EntityModificationCounter();

        if (postnummerRepository == null) {
            System.err.println("Insufficient repositories");
            return;
        }

        Level1Container<PostnummerEntity> postnummerCache = new Level1Container<PostnummerEntity>();

        System.out.println("Storing PostnummerEntities in database");

        Map<String, String> postDistrikter = prun.getPostdistrikter();
        for (String nummer : postDistrikter.keySet()) {
            String navn = postDistrikter.get(nummer);
            int postNummer = Integer.parseInt(nummer, 10);
            PostnummerEntity postnummerEntity = postnummerRepository.findByNummer(postNummer);
            PostnummerVersionEntity postnummerVersion = null;

            if (postnummerEntity == null) {
                postnummerEntity = PostnummerEntity.create();
                postnummerEntity.setNummer(postNummer);
                postnummerVersion = postnummerEntity.addVersion(createRegistrering);
                counter.countCreatedItem();

            } else if (!postnummerEntity.getLatestVersion().getNavn().equals(navn)) {
                postnummerVersion = postnummerEntity.addVersion(updateRegistrering);
                counter.countUpdatedItem();
            }

            postnummerCache.put(postNummer, postnummerEntity);

            if (postnummerVersion != null) {
                postnummerVersion.setNavn(navn);
                postnummerRepository.save(postnummerEntity);
            }
            prun.printInputProcessed();
        }
        prun.printFinalInputsProcessed();
        System.out.println("Stored PostnummerEntities in database:");
        counter.printModifications();



        Level2Container<NavngivenVejEntity> navngivenVejCache = new Level2Container<NavngivenVejEntity>();
        Collection<NavngivenVejEntity> navngivenVejEntities = this.navngivenVejRepository.findAll();
        for (NavngivenVejEntity navngivenVejEntity : navngivenVejEntities) {
            for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : navngivenVejEntity.getLatestVersion().getKommunedeleAfNavngivenVej()) {
                navngivenVejCache.put(kommunedelAfNavngivenVejEntity.getKommune().getKommunekode(), kommunedelAfNavngivenVejEntity.getVejkode(), navngivenVejEntity);
            }
        }

        for (Record record : prun) {
            try {
                PostNummer postnummerRecord = (PostNummer) record;
                int postnummer = postnummerRecord.getInt("postNr");
                PostnummerEntity postnummerEntity = postnummerCache.get(postnummer);
                if (postnummerEntity != null) {
                    NavngivenVejEntity navngivenVejEntity = navngivenVejCache.get(postnummerRecord.getInt("kommuneKode"), postnummerRecord.getInt("vejKode"));
                    if (navngivenVejEntity != null) {
                        //System.out.println("Found: ("+postnummerRecord.getInt("kommuneKode")+":"+postnummerRecord.getInt("vejKode")+") = "+navngivenVejEntity.getLatestVersion().getVejnavn());
                        Collection<HusnummerEntity> husnumre = navngivenVejEntity.getHusnumre();

                        int husNrFra = postnummerRecord.getInt("husNrFra");
                        int husNrTil = postnummerRecord.getInt("husNrTil");
                        boolean husNrLige = postnummerRecord.get("ligeUlige").equals("L");


                        for (HusnummerEntity husnummerEntity : husnumre) {
                            String husnummerBetegnelse = husnummerEntity.getHusnummerbetegnelse().replaceAll("[^\\d]", "");
                            if (!husnummerBetegnelse.isEmpty()) {
                                //System.out.println(navngivenVejEntity.getLatestVersion().getVejnavn() + " " + husnummerEntity.getHusnummerbetegnelse());
                                int nummer = Integer.parseInt(husnummerBetegnelse, 10);
                                //System.out.println(husnummerEntity.getId() + " / " + nummer + " vs " + husNrFra + "-" + husNrTil + "(" + husNrLige + ")");
                                if (
                                        (nummer % 2 == 0) == husNrLige &&
                                                nummer >= husNrFra &&
                                                nummer <= husNrTil
                                        ) {
                                    if (husnummerEntity != null) {
                                        AdgangspunktEntity adgangspunktEntity = husnummerEntity.getAdgangspunkt();
                                        if (adgangspunktEntity != null) {
                                            AdgangspunktVersionEntity adgangspunktVersionEntity = adgangspunktEntity.getLatestVersion();
                                            if (adgangspunktVersionEntity != null) {
                                                adgangspunktVersionEntity.setLiggerIPostnummer(postnummerEntity); // TODO: gør noget ved brugen af "latest"
                                            } else {
                                                System.out.println(navngivenVejEntity.getLatestVersion().getVejnavn() + " " + husnummerEntity.getHusnummerbetegnelse());
                                                System.out.println("adgangspunktVersionEntity is null");
                                            }
                                        } else {
                                            System.out.println(navngivenVejEntity.getLatestVersion().getVejnavn() + " " + husnummerEntity.getHusnummerbetegnelse());
                                            System.out.println("adgangspunktEntity is null");
                                        }
                                    } else {
                                        System.out.println(navngivenVejEntity.getLatestVersion().getVejnavn() + " " + husnummerEntity.getHusnummerbetegnelse());
                                        System.out.println("husnummerEntity is null");
                                    }
                                }
                            }
                        }
                        navngivenVejRepository.save(navngivenVejEntity);
                    }
                }
            } catch (ClassCastException e) {}
        }
    }
}
