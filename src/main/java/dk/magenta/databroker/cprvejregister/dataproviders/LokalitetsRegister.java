package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.Level2Container;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.Level3Container;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktRepository;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseVersionEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
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
public class LokalitetsRegister extends CprRegister {


    /*
    * Inner classes for parsed data
    * */
    public class Lokalitet extends Record {
        public static final String RECORDTYPE_LOKALITET = "001";
        public String getRecordType() {
            return RECORDTYPE_LOKALITET;
        }
        public Lokalitet(String line) throws ParseException {
            super(line);
            this.obtain("kommuneKode", 4, 4);
            this.obtain("vejKode", 8, 4);
            this.obtain("myndighedsNavn", 12, 20);
            this.obtain("vejadresseringsNavn", 32, 20);
            this.obtain("husNr", 52, 4);
            this.obtain("etage", 56, 2);
            this.obtain("sidedoer", 58, 4);
            this.obtain("lokalitet", 62, 34);
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    /*
    * Constructors
    * */

    public LokalitetsRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public LokalitetsRegister() {
    }

    @PostConstruct
    public void PostConstructLokalitetsRegister() {
        DataProviderEntity lokalitetsProvider = new DataProviderEntity();
        lokalitetsProvider.setUuid(UUID.randomUUID().toString());

        this.setDataProviderEntity(lokalitetsProvider);
    }


    /*
    * Data source spec
    * */

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152108/a370714.txt");
    }

