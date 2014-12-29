package dk.magenta.databroker.dawa.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerEntity;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerRepository;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerVersionEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneRepository;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeRepository;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeVersionEntity;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.register.objectcontainers.Level1Container;
import dk.magenta.databroker.register.objectcontainers.Level2Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 19-12-14.
 */

@Component
public class DawaModel {

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    private KommuneRepository kommuneRepository;

    private Level1Container<KommuneEntity> kommuneCache = null;

    public KommuneEntity setKommune(int kode, String navn) {
        boolean changed = false;
        KommuneEntity kommuneEntity = this.getKommuneCache().get(kode);
        //KommuneEntity kommuneEntity = this.kommuneRepository.getByKode(kode);
        if (kommuneEntity == null) {
            kommuneEntity = new KommuneEntity();
            kommuneEntity.setKode(kode);
            if (this.kommuneCache != null) {
                this.kommuneCache.put(kode, kommuneEntity);
            }
            changed = true;
        }
        if (navn != null && !navn.isEmpty() && !navn.equals(kommuneEntity.getNavn())) {
            kommuneEntity.setNavn(navn);
            changed = true;
        }

        if (changed) {
            this.kommuneRepository.save(kommuneEntity);
        }
        return kommuneEntity;
    }

    public KommuneEntity getKommune(int kode) {
        return this.kommuneRepository.getByKode(kode);
    }

    public Collection<KommuneEntity> getKommune(String land, String[] kommune, GlobalCondition globalCondition) {
        return this.kommuneRepository.search(land, kommune, globalCondition);
    }

    private Level1Container<KommuneEntity> getKommuneCache() {
        if (this.kommuneCache == null) {
            this.kommuneCache = new Level1Container<KommuneEntity>();
            for (KommuneEntity item : this.kommuneRepository.findAll()) {
                this.kommuneCache.put(item.getKode(), item);
            }
        }
        return this.kommuneCache;
    }


    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    private VejstykkeRepository vejstykkeRepository;

    private Level2Container<VejstykkeEntity> vejstykkeCache = null;

