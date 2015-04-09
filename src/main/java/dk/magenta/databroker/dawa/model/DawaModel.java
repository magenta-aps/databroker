package dk.magenta.databroker.dawa.model;

import dk.magenta.databroker.core.RegistreringInfo;
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
import dk.magenta.databroker.util.cache.Level2Cache;
import dk.magenta.databroker.util.objectcontainers.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.*;

/**
 * Created by lars on 19-12-14.
 */

@Component
public class DawaModel {


    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void flush() {
        this.kommuneRepository.flush();
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
        this.resetAllCaches();
    }

    private int itemCounter = 0;

    private void itemAdded(boolean noFlush) {
        this.itemCounter++;
        if (!noFlush && this.itemCounter >= 50000) {
            this.itemCounter = 0;
            this.flush();
        }
    }

    private Logger log = Logger.getLogger(DawaModel.class);

    //------------------------------------------------------------------------------------------------------------------

    public void resetAllCaches() {
    }

    public void onTransactionEnd() {
        this.transactionKommuneCache.clear();
        this.transactionVejstykkeCache.clear();
        this.transactionPostnummerCache.clear();
        this.transactionAdgangsAdresseCache.clear();
        this.transactionEnhedsAdresseCache.clear();
        this.transactionLokalitetCache.clear();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private KommuneRepository kommuneRepository;

    public KommuneEntity setKommune(int kode, String navn,
                                    RegistreringInfo registreringInfo) {
        return this.setKommune(kode, navn, registreringInfo, new ArrayList<VirkningEntity>());
    }

    public KommuneEntity setKommune(int kode, String navn,
                                    RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {
        boolean changed = false;
        KommuneEntity kommuneEntity = this.getKommune(kode);
        if (kommuneEntity == null) {
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
            this.kommuneRepository.save(kommuneEntity);
        }
        return kommuneEntity;
    }

    private Level1Container<KommuneEntity> transactionKommuneCache = new Level1Container<KommuneEntity>();

    public KommuneEntity getKommune(int kode) {
        KommuneEntity kommuneEntity = this.transactionKommuneCache.get(kode);
        if (kommuneEntity == null) {
            kommuneEntity = this.kommuneRepository.getByKode(kode);
            if (kommuneEntity != null) {
                this.putKommune(kommuneEntity);
            }
        }
        return kommuneEntity;
    }

    private void putKommune(KommuneEntity kommuneEntity) {
        this.transactionKommuneCache.put(kommuneEntity.getKode(), kommuneEntity);
    }

    public Collection<KommuneEntity> getKommune(SearchParameters parameters) {
        return this.kommuneRepository.search(parameters);
    }

    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private VejstykkeRepository vejstykkeRepository;


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

        //VejstykkeEntity vejstykkeEntity = this.vejstykkeRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
        VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode);
        time.record();
        //VejstykkeEntity vejstykkeEntity = this.vejstykkeCache.get(kommuneKode, vejKode);
        if (vejstykkeEntity == null) {
            KommuneEntity kommuneEntity = this.getKommune(kommuneKode);
            if (kommuneEntity == null) {
                this.log.warn("Unable to create vej " + kommuneKode + ":" + vejKode + " (" + vejNavn + "); Kommune with kode " + kommuneKode + " not found. (There are " + this.kommuneRepository.count() + " KommuneEntities in the table)");
                return null;
            }
            this.log.trace("Creating new VejstykkeEntity " + kommuneKode + ":" + vejKode);
            vejstykkeEntity = new VejstykkeEntity();
            vejstykkeEntity.setKode(vejKode);
            vejstykkeEntity.setKommune(kommuneEntity);
            vejstykkeEntity.generateDescriptor();
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
            vejstykkeEntity.setLatestVersion(vejstykkeVersionEntity); // TODO: may not always be relevant
            this.putVejstykke(vejstykkeEntity);
            this.vejstykkeRepository.save(vejstykkeEntity);
        }
        time.record();
        this.itemAdded(false);



        roadTimeRecorder.add(time);
        roadCount++;
        if (roadTimeRecorder.getAdded() % 1000 == 0) {
            System.out.println("model ("+roadCount+"): "+roadTimeRecorder);
            roadTimeRecorder = new TimeRecorder();
        }

        return vejstykkeEntity;
    }

    /*
    * Obtain roads from DB
    * */

    private Level2Container<VejstykkeEntity> transactionVejstykkeCache = new Level2Container<VejstykkeEntity>();
    private Level2Container<VejstykkeEntity> preloadVejstykkeCache;

