package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import dk.magenta.databroker.cprvejregister.model.RepositoryCollection;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerVersionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * Created by lars on 04-11-14.
 */
@Component
public class PostnummerRegister extends CprRegister {


    /*
    * Inner classes for parsed data
    * */

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

    /*
    * RegisterRun inner class
    * */

    public class PostnummerRegisterRun extends RegisterRun {

        private HashMap<String, String> postdistrikter;

        public PostnummerRegisterRun() {
            super();
            this.postdistrikter = new HashMap<String, String>();
        }

        public boolean add(Record record) {
            if (record.getRecordType().equals(PostNummer.RECORDTYPE_POSTNUMMER)) {
                PostNummer postnummer = (PostNummer) record;
                String nummer = postnummer.get("postNr");
                if (nummer != null) {
                    this.postdistrikter.put(nummer, postnummer.get("postDistriktTekst"));
                }
                return super.add(postnummer);
            }
            return false;
        }

        public HashMap<String, String> getPostdistrikter() {
            return this.postdistrikter;
        }
    }

    protected RegisterRun createRun() {
        return new PostnummerRegisterRun();
    }

    //------------------------------------------------------------------------------------------------------------------

    /*
    * Constructors
    * */

    public PostnummerRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public PostnummerRegister() {
    }

    @PostConstruct
    public void PostConstructPostnummerRegister() {
        DataProviderEntity postProvider = new DataProviderEntity();
        postProvider.setUuid(UUID.randomUUID().toString());

        this.setDataProviderEntity(postProvider);
    }


    /*
    * Data source spec
    * */

    public URL getRecordUrl() throws MalformedURLException {
        return new URL("https://cpr.dk/media/152114/a370712.txt");
    }

    protected String getEncoding() {
        return "ISO-8859-1";
    }


    /*
    * Parse definition
    * */

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


    /*
    * Repositories
    * */

     @Autowired
    private PostnummerRepository postnummerRepository;
    public PostnummerRepository getPostnummerRepository() {
        return postnummerRepository;
    }

    @Autowired
    private RegistreringRepository registreringRepository;




    /*
    * Registration
    * */

    // RegistreringEntities that must be attached to all versioned data entities
    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegistrering;

    private void createRegistreringEntities() {
        this.createRegistrering = registreringRepository.createNew(this);
        this.updateRegistrering = registreringRepository.createUpdate(this);
    }


    /*
    * Database save
    * */

    protected void saveRunToDatabase(RegisterRun run, RepositoryCollection repositories) {

        PostnummerRepository postnummerRepository = repositories.postnummerRepository;

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
            PostnummerVersionEntity postnummerVersion = null;

            if (postnummerEntity == null) {
                postnummerEntity = PostnummerEntity.create();
                postnummerEntity.setNummer(postNummer);
                postnummerVersion = postnummerEntity.addVersion(createRegistrering);
                postnummerRepository.save(postnummerEntity);
                counter.countCreatedItem();

            } else if (!postnummerEntity.getLatestVersion().getNavn().equals(navn)) {
                postnummerVersion = postnummerEntity.addVersion(updateRegistrering);
                counter.countUpdatedItem();
            }

            if (postnummerVersion != null) {
                postnummerVersion.setNavn(navn);
                postnummerRepository.save(postnummerEntity);
            }
            prun.printInputProcessed();
        }
        prun.printFinalInputsProcessed();
        System.out.println("Stored PostnummerEntities in database:");
        counter.printModifications();
    }
}