    /*
    * Add or modify a road
    * */
    public VejstykkeEntity setVejstykke(int kommuneKode, int vejKode, String vejNavn, String vejAddresseringsnavn,
                                        RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        return this.setVejstykke(kommuneKode, vejKode, vejNavn, vejAddresseringsnavn, createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
    }
    public VejstykkeEntity setVejstykke(int kommuneKode, int vejKode, String vejNavn, String vejAddresseringsnavn,
                                        RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {

        if (createRegistrering == null && updateRegistrering == null) {
            System.err.println("Both registrations are null; cannot create database entity");
        } else if (createRegistrering == null) {
            createRegistrering = updateRegistrering;
        } else if (updateRegistrering == null) {
            updateRegistrering = createRegistrering;
        }


        //VejstykkeEntity vejstykkeEntity = this.vejstykkeRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
        VejstykkeEntity vejstykkeEntity = this.getVejstykkeCache().get(kommuneKode, vejKode);
        if (vejstykkeEntity == null) {
            KommuneEntity kommuneEntity = this.getKommuneCache().get(kommuneKode);
            if (kommuneEntity == null) {
                System.err.println("Kommune with kode "+kommuneKode+" not found. (There are "+this.kommuneRepository.count()+" entries in the table).\n" +
                        "Unable to create vej "+vejKode+" ("+vejNavn+")");
                return null;
            }
            vejstykkeEntity = new VejstykkeEntity();
            vejstykkeEntity.setKode(vejKode);
            vejstykkeEntity.setKommune(kommuneEntity);
            if (this.vejstykkeCache != null) {
                this.vejstykkeCache.put(kommuneKode, vejKode, vejstykkeEntity);
            }
        }
        VejstykkeVersionEntity vejstykkeVersionEntity = vejstykkeEntity.getLatestVersion();
        if (vejstykkeVersionEntity == null) {
            vejstykkeVersionEntity = vejstykkeEntity.addVersion(createRegistrering, virkninger);

        } else if (!vejstykkeVersionEntity.getVejnavn().equals(vejNavn) || !vejstykkeVersionEntity.getVejadresseringsnavn().equals(vejAddresseringsnavn)) {
            vejstykkeVersionEntity = vejstykkeEntity.addVersion(updateRegistrering, virkninger);
        } else {
            // No need to update anything
            vejstykkeVersionEntity = null;
        }

        if (vejstykkeVersionEntity != null) {
            vejstykkeVersionEntity.setVejnavn(vejNavn);
            vejstykkeVersionEntity.setVejadresseringsnavn(vejAddresseringsnavn);
            vejstykkeEntity.setLatestVersion(vejstykkeVersionEntity); // TODO: may not always be relevant
            this.vejstykkeRepository.save(vejstykkeEntity);
        }

        return vejstykkeEntity;
    }

    /*
    * Obtain roads from DB
    * */
    public VejstykkeEntity getVejstykke(int kommuneKode, int vejKode) {
        return this.vejstykkeRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
    }

    public Collection<VejstykkeEntity> getVejstykke(String land, String[] kommune, String[] vej, GlobalCondition globalCondition) {
        return this.vejstykkeRepository.search(land, kommune, vej, globalCondition);
    }

    private Level2Container<VejstykkeEntity> getVejstykkeCache() {
        if (this.vejstykkeCache == null) {
            this.vejstykkeCache = new Level2Container<VejstykkeEntity>();
            for (VejstykkeEntity item : this.vejstykkeRepository.findAll()) {
                System.out.println(item.getUuid());
                this.vejstykkeCache.put(item.getKommune().getKode(), item.getKode(), item);
            }
        }
        return this.vejstykkeCache;
    }


    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    private PostNummerRepository postNummerRepository;

    private Level1Container<PostNummerEntity> postNummerCache = null;

    public PostNummerEntity setPostNummer(int nummer, String navn,
                                          RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        return this.setPostNummer(nummer, navn, createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
    }

    public PostNummerEntity setPostNummer(int nummer, String navn,
                                          RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {
        System.out.println(nummer+":"+navn);
        if (createRegistrering == null && updateRegistrering == null) {
            System.err.println("Both registrations are null; cannot create database entity");
        } else if (createRegistrering == null) {
            createRegistrering = updateRegistrering;
        } else if (updateRegistrering == null) {
            updateRegistrering = createRegistrering;
        }

        PostNummerEntity postNummerEntity = this.getPostNummerCache().get(nummer);
        if (postNummerEntity == null) {
            postNummerEntity = new PostNummerEntity();
        }

        PostNummerVersionEntity postNummerVersionEntity = postNummerEntity.getLatestVersion();
        if (postNummerVersionEntity == null) {
            postNummerVersionEntity = postNummerEntity.addVersion(createRegistrering, virkninger);

        } else if (postNummerVersionEntity.getNr() != nummer || postNummerVersionEntity.getNavn() == null || !postNummerVersionEntity.getNavn().equals(navn)) {
            postNummerVersionEntity = postNummerEntity.addVersion(updateRegistrering, virkninger);
        } else {
            // No need to update anything
            postNummerVersionEntity = null;
        }

        if (postNummerVersionEntity != null) {
            postNummerVersionEntity.setNr(nummer);
            postNummerVersionEntity.setNavn(navn);
            postNummerEntity.setLatestVersion(postNummerVersionEntity); // TODO: may not always be relevant
            this.postNummerRepository.save(postNummerEntity);
        }

        return postNummerEntity;
    }

    private Level1Container<PostNummerEntity> getPostNummerCache() {
        if (this.postNummerCache == null) {
            this.postNummerCache = new Level1Container<PostNummerEntity>();
            for (PostNummerEntity item : this.postNummerRepository.findAll()) {
                this.postNummerCache.put(item.getLatestVersion().getNr(), item);
            }
        }
        return this.postNummerCache;
    }

    public Collection<PostNummerEntity> getPostnummer(String land, String[] post, String[] kommune, GlobalCondition globalCondition) {
        return this.postNummerRepository.search(land, post, kommune, globalCondition);
    }

    //------------------------------------------------------------------------------------------------------------------

    //@Autowired
    //private AdresseRepository adresseRepository;


}