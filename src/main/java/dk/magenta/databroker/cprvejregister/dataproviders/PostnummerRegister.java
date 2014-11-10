package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class PostnummerRegister extends CprRegister {


    public class PostNummer extends Record {
        public static final String RECORDTYPE_POSTNUMMER = "001";
        public String getRecordType() {
            return RECORDTYPE_POSTNUMMER;
        }
        public PostNummer(String line) throws ParseException {
            super(line);
            this.put("kommuneKode", substr(line, 4, 4));
            this.put("vejKode", substr(line, 8, 4));
            this.put("myndighedsNavn", substr(line, 12, 20));
            this.put("vejadresseringsNavn", substr(line, 32, 20));
            this.put("husNrFra", substr(line, 52, 4));
            this.put("husNrTil", substr(line, 56, 4));
            this.put("ligeUlige", substr(line, 60, 1));
            this.put("postNr", substr(line, 61, 4));
            this.put("postDistriktTekst", substr(line, 65, 20));
        }
    }

    public PostnummerRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152114/a370712.txt");
    }

    protected Record parseTrimmedLine(String recordType, String line) {
        Record r = super.parseTrimmedLine(recordType, line);
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

    public static void main(String[] args) {
        new PostnummerRegister(null).pull();
    }
}
