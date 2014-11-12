package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.*;
import dk.magenta.databroker.cprvejregister.model.AdresseEntity;
import dk.magenta.databroker.cprvejregister.model.HusnummerEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

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

    protected void saveRunToDatabase(RegisterRun run) {

        try {
            KommuneContainer created = new KommuneContainer();
            for (Record record : run.getAll()) {
                if (record.getRecordType().equals(Lokalitet.RECORDTYPE_LOKALITET)) {
                    Lokalitet lokalitet = (Lokalitet) record;
                    String kommuneKode = lokalitet.get("kommuneKode");
                    String vejKode = lokalitet.get("vejKode");
                    String husKode = lokalitet.get("husNr");

                    HusnummerEntity husNummerEntity = created.get(kommuneKode, vejKode, husKode);
                    if (husNummerEntity == null) {
                        husNummerEntity = new HusnummerEntity();
                        husNummerEntity.setHusnummerbetegnelse(husKode);
                        // find NavngivenVej med kommuneKode og vejKode i databasen
                        // husNummerEntity.setNavngivenVej();
                        created.put(kommuneKode, vejKode, husKode, husNummerEntity);
                    }
                    AdresseEntity adresseEntity = new AdresseEntity();
                    adresseEntity.setDoerbetegnelse(lokalitet.get("sidedoer"));
                    adresseEntity.setEtagebetegnelse(lokalitet.get("etage"));
                    adresseEntity.setHusnummer(husNummerEntity);
                    // Gem adresseEntity i databasen
                }
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        LokalitetsRegister register = new LokalitetsRegister(null);
        register.pull();
        System.out.println("Finished");
    }
}
