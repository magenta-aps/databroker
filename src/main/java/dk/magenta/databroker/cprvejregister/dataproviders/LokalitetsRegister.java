package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.*;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
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
public class LokalitetsRegister extends CprRegister {


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

    private class KommuneContainer extends Level3Container<HusnummerEntity> {
    }

    public LokalitetsRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152108/a370714.txt");
    }

    protected String getEncoding() {
        return "ISO-8859-1";
    }

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

    protected void saveRunToDatabase(RegisterRun run, Map<String, JpaRepository> repositories) {

        AdresseRepository adresseRepository = (AdresseRepository) repositories.get("adresseRepository");
        HusnummerRepository husnummerRepository = (HusnummerRepository) repositories.get("husnummerRepository");
        NavngivenVejRepository navngivenVejRepository = (NavngivenVejRepository) repositories.get("navngivenVejRepository");
        KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository = (KommunedelAfNavngivenVejRepository) repositories.get("kommunedelAfNavngivenVejRepository");

        if (adresseRepository == null || husnummerRepository == null || navngivenVejRepository == null || kommunedelAfNavngivenVejRepository == null) {
            System.err.println("Insufficient repositories");
            return;
        }

        System.out.println("Storing HusnummerEntities in database");

        EntityModificationCounter husnummerCounter = new EntityModificationCounter();
        EntityModificationCounter adresseCounter = new EntityModificationCounter();

        //SegmentedList<HusnummerEntity> husnummerEntities = new SegmentedList<HusnummerEntity>(10000);
        //SegmentedList<AdresseEntity> adresseEntities = new SegmentedList<AdresseEntity>(10000);

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
            this.startInputProcessing();
            for (Record record : run) {
                if (record.getRecordType().equals(Lokalitet.RECORDTYPE_LOKALITET)) {
                    Lokalitet lokalitet = (Lokalitet) record;
                    int kommuneKode = lokalitet.getInt("kommuneKode");
                    int vejKode = lokalitet.getInt("vejKode");
                    String husKode = lokalitet.get("husNr");
                    String sidedoer = lokalitet.get("sidedoer");
                    String etage = lokalitet.get("etage");
                    String status = "hephey";

                    HusnummerEntity husNummerEntity = husnummerRepository.findFirstByKommunekodeAndVejkodeAndHusnr(kommuneKode, vejKode, husKode);
                    AdresseEntity adresseEntity = adresseRepository.findByHusnummerAndDoerbetegnelseAndEtagebetegnelse(husNummerEntity, sidedoer, etage);

                    boolean updateHusnummerEntity = false;
                    if (husNummerEntity == null) {
                        husNummerEntity = new HusnummerEntity();
                        husNummerEntity.setHusnummerUuid(UUID.randomUUID().toString());
                        updateHusnummerEntity = true;
                        husnummerCounter.countCreatedItem();
                    }
                    if (!husKode.equals(husNummerEntity.getHusnummerbetegnelse())) {
                        husNummerEntity.setHusnummerbetegnelse(husKode);
                        if (!updateHusnummerEntity) {
                            husnummerCounter.countUpdatedItem();
                        }
                        updateHusnummerEntity = true;
                    }
                    NavngivenVejEntity navngivenVejEntity = navngivenVejRepository.findByKommunekodeAndVejkode(kommuneKode, vejKode);
                    if (husNummerEntity.getNavngivenVej() == null || husNummerEntity.getNavngivenVej().getId() != navngivenVejEntity.getId()) {
                        husNummerEntity.setNavngivenVej(navngivenVejEntity);
                        if (!updateHusnummerEntity) {
                            husnummerCounter.countUpdatedItem();
                        }
                        updateHusnummerEntity = true;
                    }
                    if (updateHusnummerEntity) {
                        husnummerRepository.saveAndFlush(husNummerEntity);
                    }


                    boolean updateAdresseEntity = false;
                    if (adresseEntity == null) {
                        adresseEntity = new AdresseEntity();
                        adresseEntity.setAdresseUuid(UUID.randomUUID().toString());
                        adresseCounter.countCreatedItem();
                        updateAdresseEntity = true;
                    }
                    if (!sidedoer.equals(adresseEntity.getDoerbetegnelse())) {
                        adresseEntity.setDoerbetegnelse(sidedoer);
                        if (!updateAdresseEntity) {
                            adresseCounter.countUpdatedItem();
                            updateAdresseEntity = true;
                        }
                    }
                    if (!etage.equals(adresseEntity.getEtagebetegnelse())) {
                        adresseEntity.setEtagebetegnelse(etage);
                        if (!updateAdresseEntity) {
                            adresseCounter.countUpdatedItem();
                            updateAdresseEntity = true;
                        }
                    }
                    if (adresseEntity.getHusnummer() == null || husNummerEntity.getId() != adresseEntity.getHusnummer().getId()) {
                        adresseEntity.setHusnummer(husNummerEntity);
                        if (!updateAdresseEntity) {
                            adresseCounter.countUpdatedItem();
                            updateAdresseEntity = true;
                        }
                    }
                    if (!status.equals(adresseEntity.getStatus())) {
                        adresseEntity.setStatus(status);
                        if (!updateAdresseEntity) {
                            adresseCounter.countUpdatedItem();
                            updateAdresseEntity = true;
                        }
                    }
                    if (updateAdresseEntity) {
                        //adresseEntities.addItem(adresseEntity);
                        adresseRepository.saveAndFlush(adresseEntity);
                    }

                    this.printInputProcessed();
                }
            }
            this.printFinalInputsProcessed();
            System.out.println("Stored HusnummerEntities to database");
            husnummerCounter.printModifications();
            System.out.println("Stored AdresseEntities to database");
            adresseCounter.printModifications();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        LokalitetsRegister register = new LokalitetsRegister(null);
        register.pull();
        System.out.println("Finished");
    }
}
