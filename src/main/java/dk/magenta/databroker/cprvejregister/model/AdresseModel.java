package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.register.Model;
import dk.magenta.databroker.register.objectcontainers.EntityModificationCounter;
import dk.magenta.databroker.register.objectcontainers.InputProcessingCounter;
import dk.magenta.databroker.register.objectcontainers.Level2Container;
import dk.magenta.databroker.register.objectcontainers.Level3Container;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseVersionEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejVersionEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 16-12-14.
 */
public class AdresseModel extends Model {

    private AdresseRepository adresseRepository;
    private NavngivenVejRepository navngivenVejRepository;
    private HusnummerRepository husnummerRepository;


    public AdresseModel(AdresseRepository adresseRepository, NavngivenVejRepository navngivenVejRepository, HusnummerRepository husnummerRepository, RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        super(createRegistrering, updateRegistrering);
        this.adresseRepository = adresseRepository;
        this.navngivenVejRepository = navngivenVejRepository;
        this.husnummerRepository = husnummerRepository;
    }

    public void createAddresses(List<Record> records) {
        System.out.println("Creating addresses ("+records.size()+" items)");

        tic();
        System.out.println("Precaching entities");
        Level2Container<NavngivenVejEntity> navngivenVejCache = this.loadNavngivenVejCache();
        Level3Container<HusnummerEntity> husnummerCache = this.loadHusnummerCache();
        Level3Container<AdresseEntity> adresseCache = this.loadAdresseCache();
        System.out.println("Precaching "+ (navngivenVejCache.totalSize() + husnummerCache.totalSize() + adresseCache.totalSize()) +" items took "+toc()+" ms");

        EntityModificationCounter husnummerCounter = new EntityModificationCounter();
        EntityModificationCounter adresseCounter = new EntityModificationCounter();
        InputProcessingCounter inputCounter = new InputProcessingCounter(true);

        for (Record record : records) {
            int kommuneKode = record.getInt("kommuneKode");
            int vejKode = record.getInt("vejKode");
            String husKode = record.get("husNr");
            String etage = record.get("etage");
            String sidedoer = record.get("sidedoer");
            String status = "";
            if (kommuneKode != 0 && vejKode != 0 && husKode != null) {
                this.createAddress(kommuneKode, vejKode, husKode, etage, sidedoer, status, navngivenVejCache, husnummerCache, adresseCache, husnummerCounter, adresseCounter);
                inputCounter.printInputProcessed();
            }
        }

        inputCounter.printFinalInputsProcessed();
        System.out.println("Stored HusnummerEntities to database");
        husnummerCounter.printModifications();
        System.out.println("Stored AdresseEntities to database");
        adresseCounter.printModifications();
    }




