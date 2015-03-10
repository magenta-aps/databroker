package dk.magenta.databroker.cvr.model;

import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cvr.model.company.CompanyEntity;
import dk.magenta.databroker.cvr.model.company.CompanyRepository;
import dk.magenta.databroker.cvr.model.company.CompanyVersionEntity;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitRepository;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitVersionEntity;
import dk.magenta.databroker.cvr.model.deltager.DeltagerEntity;
import dk.magenta.databroker.cvr.model.deltager.DeltagerRepository;
import dk.magenta.databroker.cvr.model.deltager.DeltagerVersionEntity;
import dk.magenta.databroker.cvr.model.deltager.rolle.RolleEntity;
import dk.magenta.databroker.cvr.model.deltager.rolle.RolleRepository;
import dk.magenta.databroker.cvr.model.deltager.type.TypeEntity;
import dk.magenta.databroker.cvr.model.deltager.type.TypeRepository;
import dk.magenta.databroker.cvr.model.embeddable.CompanyInfo;
import dk.magenta.databroker.cvr.model.form.CompanyFormEntity;
import dk.magenta.databroker.cvr.model.form.CompanyFormRepository;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.cvr.model.industry.IndustryRepository;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.util.TimeRecorder;
import dk.magenta.databroker.util.cache.Level1Cache;
import dk.magenta.databroker.util.objectcontainers.Level1Container;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.sql.Time;
import java.util.*;

/**
 * Created by lars on 26-01-15.
 */
@Component
public class CvrModel {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private EntityManagerFactory entityManagerFactory;

    public void flush() {
        this.companyRepository.clear();
        this.companyUnitRepository.clear();
        this.deltagerRepository.clear();
        this.entityManagerFactory.getCache().evictAll();
    }

    private Logger log = Logger.getLogger(CvrModel.class);

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyRepository companyRepository;

