package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.*;
import dk.magenta.databroker.cprvejregister.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

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
            this.put("kommuneKode", substr(line, 4, 4));
            this.put("vejKode", substr(line, 8, 4));
            this.put("myndighedsNavn", substr(line, 12, 20));
            this.put("vejadresseringsNavn", substr(line, 32, 20));
            this.put("husNr", substr(line, 52, 4));
            this.put("etage", substr(line, 56, 2));
            this.put("sidedoer", substr(line, 58, 4));
            this.put("lokalitet", substr(line, 62, 34));
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

        //SegmentedList<HusnummerEntity> husnummerEntities = new SegmentedList<HusnummerEntity>(10000);
        //SegmentedList<AdresseEntity> adresseEntities = new SegmentedList<AdresseEntity>(10000);

/*
        Level2Container<NavngivenVejEntity> navngivenVejEntities = new Level2Container<NavngivenVejEntity>();
        List<NavngivenVejEntity> navngivenVejList = navngivenVejRepository.findAll();
        for (NavngivenVejEntity n : navngivenVejList) {

            Collection<KommunedelAfNavngivenVejEntity> dele = n.getKommunedeleAfNavngivenVej();
            for (KommunedelAfNavngivenVejEntity del : dele) {
                navngivenVejEntities.put(""+del.getKommune().getKommunekode(), ""+del.getVejkode(), n);
            }
        }*/

        Pattern leadingZero = Pattern.compile("^0+");
        try {
            for (Record record : run.getAll()) {
                if (record.getRecordType().equals(Lokalitet.RECORDTYPE_LOKALITET)) {
                    Lokalitet lokalitet = (Lokalitet) record;
                    int kommuneKode = Integer.parseInt(lokalitet.get("kommuneKode"), 10);
                    int vejKode = Integer.parseInt(lokalitet.get("vejKode"), 10);
                    String husKode = leadingZero.matcher(lokalitet.get("husNr")).replaceFirst("");
                    String sidedoer = lokalitet.get("sidedoer");
                    String etage = lokalitet.get("etage");
                    String status = "hephey";

/*
                    List<HusnummerEntity> husNummerEntities = husnummerRepository.findByKommunekodeAndVejkodeAndHusnr(kommuneKode, vejKode, husKode);
                    HusnummerEntity husNummerEntity = husNummerEntities.size()>0 ? husNummerEntities.get(0) : null;
*/

                    HusnummerEntity husNummerEntity = null;
                    AdresseEntity adresseEntity = null;

                    if (husNummerEntity == null) {
                        husNummerEntity = husnummerRepository.findFirstByKommunekodeAndVejkodeAndHusnr(kommuneKode, vejKode, husKode);
                        adresseEntity = adresseRepository.findByHusnummerAndDoerbetegnelseAndEtagebetegnelse(husNummerEntity, sidedoer, etage);
                    }

                    boolean updateHusnummerEntity = false;
                    if (husNummerEntity == null) {
                        //System.out.println("create new");
                        husNummerEntity = new HusnummerEntity();
                        husNummerEntity.setHusnummerUuid(UUID.randomUUID().toString());
                        updateHusnummerEntity = true;
                        this.countCreatedItem();
                    }
                    if (!husKode.equals(husNummerEntity.getHusnummerbetegnelse())) {
                        husNummerEntity.setHusnummerbetegnelse(husKode);
                        if (!updateHusnummerEntity) {
                            this.countUpdatedItem();
                        }
                        updateHusnummerEntity = true;
                    }
                    NavngivenVejEntity navngivenVejEntity = navngivenVejRepository.findByKommunekodeAndVejkode(kommuneKode, vejKode);
                    if (husNummerEntity.getNavngivenVej() != navngivenVejEntity) {
                        husNummerEntity.setNavngivenVej(navngivenVejEntity);
                        if (!updateHusnummerEntity) {
                            this.countUpdatedItem();
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
                        updateAdresseEntity = true;
                    }
                    if (!sidedoer.equals(adresseEntity.getDoerbetegnelse())) {
                        adresseEntity.setDoerbetegnelse(sidedoer);
                        updateAdresseEntity = true;
                    }
                    if (!etage.equals(adresseEntity.getEtagebetegnelse())) {
                        adresseEntity.setEtagebetegnelse(etage);
                        updateAdresseEntity = true;
                    }
                    if (adresseEntity.getHusnummer() != husNummerEntity) {
                        adresseEntity.setHusnummer(husNummerEntity);
                        updateAdresseEntity = true;
                    }
                    if (!status.equals(adresseEntity.getStatus())) {
                        adresseEntity.setStatus(status);
                        updateAdresseEntity = true;
                    }
                    if (updateAdresseEntity) {
                        //adresseEntities.addItem(adresseEntity);
                        adresseRepository.saveAndFlush(adresseEntity);
                    }

                    this.printInputProcessed();
                }
            }
            this.printFinalInputsProcessed();
            System.out.println("Stored AdresseEntities to database");
            this.printModifications();

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