    @PostConstruct
    private void loadVejstykkeCache() {
        this.preloadVejstykkeCache = new Level2Cache<VejstykkeEntity>(this.vejstykkeRepository);
    }

    public VejstykkeEntity getVejstykke(int kommuneKode, int vejKode) {
        TimeRecorder v = new TimeRecorder();
        //VejstykkeEntity vejstykkeEntity = this.transactionVejstykkeCache.get(kommuneKode, vejKode);
        VejstykkeEntity vejstykkeEntity = this.preloadVejstykkeCache.get(kommuneKode, vejKode);
        v.record();
        if (vejstykkeEntity == null) {
            vejstykkeEntity = this.vejstykkeRepository.getByDesc(VejstykkeEntity.generateDescriptor(kommuneKode, vejKode));
            v.record();
            if (vejstykkeEntity != null) {
                //this.putVejstykke(vejstykkeEntity);
                //this.transactionVejstykkeCache.put(kommuneKode, vejKode, vejstykkeEntity);
                this.preloadVejstykkeCache.put(kommuneKode, vejKode, vejstykkeEntity);
            }
            v.record();
        }
        return vejstykkeEntity;
    }

    private void putVejstykke(VejstykkeEntity vejstykkeEntity) {
        //this.transactionVejstykkeCache.put(vejstykkeEntity.getKommune().getKode(), vejstykkeEntity.getKode(), vejstykkeEntity);
        this.preloadVejstykkeCache.put(vejstykkeEntity.getKommune().getKode(), vejstykkeEntity.getKode(), vejstykkeEntity);
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

        PostNummerEntity postNummerEntity = this.transactionPostnummerCache.get(nummer);
        if (postNummerEntity == null) {
            this.log.trace("Creating new postnummerEntity " + nummer);
            postNummerEntity = new PostNummerEntity();
        }

        // Convert List of RawVej to a lists of Veje

        HashSet<VejstykkeEntity> vejListe = new HashSet<VejstykkeEntity>();
        for (RawVej delvej : veje) {
            int kommuneKode = delvej.getKommuneKode();
            int vejKode = delvej.getVejKode();

            // Obtain a VejstykkeVersion to update
            VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode);
            if (vejstykkeEntity != null) {
                vejListe.add(vejstykkeEntity);
            } else {
                this.log.warn("Not associating postnummer " + nummer + " with road " + kommuneKode + ":" + vejKode + "; road not found");
            }
        }


        // Convert List of RawVej to a list of Kommuner
        Level1Container<KommuneEntity> kommuneMap = new Level1Container<KommuneEntity>();
        for (RawVej delvej : veje) {
            int kommuneKode = delvej.getKommuneKode();
            if (!kommuneMap.containsKey(kommuneKode)) {
                kommuneMap.put(kommuneKode, this.getKommune(kommuneKode));
            }
        }


