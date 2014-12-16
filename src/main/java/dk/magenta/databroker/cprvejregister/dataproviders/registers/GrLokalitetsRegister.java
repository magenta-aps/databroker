package dk.magenta.databroker.cprvejregister.dataproviders.registers;

import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.cprvejregister.dataproviders.RegisterRun;
import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;
import dk.magenta.databroker.cprvejregister.model.LokalitetModel;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetRepository;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetVersionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by lars on 12-12-14.
 */
@Component
public class GrLokalitetsRegister extends Register {

    /*
    * RegisterRun inner class
    * */

    private class GrLokalitetRecord extends Record {
    }

    private class GrRegisterRun extends RegisterRun {
    }

    @Override
    protected RegisterRun createRun() {
        return new GrRegisterRun();
    }

    //------------------------------------------------------------------------------------------------------------------

    /*
    * Constructors
    * */

    public GrLokalitetsRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public GrLokalitetsRegister() {
    }


    /*
    * Data source spec
    * */

    @Override
    public File getRecordFile() {
        return new File("src/test/resources/grønlandLokaliteter.csv");
    }

    @Override
    protected String getEncoding() {
        return "UTF-8";
    }


    /*
    * Parse definition
    * */

    protected GrLokalitetRecord parseTrimmedLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {
            GrLokalitetRecord record = new GrLokalitetRecord();
            record.put("kommuneKode", parts[0]);
            record.put("vejKode", parts[1]);
            record.put("lokalitetsNavn", parts[2]);
            record.put("lokalitetsKode", parts[3]);
            return record;
        }
        return null;
    }


    /*
    * Repositories
    * */

    @Autowired
    private LokalitetRepository lokalitetRepository;

    @Autowired
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;


    /*
    * Database save
    * */

     protected void saveRunToDatabase(RegisterRun run) {
        GrRegisterRun grun = (GrRegisterRun) run;
        this.createRegistreringEntities();

         LokalitetModel lokalitetModel = new LokalitetModel(this.lokalitetRepository, this.kommunedelAfNavngivenVejRepository, this.getCreateRegistrering(), this.getUpdateRegistrering());
         lokalitetModel.createLocations(grun);
/*
        for (Record record : grun) {
            GrLokalitetRecord gRecord = (GrLokalitetRecord) record;
            int lokalitetsKode = gRecord.getInt("lokalitetsKode");
            String lokalitetsNavn = gRecord.get("lokalitetsNavn");
            int kommuneKode = gRecord.getInt("kommuneKode");
            int vejKode = gRecord.getInt("vejKode");

            if (kommuneKode != 0 && vejKode != 0) {
                // Several input entries will share the same LokalitetEntity
                LokalitetEntity lokalitetEntity = lokalitetRepository.findByLokalitetsKode(lokalitetsKode);
                LokalitetVersionEntity lokalitetVersionEntity = null;
                if (lokalitetEntity == null) {
                    lokalitetEntity = LokalitetEntity.create();
                    lokalitetEntity.setLokalitetsKode(lokalitetsKode);
                    lokalitetVersionEntity = lokalitetEntity.addVersion(this.getCreateRegistrering());
                } else if (!lokalitetsNavn.equals(lokalitetEntity.getLatestVersion().getLokalitetsNavn())) {
                    lokalitetVersionEntity = lokalitetEntity.addVersion(this.getUpdateRegistrering());
                }
                // If there's anything to save, do it
                if (lokalitetVersionEntity != null) {
                    lokalitetVersionEntity.setLokalitetsNavn(lokalitetsNavn);
                    this.lokalitetRepository.save(lokalitetEntity);
                }


                // Refer to the new/updated entity
                KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity = kommunedelAfNavngivenVejRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
                if (kommunedelAfNavngivenVejEntity != null && lokalitetEntity != kommunedelAfNavngivenVejEntity.getLokalitet()) {
                    kommunedelAfNavngivenVejEntity.setLokalitet(lokalitetEntity);
                    kommunedelAfNavngivenVejRepository.save(kommunedelAfNavngivenVejEntity);
                }

                // Complain if we can't find any
                if (kommunedelAfNavngivenVejEntity == null) {
                    System.out.println("No kommune for " + lokalitetsNavn + " (KommuneKode: " + kommuneKode + ", VejKode: " + vejKode + " not found)");
                }
            }
        }*/
    }
}
