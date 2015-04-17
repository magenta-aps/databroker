package dk.magenta.databroker.cvr.model;

import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cvr.model.company.CompanyEntity;
import dk.magenta.databroker.cvr.model.company.CompanyRepository;
import dk.magenta.databroker.cvr.model.company.CompanyVersionEntity;
import dk.magenta.databroker.cvr.model.company.companydeltagere.CompanyDeltagerRelationRepository;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitRepository;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitVersionEntity;
import dk.magenta.databroker.cvr.model.deltager.DeltagerEntity;
import dk.magenta.databroker.cvr.model.deltager.DeltagerRepository;
import dk.magenta.databroker.cvr.model.deltager.DeltagerVersionEntity;
import dk.magenta.databroker.cvr.model.deltager.rolle.RolleEntity;
import dk.magenta.databroker.cvr.model.deltager.rolle.RolleRepository;
import dk.magenta.databroker.cvr.model.deltager.status.StatusEntity;
import dk.magenta.databroker.cvr.model.deltager.status.StatusRepository;
import dk.magenta.databroker.cvr.model.deltager.type.TypeEntity;
import dk.magenta.databroker.cvr.model.deltager.type.TypeRepository;
import dk.magenta.databroker.cvr.model.embeddable.CompanyInfo;
import dk.magenta.databroker.cvr.model.embeddable.CvrAddress;
import dk.magenta.databroker.cvr.model.form.CompanyFormEntity;
import dk.magenta.databroker.cvr.model.form.CompanyFormRepository;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.cvr.model.industry.IndustryRepository;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.util.TimeRecorder;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.cache.Level1Cache;
import dk.magenta.databroker.util.objectcontainers.Level1Container;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.*;

/**
 * Created by lars on 26-01-15.
 */
@Component
public class CvrModel {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private EntityManagerFactory entityManagerFactory;

    private Logger log = Logger.getLogger(CvrModel.class);


    private CompanyInfo wireCompanyInfoIndustries(CompanyInfo companyInfo, Session session) {
        companyInfo.setPrimaryIndustry(this.getIndustryEntity(companyInfo.getPrimaryIndustryCode(), session));
        for (int secondaryIndustryCode : companyInfo.getSecondaryIndustryCodes()) {
            companyInfo.addSecondaryIndustry(this.getIndustryEntity(secondaryIndustryCode, session));
        }
        return companyInfo;
    }







    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyRepository companyRepository;


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyDeltagerRelationRepository companyDeltagerRelationRepository;

    private TimeRecorder companyRecorder = new TimeRecorder();