        PostNummerVersionEntity postNummerVersionEntity = postNummerEntity.getLatestVersion();
        if (postNummerVersionEntity == null) {
            this.log.trace("Creating initial PostNummerVersionEntity");
            postNummerVersionEntity = postNummerEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);

        } else {

            boolean allHaveThisPostnr = true;
            for (VejstykkeEntity vejstykkeEntity : vejListe) {
                if (!vejstykkeEntity.getLatestVersion().getPostnumre().contains(postNummerEntity)) {
                    allHaveThisPostnr = false;
                    break;
                }
            }

            // TODO: consider putting this logic into the entity class, exposing just setter methods and handling versioning from there
            if (!postNummerVersionEntity.matches(nummer, navn) ||
                    !allHaveThisPostnr ||
                    !postNummerVersionEntity.getKommuner().containsAll(kommuneMap.getList())) {
                this.log.trace("Creating updated PostNummerVersionEntity");
                postNummerVersionEntity = postNummerEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
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
            if (postNummerVersionEntity != postNummerEntity.getLatestVersion()) {
                postNummerEntity.setLatestVersion(postNummerVersionEntity); // TODO: may not always be relevant
                this.putPostnummer(nummer, postNummerEntity);
                this.postNummerRepository.save(postNummerEntity);
            }
        }


        // Update postnummer ref on vejstykkeEntities
        for (VejstykkeEntity vejstykkeEntity : vejListe) {
            VejstykkeVersionEntity vejstykkeVersionEntity = vejstykkeEntity.getLatestVersion();
            // If the newest version isn't made by us (TODO: make sure we really want that)
            // create a new version copied from the old
            if (!registreringInfo.has(vejstykkeVersionEntity.getRegistrering())) {
                this.vejstykkeRepository.save(vejstykkeEntity);
                VejstykkeVersionEntity oldVersion = vejstykkeVersionEntity;
                vejstykkeVersionEntity = vejstykkeEntity.addVersion(registreringInfo.getUpdateRegisterering());
                vejstykkeVersionEntity.copyFrom(oldVersion);
                vejstykkeEntity.setLatestVersion(vejstykkeVersionEntity);
            }
            vejstykkeVersionEntity.addPostnummer(postNummerEntity);
            this.vejstykkeRepository.save(vejstykkeEntity);
        }


        for (RawVej delvej : veje) {
            int kommuneKode = delvej.getKommuneKode();
            int vejKode = delvej.getVejKode();
            int husnrFra = delvej.getHusnrFra();
            int husnrTil = delvej.getHusnrTil();

            VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode);
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
                            this.adgangsAdresseRepository.save(adgangsAdresseEntity);
                        }
                    }
                }
            }
        }
        this.itemAdded(false);
        return postNummerEntity;
    }

    private Level1Container<PostNummerEntity> transactionPostnummerCache = new Level1Container<PostNummerEntity>();

    public PostNummerEntity getPostnummer(int postnr) {
        PostNummerEntity postNummerEntity = this.transactionPostnummerCache.get(postnr);
        if (postNummerEntity == null) {
            postNummerEntity = this.postNummerRepository.getByNr(postnr);
            if (postNummerEntity != null) {
                this.putPostnummer(postnr, postNummerEntity);
            }
        }
        return postNummerEntity;
    }

    public Collection<PostNummerEntity> getPostnummer(SearchParameters parameters) {
        return this.postNummerRepository.search(parameters);
    }

    private void putPostnummer(int nr, PostNummerEntity postNummerEntity) {
        this.transactionPostnummerCache.put(nr, postNummerEntity);
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

        adgangsAdresseEntity = this.getAdgangsAdresse(kommuneKode, vejKode, husNr);

        time.record();

        if (adgangsAdresseEntity == null) {
            //System.out.println("didn't find adgangsadresse " + kommuneKode + ":" + vejKode + ":" + husNr + ", creating");

            adgangsAdresseEntity = new AdgangsAdresseEntity();
            if (deferWiring) {
                adgangsAdresseEntity.setVejstykkeDescriptor(VejstykkeEntity.generateDescriptor(kommuneKode, vejKode));
            } else {
                VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode);
                if (vejstykkeEntity == null) {
                    //this.log.warn("Not adding address " + husNr + " on road " + kommuneKode + ":" + vejKode + "; road not found (vejstykkecache size: "+this.transactionVejstykkeCache.totalSize()+")");
                    this.log.warn("Not adding address " + husNr + " on road " + kommuneKode + ":" + vejKode + "; road not found (vejstykkecache size: "+this.preloadVejstykkeCache.totalSize()+")");
                    return null;
                }
                adgangsAdresseEntity.setVejstykke(vejstykkeEntity);
            }
            adgangsAdresseEntity.setHusnr(husNr);
            adgangsAdresseEntity.setBnr(bnr);
            adgangsAdresseEntity.generateDescriptor();
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
            adgangsAdresseEntity.setLatestVersion(adgangsAdresseVersionEntity);
            this.adgangsAdresseRepository.save(adgangsAdresseEntity);
            this.putAdgangsAdresse(kommuneKode, vejKode, husNr, adgangsAdresseEntity);
        }
        time.record();

        enhedsAdresseEntity = this.getEnhedsAdresse(kommuneKode, vejKode, husNr, etage, doer);

        time.record();
        if (enhedsAdresseEntity == null) {
            enhedsAdresseEntity = new EnhedsAdresseEntity();
            enhedsAdresseEntity.setAdgangsadresse(adgangsAdresseEntity);
            enhedsAdresseEntity.setEtage(etage);
            enhedsAdresseEntity.setDoer(doer);
            enhedsAdresseEntity.setDescriptor(enhedsAdresseEntity.generateDescriptor(kommuneKode, vejKode, husNr, etage, doer));
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
            this.enhedsAdresseRepository.save(enhedsAdresseEntity);
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
        AdgangsAdresseEntity adgangsAdresseEntity = this.transactionAdgangsAdresseCache.get(kommunekode, vejkode, husnr);
        if (adgangsAdresseEntity == null) {
            adgangsAdresseEntity = this.adgangsAdresseRepository.getByDescriptor(AdgangsAdresseEntity.generateDescriptor(kommunekode, vejkode, husnr));
            if (adgangsAdresseEntity != null) {
                this.putAdgangsAdresse(kommunekode, vejkode, husnr, adgangsAdresseEntity);
            }

        }
        return adgangsAdresseEntity;
    }

    public void putAdgangsAdresse(int kommuneKode, int vejKode, String husNr, AdgangsAdresseEntity adgangsAdresseEntity) {
        this.transactionAdgangsAdresseCache.put(kommuneKode, vejKode, husNr, adgangsAdresseEntity);
    }




    public EnhedsAdresseEntity getEnhedsAdresse(String uuid) {
        return this.enhedsAdresseRepository.getByUuid(uuid);
    }

    public Collection<EnhedsAdresseEntity> getEnhedsAdresse(SearchParameters parameters) {
        return this.enhedsAdresseRepository.search(parameters);
    }

    private Level4Container<EnhedsAdresseEntity> transactionEnhedsAdresseCache = new Level4Container<EnhedsAdresseEntity>();

    public EnhedsAdresseEntity getEnhedsAdresse(int kommuneKode, int vejKode, String husnr, String etage, String doer) {
        EnhedsAdresseEntity enhedsAdresseEntity = this.transactionEnhedsAdresseCache.get(kommuneKode, vejKode, husnr, EnhedsAdresseEntity.getFinalIdentifier(etage, doer));
        if (enhedsAdresseEntity == null) {
            enhedsAdresseEntity = this.enhedsAdresseRepository.getByDescriptor(EnhedsAdresseEntity.generateDescriptor(kommuneKode, vejKode, husnr, etage, doer));
            if (enhedsAdresseEntity != null) {
                this.putEnhedsAdresse(kommuneKode, vejKode, husnr, enhedsAdresseEntity);
            }
        }
        return enhedsAdresseEntity;
    }
    private void putEnhedsAdresse(int kommuneKode, int vejKode, String husnr, EnhedsAdresseEntity enhedsAdresseEntity) {
        this.transactionEnhedsAdresseCache.put(kommuneKode, vejKode, husnr, EnhedsAdresseEntity.getFinalIdentifier(enhedsAdresseEntity.getEtage(), enhedsAdresseEntity.getDoer()), enhedsAdresseEntity);
    }



    public List<TransactionCallback> getBulkwireCallbacks() {
        return this.adgangsAdresseRepository.getBulkwireCallbacks();
    }

    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private LokalitetRepository lokalitetRepository;

    public void setLokalitet(int kommuneKode, String lokalitetsnavn, Set<RawVej> veje,
                             RegistreringInfo registreringInfo) {
        this.setLokalitet(kommuneKode, lokalitetsnavn, veje, registreringInfo, new ArrayList<VirkningEntity>());
    }

    public void setLokalitet(int kommuneKode, String lokalitetsnavn, Set<RawVej> veje,
                             RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {

        LokalitetEntity lokalitetEntity = this.getLokalitet(kommuneKode, lokalitetsnavn);

        if (lokalitetEntity == null) {
            KommuneEntity kommuneEntity = this.getKommune(kommuneKode);
            if (kommuneEntity != null) {
                lokalitetEntity = new LokalitetEntity();
                lokalitetEntity.setNavn(lokalitetsnavn);
                lokalitetEntity.setKommune(kommuneEntity);
                this.putLokalitet(kommuneKode, lokalitetsnavn, lokalitetEntity);
                this.lokalitetRepository.save(lokalitetEntity);
            }
        }
        for (RawVej vej : veje) {
            int vejKode = vej.getVejKode();
            VejstykkeEntity vejstykkeEntity = this.getVejstykke(kommuneKode, vejKode);

            if (vejstykkeEntity != null) {
                VejstykkeVersionEntity vejstykkeVersionEntity = vejstykkeEntity.getLatestVersion();
                if (vejstykkeVersionEntity != null) {
                    if (!vejstykkeVersionEntity.hasLokalitet(lokalitetEntity) && !registreringInfo.has(vejstykkeVersionEntity.getRegistrering())) {
                        this.vejstykkeRepository.save(vejstykkeEntity);
                        VejstykkeVersionEntity oldVersionEntity = vejstykkeVersionEntity;
                        vejstykkeVersionEntity = vejstykkeEntity.addVersion(registreringInfo.getUpdateRegisterering());
                        vejstykkeVersionEntity.copyFrom(oldVersionEntity);
                    }
                    vejstykkeVersionEntity.addLokalitet(lokalitetEntity);
                    this.vejstykkeRepository.save(vejstykkeEntity);
                }
            }
        }
        this.itemAdded(false);
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
        this.transactionLokalitetCache.put(kommuneKode, lokalitetsNavn, lokalitetEntity);
    }

}