package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.register.objectcontainers.*;
import dk.magenta.databroker.register.records.Record;
import dk.magenta.databroker.cprvejregister.dataproviders.registers.VejRegister;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejVersionEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by lars on 16-12-14.
 */
public class VejModel {

    private KommuneRepository kommuneRepository;
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;
    private NavngivenVejRepository navngivenVejRepository;
    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegistrering;


    public VejModel(KommuneRepository kommuneRepository, KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository, NavngivenVejRepository navngivenVejRepository, RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        this.kommuneRepository = kommuneRepository;
        this.kommunedelAfNavngivenVejRepository = kommunedelAfNavngivenVejRepository;
        this.navngivenVejRepository = navngivenVejRepository;
        this.createRegistrering = createRegistrering;
        this.updateRegistrering = updateRegistrering;
    }

    public void createVeje(List<Record> records) {
        System.out.println("Creating roads ("+records.size()+" items)");

        tic();
        System.out.println("Precaching entities");
        Level1Container<KommuneEntity> kommuneCache = this.loadKommuneCache();
        Level2Container<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVejCache = this.loadKommuneDelAfNavngivenVejCache();
        Level2Container<NavngivenVejEntity> navngivenVejCache = this.loadNavngivenVejCache();
        System.out.println("Precaching took "+toc()+" ms");

        EntityModificationCounter navngivenvejCounter = new EntityModificationCounter();
        EntityModificationCounter kommunedelAfNavngivenVejCounter = new EntityModificationCounter();
        InputProcessingCounter inputCounter = new InputProcessingCounter(true);

        for (Record record : records) {
            int kommuneKode = record.getInt("kommuneKode");
            int vejKode = record.getInt("vejKode");
            String vejNavn = record.get("vejNavn");
            String vejAdresseringsnavn = record.get("vejAdresseringsnavn");
            if (kommuneKode != 0 && vejKode != 0 && vejNavn != null) {
                this.createRoad((VejRegister.AktivVej) record, kommuneCache, kommunedelAfNavngivenVejCache, navngivenVejCache, navngivenvejCounter, kommunedelAfNavngivenVejCounter);
                inputCounter.printInputProcessed();
            }
        }

        inputCounter.printFinalInputsProcessed();
        System.out.println("Stored NavngivenVejEntities to database");
        navngivenvejCounter.printModifications();
        System.out.println("Stored KommunedelAfNavngivenVejEntities to database");
        kommunedelAfNavngivenVejCounter.printModifications();
    }


