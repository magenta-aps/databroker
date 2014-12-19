package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.register.Model;
import dk.magenta.databroker.register.objectcontainers.EntityModificationCounter;
import dk.magenta.databroker.register.objectcontainers.InputProcessingCounter;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetRepository;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetVersionEntity;

import java.util.List;

/**
 * Created by lars on 16-12-14.
 */
public class LokalitetModel extends Model {

    private LokalitetRepository lokalitetRepository;
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;

    public LokalitetModel(LokalitetRepository lokalitetRepository, KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository, RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        super(createRegistrering, updateRegistrering);
        this.lokalitetRepository = lokalitetRepository;
        this.kommunedelAfNavngivenVejRepository = kommunedelAfNavngivenVejRepository;
    }


    public void createLocations(List<Record> records) {
        System.out.println("Creating addresses ("+records.size()+" items)");

        //tic();
        //System.out.println("Precaching entities");
        //Level2Container<NavngivenVejEntity> navngivenVejCache = this.loadNavngivenVejCache();
        //Level3Container<HusnummerEntity> husnummerCache = this.loadHusnummerCache();
        //Level3Container<AdresseEntity> adresseCache = this.loadAdresseCache();
        //System.out.println("Precaching took "+toc()+" ms");

        EntityModificationCounter lokalitetsCounter = new EntityModificationCounter();
        InputProcessingCounter inputCounter = new InputProcessingCounter(true);

        for (Record record : records) {
            int kommuneKode = record.getInt("kommuneKode");
            int vejKode = record.getInt("vejKode");
            int lokalitetsKode = record.getInt("lokalitetsKode");
            String lokalitetsNavn = record.get("lokalitetsNavn");
            if (kommuneKode != 0 && vejKode != 0 && lokalitetsKode != 0) {
                this.createLocation(kommuneKode, vejKode, lokalitetsKode, lokalitetsNavn, lokalitetsCounter);
                inputCounter.printInputProcessed();
            }
        }

        inputCounter.printFinalInputsProcessed();
        System.out.println("Stored LokalitetEntities to database");
        lokalitetsCounter.printModifications();
    }




    private void createLocation(int kommuneKode, int vejKode, int lokalitetsKode, String lokalitetsNavn,
                               EntityModificationCounter lokalitetCounter) {

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
            KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity = this.kommunedelAfNavngivenVejRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
            if (kommunedelAfNavngivenVejEntity != null && lokalitetEntity != kommunedelAfNavngivenVejEntity.getLokalitet()) {
                kommunedelAfNavngivenVejEntity.setLokalitet(lokalitetEntity);
                this.kommunedelAfNavngivenVejRepository.save(kommunedelAfNavngivenVejEntity);
            }

            // Complain if we can't find any
            if (kommunedelAfNavngivenVejEntity == null) {
                System.err.println("No kommune for " + lokalitetsNavn + " (KommuneKode: " + kommuneKode + ", VejKode: " + vejKode + " not found)");
            }
        }
    }
}
