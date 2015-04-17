package dk.magenta.databroker.dawa.model;

import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.util.TimeRecorder;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetRepository;
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
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.objectcontainers.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by lars on 19-12-14.
 */

@Component
public class DawaModel {

    public void flush() {
        System.out.println("DawaModel.flush()");
        /*this.kommuneRepository.flush();
        this.kommuneRepository.clear();
        this.vejstykkeRepository.flush();
        this.vejstykkeRepository.clear();
        this.adgangsAdresseRepository.flush();
        this.adgangsAdresseRepository.clear();
        this.enhedsAdresseRepository.flush();
        this.enhedsAdresseRepository.clear();
        this.lokalitetRepository.flush();
        this.lokalitetRepository.clear();
        this.postNummerRepository.flush();
        this.postNummerRepository.clear();
        this.entityManagerFactory.getCache().evictAll();
        this.resetAllCaches();*/
    }

    private int itemCounter = 0;

    private void itemAdded(boolean noFlush) {
        this.itemCounter++;
        /*if (!noFlush && this.itemCounter >= 50000) {
            this.itemCounter = 0;
            this.flush();
        }*/
    }

    private Logger log = Logger.getLogger(DawaModel.class);

    //------------------------------------------------------------------------------------------------------------------

    public void resetAllCaches() {
    }

    public void onTransactionEnd() {
        System.out.println("DawaModel.onTransactionEnd()");
        /*this.transactionKommuneCache.clear();
        this.transactionVejstykkeCache.clear();
        this.transactionPostnummerCache.clear();
        this.transactionAdgangsAdresseCache.clear();
        this.transactionEnhedsAdresseCache.clear();
        this.transactionLokalitetCache.clear();*/
    }

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private KommuneRepository kommuneRepository;


    @PostConstruct
    private void setupKommuneRepository() {
        this.kommuneRepository.setUseCachedDescriptorList(true);
    }

    public KommuneEntity setKommune(int kode, String navn,
                                    RegistreringInfo registreringInfo) {
        return this.setKommune(kode, navn, registreringInfo, new ArrayList<VirkningEntity>());
    }