    /*private void createRoad(int kommuneKode, int vejKode, String vejNavn, String vejAdresseringsNavn,
                            Level1Container<KommuneEntity> kommuneCache, Level2Container<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVejCache, Level2Container<NavngivenVejEntity> navngivenVejCache,
                            EntityModificationCounter navngivenVejCounter, EntityModificationCounter kommunedelAfNavngivenVejCounter) {

    }*/
    private void createRoad(VejRegister.AktivVej aktivVej,
                Level1Container<KommuneEntity> kommuneCache, Level2Container<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVejCache, Level2Container<NavngivenVejEntity> navngivenVejCache,
                EntityModificationCounter navngivenVejCounter, EntityModificationCounter kommunedelAfNavngivenVejCounter) {
            ArrayList<Long> time = new ArrayList<Long>();

        int kommuneKode = aktivVej.getInt("kommuneKode");
        int vejKode = aktivVej.getInt("vejKode");
        String vejNavn = aktivVej.get("vejNavn");
        String vejAdresseringsNavn = aktivVej.get("vejAdresseringsnavn");


        //KommuneEntity kommune = kommuneRepository.getByKommunekode(kommuneKode);
        KommuneEntity kommune = kommuneCache.get(kommuneKode);

        if (kommune == null) {
            System.err.println("Kommune with id "+kommuneKode+" not found for vej "+aktivVej.get("vejNavn"));
        } else {
            KommunedelAfNavngivenVejEntity delvejEntity = kommunedelAfNavngivenVejCache.get(kommuneKode, vejKode);

            boolean createdDelvej = false;
            boolean updatedDelvej = false;

            NavngivenVejEntity navngivenVej = null;
            NavngivenVejVersionEntity navngivenVejVersion;

            // If we don't have a KommunedelAfNavngivenVejEntity for this vejKode and kommune, create one
            if (delvejEntity == null) {
                delvejEntity = KommunedelAfNavngivenVejEntity.create();
                delvejEntity.setVejkode(vejKode);
                delvejEntity.setKommune(kommune);
                kommunedelAfNavngivenVejCounter.countCreatedItem();
                updatedDelvej = createdDelvej = true;
            } else {
                // Since we do have a delvej, try to find an existing navngivenvej by looking in it
                NavngivenVejVersionEntity delvejRefVersion = delvejEntity.getNavngivenVejVersion();
                if (delvejRefVersion != null) {
                    NavngivenVejEntity navngivenVejEntity = delvejRefVersion.getEntity();
                    if (navngivenVejEntity != null && vejNavn.equals(navngivenVejEntity.getLatestVersion().getVejnavn())) {
                        navngivenVej = navngivenVejEntity;
                    }
                }
            }

            if (navngivenVej == null) {
                for (VejRegister.AktivVej otherVej : aktivVej.getConnections()) {
                    navngivenVej = findNavngivenVejByAktivVej(otherVej, vejNavn, kommunedelAfNavngivenVejCache);
                    if (navngivenVej != null) {
                        break;
                    }
                }
            }

            // No navngivenvej instances found - create one
            // Also create a new vejversion to put data in
            if (navngivenVej == null) {
                navngivenVej = NavngivenVejEntity.create();
                navngivenVejVersion = navngivenVej.addVersion(this.createRegistrering);
                navngivenVejCounter.countCreatedItem();
                navngivenVejCache.put(kommuneKode, vejKode, navngivenVej);
            } else {
                // Put a version on our existing navngivenvej
                NavngivenVejVersionEntity latestVersion = navngivenVej.getLatestVersion();
                if (latestVersion.getRegistrering().equals(this.updateRegistrering) || latestVersion.getRegistrering().equals(this.createRegistrering)) {
                    // If we happen to have created a version for an existing vej in this very run, reuse that version
                    navngivenVejVersion = latestVersion;
                } else {
                    // Otherwise append a new version
                    navngivenVejVersion = navngivenVej.addVersion(this.updateRegistrering);
                }
            }

            // Update and save navngivenVejVersion
            navngivenVejVersion.setAnsvarligKommune(kommune);
            navngivenVejVersion.setVejnavn(vejNavn);
            navngivenVejVersion.setVejaddresseringsnavn(vejAdresseringsNavn);
            navngivenVejVersion.addKommunedelAfNavngivenVej(delvejEntity);
            this.navngivenVejRepository.save(navngivenVej);

            // Update and save delvejEntity
            delvejEntity.setNavngivenVejVersion(navngivenVejVersion);
            if (!updatedDelvej) {
                kommunedelAfNavngivenVejCounter.countUpdatedItem();
                updatedDelvej = true;
            }
            if (createdDelvej || updatedDelvej) {
                kommunedelAfNavngivenVejCache.put(kommuneKode, vejKode, delvejEntity);
                this.kommunedelAfNavngivenVejRepository.save(delvejEntity);
            }

            // Process any linked items
            aktivVej.setVisited();
            for (VejRegister.AktivVej otherVej : aktivVej.getConnections()) {
                if (!otherVej.getVisited()) {
                    this.createRoad(otherVej, kommuneCache, kommunedelAfNavngivenVejCache, navngivenVejCache, navngivenVejCounter, kommunedelAfNavngivenVejCounter);
                }
            }
        }
    }

