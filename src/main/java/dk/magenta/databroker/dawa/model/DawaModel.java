package dk.magenta.databroker.dawa.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktVersionEntity;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseRepository;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseVersionEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseRepository;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseVersionEntity;
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
import dk.magenta.databroker.register.objectcontainers.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

        } else {
            String dbVejNavn = vejstykkeVersionEntity.getVejnavn();
            String dbVejAddresseringsNavn = vejstykkeVersionEntity.getVejadresseringsnavn();
            if (!(dbVejNavn != null || dbVejNavn.equals(vejNavn)) ||
                    !(dbVejAddresseringsNavn != null && dbVejAddresseringsNavn.equals(vejAddresseringsnavn))) {
            vejstykkeVersionEntity = vejstykkeEntity.addVersion(updateRegistrering, virkninger);
        } else {
            // No need to update anything
            vejstykkeVersionEntity = null;
        }
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

    public VejstykkeEntity getVejstykke(String uuid) {
        return this.vejstykkeRepository.getByUuid(uuid);
    }


    private Level2Container<VejstykkeEntity> getVejstykkeCache() {
        if (this.vejstykkeCache == null) {
            this.vejstykkeCache = new Level2Container<VejstykkeEntity>();
            for (VejstykkeEntity item : this.vejstykkeRepository.findAll()) {
                this.vejstykkeCache.put(item.getKommune().getKode(), item.getKode(), item);
            }
        }
        return this.vejstykkeCache;
    }


    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    private PostNummerRepository postNummerRepository;

    private Level1Container<PostNummerEntity> postNummerCache = null;

    public PostNummerEntity setPostNummer(int nummer, String navn, Set<Pair<Integer, Integer>> veje,
                                          RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        return this.setPostNummer(nummer, navn, veje, createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
    }

    public PostNummerEntity setPostNummer(int nummer, String navn, Set<Pair<Integer, Integer>> veje,
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
            System.out.println("Creating new postnummerEntity "+nummer);
        }


        ArrayList<VejstykkeVersionEntity> vejListe = new ArrayList<VejstykkeVersionEntity>();
        Level1Container<KommuneEntity> kommuneMap = new Level1Container<KommuneEntity>();
        for (Pair<Integer, Integer> delvej : veje) {
            int kommuneKode = delvej.getLeft();
            int vejKode = delvej.getRight();
            VejstykkeEntity vejstykkeEntity = this.getVejstykkeCache().get(kommuneKode, vejKode);
            //VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode);
            if (vejstykkeEntity != null) {
                vejListe.add(vejstykkeEntity.getLatestVersion());
            }
            if (!kommuneMap.containsKey(kommuneKode)) {
                KommuneEntity kommuneEntity = this.getKommuneCache().get(kommuneKode);
                //KommuneEntity kommuneEntity = this.getKommune(kommuneKode);
                if (kommuneEntity != null) {
                    kommuneMap.put(kommuneKode, kommuneEntity);
                }
            }
        }
        System.out.println("postnr "+nummer+" har "+vejListe.size()+" veje og "+kommuneMap.size()+" kommuner");


        PostNummerVersionEntity postNummerVersionEntity = postNummerEntity.getLatestVersion();
        if (postNummerVersionEntity == null) {
            postNummerVersionEntity = postNummerEntity.addVersion(createRegistrering, virkninger);

        } else if (postNummerVersionEntity.getNr() != nummer ||
                postNummerVersionEntity.getNavn() == null || !postNummerVersionEntity.getNavn().equals(navn) ||
                !postNummerEntity.getVejstykkeVersioner().equals(vejListe) ||
                !postNummerVersionEntity.getKommuner().equals(kommuneMap)) {
            postNummerVersionEntity = postNummerEntity.addVersion(updateRegistrering, virkninger);
        } else {
            // No need to update anything
            postNummerVersionEntity = null;
        }

        if (postNummerVersionEntity != null) {
            postNummerVersionEntity.setNr(nummer);
            postNummerVersionEntity.setNavn(navn);

            postNummerEntity.setVejstykkeVersioner(vejListe);
            for (VejstykkeVersionEntity vejstykkeVersionEntity : vejListe) {
                vejstykkeVersionEntity.setPostnummer(postNummerEntity);
            }

            postNummerVersionEntity.setKommuner(kommuneMap.getList());
            for (KommuneEntity kommune : kommuneMap.getList()) {
                kommune.addPostnummer(postNummerVersionEntity);
            }

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



    public PostNummerEntity getPostnummer(int postnr) {
        return this.postNummerRepository.getByNr(postnr);
    }

    public Collection<PostNummerEntity> getPostnummer(String land, String[] post, String[] kommune, GlobalCondition globalCondition) {
        return this.postNummerRepository.search(land, post, kommune, globalCondition);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    private AdgangsAdresseRepository adgangsAdresseRepository;

    @Autowired
    private EnhedsAdresseRepository enhedsAdresseRepository;


    public AdgangsAdresseEntity setAdresse(int kommuneKode, int vejKode, String husNr, String etage, String doer,
                                          RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        return this.setAdresse(kommuneKode, vejKode, husNr, etage, doer, createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
    }

    public AdgangsAdresseEntity setAdresse(int kommuneKode, int vejKode, String husNr, String etage, String doer,
                                          RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {

        System.out.println("setAdresse("+kommuneKode+", "+vejKode+", "+husNr+", "+etage+", "+doer+")");
        VejstykkeEntity vejstykkeEntity = this.getVejstykkeCache().get(kommuneKode, vejKode);
        AdgangsAdresseEntity adgangsAdresseEntity = null;

        Collection<AdgangsAdresseVersionEntity> adgangsAdresseVersionEntities = vejstykkeEntity.getAdgangsAdresseVersioner();
        if (adgangsAdresseVersionEntities != null) {
            for (AdgangsAdresseVersionEntity adgangsAdresseVersionEntity : adgangsAdresseVersionEntities) {
                if (husNr.equals(adgangsAdresseVersionEntity.getHusnr())) {
                    adgangsAdresseEntity = adgangsAdresseVersionEntity.getEntity();
                }
            }
        }
        if (adgangsAdresseEntity == null) {
            adgangsAdresseEntity = new AdgangsAdresseEntity();
            System.out.println("Creating new AdgangsAdresseEntity");
        }


        AdgangsAdresseVersionEntity adgangsAdresseVersionEntity = adgangsAdresseEntity.getLatestVersion();
        if (adgangsAdresseVersionEntity == null) {
            adgangsAdresseVersionEntity = adgangsAdresseEntity.addVersion(createRegistrering, virkninger);
            System.out.println("Creating initial AdgangsAdresseVersionEntity");
        } else if (false) {

            System.out.println("Creating updated AdgangsAdresseVersionEntity");
        } else {
            adgangsAdresseVersionEntity = null;
        }

        if (adgangsAdresseVersionEntity != null) {
            adgangsAdresseVersionEntity.setVejstykke(vejstykkeEntity);
            adgangsAdresseVersionEntity.setHusnr(husNr);
            adgangsAdresseEntity.setLatestVersion(adgangsAdresseVersionEntity);
            this.adgangsAdresseRepository.save(adgangsAdresseEntity);
        }



        EnhedsAdresseEntity enhedsAdresseEntity = null;
        for (EnhedsAdresseVersionEntity e : adgangsAdresseEntity.getEnhedsAdresseVersioner()) {
            if (e.getEntity().getLatestVersion() == e && e.getEtage().equals(etage) && e.getDoer().equals(doer)) {
                enhedsAdresseEntity = e.getEntity();
            }
        }
        if (enhedsAdresseEntity == null) {
            enhedsAdresseEntity = new EnhedsAdresseEntity();
            System.out.println("Creating new EnhedsAdresseEntity");
        }

        EnhedsAdresseVersionEntity enhedsAdresseVersionEntity = enhedsAdresseEntity.getLatestVersion();
        if (enhedsAdresseVersionEntity == null) {
            enhedsAdresseVersionEntity = enhedsAdresseEntity.addVersion(createRegistrering, virkninger);
            System.out.println("Creating initial EnhedsAdresseVersionEntity");
        } else if (!enhedsAdresseVersionEntity.getEtage().equals(etage) ||
                !enhedsAdresseVersionEntity.getDoer().equals(doer) ||
                enhedsAdresseVersionEntity.getAdgangsadresse() != adgangsAdresseEntity) {
            enhedsAdresseVersionEntity = enhedsAdresseEntity.addVersion(updateRegistrering, virkninger);
            System.out.println("Creating updated EnhedsAdresseVersionEntity");
        } else {
            enhedsAdresseVersionEntity = null;
        }

        if (enhedsAdresseVersionEntity != null) {
            enhedsAdresseVersionEntity.setEtage(etage);
            enhedsAdresseVersionEntity.setDoer(doer);
            enhedsAdresseVersionEntity.setAdgangsadresse(adgangsAdresseEntity);
            this.enhedsAdresseRepository.save(enhedsAdresseEntity);
        }

        return adgangsAdresseEntity;
    }


    public AdgangsAdresseEntity getAdgangsAdresse(String uuid) {
        return this.adgangsAdresseRepository.getByUuid(uuid);
    }

    public Collection<AdgangsAdresseEntity> getAdgangsAdresse(String land, String[] post, String[] kommune, String[] vej, String[] husnr, GlobalCondition globalCondition) {
        return this.adgangsAdresseRepository.search(land, kommune, post, vej, husnr, globalCondition);
    }

    public EnhedsAdresseEntity getEnhedsAdresse(String uuid) {
        return this.enhedsAdresseRepository.getByUuid(uuid);
    }

    public Collection<EnhedsAdresseEntity> getEnhedsAdresse(String land, String[] post, String[] kommune, String[] vej, String[] husnr, String[] etage, String[] doer, GlobalCondition globalCondition) {
        return this.enhedsAdresseRepository.search(land, post, kommune, vej, husnr, etage, doer, globalCondition);
    }

    }