    public CompanyEntity setCompany(String cvrKode, String name,
                                    int primaryIndustryCode, int[] secondaryIndustryCodes, int formCode,
                                    Date startDate, Date endDate, Date ajourDate,
                                    RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {
        return this.setCompany(cvrKode, name, primaryIndustryCode, secondaryIndustryCodes, formCode, startDate, endDate, ajourDate, null, registreringInfo, virkninger);
    }

    private int companiesCreated = 0;
    private TimeRecorder companyRecorder = new TimeRecorder();

    public CompanyEntity setCompany(String cvrKode, String name,
        int primaryIndustryCode, int[] secondaryIndustryCodes, int formCode,
        Date startDate, Date endDate, Date ajourDate, String primaryAddressDescriptor,
                RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {

        TimeRecorder time = new TimeRecorder();

        CompanyEntity companyEntity = this.getCompany(cvrKode, true);

        time.record();
        if (name == null) {
            name = "";
        }
        if (cvrKode == null) {
            return null;
        }

        if (companyEntity == null) {
            this.log.trace("Creating new CompanyEntity " + cvrKode);
            companyEntity = new CompanyEntity();
            companyEntity.setCvrNummer(cvrKode);
            this.companyCache.put(companyEntity);
        }
        time.record();

        CompanyFormEntity form = this.getFormEntity(formCode);

        IndustryEntity primaryIndustry = this.getIndustryEntity(primaryIndustryCode);
        HashSet<IndustryEntity> secondaryIndustries = new HashSet<IndustryEntity>();
        for (int secondaryIndustryCode : secondaryIndustryCodes) {
            secondaryIndustries.add(this.getIndustryEntity(secondaryIndustryCode));
        }
        time.record();

        CompanyVersionEntity companyVersionEntity = companyEntity.getLatestVersion();

        if (companyVersionEntity == null) {
            this.log.trace("Creating initial CompanyVersionEntity");
            companyVersionEntity = companyEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else if (!companyVersionEntity.matches(name, form, primaryIndustry, secondaryIndustries, startDate, endDate, primaryAddressDescriptor)) {
            this.log.trace("Creating updated CompanyVersionEntity");
            companyVersionEntity = companyEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
        } else {
            companyVersionEntity = null;
        }
        time.record();

        if (companyVersionEntity != null) {
            CompanyInfo cInfo = companyVersionEntity.getCompanyInfo();
            cInfo.setName(name);
            companyVersionEntity.setForm(form);
            cInfo.setPrimaryIndustry(primaryIndustry);
            for (IndustryEntity industryEntity : secondaryIndustries) {
                cInfo.addSecondaryIndustry(industryEntity);
            }
            //companyVersionEntity.setPrimaryUnit(primaryUnit);
            cInfo.getLifeCycle().setStartDate(startDate);
            cInfo.getLifeCycle().setEndDate(endDate);
            cInfo.getLocationAddress().setDescriptor(primaryAddressDescriptor);
            cInfo.setUpdateDate(ajourDate);
            this.companyRepository.save(companyEntity);
        }
        time.record();

        companiesCreated++;
        companyRecorder.add(time);

        if (companiesCreated > 10000) {
            System.out.println(companyRecorder);
            companyRecorder = new TimeRecorder();
            companiesCreated = 0;
        }
        return companyEntity;
    }

    public CompanyEntity getCompany(String cvrNummer, boolean noCache) {
        if (noCache) {
            return this.companyRepository.getByCvrNummer(cvrNummer);
        } else {
            return this.companyCache.get(cvrNummer);
        }
    }

    public Collection<CompanyEntity> getCompany(SearchParameters parameters) {
        return this.companyRepository.search(parameters);
    }

    public void flushCompanies() {
        this.companyRepository.clear();
        this.companyUnitRepository.clear();
        this.deltagerRepository.clear();
    }

    //--------------------------------------------------

    private Level1Cache<CompanyEntity> companyCache;

    @PostConstruct
    private void loadCompanyCache() {
        this.companyCache = new Level1Cache<CompanyEntity>(this.companyRepository);
    }

    //------------------------------------------------------------------------------------------------------------------
    private int unitsCreated = 0;
    private TimeRecorder unitRecorder = new TimeRecorder();


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyUnitRepository companyUnitRepository;

    public CompanyUnitEntity setCompanyUnit(long pNummer, String name, String cvrNummer,
                                    int primaryIndustryCode, int[] secondaryIndustryCodes,
                                    EnhedsAdresseEntity address, Date addressDate, String addressDescriptor,
                                    String phone, String fax, String email, boolean isPrimaryUnit,
                                    Date startDate, Date endDate,
                                    boolean advertProtection,
                                    RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {
        TimeRecorder time = new TimeRecorder();

        CompanyUnitEntity companyUnitEntity = this.companyUnitCache.get(pNummer);
        time.record();
        //CompanyEntity companyEntity = this.getCompanyVersion(cvrNummer);
        //CompanyVersionEntity companyVersionEntity = companyEntity.getLatestVersion();

        if (companyUnitEntity == null) {
            this.log.trace("Creating new CompanyUnitEntity " + pNummer);
            companyUnitEntity = new CompanyUnitEntity();
            companyUnitEntity.setPNO(pNummer);
            //companyUnitEntity.setCompanyVersion(company);
            companyUnitEntity.setCvrNummer(cvrNummer);
            //this.putCompanyUnitCache(companyUnitEntity);
        }
        time.record();

        /*
        if (company != null && companyUnitEntity != null &&
                company.getLatestVersion().getPrimaryUnit() == null &&
                company.getLatestVersion().getPrimaryUnitCode() == pNummer) {
            company.getLatestVersion().setPrimaryUnit(companyUnitEntity);
            this.companyRepository.save(company);
        }*/


        IndustryEntity primaryIndustry = this.getIndustryEntity(primaryIndustryCode);
        ArrayList<IndustryEntity> secondaryIndustries = new ArrayList<IndustryEntity>();
        for (int secondaryIndustryCode : secondaryIndustryCodes) {
            secondaryIndustries.add(this.getIndustryEntity(secondaryIndustryCode));
        }
        time.record();

        CompanyUnitVersionEntity companyUnitVersionEntity = companyUnitEntity.getLatestVersion();

        time.record();
        if (companyUnitVersionEntity == null) {
            this.log.trace("Creating initial CompanyUnitVersionEntity");
            companyUnitVersionEntity = companyUnitEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else if (!companyUnitVersionEntity.matches(name, address, addressDate, primaryIndustry, secondaryIndustries, phone, fax, email, isPrimaryUnit, advertProtection, startDate, endDate)) {
            this.log.trace("Creating updated CompanyUnitVersionEntity");
            companyUnitVersionEntity = companyUnitEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
        } else {
            companyUnitVersionEntity = null;
        }
        time.record();


        if (companyUnitVersionEntity != null) {
            CompanyInfo cInfo = companyUnitVersionEntity.getCompanyInfo();
            cInfo.setName(name);
            cInfo.getLocationAddress().setEnhedsAdresse(address);
            cInfo.getLocationAddress().setValidFrom(addressDate);
            cInfo.getLocationAddress().setDescriptor(addressDescriptor);
            cInfo.setPrimaryIndustry(primaryIndustry);
            for (IndustryEntity industryEntity : secondaryIndustries) {
                cInfo.addSecondaryIndustry(industryEntity);
            }
            cInfo.setAdvertProtection(advertProtection);
            cInfo.getLifeCycle().setStartDate(startDate);
            cInfo.getLifeCycle().setEndDate(endDate);
            cInfo.getTelephoneNumber().setText(phone);
            cInfo.getTelefaxNumber().setText(fax);
            cInfo.getEmail().setText(email);
            companyUnitVersionEntity.setPrimaryUnit(isPrimaryUnit);
            this.companyUnitRepository.save(companyUnitEntity);
        }

        time.record();
        unitsCreated++;
        unitRecorder.add(time);

        if (unitsCreated > 10000) {
            System.out.println("10000 units added: "+unitRecorder);
            unitRecorder = new TimeRecorder();
            unitsCreated = 0;
        }

        return companyUnitEntity;
    }

    public CompanyUnitEntity getCompanyUnit(long pNummer) {
        CompanyUnitEntity companyUnitEntity = this.companyUnitCache.get(pNummer);
        if (companyUnitEntity != null) {
            return companyUnitEntity;
        } else {
            companyUnitEntity = this.companyUnitRepository.getByPno(pNummer);
            if (companyUnitEntity != null) {
                this.companyUnitCache.put(pNummer, companyUnitEntity);
            }
            return companyUnitEntity;
        }
    }

    //--------------------------------------------------

    private Level1Cache<CompanyUnitEntity> companyUnitCache;

    @PostConstruct
    private void loadCompanyUnitCache() {
        this.companyUnitCache = new Level1Cache<CompanyUnitEntity>(this.companyUnitRepository);
    }














    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DeltagerRepository deltagerRepository;

    public DeltagerEntity setDeltager(long deltagerNummer, String name, String cvrNummer,
                                            Date ajourDate, Date gyldigDate,
                                            String typeName, String rolleName,
                                            RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {

        DeltagerEntity deltagerEntity = this.getDeltager(deltagerNummer);

        if (deltagerEntity == null) {
            this.log.trace("Creating new DeltagerEntity " + deltagerNummer);
            deltagerEntity = new DeltagerEntity();
            deltagerEntity.setDeltagerNummer(deltagerNummer);
            deltagerEntity.setCvrNummer(cvrNummer);
        }

        TypeEntity typeEntity = this.setType(typeName);
        RolleEntity rolleEntity = this.setRolle(rolleName);

        DeltagerVersionEntity deltagerVersionEntity = deltagerEntity.getLatestVersion();

        if (deltagerVersionEntity == null) {
            this.log.trace("Creating initial DeltagerVersionEntity");
            deltagerVersionEntity = deltagerEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else if (!deltagerVersionEntity.matches(name, ajourDate, gyldigDate, typeEntity, rolleEntity)) {
            this.log.trace("Creating updated DeltagerVersionEntity");
            deltagerVersionEntity = deltagerEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
        } else {
            deltagerVersionEntity = null;
        }

        if (deltagerVersionEntity != null) {
            deltagerVersionEntity.setName(name);
            deltagerVersionEntity.setAjourDate(ajourDate);
            deltagerVersionEntity.setGyldigDate(gyldigDate);
            deltagerVersionEntity.setType(typeEntity);
            deltagerVersionEntity.setRolle(rolleEntity);
            this.deltagerRepository.save(deltagerEntity);
            this.deltagerCache.put(deltagerEntity);
        }

        return deltagerEntity;
    }

    public DeltagerEntity getDeltager(long deltagerNummer) {
        return this.deltagerCache.get(deltagerNummer);
        //return this.deltagerRepository.getByDeltagerNummer(deltagerNummer);
        /*CompanyUnitEntity companyUnitEntity = this.companyUnitCache.get(pNummer);
        if (companyUnitEntity != null) {
            return companyUnitEntity;
        } else {
            companyUnitEntity = this.companyUnitRepository.getByPno(pNummer);
            if (companyUnitEntity != null) {
                this.companyUnitCache.put(pNummer, companyUnitEntity);
            }
            return companyUnitEntity;
        }*/
    }

    private Level1Cache<DeltagerEntity> deltagerCache;

    @PostConstruct
    private void loadDeltagerCache() {
        this.deltagerCache = new Level1Cache<DeltagerEntity>(this.deltagerRepository);
    }














    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private IndustryRepository industryRepository;

    public IndustryEntity setIndustry(int code, String name) {
        return this.setIndustry(code, name, false);
    }
    public IndustryEntity setIndustry(int code, String name, boolean noUpdate) {
        IndustryEntity industryEntity = this.getIndustryCache().get(code);
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
            this.industryRepository.save(industryEntity);
            this.putIndustryCache(industryEntity);
        }
        return industryEntity;
    }

    public IndustryEntity getIndustryEntity(int code) {
        return this.getIndustryCache().get(code);
        //return this.industryRepository.getByCode(code);
    }

    //--------------------------------------------------

    private Level1Container<IndustryEntity> industryCache;
    private Level1Container<IndustryEntity> getIndustryCache() {
        if (this.industryCache == null) {
            this.industryCache = new Level1Container<IndustryEntity>();
            for (IndustryEntity item : this.industryRepository.findAll()) {
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

    public CompanyFormEntity setForm(int code, String name) {
        return this.setForm(code, name, false);
    }
    public CompanyFormEntity setForm(int code, String name, boolean noUpdate) {
        CompanyFormEntity companyFormEntity = this.getFormCache().get(code);
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
            this.companyFormRepository.save(companyFormEntity);
            this.putFormCache(companyFormEntity);
        }
        return companyFormEntity;
    }

    public CompanyFormEntity getFormEntity(int code) {
        return this.getFormCache().get(code);
        //return this.industryRepository.getByCode(code);
    }

    //--------------------------------------------------

    private Level1Container<CompanyFormEntity> formCache;
    private Level1Container<CompanyFormEntity> getFormCache() {
        if (this.formCache == null) {
            this.formCache = new Level1Container<CompanyFormEntity>();
            for (CompanyFormEntity item : this.companyFormRepository.findAll()) {
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

    public TypeEntity setType(String name) {
        TypeEntity typeEntity = this.typeCache.get(name);
        if (typeEntity == null) {
            typeEntity = new TypeEntity();
            typeEntity.setName(name);
            this.deltagerTypeRepository.save(typeEntity);
            this.typeCache.put(typeEntity);
        }
        return typeEntity;
    }

    private Level1Cache<TypeEntity> typeCache;

    @PostConstruct
    private void loadTypeCache() {
        this.typeCache = new Level1Cache<TypeEntity>(this.deltagerTypeRepository);
    }


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private RolleRepository deltagerRolleRepository;

    public RolleEntity setRolle(String name) {
        RolleEntity rolleEntity = this.rolleCache.get(name);
        if (rolleEntity == null) {
            rolleEntity = new RolleEntity();
            rolleEntity.setName(name);
            this.deltagerRolleRepository.save(rolleEntity);
            this.rolleCache.put(rolleEntity);
        }
        return rolleEntity;
    }

    private Level1Cache<RolleEntity> rolleCache;

    @PostConstruct
    private void loadRolleCache() {
        this.rolleCache = new Level1Cache<RolleEntity>(this.deltagerRolleRepository);
    }

    //------------------------------------------------------------------------------------------------------------------

    public void bulkWireReferences() {
        this.companyUnitRepository.bulkWireReferences();
        this.deltagerRepository.bulkWireReferences();
    }
}
