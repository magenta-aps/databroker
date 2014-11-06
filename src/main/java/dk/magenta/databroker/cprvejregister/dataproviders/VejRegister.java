package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.cprvejregister.dataproviders.records.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;


/**
 * Created by lars on 04-11-14.
 */
public class VejRegister extends CprRegister {

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152096/vejregister_hele_landet_pr_141101.zip");
    }

    public VejRegister(dk.magenta.databroker.core.model.DataProvider dbObject) {
        super(dbObject);
    }

    protected Record parseTrimmedLine(String recordType, String line) {
        Record r = super.parseTrimmedLine(recordType, line);
        if (r != null) {
            return r;
        }
        try {
            if (recordType.equals(VejDataRecord.RECORDTYPE_AKTVEJ)) {
                return new AktivVej(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BOLIG)) {
                return new Bolig(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BYDISTRIKT)) {
                return new ByDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_POSTDIST)) {
                return new PostDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_NOTATVEJ)) {
                return new NotatVej(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BYFORNYDIST)) {
                return new ByfornyelsesDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_DIVDIST)) {
                return new DiverseDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_EVAKUERDIST)) {
                return new EvakueringsDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_KIRKEDIST)) {
                return new KirkeDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SKOLEDIST)) {
                return new SkoleDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_BEFOLKDIST)) {
                return new BefolkningsDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SOCIALDIST)) {
                return new SocialDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_SOGNEDIST)) {
                return new SogneDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_VALGDIST)) {
                return new ValgDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_VARMEDIST)) {
                return new VarmeDistrikt(line);
            }
            if (recordType.equals(VejDataRecord.RECORDTYPE_HISTORISKVEJ)) {
                return new HistoriskVej(line);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new VejRegister(null).pull();
    }
}
