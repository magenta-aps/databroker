package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.dawa.model.DawaModel;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.register.objectcontainers.EntityModificationCounter;
import dk.magenta.databroker.register.objectcontainers.InputProcessingCounter;
import dk.magenta.databroker.register.objectcontainers.Level1Container;
import dk.magenta.databroker.register.objectcontainers.Level2Container;
import dk.magenta.databroker.cprvejregister.dataproviders.records.CprRecord;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.cprvejregister.model.PostnummerModel;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktVersionEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * Created by lars on 04-11-14.
 */
@Component
public class PostnummerRegister extends CprSubRegister {


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

        private Level1Container<String> postnumre;

        public PostnummerRegisterRun() {
            this.postnumre = new Level1Container<String>();
        }

        public boolean add(Record record) {
            if (record.getClass() == PostNummer.class) {
                int postnr = record.getInt("postNr");
                String navn = record.get("postDistriktTekst");
                if (!this.postnumre.containsKey(postnr)) {
                    this.postnumre.put(postnr, navn);
                }
                return super.add(record);
            }
            return false;
        }

        public Level1Container<String> getPostnumre() {
            return this.postnumre;
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


    /*
    * Data source spec
    * */

    @Override
    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152114/a370712.txt");
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
    private NavngivenVejRepository navngivenVejRepository;



    @Autowired
    private DawaModel model;

    /*
    * Database save
    * */

    protected void saveRunToDatabase(RegisterRun run) {
        this.createRegistreringEntities();

        PostnummerRegisterRun prun = (PostnummerRegisterRun) run;

        if (postnummerRepository == null) {
            System.err.println("Insufficient repositories");
            return;
        }

        Level1Container<PostnummerEntity> postnummerCache;

        System.out.println("Storing PostnummerEntities in database");
/*
        PostnummerModel postnummerModel = new PostnummerModel(this.postnummerRepository, this.getCreateRegistrering(), this.getUpdateRegistrering());
        postnummerCache = postnummerModel.createPostnumre(prun);
*/

        InputProcessingCounter postCounter = new InputProcessingCounter();
        for (String nummer : prun.getPostnumre().keySet()) {
            int nr = Integer.parseInt(nummer, 10);
            String navn = prun.getPostnumre().get(nummer);
            model.setPostNummer(nr, navn, this.getCreateRegistrering(), this.getUpdateRegistrering());
            postCounter.printInputProcessed();
        }
        postCounter.printFinalInputsProcessed();


/*        for (String nummer : postDistrikter.keySet()) {
            String navn = postDistrikter.get(nummer);
            int postNummer = Integer.parseInt(nummer, 10);
            PostnummerEntity postnummerEntity = postnummerRepository.findByNummer(postNummer);
            PostnummerVersionEntity postnummerVersion = null;

            if (postnummerEntity == null) {
                postnummerEntity = PostnummerEntity.create();
                postnummerEntity.setNummer(postNummer);
                postnummerVersion = postnummerEntity.addVersion(this.getCreateRegistrering());
                counter.countCreatedItem();

            } else if (!postnummerEntity.getLatestVersion().getNavn().equals(navn)) {
                postnummerVersion = postnummerEntity.addVersion(this.getUpdateRegistrering());
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
*/
        //createAdgangspunktPostRef(prun, postnummerCache);
    }





    private void createAdgangspunktPostRef(PostnummerRegisterRun prun, Level1Container<PostnummerEntity> postnummerCache) {
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