    public void setCompany(long cvrKode,
        int formCode,
        CompanyInfo companyInfo,
        Map<Long, Date> deltagere,
                RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {

        boolean useCache = false;
        Session session = registreringInfo.getSession();

        this.wireCompanyInfoIndustries(companyInfo, session);


        TimeRecorder time = new TimeRecorder();
        CompanyEntity companyEntity = this.getCompany(cvrKode, session);

        boolean entityExists = (companyEntity != null);

        time.record();
        if (cvrKode == 0) {
            return;
        }

        if (!entityExists) {
            this.log.trace("Creating new CompanyEntity " + cvrKode);
            companyEntity = new CompanyEntity();
            companyEntity.setCvrNummer(cvrKode);
            if (useCache) {
                this.putCompany(companyEntity);
            }
            this.companyRepository.save(companyEntity, session, false);
        }
        time.record();

        CompanyFormEntity form = this.getFormEntity(formCode, session);

        time.record();

        CompanyVersionEntity companyVersionEntity = companyEntity.getLatestVersion();

        if (companyVersionEntity == null) {
            this.log.trace("Creating initial CompanyVersionEntity");
            companyVersionEntity = companyEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else if (!companyVersionEntity.matches(form, companyInfo, deltagere)) {
            this.log.trace("Creating updated CompanyVersionEntity " + registreringInfo.getUpdateRegisterering().getAktoerUUID());
            companyVersionEntity = companyEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
        } else {
            companyVersionEntity = null;
        }
        time.record();

        if (companyVersionEntity != null) {
            companyVersionEntity.setForm(form);
            companyVersionEntity.setCompanyInfo(companyInfo);
            this.addKnownCvrNumber(cvrKode);

            /*if (deltagere != null) {
                for (Long deltagerNummer : deltagere.keySet()) {
                    CompanyDeltagerRelationEntity companyDeltagerRelationEntity = new CompanyDeltagerRelationEntity();
                    companyDeltagerRelationEntity.setDeltagerNummer(deltagerNummer);
                    companyDeltagerRelationEntity.setValidFrom(deltagere.get(deltagerNummer));
                    companyDeltagerRelationEntity.setCompanyVersionEntity(companyVersionEntity);
                    companyVersionEntity.addCompanyDeltagerRelationEntitiy(companyDeltagerRelationEntity);
                    //this.companyDeltagerRelationRepository.save(companyDeltagerRelationEntity);
                }
            }*/
            this.companyRepository.save(companyVersionEntity, session, false);
            this.companyRepository.save(companyEntity, session, true);
            //this.companyRepository.detach(companyVersionEntity);
        }
        time.record();

        companyRecorder.add(time);
        //this.companyRepository.detach(companyEntity);
        //this.companyRepository.clear();
    }


    public TimeRecorder getCompanyTimer() {
        return this.companyRecorder;
    }
    public void resetCompanyTimer() {
        this.companyRecorder.reset();
    }




    private HashSet<Long> knownCvrNumbers = null;
    private void addKnownCvrNumber(long cvr) {
        if (knownCvrNumbers == null) {
            this.findKnownCvrNumbers();
        }
        this.knownCvrNumbers.add(cvr);
    }

    private boolean hasKnownCvr(long cvr) {
        if (knownCvrNumbers == null) {
            this.findKnownCvrNumbers();
        }
        return this.knownCvrNumbers.contains(cvr);
    }

    private void findKnownCvrNumbers() {
        this.knownCvrNumbers = this.companyRepository.getKnownDescriptors();
    }

    public CompanyEntity getCompany(long cvrNummer, boolean useCache) {
        CompanyEntity companyEntity = null;
        if (this.hasKnownCvr(cvrNummer)) {
            if (useCache) {
                companyEntity = this.companyCache.get(cvrNummer);
            }
            if (companyEntity == null) {
                companyEntity = this.companyRepository.getByDescriptor(cvrNummer);
                if (useCache) {
                    this.putCompany(companyEntity);
                }
            }
        }
        return companyEntity;
    }

    public CompanyEntity getCompany(long cvrNummer, Session session) {
        return this.companyRepository.getByDescriptor(cvrNummer, session);
    }

    public Collection<CompanyEntity> getCompany(SearchParameters parameters) {
        return this.companyRepository.search(parameters);
    }

    public void putCompany(CompanyEntity companyEntity) {
        this.companyCache.put(companyEntity);
    }

    private Level1Cache<CompanyEntity> companyCache;

    @PostConstruct
    private void loadCompanyCache() {
        this.companyCache = new Level1Cache<CompanyEntity>(this.companyRepository);
    }



















    //------------------------------------------------------------------------------------------------------------------
    private TimeRecorder unitRecorder = new TimeRecorder();

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyUnitRepository companyUnitRepository;

    public void setCompanyUnit(long pNummer, long cvrNummer,
                                    CompanyInfo companyInfo,
                                    boolean isPrimaryUnit,
                                    RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {
        TimeRecorder time = new TimeRecorder();

        boolean useCache = false;
        Session session = registreringInfo.getSession();

        CompanyUnitEntity companyUnitEntity = this.getCompanyUnit(pNummer, session);
        boolean entityExists = (companyUnitEntity != null);
        time.record();

        if (!entityExists) {
            this.log.trace("Creating new CompanyUnitEntity " + pNummer);
            companyUnitEntity = new CompanyUnitEntity();
            companyUnitEntity.setPNO(pNummer);
            this.companyUnitRepository.save(companyUnitEntity, session, false);
        }
        time.record();

        this.wireCompanyInfoIndustries(companyInfo, session);
        time.record();

        CompanyUnitVersionEntity companyUnitVersionEntity = companyUnitEntity.getLatestVersion();

        time.record();
        if (companyUnitVersionEntity == null) {
            this.log.trace("Creating initial CompanyUnitVersionEntity");
            companyUnitVersionEntity = companyUnitEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else if (!companyUnitVersionEntity.matches(cvrNummer, companyInfo, isPrimaryUnit)) {
            this.log.trace("Creating updated CompanyUnitVersionEntity");
            companyUnitVersionEntity = companyUnitEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
        } else {
            companyUnitVersionEntity = null;
        }
        time.record();


        if (companyUnitVersionEntity != null) {
            companyUnitVersionEntity.setCompanyInfo(companyInfo);
            companyUnitVersionEntity.setPrimaryUnit(isPrimaryUnit);
            companyUnitVersionEntity.setCvrNummer(cvrNummer);
            this.companyUnitRepository.save(companyUnitVersionEntity, session, false);
            this.companyUnitRepository.save(companyUnitEntity, session, true);
            if (useCache) {
                this.companyUnitCache.put(companyUnitEntity);
            }
            this.addKnownUnitNumber(pNummer);
            //this.companyUnitRepository.detach(companyUnitVersionEntity);
        }

        time.record();
        unitRecorder.add(time);
        //this.companyUnitRepository.detach(companyUnitEntity);
        //this.companyUnitRepository.clear();
    }

    public CompanyUnitEntity getCompanyUnit(long pNummer, boolean useCache) {
        CompanyUnitEntity companyUnitEntity = null;
        if (this.hasKnownUnitNumber(pNummer)) {
            if (useCache) {
                companyUnitEntity = this.companyUnitCache.get(pNummer);
            }
            if (companyUnitEntity == null) {
                companyUnitEntity = this.companyUnitRepository.getByDescriptor(pNummer);
                if (useCache) {
                    this.companyUnitCache.put(companyUnitEntity);
                }
            }
        }
        return companyUnitEntity;
    }

    public CompanyUnitEntity getCompanyUnit(long pNummer, Session session) {
        return this.companyUnitRepository.getByDescriptor(pNummer, session);
    }

    public Collection<CompanyUnitEntity> getCompanyUnit(SearchParameters parameters) {
        return this.companyUnitRepository.search(parameters);
    }

    private Level1Cache<CompanyUnitEntity> companyUnitCache;

    @PostConstruct
    private void loadCompanyUnitCache() {
        this.companyUnitCache = new Level1Cache<CompanyUnitEntity>(this.companyUnitRepository);
    }

    private HashSet<Long> knownUnitNumbers = null;
    private void addKnownUnitNumber(long number) {
        if (this.knownUnitNumbers == null) {
            this.findKnownUnitNumbers();
        }
        this.knownUnitNumbers.add(number);
    }

    private boolean hasKnownUnitNumber(long number) {
        if (this.knownUnitNumbers == null) {
            this.findKnownUnitNumbers();
        }
        return this.knownUnitNumbers.contains(number);
    }

    private void findKnownUnitNumbers() {
        this.knownUnitNumbers = this.companyUnitRepository.getKnownDescriptors();
    }

    public TimeRecorder getUnitTimer() {
        return unitRecorder;
    }

    public void resetUnitTimer() {
        this.unitRecorder.reset();
    }


















//------------------------------------------------------------------------------------------------------------------


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DeltagerRepository deltagerRepository;

    private TimeRecorder deltagerRecorder = new TimeRecorder();

    private boolean firstDeltager = true;

    public void setDeltager(long deltagerNummer, String name, String cvrNummer,
                                            Date ajourDate, Date gyldigDate,
                                            String typeName, String rolleName, String statusName,
                                            CvrAddress locationAddress,
                                            RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {
        boolean useCache = false;
        Session session = registreringInfo.getSession();


        TimeRecorder time = new TimeRecorder();
        DeltagerEntity deltagerEntity = this.getDeltager(deltagerNummer, session);
        boolean entityExists = (deltagerEntity != null);
        time.record();

        if (!entityExists) {
            this.log.trace("Creating new DeltagerEntity " + deltagerNummer);
            deltagerEntity = new DeltagerEntity();
            deltagerEntity.setDeltagerNummer(deltagerNummer);
            this.deltagerRepository.save(deltagerEntity, registreringInfo.getSession(), false);
        }
        time.record();

        TypeEntity typeEntity = this.setType(typeName, session);
        RolleEntity rolleEntity = this.setRolle(rolleName, session);
        StatusEntity statusEntity = this.setStatus(statusName, session);

        DeltagerVersionEntity deltagerVersionEntity = deltagerEntity.getLatestVersion();



        time.record();
        if (deltagerVersionEntity == null) {
            this.log.trace("Creating initial DeltagerVersionEntity for " + deltagerNummer);
            deltagerVersionEntity = deltagerEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else {

            /*boolean match = false;
            for (DeltagerVersionEntity version : deltagerEntity.getVersions()) {
                if (version.matches(name, cvrNummer, ajourDate, gyldigDate, typeEntity, rolleEntity, statusEntity, locationAddress)) {
                    match = true;
                    break;
                }
            }*/
            boolean match = deltagerEntity.getLatestVersion().matches(name, cvrNummer, ajourDate, gyldigDate, typeEntity, rolleEntity, statusEntity, locationAddress);
            if (!match) {
                this.log.trace("Creating updated DeltagerVersionEntity for " + deltagerNummer);
                deltagerVersionEntity = deltagerEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
            } else {
                deltagerVersionEntity = null;
            }
        }
        time.record();

        if (deltagerVersionEntity != null) {
            deltagerVersionEntity.setName(name);
            deltagerVersionEntity.setCvrNummer(cvrNummer);
            deltagerVersionEntity.setAjourDate(ajourDate);
            deltagerVersionEntity.setGyldigDate(gyldigDate);
            deltagerVersionEntity.setType(typeEntity);
            deltagerVersionEntity.setRolle(rolleEntity);
            deltagerVersionEntity.setStatus(statusEntity);
            deltagerVersionEntity.setLocationAddress(locationAddress);
            this.deltagerRepository.save(deltagerVersionEntity, session, false);
            this.deltagerRepository.save(deltagerEntity, session, true);
            if (useCache) {
                this.deltagerCache.put(deltagerEntity);
            }
            this.addKnownDeltagerNumber(deltagerNummer);
            this.deltagerRepository.detach(deltagerVersionEntity);
        }
        time.record();
        this.deltagerRecorder.add(time);
        this.deltagerRepository.detach(deltagerEntity);
        this.deltagerRepository.clear();
    }

    public DeltagerEntity getDeltager(long deltagerNummer, boolean useCache) {
            DeltagerEntity deltagerEntity = null;
        if (this.hasKnownDeltagerNumber(deltagerNummer)) {
            if (useCache) {
                deltagerEntity = this.deltagerCache.get(deltagerNummer);
            }
            if (deltagerEntity == null) {
                deltagerEntity = this.deltagerRepository.getByDescriptor(deltagerNummer);
                if (useCache) {
                    this.deltagerCache.put(deltagerEntity);
                }
            }
        }
        return deltagerEntity;
    }

    public DeltagerEntity getDeltager(long deltagerNummer, Session session) {
        return this.deltagerRepository.getByDescriptor(deltagerNummer, session);
    }

    private Level1Cache<DeltagerEntity> deltagerCache;

    @PostConstruct
    private void loadDeltagerCache() {
        this.deltagerCache = new Level1Cache<DeltagerEntity>(this.deltagerRepository);
    }




    private HashSet<Long> knownDeltagerNumbers = null;
    private void addKnownDeltagerNumber(long number) {
        if (knownDeltagerNumbers == null) {
            this.findKnownDeltagerNumbers();
        }
        this.knownDeltagerNumbers.add(number);
    }

    private boolean hasKnownDeltagerNumber(long number) {
        if (knownDeltagerNumbers == null) {
            this.findKnownDeltagerNumbers();
        }
        return this.knownDeltagerNumbers.contains(number);
    }

    private void findKnownDeltagerNumbers() {
        this.knownDeltagerNumbers = this.deltagerRepository.getKnownDescriptors();
    }


    public TimeRecorder getDeltagerTimer() {
        return deltagerRecorder;
    }

    public void resetDeltagerTimer() {
        this.deltagerRecorder.reset();
    }











    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private IndustryRepository industryRepository;

    public IndustryEntity setIndustry(int code, String name, Session session) {
        return this.setIndustry(code, name, session, false);
    }
    public IndustryEntity setIndustry(int code, String name, Session session, boolean noUpdate) {
        IndustryEntity industryEntity = this.getIndustryCache(session).get(code);
        boolean created = false;
        if (industryEntity == null) {
            industryEntity = new IndustryEntity();
            industryEntity.setCode(code);
            created = true;
        }
        if (created ||
                (name != null && !name.isEmpty() &&
                        ((industryEntity.getName() == null) || !(noUpdate || industryEntity.getName().equals(name)))
                )) {
            industryEntity.setName(name);
            this.industryRepository.save(industryEntity, session, !created);
            this.putIndustryCache(industryEntity);
        }
        return industryEntity;
    }

    public IndustryEntity getIndustryEntity(int code, Session session) {
        return this.getIndustryCache(session).get(code);
        //return this.industryRepository.getByCode(code);
    }

    //--------------------------------------------------

    private Level1Container<IndustryEntity> industryCache;
    private Level1Container<IndustryEntity> getIndustryCache(Session session) {
        if (this.industryCache == null) {
            this.industryCache = new Level1Container<IndustryEntity>();
            for (IndustryEntity item : this.industryRepository.findAll(session)) {
                this.putIndustryCache(item);
            }
        }
        return this.industryCache;
    }
    private void putIndustryCache(IndustryEntity item) {
        if (this.industryCache == null) {
            this.industryCache = new Level1Container<IndustryEntity>();
        }
        this.industryCache.put(item.getCode(), item);
    }

    public void resetIndustryCache() {
        this.industryCache = null;
    }

    //------------------------------------------------------------------------------------------------------------------



    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyFormRepository companyFormRepository;

    public CompanyFormEntity setForm(int code, String name, Session session) {
        return this.setForm(code, name, session, false);
    }
    public CompanyFormEntity setForm(int code, String name, Session session, boolean noUpdate) {
        CompanyFormEntity companyFormEntity = this.getFormCache(session).get(code);
        boolean created = false;
        if (companyFormEntity == null) {
            companyFormEntity = new CompanyFormEntity();
            companyFormEntity.setCode(code);
            created = true;
        }
        if (created ||
                (name != null && !name.isEmpty() &&
                        ((companyFormEntity.getName() == null) || !(noUpdate || companyFormEntity.getName().equals(name)))
                )) {
            companyFormEntity.setName(name);
            this.companyFormRepository.save(companyFormEntity, session, !created);
            this.putFormCache(companyFormEntity);
        }
        return companyFormEntity;
    }

    public CompanyFormEntity getFormEntity(int code, Session session) {
        return this.getFormCache(session).get(code);
        //return this.industryRepository.getByCode(code);
    }

    //--------------------------------------------------

    private Level1Container<CompanyFormEntity> formCache;
    private Level1Container<CompanyFormEntity> getFormCache(Session session) {
        if (this.formCache == null) {
            this.formCache = new Level1Container<CompanyFormEntity>();
            for (CompanyFormEntity item : this.companyFormRepository.findAll(session)) {
                this.putFormCache(item);
            }
        }
        return this.formCache;
    }
    private void putFormCache(CompanyFormEntity item) {
        if (this.formCache == null) {
            this.formCache = new Level1Container<CompanyFormEntity>();
        }
        this.formCache.put(item.getCode(), item);
    }

    public void resetFormCache() {
        this.formCache = null;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private TypeRepository deltagerTypeRepository;

    public TypeEntity setType(String name, Session session) {
        TypeEntity typeEntity = this.typeCache.get(name);
        if (typeEntity == null) {
            typeEntity = this.deltagerTypeRepository.getByName(name, session);
            this.typeCache.put(name, typeEntity);
        }
        if (typeEntity == null) {
            typeEntity = new TypeEntity();
            typeEntity.setName(name);
            this.deltagerTypeRepository.save(typeEntity, session, false);
            this.typeCache.put(name, typeEntity);
        }
        return typeEntity;
    }

    private Level1Container<TypeEntity> typeCache;

    @PostConstruct
    private void loadTypeCache() {
        this.typeCache = new Level1Container<TypeEntity>();
    }


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private RolleRepository deltagerRolleRepository;

    public RolleEntity setRolle(String name, Session session) {
        RolleEntity rolleEntity = this.rolleCache.get(name);
        if (rolleEntity == null) {
            rolleEntity = this.deltagerRolleRepository.getByName(name, session);
            this.rolleCache.put(name, rolleEntity);
        }
        if (rolleEntity == null) {
            rolleEntity = new RolleEntity();
            rolleEntity.setName(name);
            this.deltagerRolleRepository.save(rolleEntity, session, false);
            this.rolleCache.put(name, rolleEntity);
        }
        return rolleEntity;
    }

    private Level1Container<RolleEntity> rolleCache;

    @PostConstruct
    private void loadRolleCache() {
        this.rolleCache = new Level1Container<RolleEntity>();
    }


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private StatusRepository deltagerStatusRepository;

    public StatusEntity setStatus(String name, Session session) {
        if (name == null) {
            return null;
        } else {
            StatusEntity statusEntity = this.statusCache.get(name);
            if (statusEntity == null) {
                statusEntity = this.deltagerStatusRepository.getByName(name, session);
                this.statusCache.put(name, statusEntity);
            }
            if (statusEntity == null) {
                statusEntity = new StatusEntity();
                statusEntity.setName(name);
                this.deltagerStatusRepository.save(statusEntity, session, false);
                this.statusCache.put(name, statusEntity);
            }
            return statusEntity;
        }
    }

    private Level1Container<StatusEntity> statusCache;

    @PostConstruct
    private void loadStatusCache() {
        this.statusCache = new Level1Container<StatusEntity>();
    }

    //------------------------------------------------------------------------------------------------------------------

    public List<TransactionCallback> getBulkwireCallbacks() {
        ArrayList<TransactionCallback> transactionCallbacks = new ArrayList<TransactionCallback>();
        transactionCallbacks.addAll(this.companyRepository.getBulkwireCallbacks());
        transactionCallbacks.addAll(this.companyUnitRepository.getBulkwireCallbacks());
        transactionCallbacks.addAll(this.deltagerRepository.getBulkwireCallbacks());
        return transactionCallbacks;
    }
}