    public KommuneEntity setKommune(int kode, String navn,
                                    RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {
        boolean changed = false;
        Session session = registreringInfo.getSession();
        KommuneEntity kommuneEntity = this.kommuneRepository.getByDescriptor(KommuneEntity.generateDescriptor(kode), session);
        boolean entityExists = (kommuneEntity != null);
        if (!entityExists) {
            this.log.trace("Creating new KommuneEntity " + kode);
            kommuneEntity = new KommuneEntity();
            kommuneEntity.setKode(kode);
            changed = true;
        }
        if (!kommuneEntity.matches(kode, navn)) {
            kommuneEntity.setNavn(navn);
            changed = true;
        }

        if (changed) {
            this.putKommune(kommuneEntity);
            this.kommuneRepository.save(kommuneEntity, session, entityExists);

        }
        return kommuneEntity;
    }

    //private Level1Container<KommuneEntity> transactionKommuneCache = new Level1Container<KommuneEntity>();

    public KommuneEntity getKommune(int kode) {
        KommuneEntity kommuneEntity = null;//this.transactionKommuneCache.get(kode);
        if (kommuneEntity == null) {
            kommuneEntity = this.kommuneRepository.getByKode(kode);
            if (kommuneEntity != null) {
                this.putKommune(kommuneEntity);
            }
        }
        return kommuneEntity;
    }
    public KommuneEntity getKommune(int kode, Session session) {
        KommuneEntity kommuneEntity = this.kommuneRepository.getByDescriptor(kode, session);
        if (kommuneEntity != null) {
            this.putKommune(kommuneEntity);
        }
        return kommuneEntity;
    }

    private void putKommune(KommuneEntity kommuneEntity) {
        this.kommuneRepository.addKnownDescriptor(kommuneEntity.getKode(), true);
    }

    public Collection<KommuneEntity> getKommune(SearchParameters parameters) {
        return this.kommuneRepository.search(parameters);
    }

    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private VejstykkeRepository vejstykkeRepository;

    @PostConstruct
    private void setupVejstykkeRepository() {
        this.vejstykkeRepository.setUseCachedDescriptorList(true);
    }


    private int roadCount = 0;
    private TimeRecorder roadTimeRecorder = new TimeRecorder();


    /*
    * Add or modify a road
    * */
    public VejstykkeEntity setVejstykke(int kommuneKode, int vejKode, String vejNavn, String vejAddresseringsnavn,
                                        RegistreringInfo registreringInfo) {
        return this.setVejstykke(kommuneKode, vejKode, vejNavn, vejAddresseringsnavn, registreringInfo, new ArrayList<VirkningEntity>());
    }

    public VejstykkeEntity setVejstykke(int kommuneKode, int vejKode, String vejNavn, String vejAddresseringsnavn,
                                        RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {
        TimeRecorder time = new TimeRecorder();
        Session session = registreringInfo.getSession();

        //VejstykkeEntity vejstykkeEntity = this.vejstykkeRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
        VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode, session);
        time.record();
        //VejstykkeEntity vejstykkeEntity = this.vejstykkeCache.get(kommuneKode, vejKode);
        boolean entityExists = (vejstykkeEntity != null);
        if (!entityExists) {
            KommuneEntity kommuneEntity = this.getKommune(kommuneKode, session);
            if (kommuneEntity == null) {
                this.log.warn("Unable to create vej " + kommuneKode + ":" + vejKode + " (" + vejNavn + "); Kommune with kode " + kommuneKode + " not found. (There are " + this.kommuneRepository.count(session) + " KommuneEntities in the table)");
                return null;
            }
            this.log.trace("Creating new VejstykkeEntity " + kommuneKode + ":" + vejKode);
            vejstykkeEntity = new VejstykkeEntity();
            vejstykkeEntity.setKode(vejKode);
            vejstykkeEntity.setKommune(kommuneEntity);
            vejstykkeEntity.generateDescriptor();
            this.vejstykkeRepository.save(vejstykkeEntity, session, false);

        }
        time.record();
        VejstykkeVersionEntity vejstykkeVersionEntity = vejstykkeEntity.getLatestVersion();
        if (vejstykkeVersionEntity == null) {
            this.log.trace("Creating initial VejstykkeVersionEntity");
            vejstykkeVersionEntity = vejstykkeEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else {
            if (!vejstykkeVersionEntity.matches(vejNavn, vejAddresseringsnavn)) {
                this.log.trace("Creating updated VejstykkeVersionEntity");
                vejstykkeVersionEntity = vejstykkeEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
            } else {
                // No need to update anything
                vejstykkeVersionEntity = null;
            }
        }
        time.record();

        if (vejstykkeVersionEntity != null) {

            vejstykkeVersionEntity.setVejnavn(vejNavn);
            vejstykkeVersionEntity.setVejadresseringsnavn(vejAddresseringsnavn);
            this.putVejstykke(vejstykkeEntity);
            this.vejstykkeRepository.save(vejstykkeVersionEntity, session, false);
            this.vejstykkeRepository.save(vejstykkeEntity, session, true);
        }
        time.record();
        this.itemAdded(false);

        roadTimeRecorder.add(time);
        roadCount++;
        if (roadCount % 1000 == 0) {
            System.out.println("model ("+roadCount+"): "+roadTimeRecorder);
            roadTimeRecorder = new TimeRecorder();
        }

        return vejstykkeEntity;
    }

    /*
    * Obtain roads from DB
    * */

    //private Level2Container<VejstykkeEntity> transactionVejstykkeCache = new Level2Container<VejstykkeEntity>();
    //private Level2Container<VejstykkeEntity> preloadVejstykkeCache;

    @PostConstruct
    private void loadVejstykkeCache() {
        //this.preloadVejstykkeCache = new Level2Cache<VejstykkeEntity>(this.vejstykkeRepository);
    }

    public VejstykkeEntity getVejstykke(int kommuneKode, int vejKode) {
        return this.getVejstykke(kommuneKode, vejKode, null);
    }

    public VejstykkeEntity getVejstykke(int kommuneKode, int vejKode, Session session) {
        VejstykkeEntity vejstykkeEntity = null;
        if (vejstykkeEntity == null) {
            vejstykkeEntity = this.vejstykkeRepository.getByDescriptor(VejstykkeEntity.generateDescriptor(kommuneKode, vejKode), session);
            if (vejstykkeEntity != null) {
                //this.transactionVejstykkeCache.put(kommuneKode, vejKode, vejstykkeEntity);
                //this.preloadVejstykkeCache.put(kommuneKode, vejKode, vejstykkeEntity);
            }
        }
        return vejstykkeEntity;
    }

    private void putVejstykke(VejstykkeEntity vejstykkeEntity) {
        //this.transactionVejstykkeCache.put(vejstykkeEntity.getKommune().getKode(), vejstykkeEntity.getKode(), vejstykkeEntity);
        //this.preloadVejstykkeCache.put(vejstykkeEntity.getKommune().getKode(), vejstykkeEntity.getKode(), vejstykkeEntity);
        this.vejstykkeRepository.addKnownDescriptor(vejstykkeEntity.getDescriptor(), true);
    }

    public Collection<VejstykkeEntity> getVejstykke(SearchParameters parameters) {
        return this.vejstykkeRepository.search(parameters);
    }

    public VejstykkeEntity getVejstykke(String uuid) {
        return this.vejstykkeRepository.getByUuid(uuid);
    }

    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private PostNummerRepository postNummerRepository;

    public PostNummerEntity setPostNummer(int nummer, String navn, Set<RawVej> veje,
                                          RegistreringInfo registreringInfo) {
        return this.setPostNummer(nummer, navn, veje, registreringInfo, new ArrayList<VirkningEntity>());
    }

    public PostNummerEntity setPostNummer(int nummer, String navn, Set<RawVej> veje,
                                          RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {

        Session session = registreringInfo.getSession();
        //PostNummerEntity postNummerEntity = this.transactionPostnummerCache.get(nummer);
        PostNummerEntity postNummerEntity = this.getPostnummer(nummer, session);
        boolean entityExists = (postNummerEntity != null);
        if (!entityExists) {
            this.log.trace("Creating new postnummerEntity " + nummer);
            postNummerEntity = new PostNummerEntity();
            postNummerEntity.setNr(nummer);
            this.putPostnummer(nummer, postNummerEntity);
            this.postNummerRepository.save(postNummerEntity, session, false);
        }

        // VejstykkeVersionEntities must point to postnumre
        // KommuneEntities must be linked to postnumre by M2M intermediate table


        // Convert List of RawVej to a lists of Veje

        HashSet<VejstykkeEntity> vejListe = new HashSet<VejstykkeEntity>();
        for (RawVej delvej : veje) {
            int kommuneKode = delvej.getKommuneKode();
            int vejKode = delvej.getVejKode();

            // Obtain a VejstykkeVersion to update
            VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode, session);
            if (vejstykkeEntity != null) {
                vejListe.add(vejstykkeEntity);
            } else {
                this.log.warn("Not associating postnummer " + nummer + " with road " + kommuneKode + ":" + vejKode + " (" + VejstykkeEntity.generateDescriptor(kommuneKode,vejKode) + "); road not found");
            }
        }


        // Convert List of RawVej to a list of Kommuner
        /*Level1Container<KommuneEntity> kommuneMap = new Level1Container<KommuneEntity>();
        for (RawVej delvej : veje) {
            int kommuneKode = delvej.getKommuneKode();
            if (!kommuneMap.containsKey(kommuneKode)) {
                kommuneMap.put(kommuneKode, this.getKommune(kommuneKode));
            }
        }*/


        PostNummerVersionEntity postNummerVersionEntity = postNummerEntity.getLatestVersion();
        if (postNummerVersionEntity == null) {
            this.log.trace("Creating initial PostNummerVersionEntity");
            postNummerVersionEntity = postNummerEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);

        } else {
/*
            boolean allHaveThisPostnr = true;
            for (VejstykkeEntity vejstykkeEntity : vejListe) {
                if (!vejstykkeEntity.getLatestVersion().getPostnumre().contains(postNummerEntity)) {
                    allHaveThisPostnr = false;
                    break;
                }
            }*/

            // TODO: consider putting this logic into the entity class, exposing just setter methods and handling versioning from there
            if (!postNummerVersionEntity.matches(navn)/* ||
                    !allHaveThisPostnr ||
                    !postNummerVersionEntity.getKommuner().containsAll(kommuneMap.getList())*/) {
                this.log.trace("Creating updated PostNummerVersionEntity");
                postNummerVersionEntity = postNummerEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
            } else {
                // No need to update anything
                postNummerVersionEntity = null;
            }
        }

        if (postNummerVersionEntity != null) {
            postNummerVersionEntity.setNavn(navn);
/*
            for (KommuneEntity kommuneEntity : kommuneMap.getList()) {
                if (kommuneEntity != null && !kommuneEntity.getPostnumre().contains(postNummerVersionEntity)) {
                    postNummerVersionEntity.addKommune(kommuneEntity);
                    this.kommuneRepository.save(kommuneEntity, registreringInfo.getSession(), true);
                }
            }*/

            this.postNummerRepository.save(postNummerVersionEntity, session, false);
            this.postNummerRepository.save(postNummerEntity, session, true);
        }

/*
        // Update postnummer ref on vejstykkeEntities
        for (VejstykkeEntity vejstykkeEntity : vejListe) {
            VejstykkeVersionEntity vejstykkeVersionEntity = vejstykkeEntity.getLatestVersion();
            // If the newest version isn't made by us (TODO: make sure we really want that)
            // create a new version copied from the old
            if (vejstykkeVersionEntity != null) {
                boolean useOldVersion = true;
                if (!registreringInfo.has(vejstykkeVersionEntity.getRegistrering())) {
                    useOldVersion = false;
                    this.vejstykkeRepository.save(vejstykkeEntity, session, true);
                    VejstykkeVersionEntity oldVersion = vejstykkeVersionEntity;
                    vejstykkeVersionEntity = vejstykkeEntity.addVersion(registreringInfo.getUpdateRegisterering());
                    vejstykkeVersionEntity.copyFrom(oldVersion);
                }
                vejstykkeVersionEntity.addPostnummer(postNummerEntity);
                this.vejstykkeRepository.save(vejstykkeVersionEntity, session, useOldVersion);
                this.vejstykkeRepository.save(vejstykkeEntity, session, true);
            } else {
                //System.err.println("vej ("+vejstykkeEntity.getDescriptor()+") has no version");
            }
        }
*/

/*
        for (RawVej delvej : veje) {
            int kommuneKode = delvej.getKommuneKode();
            int vejKode = delvej.getVejKode();
            int husnrFra = delvej.getHusnrFra();
            int husnrTil = delvej.getHusnrTil();

            VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode, session);
            if (vejstykkeEntity != null) {

                for (AdgangsAdresseEntity adgangsAdresseEntity : vejstykkeEntity.getAdgangsAdresser()) {
                    int husnr = adgangsAdresseEntity.getIntHusnr();
                    if (husnr % 2 == husnrFra % 2 && husnr >= husnrFra && husnr <= husnrTil) {
                        AdgangsAdresseVersionEntity adgangsAdresseVersionEntity = adgangsAdresseEntity.getLatestVersion();
                        if (adgangsAdresseVersionEntity == null) {
                            adgangsAdresseVersionEntity = adgangsAdresseEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
                        } else if (adgangsAdresseVersionEntity.getPostnummer() != postNummerEntity) {
                            adgangsAdresseVersionEntity = adgangsAdresseEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
                        } else {
                            adgangsAdresseVersionEntity = null;
                        }
                        if (adgangsAdresseVersionEntity != null) {
                            adgangsAdresseEntity.getLatestVersion().setPostnummer(postNummerEntity);

                            this.adgangsAdresseRepository.save(adgangsAdresseVersionEntity, session, false);
                            this.adgangsAdresseRepository.save(adgangsAdresseEntity, session, true);
                        }
                    }
                }
            }
        }*/
        this.itemAdded(false);
        return postNummerEntity;
    }

    private Level1Container<PostNummerEntity> transactionPostnummerCache = new Level1Container<PostNummerEntity>();

    public PostNummerEntity getPostnummer(int postnr) {
        return this.getPostnummer(postnr, null);
    }

    public PostNummerEntity getPostnummer(int postnr, Session session) {
        PostNummerEntity postNummerEntity = null;//this.transactionPostnummerCache.get(postnr);
        if (postNummerEntity == null) {
            postNummerEntity = this.postNummerRepository.getByDescriptor(postnr, session);
            if (postNummerEntity != null) {
                //this.putPostnummer(postnr, postNummerEntity);
            }
        }
        return postNummerEntity;
    }

    public Collection<PostNummerEntity> getPostnummer(SearchParameters parameters) {
        return this.postNummerRepository.search(parameters);
    }

    private void putPostnummer(int nr, PostNummerEntity postNummerEntity) {
        //this.transactionPostnummerCache.put(nr, postNummerEntity);
        this.postNummerRepository.addKnownDescriptor(postNummerEntity.getDescriptor(), true);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private AdgangsAdresseRepository adgangsAdresseRepository;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private EnhedsAdresseRepository enhedsAdresseRepository;

    public EnhedsAdresseEntity setAdresse(int kommuneKode, int vejKode, String husNr, String bnr, String etage, String doer,
                                           RegistreringInfo registreringInfo) {
        return this.setAdresse(kommuneKode, vejKode, husNr, bnr, etage, doer, null, registreringInfo, new ArrayList<VirkningEntity>(), false, false);
    }


    private int addressCount = 0;
    private TimeRecorder addressTimeRecorder = new TimeRecorder();

    public EnhedsAdresseEntity setAdresse(int kommuneKode, int vejKode, String husNr, String bnr, String etage, String doer, String kaldenavn,
                                          RegistreringInfo registreringInfo, List<VirkningEntity> virkninger, boolean deferWiring, boolean noFlush) {

        Session session = registreringInfo.getSession();

        AdgangsAdresseEntity adgangsAdresseEntity = null;
        EnhedsAdresseEntity enhedsAdresseEntity = null;

        if (etage != null && etage.isEmpty()) {
            etage = null;
        }
        if (doer != null && doer.isEmpty()) {
            doer = null;
        }
        if (bnr != null && bnr.isEmpty()) {
            bnr = null;
        }
        if (husNr == null) {
            husNr = "";
        }
        TimeRecorder time = new TimeRecorder();

        adgangsAdresseEntity = this.getAdgangsAdresse(kommuneKode, vejKode, husNr, session);
        boolean entityExists = (adgangsAdresseEntity != null);

        time.record();

        if (!entityExists) {
            //System.out.println("didn't find adgangsadresse " + kommuneKode + ":" + vejKode + ":" + husNr + ", creating");

            adgangsAdresseEntity = new AdgangsAdresseEntity();
            if (deferWiring) {
                adgangsAdresseEntity.setVejstykkeDescriptor(VejstykkeEntity.generateDescriptor(kommuneKode, vejKode));
            } else {
                VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode, session);
                if (vejstykkeEntity == null) {
                    //this.log.warn("Not adding address " + husNr + " on road " + kommuneKode + ":" + vejKode + "; road not found (vejstykkecache size: "+this.transactionVejstykkeCache.totalSize()+")");
                    //this.log.warn("Not adding address " + husNr + " on road " + kommuneKode + ":" + vejKode + "; road not found (vejstykkecache size: "+this.preloadVejstykkeCache.totalSize()+")");
                    return null;
                }
                adgangsAdresseEntity.setVejstykke(vejstykkeEntity);
            }
            adgangsAdresseEntity.setHusnr(husNr);
            adgangsAdresseEntity.setBnr(bnr);
            adgangsAdresseEntity.generateDescriptor();
            this.adgangsAdresseRepository.save(adgangsAdresseEntity, session, false);
            this.putAdgangsAdresse(kommuneKode, vejKode, husNr, adgangsAdresseEntity);
            this.log.trace("Creating new AdgangsAdresseEntity");
        } else {
            //System.out.println("Did find adgangsadresse "+kommuneKode+":"+vejKode+":"+husNr);
        }

        time.record();

        AdgangsAdresseVersionEntity adgangsAdresseVersionEntity = adgangsAdresseEntity.getLatestVersion();
        if (adgangsAdresseVersionEntity == null) {
            adgangsAdresseVersionEntity = adgangsAdresseEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
            this.log.trace("Creating initial AdgangsAdresseVersionEntity");
        } else if (false) {
            this.log.trace("Creating updated AdgangsAdresseVersionEntity");
            adgangsAdresseVersionEntity = adgangsAdresseEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
        } else {
            adgangsAdresseVersionEntity = null;
        }
        time.record();


        if (adgangsAdresseVersionEntity != null) {
            this.adgangsAdresseRepository.save(adgangsAdresseVersionEntity, session, false);
            this.adgangsAdresseRepository.save(adgangsAdresseEntity, session, true);
            this.putAdgangsAdresse(kommuneKode, vejKode, husNr, adgangsAdresseEntity);
        }
        time.record();

        enhedsAdresseEntity = this.getEnhedsAdresse(kommuneKode, vejKode, husNr, etage, doer, session);
        entityExists = (enhedsAdresseEntity != null);

        time.record();
        if (enhedsAdresseEntity == null) {
            enhedsAdresseEntity = new EnhedsAdresseEntity();
            enhedsAdresseEntity.setAdgangsadresse(adgangsAdresseEntity);
            enhedsAdresseEntity.setEtage(etage);
            enhedsAdresseEntity.setDoer(doer);
            enhedsAdresseEntity.setDescriptor(enhedsAdresseEntity.generateDescriptor(kommuneKode, vejKode, husNr, etage, doer));
            this.enhedsAdresseRepository.save(enhedsAdresseEntity, session, false);
            this.putEnhedsAdresse(kommuneKode, vejKode, husNr, enhedsAdresseEntity);
            this.log.trace("Creating new EnhedsAdresseEntity");
        }
        time.record();

        EnhedsAdresseVersionEntity enhedsAdresseVersionEntity = enhedsAdresseEntity.getLatestVersion();
        if (enhedsAdresseVersionEntity == null) {
            enhedsAdresseVersionEntity = enhedsAdresseEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
            this.log.trace("Creating initial EnhedsAdresseVersionEntity");
        } else if (!enhedsAdresseVersionEntity.matches(kaldenavn)) {
            enhedsAdresseVersionEntity = enhedsAdresseEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
            this.log.trace("Creating updated EnhedsAdresseVersionEntity");
        } else {
            enhedsAdresseVersionEntity = null;
        }
        time.record();

        if (enhedsAdresseVersionEntity != null) {
            enhedsAdresseVersionEntity.setKaldenavn(kaldenavn);
            this.putEnhedsAdresse(kommuneKode, vejKode, husNr, enhedsAdresseEntity);
            this.enhedsAdresseRepository.save(enhedsAdresseVersionEntity, session, false);
            this.enhedsAdresseRepository.save(enhedsAdresseEntity, session, true);
        }


        this.itemAdded(noFlush);
        addressTimeRecorder.add(time);
        addressCount++;
        if (addressCount % 1000 == 0) {
            System.out.println("model ("+addressCount+"): "+addressTimeRecorder);
            addressTimeRecorder = new TimeRecorder();
            //this.adgangsAdresseRepository.flush();
            //this.enhedsAdresseRepository.flush();
        }
        return enhedsAdresseEntity;
    }


    public AdgangsAdresseEntity getAdgangsAdresse(String uuid) {
        return this.adgangsAdresseRepository.getByUuid(uuid);
    }

    public Collection<AdgangsAdresseEntity> getAdgangsAdresse(SearchParameters parameters) {
        return this.adgangsAdresseRepository.search(parameters);
    }

    private Level3Container<AdgangsAdresseEntity> transactionAdgangsAdresseCache = new Level3Container<AdgangsAdresseEntity>();

    public AdgangsAdresseEntity getAdgangsAdresse(int kommunekode, int vejkode, String husnr) {
        return this.getAdgangsAdresse(kommunekode, vejkode, husnr, null);
    }

    public AdgangsAdresseEntity getAdgangsAdresse(int kommunekode, int vejkode, String husnr, Session session) {
        AdgangsAdresseEntity adgangsAdresseEntity = null;//this.transactionAdgangsAdresseCache.get(kommunekode, vejkode, husnr);
        if (adgangsAdresseEntity == null) {
            adgangsAdresseEntity = this.adgangsAdresseRepository.getByDescriptor(AdgangsAdresseEntity.generateDescriptor(kommunekode, vejkode, husnr), session);
            if (adgangsAdresseEntity != null) {
                //this.putAdgangsAdresse(kommunekode, vejkode, husnr, adgangsAdresseEntity);
            }

        }
        return adgangsAdresseEntity;
    }

    public void putAdgangsAdresse(int kommuneKode, int vejKode, String husNr, AdgangsAdresseEntity adgangsAdresseEntity) {
        //this.transactionAdgangsAdresseCache.put(kommuneKode, vejKode, husNr, adgangsAdresseEntity);
        this.adgangsAdresseRepository.addKnownDescriptor(adgangsAdresseEntity.getDescriptor(), true);
    }




    public EnhedsAdresseEntity getEnhedsAdresse(String uuid) {
        return this.enhedsAdresseRepository.getByUuid(uuid);
    }

    public Collection<EnhedsAdresseEntity> getEnhedsAdresse(SearchParameters parameters) {
        return this.enhedsAdresseRepository.search(parameters);
    }

    private Level4Container<EnhedsAdresseEntity> transactionEnhedsAdresseCache = new Level4Container<EnhedsAdresseEntity>();

    public EnhedsAdresseEntity getEnhedsAdresse(int kommuneKode, int vejKode, String husnr, String etage, String doer) {
        return this.getEnhedsAdresse(kommuneKode, vejKode, husnr, etage, doer, null);
    }
    public EnhedsAdresseEntity getEnhedsAdresse(int kommuneKode, int vejKode, String husnr, String etage, String doer, Session session) {
        EnhedsAdresseEntity enhedsAdresseEntity = null;//this.transactionEnhedsAdresseCache.get(kommuneKode, vejKode, husnr, EnhedsAdresseEntity.getFinalIdentifier(etage, doer));
        if (enhedsAdresseEntity == null) {
            enhedsAdresseEntity = this.enhedsAdresseRepository.getByDescriptor(EnhedsAdresseEntity.generateDescriptor(kommuneKode, vejKode, husnr, etage, doer), session);
            if (enhedsAdresseEntity != null) {
                //this.putEnhedsAdresse(kommuneKode, vejKode, husnr, enhedsAdresseEntity);
            }
        }
        return enhedsAdresseEntity;
    }
    private void putEnhedsAdresse(int kommuneKode, int vejKode, String husnr, EnhedsAdresseEntity enhedsAdresseEntity) {
        //this.transactionEnhedsAdresseCache.put(kommuneKode, vejKode, husnr, EnhedsAdresseEntity.getFinalIdentifier(enhedsAdresseEntity.getEtage(), enhedsAdresseEntity.getDoer()), enhedsAdresseEntity);
        this.enhedsAdresseRepository.addKnownDescriptor(enhedsAdresseEntity.getDescriptor(), true);
    }



    public List<TransactionCallback> getBulkwireCallbacks() {
        return this.adgangsAdresseRepository.getBulkwireCallbacks();
    }

    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private LokalitetRepository lokalitetRepository;

    private TimeRecorder lokalitetRecorder = new TimeRecorder();
    private int lokalitetCount = 0;

    public void setLokalitet(int kommuneKode, String lokalitetsnavn, Set<RawVej> veje,
                             RegistreringInfo registreringInfo) {
        this.setLokalitet(kommuneKode, lokalitetsnavn, veje, registreringInfo, new ArrayList<VirkningEntity>());
    }

    public void setLokalitet(int kommuneKode, String lokalitetsnavn, Set<RawVej> veje,
                             RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {

        TimeRecorder time = new TimeRecorder();
        Session session = registreringInfo.getSession();
        LokalitetEntity lokalitetEntity = this.getLokalitet(kommuneKode, lokalitetsnavn);
        boolean entityExists = (lokalitetEntity != null);

        time.record();
        if (!entityExists) {
            KommuneEntity kommuneEntity = this.getKommune(kommuneKode, session);
            if (kommuneEntity != null) {
                lokalitetEntity = new LokalitetEntity();
                lokalitetEntity.setNavn(lokalitetsnavn);
                lokalitetEntity.setKommune(kommuneEntity);
                this.putLokalitet(kommuneKode, lokalitetsnavn, lokalitetEntity);
                this.lokalitetRepository.save(lokalitetEntity, registreringInfo.getSession(), entityExists);
                this.putLokalitet(kommuneKode, lokalitetsnavn, lokalitetEntity);
            }
        }
        time.record();
        for (RawVej vej : veje) {
            int vejKode = vej.getVejKode();
            VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode);

            if (vejstykkeEntity != null) {
                VejstykkeVersionEntity vejstykkeVersionEntity = vejstykkeEntity.getLatestVersion();
                if (vejstykkeVersionEntity != null) {
                    if (!vejstykkeVersionEntity.hasLokalitet(lokalitetEntity) && !registreringInfo.has(vejstykkeVersionEntity.getRegistrering())) {
                        VejstykkeVersionEntity oldVersionEntity = vejstykkeVersionEntity;
                        vejstykkeVersionEntity = vejstykkeEntity.addVersion(registreringInfo.getUpdateRegisterering());
                        vejstykkeVersionEntity.copyFrom(oldVersionEntity);
                    }
                    vejstykkeVersionEntity.addLokalitet(lokalitetEntity);
                    this.vejstykkeRepository.save(vejstykkeVersionEntity, registreringInfo.getSession(), false);
                    this.vejstykkeRepository.save(vejstykkeEntity, registreringInfo.getSession(), true);
                }
            }
        }
        time.record();
        this.itemAdded(false);


        lokalitetRecorder.add(time);
        lokalitetCount++;
        if (lokalitetCount % 1000 == 0) {
            System.out.println("model ("+lokalitetCount+"): "+lokalitetRecorder);
            lokalitetRecorder = new TimeRecorder();
        }
    }

    private Level2Container<LokalitetEntity> transactionLokalitetCache = new Level2Container<LokalitetEntity>();

    public LokalitetEntity getLokalitet(String uuid) {
        return this.lokalitetRepository.getByUuid(uuid);
    }

    public LokalitetEntity getLokalitet(int kommuneKode, String lokalitetsNavn) {
        LokalitetEntity lokalitetEntity = this.transactionLokalitetCache.get(kommuneKode, lokalitetsNavn);
        if (lokalitetEntity == null) {
            lokalitetEntity = this.lokalitetRepository.getByKommunekodeAndLokalitetsnavn(kommuneKode, lokalitetsNavn);
            if (lokalitetEntity != null) {
                this.putLokalitet(kommuneKode, lokalitetsNavn, lokalitetEntity);
            }
        }
        return lokalitetEntity;
    }

    public Collection<LokalitetEntity> getLokalitet(SearchParameters parameters) {
        return this.lokalitetRepository.search(parameters);
    }

    private void putLokalitet(int kommuneKode, String lokalitetsNavn, LokalitetEntity lokalitetEntity) {
        //this.transactionLokalitetCache.put(kommuneKode, lokalitetsNavn, lokalitetEntity);
        this.lokalitetRepository.addKnownDescriptor(lokalitetEntity.getDescriptor(), true);
    }

}