    protected String getEncoding() {
        return "ISO-8859-1";
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
            if (recordType.equals(Lokalitet.RECORDTYPE_LOKALITET)) {
                return new Lokalitet(line);
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
    private AdresseRepository adresseRepository;

    @Autowired
    private HusnummerRepository husnummerRepository;

    @Autowired
    private NavngivenVejRepository navngivenVejRepository;

    @Autowired
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;

    @Autowired
    private RegistreringRepository registreringRepository;

    @Autowired
    private AdgangspunktRepository adgangspunktRepository;


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

        if (adresseRepository == null || husnummerRepository == null || navngivenVejRepository == null || kommunedelAfNavngivenVejRepository == null) {
            System.err.println("Insufficient repositories");
            return;
        }

        System.out.println("Storing HusnummerEntities in database");

        EntityModificationCounter husnummerCounter = new EntityModificationCounter();
        EntityModificationCounter adresseCounter = new EntityModificationCounter();


/*
        Level2Container<NavngivenVejEntity> navngivenVejEntities = new Level2Container<NavngivenVejEntity>();
        List<NavngivenVejEntity> navngivenVejList = navngivenVejRepository.findAll();
        for (NavngivenVejEntity n : navngivenVejList) {

            Collection<KommunedelAfNavngivenVejEntity> dele = n.getKommunedeleAfNavngivenVej();
            for (KommunedelAfNavngivenVejEntity del : dele) {
                navngivenVejEntities.add(""+del.getKommune().getKommunekode(), ""+del.getVejkode(), n);
            }
        }*/



        try {
            run.startInputProcessing();

            int count = 0;
            for (Record record : run) {
                if (record.getRecordType().equals(Lokalitet.RECORDTYPE_LOKALITET)) {
                    count++;
                }
            }
            System.out.println("There are "+count+" records");

            Level2Container<NavngivenVejEntity> navngivenVejCache = new Level2Container<NavngivenVejEntity>();
            Level3Container<HusnummerEntity> husnummerCache = new Level3Container<HusnummerEntity>();
            Level3Container<AdresseEntity> adresseCache = new Level3Container<AdresseEntity>();

            //int limit = 1000;


            for (Record record : run) {
                if (record.getRecordType().equals(Lokalitet.RECORDTYPE_LOKALITET)) {

                    //if (limit-- <= 0) break;

                    ArrayList<Long> times = new ArrayList<Long>();

                    Lokalitet lokalitet = (Lokalitet) record;
                    int kommuneKode = lokalitet.getInt("kommuneKode");
                    int vejKode = lokalitet.getInt("vejKode");
                    String husKode = lokalitet.get("husNr");
                    String sidedoer = lokalitet.get("sidedoer");
                    String etage = lokalitet.get("etage");
                    String status = "hephey";

                    AdresseEntity adresseEntity = null;
                    AdgangspunktEntity adgangspunktEntity = null;

                    tic();
                    HusnummerEntity husNummerEntity = husnummerCache.get(kommuneKode, vejKode, husKode);
                    if (husNummerEntity == null) {
                        husNummerEntity = husnummerRepository.getByKommunekodeAndVejkodeAndHusnr(kommuneKode, vejKode, husKode);
                    }
                    times.add(toc());



                    tic();
                    if (husNummerEntity == null) {
                        husNummerEntity = HusnummerEntity.create();
                        husNummerEntity.setHusnummerbetegnelse(husKode);
                        NavngivenVejEntity navngivenVejEntity = navngivenVejCache.get(kommuneKode, vejKode);
                        if (navngivenVejEntity == null) {
                            navngivenVejEntity = navngivenVejRepository.findByKommunekodeAndVejkode(kommuneKode, vejKode);
                        }
                        if (navngivenVejEntity != null) {
                            husNummerEntity.setNavngivenVej(navngivenVejEntity);
                            husnummerCounter.countCreatedItem();
                            husnummerRepository.save(husNummerEntity);
                            navngivenVejCache.put(kommuneKode, vejKode, navngivenVejEntity);
                        }




                    } else {
                        //adresseEntity = adresseCache.get(husNummerEntity.getUuid(), sidedoer, etage);
                        if (adresseEntity == null) {
                            adresseEntity = adresseRepository.findByHusnummerAndDoerbetegnelseAndEtagebetegnelse(husNummerEntity, sidedoer, etage);
                        }

                        adgangspunktEntity = husNummerEntity.getAdgangspunkt();
                    }

                    if (adgangspunktEntity == null) {
                        adgangspunktEntity = AdgangspunktEntity.create();
                        adgangspunktEntity.addVersion(createRegistrering);
                        adgangspunktEntity.setHusnummer(husNummerEntity);
                        husNummerEntity.setAdgangspunkt(adgangspunktEntity);
                        husnummerRepository.save(husNummerEntity);
                    }



                    husnummerCache.put(kommuneKode, vejKode, husKode, husNummerEntity);
                    times.add(toc());



                    tic();
                    AdresseVersionEntity adresseVersion = null;
                    if (adresseEntity == null) {
                        adresseEntity = AdresseEntity.create();
                        adresseEntity.setHusnummer(husNummerEntity);
                        adresseVersion = adresseEntity.addVersion(createRegistrering);
                        adresseCounter.countCreatedItem();
                    } else {
                        AdresseVersionEntity latestVersion = adresseEntity.getLatestVersion();
                        if (!(
                                etage.equals(latestVersion.getEtageBetegnelse()) &&
                                sidedoer.equals(latestVersion.getDoerBetegnelse()) &&
                                status.equals(latestVersion.getStatus())
                            )) {
                            adresseVersion = adresseEntity.addVersion(updateRegistrering);
                            adresseCounter.countUpdatedItem();
                        }
                    }
                    //adresseCache.put(husNummerEntity.getUuid(), sidedoer, etage, adresseEntity);
                    times.add(toc());



                    tic();
                    if (adresseVersion != null) {
                        adresseVersion.setEtageBetegnelse(etage);
                        adresseVersion.setDoerBetegnelse(sidedoer);
                        adresseVersion.setStatus(status);
                        adresseRepository.save(adresseEntity);
                    }
                    /*if (adgangspunktEntity != null) {
                        this.adgangspunktRepository.save(adgangspunktEntity);
                    }*/


                    times.add(toc());
/*
                    StringBuilder timeStr = new StringBuilder();
                    for (long time : times) {
                        timeStr.append(time);
                        timeStr.append(",");
                    }
                    System.out.println(timeStr.toString());
*/
                    run.printInputProcessed();
                }

            }
            run.printFinalInputsProcessed();
            System.out.println("Stored HusnummerEntities to database");
            husnummerCounter.printModifications();
            System.out.println("Stored AdresseEntities to database");
            adresseCounter.printModifications();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