    private void createAddress(int kommuneKode, int vejKode, String husKode, String etage, String sidedoer, String status,
                              Level2Container<NavngivenVejEntity> navngivenVejCache, Level3Container<HusnummerEntity> husnummerCache, Level3Container<AdresseEntity> adresseCache,
                              EntityModificationCounter husnummerCounter, EntityModificationCounter adresseCounter) {

        AdresseEntity adresseEntity = null;
        AdgangspunktEntity adgangspunktEntity = null;

        ArrayList<Long> times = new ArrayList<Long>();

        // Create husnummerEntity if needed
        tic();
        boolean createdHusnummer = false;
        boolean updatedHusnummer = false;
        HusnummerEntity husNummerEntity = null;
        if (husnummerCache != null) {
            husNummerEntity = husnummerCache.get(kommuneKode, vejKode, husKode);
        }
        times.add(toc());
        tic();
        /*if (husNummerEntity == null) {
            husNummerEntity = this.husnummerRepository.getByKommunekodeAndVejkodeAndHusnr(kommuneKode, vejKode, husKode);
        }*/
        times.add(toc());
        tic();

        NavngivenVejEntity navngivenVejEntity = null;
        if (husNummerEntity == null) {
            husNummerEntity = HusnummerEntity.create();
            createdHusnummer = true;
            updatedHusnummer = true;
            husNummerEntity.setHusnummerbetegnelse(husKode);
            if (navngivenVejEntity == null && navngivenVejCache != null) {
                navngivenVejEntity = navngivenVejCache.get(kommuneKode, vejKode);
            }
            /*if (navngivenVejEntity == null && this.navngivenVejRepository.count() > 0) {
                navngivenVejEntity = this.navngivenVejRepository.findByKommunekodeAndVejkode(kommuneKode, vejKode);
            }*/
            if (navngivenVejEntity != null) {
                husNummerEntity.setNavngivenVej(navngivenVejEntity);
                if (husnummerCounter != null) {
                    husnummerCounter.countCreatedItem();
                }
                if (navngivenVejCache != null) {
                    navngivenVejCache.put(kommuneKode, vejKode, navngivenVejEntity);
                }
            }
        }
        times.add(toc());
        tic();
        if (husnummerCache != null) {
            husnummerCache.put(kommuneKode, vejKode, husKode, husNummerEntity);
        }
        times.add(toc());
        tic();


        // Create adgangspunktEntity

        if (husNummerEntity != null && !createdHusnummer) {
            adgangspunktEntity = husNummerEntity.getAdgangspunkt();
        }
        times.add(toc());
        tic();

        if (adgangspunktEntity == null) {
            if (navngivenVejEntity == null && husNummerEntity.getNavngivenVej() == null) {
                System.err.println("No navngivenVejEntity for husnummer "+husNummerEntity.getUuid()+" (created: "+createdHusnummer+")");
            } else {
                adgangspunktEntity = AdgangspunktEntity.create();
                adgangspunktEntity.addVersion(this.getCreateRegistrering());
                adgangspunktEntity.setHusnummer(husNummerEntity);
                husNummerEntity.setAdgangspunkt(adgangspunktEntity);
                updatedHusnummer = true;
            }
        }

        times.add(toc());
        tic();
        if (updatedHusnummer) {
            //System.out.println("Saving husnummer "+husNummerEntity.getNavngivenVej().getLatestVersion().getVejnavn()+" "+husNummerEntity.getHusnummerbetegnelse());
            this.husnummerRepository.save(husNummerEntity);
        }

        times.add(toc());
        tic();
        // Create AdresseEntity

        if (husNummerEntity != null && !createdHusnummer) {
            if (adresseEntity == null && adresseCache != null) {
                adresseEntity = adresseCache.get(husNummerEntity.getUuid(), etage, sidedoer);
            }
            /*if (adresseEntity == null) {
                adresseEntity = this.adresseRepository.findByHusnummerAndDoerbetegnelseAndEtagebetegnelse(husNummerEntity, sidedoer, etage);
            }*/
        }

        times.add(toc());
        tic();

        AdresseVersionEntity adresseVersion = null;
        if (adresseEntity == null) {
            adresseEntity = AdresseEntity.create();
            adresseEntity.setHusnummer(husNummerEntity);
            adresseVersion = adresseEntity.addVersion(this.getCreateRegistrering());
            if (adresseCounter != null) {
                adresseCounter.countCreatedItem();
            }
        } else {
            AdresseVersionEntity latestVersion = adresseEntity.getLatestVersion();
            if (!(
                    etage.equals(latestVersion.getEtageBetegnelse()) &&
                            sidedoer.equals(latestVersion.getDoerBetegnelse()) &&
                            status.equals(latestVersion.getStatus())
            )) {
                adresseVersion = adresseEntity.addVersion(this.getUpdateRegistrering());
                if (adresseCounter != null) {
                    adresseCounter.countUpdatedItem();
                }
            }
        }
        times.add(toc());
        tic();
        adresseCache.put(husNummerEntity.getUuid(), etage, sidedoer, adresseEntity);

        times.add(toc());
        tic();
        if (adresseVersion != null) {
            adresseVersion.setEtageBetegnelse(etage);
            adresseVersion.setDoerBetegnelse(sidedoer);
            adresseVersion.setStatus(status);
            /*System.out.println("Saving adresse "+
                    adresseEntity.getHusnummer().getNavngivenVej().getLatestVersion().getVejnavn()+" "+
                    adresseEntity.getHusnummer().getHusnummerbetegnelse()+" "+
                    adresseVersion.getEtageBetegnelse()+" "+
                    adresseVersion.getDoerBetegnelse());*/
            this.adresseRepository.save(adresseEntity);
        }
        times.add(toc());
        StringBuilder sb = new StringBuilder();
        for (long t : times) {
            sb.append(t);
            sb.append(",");
        }
        //System.out.println(sb.toString());
    }





    private Level2Container<NavngivenVejEntity> loadNavngivenVejCache() {
        Level2Container<NavngivenVejEntity> navngivenVejCache = new Level2Container<NavngivenVejEntity>();
        Collection<NavngivenVejEntity> navngivenVejList = this.navngivenVejRepository.findAll();
        for (NavngivenVejEntity navngivenVejEntity : navngivenVejList) {
            NavngivenVejVersionEntity navngivenVejVersionEntity = navngivenVejEntity.getLatestVersion();
            for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : navngivenVejVersionEntity.getKommunedeleAfNavngivenVej()) {
                if (kommunedelAfNavngivenVejEntity.getUuid() != null) {
                    int kommuneKode = kommunedelAfNavngivenVejEntity.getKommune().getKommunekode();
                    int vejKode = kommunedelAfNavngivenVejEntity.getVejkode();
                    navngivenVejCache.put(kommuneKode, vejKode, navngivenVejEntity);
                }
            }
        }
        return navngivenVejCache;
    }

    private Level3Container<HusnummerEntity> loadHusnummerCache() {
        Level3Container<HusnummerEntity> husnummerCache = new Level3Container<HusnummerEntity>();
        Collection<HusnummerEntity> husnummerList = this.husnummerRepository.findAll();
        for (HusnummerEntity husnummerEntity : husnummerList) {
            NavngivenVejVersionEntity navngivenVejVersionEntity = husnummerEntity.getNavngivenVej().getLatestVersion();
            for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : navngivenVejVersionEntity.getKommunedeleAfNavngivenVej()) {
                husnummerCache.put(kommunedelAfNavngivenVejEntity.getKommune().getKommunekode(), kommunedelAfNavngivenVejEntity.getVejkode(), husnummerEntity.getHusnummerbetegnelse(), husnummerEntity);
            }
        }
        return husnummerCache;
    }

    private Level3Container<AdresseEntity> loadAdresseCache() {
        Level3Container<AdresseEntity> adresseCache = new Level3Container<AdresseEntity>();
        Collection<AdresseEntity> adresseList = this.adresseRepository.findAll();
        for (AdresseEntity adresseEntity : adresseList) {
            AdresseVersionEntity adresseVersionEntity = adresseEntity.getLatestVersion();
            adresseCache.put(adresseEntity.getHusnummer().getUuid(), adresseVersionEntity.getEtageBetegnelse(), adresseVersionEntity.getDoerBetegnelse(), adresseEntity);
        }
        return adresseCache;
    }

}