    // Clean up redundant versions on NavngivenVej entities
    public void cleanNavngivneVeje() {
        System.out.println("Cleaning versions");
        tic();
        for (NavngivenVejEntity navngivenVejEntity : this.navngivenVejRepository.findAll()) {
            navngivenVejEntity.cleanLatestVersion(this.kommunedelAfNavngivenVejRepository);
            this.navngivenVejRepository.save(navngivenVejEntity);
        }
        System.out.println("Version cleaning took " + toc() + "ms");
    }


    private NavngivenVejEntity findNavngivenVejByAktivVej(VejRegister.AktivVej aktivVej, Level2Container<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVejCache) {
        return this.findNavngivenVejByAktivVej(aktivVej, null, kommunedelAfNavngivenVejCache);
    }
    private NavngivenVejEntity findNavngivenVejByAktivVej(VejRegister.AktivVej aktivVej, String vejNavn, Level2Container<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVejCache) {
        if (aktivVej != null) {
            int kommuneKode = aktivVej.getInt("kommuneKode");
            int vejKode = aktivVej.getInt("vejKode");
            try {
                //KommunedelAfNavngivenVejEntity andenVejEntity = kommunedelAfNavngivenVejRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
                KommunedelAfNavngivenVejEntity andenVejEntity = kommunedelAfNavngivenVejCache.get(kommuneKode, vejKode);
                if (andenVejEntity != null && (vejNavn == null || vejNavn.equals(andenVejEntity.getNavngivenVejVersion().getVejnavn()))) {
                    return andenVejEntity.getNavngivenVejVersion().getEntity();
                }
            } catch (Exception e) {
                System.out.println("Failed on "+kommuneKode+":"+vejKode);
            }
        }
        return null;
    }


    private Level1Container<KommuneEntity> loadKommuneCache() {
        Level1Container<KommuneEntity> kommuneCache = new Level1Container<KommuneEntity>();
        Collection<KommuneEntity> kommuneListe = kommuneRepository.findAll();
        for (KommuneEntity kommune : kommuneListe) {
            kommuneCache.put(kommune.getKommunekode(), kommune);
        }
        return kommuneCache;
    }

    private Level2Container<KommunedelAfNavngivenVejEntity> loadKommuneDelAfNavngivenVejCache() {
        Level2Container<KommunedelAfNavngivenVejEntity> kommunedelAfNavngivenVejCache = new Level2Container<KommunedelAfNavngivenVejEntity>();
        Collection<KommunedelAfNavngivenVejEntity> delvejListe = kommunedelAfNavngivenVejRepository.getAllLatest();
        for (KommunedelAfNavngivenVejEntity delvej : delvejListe) {
            kommunedelAfNavngivenVejCache.put(delvej.getKommune().getKommunekode(), delvej.getVejkode(), delvej);
        }
        return kommunedelAfNavngivenVejCache;
    }

    private Level2Container<NavngivenVejEntity> loadNavngivenVejCache() {
        Level2Container<NavngivenVejEntity> navngivenVejCache = new Level2Container<NavngivenVejEntity>();
        Collection<NavngivenVejEntity> navngivenVejList = this.navngivenVejRepository.findAll();
        for (NavngivenVejEntity navngivenVejEntity : navngivenVejList) {
            NavngivenVejVersionEntity navngivenVejVersionEntity = navngivenVejEntity.getLatestVersion();
            for (KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity : navngivenVejVersionEntity.getKommunedeleAfNavngivenVej()) {
                navngivenVejCache.put(kommunedelAfNavngivenVejEntity.getKommune().getKommunekode(), kommunedelAfNavngivenVejEntity.getVejkode(), navngivenVejEntity);
            }
        }
        return navngivenVejCache;
    }


    private long ticTime = 0;
    protected long tic() {
        this.ticTime = this.indepTic();
        return this.ticTime;
    }
    protected long indepTic() {
        return new Date().getTime();
    }
    protected long toc(long ticTime) {
        return new Date().getTime() - ticTime;
    }
    protected long toc() {
        return new Date().getTime() - this.ticTime;
    }
}