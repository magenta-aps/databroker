package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.Level1Container;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import dk.magenta.databroker.cprvejregister.model.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.PostnummerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

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
            this.obtain("kommuneKode", 4, 4);
            this.obtain("vejKode", 8, 4);
            this.obtain("myndighedsNavn", 12, 20);
            this.obtain("vejadresseringsNavn", 32, 20);
            this.obtain("husNrFra", 52, 4);
            this.obtain("husNrTil", 56, 4);
            this.obtain("ligeUlige", 60, 1);
            this.obtain("postNr", 61, 4);
            this.obtain("postDistriktTekst", 65, 20);
        }
    }


    public class PostnummerRegisterRun extends RegisterRun {

        private HashMap<String, String> postdistrikter;

        public PostnummerRegisterRun() {
            super();
            this.postdistrikter = new HashMap<String, String>();
        }

        public void saveRecord(Record record) {
            if (record.getRecordType().equals(PostNummer.RECORDTYPE_POSTNUMMER)) {
                this.saveRecord((PostNummer) record);
            }
        }

        public void saveRecord(PostNummer postnummer) {
            super.saveRecord(postnummer);
            String nummer = postnummer.get("postNr");
            if (nummer != null) {
                this.postdistrikter.put(nummer, postnummer.get("postDistriktTekst"));
            }
        }

        public HashMap<String, String> getPostdistrikter() {
            return this.postdistrikter;
        }
    }


    public PostnummerRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152114/a370712.txt");
    }

    protected RegisterRun createRun() {
        return new PostnummerRegisterRun();
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

    protected void saveRunToDatabase(RegisterRun run, Map<String, JpaRepository> repositories) {
        PostnummerRepository postnummerRepository = (PostnummerRepository) repositories.get("postnummerRepository");
        PostnummerRegisterRun prun = (PostnummerRegisterRun) run;
        EntityModificationCounter counter = new EntityModificationCounter();

        if (postnummerRepository == null) {
            System.err.println("Insufficient repositories");
            return;
        }

        System.out.println("Storing PostnummerEntities in database");

        Map<String, String> postDistrikter = prun.getPostdistrikter();
        for (String nummer : postDistrikter.keySet()) {
            String navn = postDistrikter.get(nummer);
            int postNummer = Integer.parseInt(nummer, 10);
            PostnummerEntity postnummerEntity = postnummerRepository.findByNummer(postNummer);
            boolean updatePostnummerEntity = false;
            if (postnummerEntity == null) {
                postnummerEntity = new PostnummerEntity();
                postnummerEntity.setNummer(postNummer);
                updatePostnummerEntity = true;
                counter.countCreatedItem();
            }
            if (!navn.equals(postnummerEntity.getNavn())) {
                postnummerEntity.setNavn(navn);
                if (!updatePostnummerEntity) {
                    counter.countUpdatedItem();
                }
                updatePostnummerEntity = true;
            }
            if (updatePostnummerEntity) {
                postnummerRepository.saveAndFlush(postnummerEntity);
            }
            this.printInputProcessed();
        }
        this.printFinalInputsProcessed();
        System.out.println("Stored PostnummerEntities in database:");
        counter.printModifications();
    }

    public static void main(String[] args) {
        new PostnummerRegister(null).pull();
    }
}
