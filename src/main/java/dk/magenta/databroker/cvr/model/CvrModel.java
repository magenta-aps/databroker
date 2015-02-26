package dk.magenta.databroker.cvr.model;

import dk.magenta.databroker.core.RegistreringInfo;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
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
import dk.magenta.databroker.cvr.model.form.FormEntity;
import dk.magenta.databroker.cvr.model.form.FormRepository;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.cvr.model.industry.IndustryRepository;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
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
                                    Date startDate, Date endDate,
                                    RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {

        CompanyEntity companyEntity = this.getCompany(cvrKode);

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

        FormEntity form = this.getFormEntity(formCode);

        IndustryEntity primaryIndustry = this.getIndustryEntity(primaryIndustryCode);
        HashSet<IndustryEntity> secondaryIndustries = new HashSet<IndustryEntity>();
        for (int secondaryIndustryCode : secondaryIndustryCodes) {
            secondaryIndustries.add(this.getIndustryEntity(secondaryIndustryCode));
        }

        CompanyVersionEntity companyVersionEntity = companyEntity.getLatestVersion();

        if (companyVersionEntity == null) {
            this.log.trace("Creating initial CompanyVersionEntity");
            companyVersionEntity = companyEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else if (!companyVersionEntity.matches(name, form, primaryIndustry, secondaryIndustries, startDate, endDate)) {
            this.log.trace("Creating updated CompanyVersionEntity");
            companyVersionEntity = companyEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
        } else {
            companyVersionEntity = null;
        }

        if (companyVersionEntity != null) {
            companyVersionEntity.setName(name);
            companyVersionEntity.setForm(form);
            companyVersionEntity.setPrimaryIndustry(primaryIndustry);
            for (IndustryEntity industryEntity : secondaryIndustries) {
                companyVersionEntity.addSecondaryIndustry(industryEntity);
            }
            //companyVersionEntity.setPrimaryUnit(primaryUnit);
            companyVersionEntity.setStartDate(startDate);
            companyVersionEntity.setEndDate(endDate);

            this.companyRepository.save(companyEntity);
        }
        return companyEntity;
    }

    public CompanyEntity getCompany(String cvrNummer) {
        return this.companyCache.get(cvrNummer);
        //return this.companyRepository.getByCvrNummer(cvrNummer);
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


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyUnitRepository companyUnitRepository;

    public CompanyUnitEntity setCompanyUnit(long pNummer, String name, String cvrNummer,
                                    int primaryIndustryCode, int[] secondaryIndustryCodes,
                                    EnhedsAdresseEntity address, Date addressDate, String phone, String fax, String email,
                                    Date startDate, Date endDate,
                                    boolean advertProtection,
                                    RegistreringInfo registreringInfo, List<VirkningEntity> virkninger) {

        CompanyUnitEntity companyUnitEntity = this.companyUnitCache.get(pNummer);

        if (companyUnitEntity == null) {
            this.log.trace("Creating new CompanyUnitEntity " + pNummer);
            companyUnitEntity = new CompanyUnitEntity();
            companyUnitEntity.setPNO(pNummer);
            //companyUnitEntity.setCompany(company);
            companyUnitEntity.setCvrNummer(cvrNummer);
            //this.putCompanyUnitCache(companyUnitEntity);
        }

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

        CompanyUnitVersionEntity companyUnitVersionEntity = companyUnitEntity.getLatestVersion();

        if (companyUnitVersionEntity == null) {
            this.log.trace("Creating initial CompanyUnitVersionEntity");
            companyUnitVersionEntity = companyUnitEntity.addVersion(registreringInfo.getCreateRegistrering(), virkninger);
        } else if (!companyUnitVersionEntity.matches(name, address, addressDate, primaryIndustry, secondaryIndustries, phone, fax, email, advertProtection, startDate, endDate)) {
            this.log.trace("Creating updated CompanyUnitVersionEntity");
            companyUnitVersionEntity = companyUnitEntity.addVersion(registreringInfo.getUpdateRegisterering(), virkninger);
        } else {
            companyUnitVersionEntity = null;
        }


        if (companyUnitVersionEntity != null) {
            companyUnitVersionEntity.setName(name);
            companyUnitVersionEntity.setAddress(address);
            companyUnitVersionEntity.setAddressDate(addressDate);
            companyUnitVersionEntity.setPrimaryIndustry(primaryIndustry);
            for (IndustryEntity industryEntity : secondaryIndustries) {
                companyUnitVersionEntity.addSecondaryIndustry(industryEntity);
            }
            companyUnitVersionEntity.setAdvertProtection(advertProtection);
            companyUnitVersionEntity.setStartDate(startDate);
            companyUnitVersionEntity.setEndDate(endDate);
            companyUnitVersionEntity.setPhone(phone);
            companyUnitVersionEntity.setFax(fax);
            companyUnitVersionEntity.setEmail(email);
            this.companyUnitRepository.save(companyUnitEntity);
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
    private FormRepository formRepository;

    public FormEntity setForm(int code, String name) {
        return this.setForm(code, name, false);
    }
    public FormEntity setForm(int code, String name, boolean noUpdate) {
        FormEntity formEntity = this.getFormCache().get(code);
        boolean created = false;
        if (formEntity == null) {
            formEntity = new FormEntity();
            formEntity.setCode(code);
            created = true;
        }
        if (created ||
                (name != null && !name.isEmpty() &&
                        ((formEntity.getName() == null) || !(noUpdate || formEntity.getName().equals(name)))
                )) {
            formEntity.setName(name);
            this.formRepository.save(formEntity);
            this.putFormCache(formEntity);
        }
        return formEntity;
    }

    public FormEntity getFormEntity(int code) {
        return this.getFormCache().get(code);
        //return this.industryRepository.getByCode(code);
    }

    //--------------------------------------------------

    private Level1Container<FormEntity> formCache;
    private Level1Container<FormEntity> getFormCache() {
        if (this.formCache == null) {
            this.formCache = new Level1Container<FormEntity>();
            for (FormEntity item : this.formRepository.findAll()) {
                this.putFormCache(item);
            }
        }
        return this.formCache;
    }
    private void putFormCache(FormEntity item) {
        if (this.formCache == null) {
            this.formCache = new Level1Container<FormEntity>();
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
