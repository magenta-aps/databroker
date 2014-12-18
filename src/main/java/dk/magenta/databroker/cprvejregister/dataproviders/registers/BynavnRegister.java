package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.CprRecord;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.UUID;

/**
 * Created by lars on 04-11-14.
 */
public class BynavnRegister extends CprRegister {

    public class ByNavn extends CprRecord {
        public static final String RECORDTYPE_BYNAVN = "001";
        public String getRecordType() {
            return RECORDTYPE_BYNAVN;
        }
        public ByNavn(String line) throws ParseException {
            super(line);
            this.obtain("kommuneKode", 4, 4);
            this.obtain("vejKode", 8, 4);
            this.obtain("myndighedsNavn", 12, 20);
            this.obtain("vejadresseringsNavn", 32, 20);
            this.obtain("husNrFra", 52, 4);
            this.obtain("husNrTil", 56, 4);
            this.obtain("ligeUlige", 60, 1);
            this.obtain("byNavn", 61, 34);
        }
    }

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152120/a370713.txt");
    }

    protected CprRecord parseTrimmedLine(String recordType, String line) {
        CprRecord r = super.parseTrimmedLine(recordType, line);
        if (r != null) {
            return r;
        }
        try {
            if (recordType.equals(ByNavn.RECORDTYPE_BYNAVN)) {
                return new ByNavn(line);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        DataProviderEntity dp = new DataProviderEntity();
        dp.setUuid(UUID.randomUUID().toString());
        new BynavnRegister().pull(dp);
    }
}
