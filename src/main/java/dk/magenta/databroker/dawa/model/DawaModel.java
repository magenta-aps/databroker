package dk.magenta.databroker.dawa.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lars on 19-12-14.
 */

@Component
public class DawaModel {

    //------------------------------------------------------------------------------------------------------------------

    private boolean printProcessing = false;


    @Autowired
    private KommuneRepository kommuneRepository;

    private Level1Container<KommuneEntity> kommuneCache = null;

    public KommuneEntity setKommune(int kode, String navn,
                                    RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        return this.setKommune(kode, navn, createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
    }

    public KommuneEntity setKommune(int kode, String navn,
                                    RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {
        boolean changed = false;
        KommuneEntity kommuneEntity = this.getKommuneCache().get(kode);
        //KommuneEntity kommuneEntity = this.kommuneRepository.getByKode(kode);
        if (kommuneEntity == null) {
            if (printProcessing) {
                System.out.println("    creating new KommuneEntity " + kode);
            }
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
        } else {
            if (printProcessing) {
                System.out.println("    no changes");
            }
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
            if (printProcessing) {
                System.out.println("    creating new VejstykkeEntity " + kommuneKode + ":" + vejKode);
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
            if (printProcessing) {
                System.out.println("    creating initial VejstykkeVersionEntity");
            }
            vejstykkeVersionEntity = vejstykkeEntity.addVersion(createRegistrering, virkninger);

        } else {
            String dbVejNavn = vejstykkeVersionEntity.getVejnavn();
            String dbVejAddresseringsNavn = vejstykkeVersionEntity.getVejadresseringsnavn();
            if (!(dbVejNavn == null || dbVejNavn.equals(vejNavn)) ||
                    !(dbVejAddresseringsNavn == null || dbVejAddresseringsNavn.equals(vejAddresseringsnavn))) {
                if (printProcessing) {
                    System.out.println("    creating updated VejstykkeVersionEntity (" + dbVejNavn + " != " + vejNavn + " || " + dbVejAddresseringsNavn + " != " + vejAddresseringsnavn + ")");
                }
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
        } else {
            if (printProcessing) {
                System.out.println("    no changes");
            }
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

    public PostNummerEntity setPostNummer(int nummer, String navn, Set<RawVej> veje,
                                          RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        return this.setPostNummer(nummer, navn, veje, createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
    }

    public PostNummerEntity setPostNummer(int nummer, String navn, Set<RawVej> veje,
                                          RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {
        if (createRegistrering == null && updateRegistrering == null) {
            System.err.println("Both registrations are null; cannot create database entity");
        } else if (createRegistrering == null) {
            createRegistrering = updateRegistrering;
        } else if (updateRegistrering == null) {
            updateRegistrering = createRegistrering;
        }

        PostNummerEntity postNummerEntity = this.getPostNummerCache().get(nummer);
        if (postNummerEntity == null) {
            if (printProcessing) {
                System.out.println("    creating new postnummerEntity " + nummer);
            }
            postNummerEntity = new PostNummerEntity();
        }



        // Convert List of RawVej to a lists of Veje

        HashSet<VejstykkeEntity> vejListe = new HashSet<VejstykkeEntity>();
        for (RawVej delvej : veje) {
            int kommuneKode = delvej.getKommuneKode();
            int vejKode = delvej.getVejKode();

            // Obtain a VejstykkeVersion to update
            VejstykkeEntity vejstykkeEntity = this.getVejstykkeCache().get(kommuneKode, vejKode);
            //VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode);
            if (vejstykkeEntity != null) {
                vejListe.add(vejstykkeEntity);
            } else {
                System.out.println("NOT adding vej " + kommuneKode + ":" + vejKode);
            }
        }


        // Convert List of RawVej to a list of Kommuner
        Level1Container<KommuneEntity> kommuneMap = new Level1Container<KommuneEntity>();
        for (RawVej delvej : veje) {
            int kommuneKode = delvej.getKommuneKode();
            if (!kommuneMap.containsKey(kommuneKode)) {
                kommuneMap.put(kommuneKode, this.getKommuneCache().get(kommuneKode));
            }
        }


        PostNummerVersionEntity postNummerVersionEntity = postNummerEntity.getLatestVersion();
        if (postNummerVersionEntity == null) {
            if (printProcessing) {
                System.out.println("    creating initial PostNummerVersionEntity");
            }
            postNummerVersionEntity = postNummerEntity.addVersion(createRegistrering, virkninger);

        } else {

            boolean allHaveThisPostnr = true;
            for (VejstykkeEntity vejstykkeEntity : vejListe) {
                if (!vejstykkeEntity.getLatestVersion().getPostnumre().contains(postNummerEntity)) {
                    allHaveThisPostnr = false;
                    break;
                }
            }

            if (postNummerVersionEntity.getNr() != nummer ||
                    postNummerVersionEntity.getNavn() == null || !postNummerVersionEntity.getNavn().equals(navn) ||
                    !allHaveThisPostnr ||
                    !postNummerVersionEntity.getKommuner().containsAll(kommuneMap.getList())) {

                if (printProcessing) {
                    System.out.println("    creating updated PostNummerVersionEntity");
                }

                postNummerVersionEntity = postNummerEntity.addVersion(updateRegistrering, virkninger);
            } else {
                // No need to update anything
                postNummerVersionEntity = null;
            }
        }

        if (postNummerVersionEntity != null) {
            postNummerVersionEntity.setNr(nummer);
            postNummerVersionEntity.setNavn(navn);

            for (KommuneEntity kommuneEntity : kommuneMap.getList()) {
                if (kommuneEntity != null && !kommuneEntity.getPostnumre().contains(postNummerVersionEntity)) {
                    postNummerVersionEntity.addKommune(kommuneEntity);
                    this.kommuneRepository.save(kommuneEntity);
                }
            }

            this.postNummerRepository.save(postNummerEntity);

            for (VejstykkeEntity vejstykkeEntity : vejListe) {

                VejstykkeVersionEntity vejstykkeVersionEntity = vejstykkeEntity.getLatestVersion();

                // If the newest version isn't made by us (TODO: make sure we really want that)
                // create a new version copied from the old
                if (vejstykkeVersionEntity.getRegistrering() != createRegistrering && vejstykkeVersionEntity.getRegistrering() != updateRegistrering) {
                    this.vejstykkeRepository.save(vejstykkeEntity);
                    VejstykkeVersionEntity oldVersion = vejstykkeVersionEntity;
                    vejstykkeVersionEntity = vejstykkeEntity.addVersion(updateRegistrering);
                    vejstykkeVersionEntity.copyFrom(oldVersion);
                    vejstykkeEntity.setLatestVersion(vejstykkeVersionEntity);
                }
                vejstykkeVersionEntity.addPostnummer(postNummerEntity);

                this.vejstykkeRepository.save(vejstykkeEntity);
            }

            postNummerEntity.setLatestVersion(postNummerVersionEntity); // TODO: may not always be relevant
            this.postNummerRepository.save(postNummerEntity);
        } else {
            if (printProcessing) {
                System.out.println("    no changes");
            }
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

        VejstykkeEntity vejstykkeEntity = this.getVejstykkeCache().get(kommuneKode, vejKode);
        AdgangsAdresseEntity adgangsAdresseEntity = null;

        if (vejstykkeEntity == null) {
            System.err.println("Vejstykke "+kommuneKode+":"+vejKode+" not found");
        } else {
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
                if (printProcessing) {
                    System.out.println("    creating new AdgangsAdresseEntity");
                }
            }


            AdgangsAdresseVersionEntity adgangsAdresseVersionEntity = adgangsAdresseEntity.getLatestVersion();
            if (adgangsAdresseVersionEntity == null) {
                adgangsAdresseVersionEntity = adgangsAdresseEntity.addVersion(createRegistrering, virkninger);
                if (printProcessing) {
                    System.out.println("    creating initial AdgangsAdresseVersionEntity");
                }
            } else if (false) {
// TODO
                if (printProcessing) {
                    System.out.println("    creating updated AdgangsAdresseVersionEntity");
                }
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
                if (printProcessing) {
                    System.out.println("    creating new EnhedsAdresseEntity");
                }
            }

            EnhedsAdresseVersionEntity enhedsAdresseVersionEntity = enhedsAdresseEntity.getLatestVersion();
            if (enhedsAdresseVersionEntity == null) {
                enhedsAdresseVersionEntity = enhedsAdresseEntity.addVersion(createRegistrering, virkninger);
                if (printProcessing) {
                    System.out.println("    creating initial EnhedsAdresseVersionEntity");
                }
            } else if (!enhedsAdresseVersionEntity.getEtage().equals(etage) ||
                    !enhedsAdresseVersionEntity.getDoer().equals(doer) ||
                    enhedsAdresseVersionEntity.getAdgangsadresse() != adgangsAdresseEntity) {
                enhedsAdresseVersionEntity = enhedsAdresseEntity.addVersion(updateRegistrering, virkninger);
                if (printProcessing) {
                    System.out.println("    creating updated EnhedsAdresseVersionEntity");
                }
            } else {
                enhedsAdresseVersionEntity = null;
            }

            if (enhedsAdresseVersionEntity != null) {
                enhedsAdresseVersionEntity.setEtage(etage);
                enhedsAdresseVersionEntity.setDoer(doer);
                enhedsAdresseVersionEntity.setAdgangsadresse(adgangsAdresseEntity);
                this.enhedsAdresseRepository.save(enhedsAdresseEntity);
            } else {
                if (printProcessing) {
                    System.out.println("    no changes");
                }
            }
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

    //------------------------------------------------------------------------------------------------------------------


    public void setLokalitet(String lokalitetsnavn, Set<RawVej> veje,
                             RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        this.setLokalitet(lokalitetsnavn, veje, createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
    }

    public void setLokalitet(String lokalitetsnavn, Set<RawVej> veje,
                             RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {
        // TODO: Put the lokalitet into the database (need to create a model for it first)
        /*System.out.println("lokalitet(" + lokalitetsnavn + ") {");
        for (RawVej vej : veje) {
            System.out.println(vej.getKommuneKode()+":"+vej.getVejKode());
        }
        System.out.println("}");*/
    }

}