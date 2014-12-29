package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.CprKommuneVersionEntity;
import dk.magenta.databroker.register.Model;
import dk.magenta.databroker.register.objectcontainers.EntityModificationCounter;
import dk.magenta.databroker.register.objectcontainers.InputProcessingCounter;
import dk.magenta.databroker.register.objectcontainers.Level1Container;
import dk.magenta.databroker.register.records.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 16-12-14.
 */
public class KommuneModel extends Model {

    private CprKommuneRepository kommuneRepository;

    public KommuneModel(CprKommuneRepository kommuneRepository, RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        super(createRegistrering, updateRegistrering);
        this.kommuneRepository = kommuneRepository;
    }


    public void createKommuner(List<Record> records) {
        System.out.println("Creating kommuner ("+records.size()+" items)");

        tic();
        System.out.println("Precaching entities");
        Level1Container<CprKommuneEntity> kommuneCache = this.loadKommuneCache();
        System.out.println("Precaching "+kommuneCache.size()+" items took "+toc()+" ms");

        EntityModificationCounter kommuneCounter = new EntityModificationCounter();
        InputProcessingCounter inputCounter = new InputProcessingCounter(true);

        for (Record record : records) {
            int kommuneKode = record.getInt("myndighedsKode");
            String kommuneNavn = record.get("myndighedsNavn");
            if (kommuneKode != 0 && kommuneNavn != null) {
                this.createKommune(kommuneKode, kommuneNavn, kommuneCache, kommuneCounter);
                inputCounter.printInputProcessed();
            }
        }

        inputCounter.printFinalInputsProcessed();
        System.out.println("Stored LokalitetEntities to database");
        kommuneCounter.printModifications();
    }

    private void createKommune(int kommuneKode, String kommuneNavn,
                               Level1Container<CprKommuneEntity> kommuneCache,
                                EntityModificationCounter kommuneCounter) {

        if (kommuneKode != 0 && kommuneNavn != null) {
            CprKommuneEntity kommuneEntity = null;
            if (kommuneCache != null) {
                kommuneEntity = kommuneCache.get(kommuneKode);
            }
            if (kommuneEntity == null) {
                this.kommuneRepository.getByKommunekode(kommuneKode);
            }

            List<VirkningEntity> virkninger = new ArrayList<VirkningEntity>(); // TODO: Populate this list

            CprKommuneVersionEntity kommuneVersion = null;
            if (kommuneEntity == null) {
                kommuneEntity = CprKommuneEntity.create();
                kommuneEntity.setKommunekode(kommuneKode);
                kommuneVersion = kommuneEntity.addVersion(this.getCreateRegistrering(), virkninger);
                kommuneCounter.countCreatedItem();
            } else if (!kommuneEntity.getLatestVersion().getNavn().equals(kommuneNavn)) {
                kommuneVersion = kommuneEntity.addVersion(this.getUpdateRegistrering(), virkninger);
                kommuneCounter.countUpdatedItem();
            }

            if (kommuneVersion != null) {
                kommuneVersion.setNavn(kommuneNavn);
                this.kommuneRepository.save(kommuneEntity);
            }
        }
    }



    private Level1Container<CprKommuneEntity> loadKommuneCache() {
        Level1Container<CprKommuneEntity> kommuneCache = new Level1Container<CprKommuneEntity>();
        Collection<CprKommuneEntity> kommuneListe = this.kommuneRepository.findAll();
        for (CprKommuneEntity kommune : kommuneListe) {
            kommuneCache.put(kommune.getKommunekode(), kommune);
        }
        return kommuneCache;
    }

}