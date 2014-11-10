package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.records.*;
//import dk.magenta.databroker.models.adresser.Kommune;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by lars on 04-11-14.
 */
public class MyndighedsRegister extends CprRegister {

    public abstract class MyndighedsDataRecord extends Record {
        public static final String RECORDTYPE_MYNDIGHED = "001";
        public static final String RECORDTYPE_KOMMUNEREL = "002";
        protected int getTimestampStart() {
            return 11;
        }
        public MyndighedsDataRecord(String line) throws ParseException {
            super(line);
            this.put("myndighedsKode", substr(line, 4, 4));
            this.put("myndighedsType", substr(line, 8, 2));
            this.put("timestamp", substr(line, this.getTimestampStart(), 12));
        }
    }

    public class Myndighed extends MyndighedsDataRecord {
        public String getRecordType() {
            return RECORDTYPE_MYNDIGHED;
        }
        protected int getTimestampStart() {
            return 11;
        }
        public Myndighed(String line) throws ParseException {
            super(line);
            this.put("myndighedsGruppe", substr(line, 10, 1));
            this.put("telefon", substr(line, 23, 8));
            this.put("startDato", substr(line, 31, 12));
            this.put("slutDato", substr(line, 43, 12));
            this.put("myndighedsNavn", substr(line, 55, 20));
            this.put("myndighedsAdressat", substr(line, 75, 34));
            this.put("adresselinie1", substr(line, 109, 34));
            this.put("adresselinie2", substr(line, 143, 34));
            this.put("adresselinie3", substr(line, 177, 34));
            this.put("adresselinie4", substr(line, 211, 34));
            this.put("telefax", substr(line, 245, 8));
            this.put("myndighedsNavnFull", substr(line, 253, 60));
            this.put("email", substr(line, 313, 100));
            this.put("landekodeA2", substr(line, 413, 2));
            this.put("landekodeA3", substr(line, 415, 3));
            this.put("landekodeN", substr(line, 418, 3));
        }
    }

    public class KommuneRelation extends MyndighedsDataRecord {
        public String getRecordType() {
            return RECORDTYPE_KOMMUNEREL;
        }
        protected int getTimestampStart() {
            return 15;
        }
        public KommuneRelation(String line) throws ParseException {
            super(line);
            this.put("kommuneKode", substr(line, 11, 4));
        }
    }



    public MyndighedsRegister(DataProviderEntity dbObject) {
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

    protected void saveRunToDatabase(RegisterRun run) {
        /*for (Record record : run.getByType(MyndighedsDataRecord.RECORDTYPE_MYNDIGHED)) {
            Myndighed myndighed = (Myndighed) record;
            System.out.println("For myndighed (myndighedsNavn:"+myndighed.get("myndighedsNavn")+", myndighedsKode:"+myndighed.get("myndighedsKode")+", myndighedsType: "+myndighed.get("myndighedsType")+") findes relationerne:");

            String myndighedsKode = myndighed.get("myndighedsKode");
            List<Record> relations = run.findRecord("KommuneRelation", "myndighedsKode", myndighedsKode);
            if (relations != null) {
                for (Record r : relations) {
                    System.out.println("    kommuneKode: " + r.get("kommuneKode")+", myndighedsKode:"+r.get("myndighedsKode")+", myndighedsType: "+r.get("myndighedsType"));
                }
            }
        }*/
        /*List<Record> relations = run.findRecord("Myndighed", "myndighedsType", "05"); // hvis vi ikke har yderligere brug for lookups, flyt dette til pull() og gem data efterhånden som vi parser
        if (relations != null) {
            for (Record r : relations) {
                System.out.println("Kommune(" + r.get("myndighedsKode")+", " + r.get("myndighedsNavn")+")");
                Kommune kommune = new Kommune(Integer.parseInt(r.get("myndighedsKode")), r.get("myndighedsNavn"));
                // Vi har nu skabt en Kommune-instans. Gem den somehow
            }
        }*/
    }

    public static void main(String[] args) {
        MyndighedsRegister register = new MyndighedsRegister(null);
        register.pull();
    }

}
