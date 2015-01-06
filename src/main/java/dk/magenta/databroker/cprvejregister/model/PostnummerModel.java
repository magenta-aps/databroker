package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.register.Model;
import dk.magenta.databroker.register.objectcontainers.EntityModificationCounter;
import dk.magenta.databroker.register.objectcontainers.InputProcessingCounter;
import dk.magenta.databroker.register.objectcontainers.Level1Container;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerVersionEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 16-12-14.
 */
public class PostnummerModel extends Model {

    private PostnummerRepository postnummerRepository;

    public PostnummerModel(PostnummerRepository postnummerRepository, RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        super(createRegistrering, updateRegistrering);
        this.postnummerRepository = postnummerRepository;
    }


    public Level1Container<PostnummerEntity> createPostnumre(List<Record> records) {
        System.out.println("Creating postnumre (" + records.size() + " items)");

        tic();
        System.out.println("Precaching entities");
        Level1Container<PostnummerEntity> postCache = this.loadPostCache();
        System.out.println("Precaching "+postCache.size()+" items took " + toc() + " ms");

        EntityModificationCounter kommuneCounter = new EntityModificationCounter();
        InputProcessingCounter inputCounter = new InputProcessingCounter(true);
        Level1Container<PostnummerEntity> postnummerEntityCache = new Level1Container<PostnummerEntity>();

        for (Record record : records) {
            int postNummer = record.getInt("postNr");
            String navn = record.get("postDistriktTekst");
            if (postNummer != 0 && navn != null) {
                PostnummerEntity entity = this.createPostnummer(postNummer, navn, postCache, kommuneCounter);
                inputCounter.printInputProcessed();
                postnummerEntityCache.put(postNummer, entity);
            }
        }

        inputCounter.printFinalInputsProcessed();
        System.out.println("Stored LokalitetEntities to database");
        kommuneCounter.printModifications();
        return postnummerEntityCache;
    }


    private PostnummerEntity createPostnummer(int postNummer, String navn,
                                  Level1Container<PostnummerEntity> postnummerCache,
                                  EntityModificationCounter postnummerCounter) {

        PostnummerEntity postnummerEntity = null;
        if (postnummerCache != null) {
            postnummerEntity = postnummerCache.get(postNummer);
        }
        if (postnummerEntity == null) {
            this.postnummerRepository.findByNummer(postNummer);
        }

        PostnummerVersionEntity postnummerVersion = null;
        if (postnummerEntity == null) {
            postnummerEntity = PostnummerEntity.create();
            postnummerEntity.setNummer(postNummer);
            postnummerVersion = postnummerEntity.addVersion(this.getCreateRegistrering());
            postnummerCounter.countCreatedItem();

        } else if (!postnummerEntity.getLatestVersion().getNavn().equals(navn)) {
            postnummerVersion = postnummerEntity.addVersion(this.getUpdateRegistrering());
            postnummerCounter.countUpdatedItem();
        }

        postnummerCache.put(postNummer, postnummerEntity);

        if (postnummerVersion != null) {
            postnummerVersion.setNavn(navn);
            postnummerRepository.save(postnummerEntity);
        }
        return postnummerEntity;
    }




    private Level1Container<PostnummerEntity> loadPostCache() {
        Level1Container<PostnummerEntity> postCache = new Level1Container<PostnummerEntity>();
        Collection<PostnummerEntity> postListe = this.postnummerRepository.findAll();
        for (PostnummerEntity post : postListe) {
            postCache.put(post.getNummer(), post);
        }
        return postCache;
    }


}
