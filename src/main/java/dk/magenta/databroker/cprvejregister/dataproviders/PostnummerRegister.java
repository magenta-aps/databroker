package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.cprvejregister.dataproviders.records.PostNummer;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class PostnummerRegister extends CprRegister {

    public PostnummerRegister(dk.magenta.databroker.core.model.DataProvider dbObject) {
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
