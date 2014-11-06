package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.cprvejregister.dataproviders.records.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class MyndighedsRegister extends CprRegister {

    public MyndighedsRegister(dk.magenta.databroker.core.model.DataProvider dbObject) {
        super(dbObject);
    }

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/219468/a370716.txt");
    }

    protected Record parseTrimmedLine(String recordType, String line) {
        Record r = super.parseTrimmedLine(recordType, line);
        if (r != null) {
            return r;
        }
        try {
            if (recordType.equals(MyndighedsDataRecord.RECORDTYPE_MYNDIGHED)) {
                return new Myndighed(line);
            }
            if (recordType.equals(MyndighedsDataRecord.RECORDTYPE_KOMMUNEREL)) {
                return new KommuneRelation(line);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new MyndighedsRegister(null).pull();
    }

}
