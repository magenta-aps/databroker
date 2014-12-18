package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.register.RegisterRun;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
import dk.magenta.databroker.cprvejregister.model.AdresseModel;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.register.records.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by lars on 04-11-14.
 */
@Component
public class LokalitetsRegister extends CprSubRegister {


    /*
    * Inner classes for parsed data
    * */
    public class Lokalitet extends CprRecord {
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

    private class LokalitetRegisterRun extends RegisterRun {
    }


    @Override
    protected RegisterRun createRun() {
        return new LokalitetRegisterRun();
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



    /*
    * Data source spec
    * */

    @Override
    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152108/a370714.txt");
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


    /*
    * Database save
    * */

    protected void saveRunToDatabase(RegisterRun run) {
        this.createRegistreringEntities();
        LokalitetRegisterRun lrun = (LokalitetRegisterRun) run;

        if (adresseRepository == null || husnummerRepository == null || navngivenVejRepository == null || kommunedelAfNavngivenVejRepository == null) {
            System.err.println("Insufficient repositories");
            return;
        }

        System.out.println("Storing HusnummerEntities in database");

        try {

            AdresseModel adresseModel = new AdresseModel(this.adresseRepository, this.navngivenVejRepository, this.husnummerRepository, this.getCreateRegistrering(), this.getUpdateRegistrering());
            ArrayList<Record> lokaliteter = new ArrayList<Record>();

            for (Record record : run) {
                if (record.getRecordType().equals(Lokalitet.RECORDTYPE_LOKALITET)) {
                    lokaliteter.add(record);
                }
            }
            adresseModel.createAddresses(lokaliteter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
