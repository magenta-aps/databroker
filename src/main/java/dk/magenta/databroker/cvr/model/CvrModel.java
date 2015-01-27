package dk.magenta.databroker.cvr.model;

import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.VirkningEntity;
import dk.magenta.databroker.cvr.model.company.CompanyEntity;
import dk.magenta.databroker.cvr.model.company.CompanyRepository;
import dk.magenta.databroker.cvr.model.company.CompanyVersionEntity;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitEntity;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitRepository;
import dk.magenta.databroker.cvr.model.companyunit.CompanyUnitVersionEntity;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
import dk.magenta.databroker.cvr.model.industry.IndustryRepository;
import dk.magenta.databroker.dawa.model.enhedsadresser.EnhedsAdresseEntity;
import dk.magenta.databroker.register.objectcontainers.Level1Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lars on 26-01-15.
 */
@Component
public class CvrModel {

    private boolean printProcessing = true;


    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyRepository companyRepository;

    private Level1Container<CompanyEntity> companyCache;

    /*public CompanyEntity setCompany(String cvrKode, String name,
                                    int primaryIndustryCode, int[] secondaryIndustryCodes, int formCode,
                                    EnhedsAdresseEntity address, String phone, String fax, String email,
                                    Date startDate, Date endDate,
                                    boolean advertProtection,
                                    RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering) {
        return this.setCompany(cvrKode, name, primaryIndustryCode, secondaryIndustryCodes, formCode, address, phone, fax, email, startDate, endDate, advertProtection,
                createRegistrering, updateRegistrering, new ArrayList<VirkningEntity>());
    }*/

    /*public CompanyEntity setCompany(String cvrKode, String name,
                                    int primaryIndustryCode, int[] secondaryIndustryCodes, int formCode,
                                    EnhedsAdresseEntity address, String phone, String fax, String email,
                                    Date startDate, Date endDate,
                                    boolean advertProtection,
                                    RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {
    }
*/

    public CompanyEntity setCompany(String cvrKode, String name, long primaryUnitNumber, long[] unitNumbers,
                                    int primaryIndustryCode, int[] secondaryIndustryCodes, int formCode,
                                    Date startDate, Date endDate,
                                    RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {

        CompanyEntity companyEntity = this.getCompanyCache().get(cvrKode);
        if (companyEntity == null) {
            if (printProcessing) {
                System.out.println("    creating new CompanyEntity " + cvrKode);
            }
            companyEntity = new CompanyEntity();
            companyEntity.setCvrNummer(cvrKode);
            this.getCompanyCache().put(cvrKode, companyEntity);
        }

        IndustryEntity primaryIndustry = this.getIndustryEntity(primaryIndustryCode);
        HashSet<IndustryEntity> secondaryIndustries = new HashSet<IndustryEntity>();
        for (int secondaryIndustryCode : secondaryIndustryCodes) {
            secondaryIndustries.add(this.getIndustryEntity(secondaryIndustryCode));
        }

        CompanyUnitEntity primaryUnit = this.getCompanyUnit(primaryUnitNumber);
        HashSet<CompanyUnitEntity> units = new HashSet<CompanyUnitEntity>();
        for (long pNummer : unitNumbers) {
            units.add(this.getCompanyUnit(pNummer));
        }

        CompanyVersionEntity companyVersionEntity = companyEntity.getLatestVersion();

        if (companyVersionEntity == null) {
            if (printProcessing) {
                System.out.println("    creating initial CompanyVersionEntity");
            }
            companyVersionEntity = companyEntity.addVersion(createRegistrering, virkninger);
        } else if (!companyVersionEntity.matches(name, primaryIndustry, secondaryIndustries, primaryUnit, units, startDate, endDate)) {
            if (printProcessing) {
                System.out.println("    creating updated CompanyVersionEntity");
            }
            companyVersionEntity = companyEntity.addVersion(updateRegistrering, virkninger);
        } else {
            companyVersionEntity = null;
        }


        if (companyVersionEntity != null) {
            companyVersionEntity.setName(name);
            companyVersionEntity.setPrimaryIndustry(primaryIndustry);
            for (IndustryEntity industryEntity : secondaryIndustries) {
                companyVersionEntity.addSecondaryIndustry(industryEntity);
            }
            companyVersionEntity.setPrimaryUnit(primaryUnit);
            for (CompanyUnitEntity companyUnitEntity : units) {
                companyVersionEntity.addUnit(companyUnitEntity);
            }
            companyVersionEntity.setStartDate(startDate);
            companyVersionEntity.setEndDate(endDate);

            this.companyRepository.save(companyEntity);
        } else {
            if (printProcessing) {
                System.out.println("    no changes");
            }
        }
        return companyEntity;
    }

    private Level1Container<CompanyEntity> getCompanyCache() {
        if (this.companyCache == null) {
            this.companyCache = new Level1Container<CompanyEntity>();
            for (CompanyEntity item : this.companyRepository.findAll()) {
                this.companyCache.put(item.getCvrNummer(), item);
            }
        }
        return this.companyCache;
    }

    public void resetCompanyCache() {
        this.companyCache = null;
    }

    //------------------------------------------------------------------------------------------------------------------


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CompanyUnitRepository companyUnitRepository;

    public CompanyUnitEntity setCompanyUnit(long pNummer, String name,
                                    int primaryIndustryCode, int[] secondaryIndustryCodes,
                                    EnhedsAdresseEntity address, String phone, String fax, String email,
                                    Date startDate, Date endDate,
                                    boolean advertProtection,
                                    RegistreringEntity createRegistrering, RegistreringEntity updateRegistrering, List<VirkningEntity> virkninger) {
        CompanyUnitEntity companyUnitEntity = this.getCompanyUnitCache().get(pNummer);
        if (companyUnitEntity == null) {
            if (printProcessing) {
                System.out.println("    creating new CompanyUnitEntity " + pNummer);
            }
            companyUnitEntity = new CompanyUnitEntity();
            companyUnitEntity.setPNO(pNummer);
            this.getCompanyUnitCache().put(pNummer, companyUnitEntity);
        }


        IndustryEntity primaryIndustry = this.getIndustryEntity(primaryIndustryCode);
        System.out.println("primaryIndustryCode: "+primaryIndustryCode);
        System.out.println("primaryIndustry: "+primaryIndustry);
        ArrayList<IndustryEntity> secondaryIndustries = new ArrayList<IndustryEntity>();
        for (int secondaryIndustryCode : secondaryIndustryCodes) {
            secondaryIndustries.add(this.getIndustryEntity(secondaryIndustryCode));
        }

        CompanyUnitVersionEntity companyUnitVersionEntity = companyUnitEntity.getLatestVersion();

        if (companyUnitVersionEntity == null) {
            if (printProcessing) {
                System.out.println("    creating initial CompanyUnitVersionEntity");
            }
            companyUnitVersionEntity = companyUnitEntity.addVersion(createRegistrering, virkninger);
        } else if (!companyUnitVersionEntity.matches(name, address, primaryIndustry, secondaryIndustries, phone, fax, email, advertProtection, startDate, endDate)) {
            if (printProcessing) {
                System.out.println("    creating updated CompanyUnitVersionEntity");
            }
            companyUnitVersionEntity = companyUnitEntity.addVersion(updateRegistrering, virkninger);
        } else {
            companyUnitVersionEntity = null;
        }

        if (companyUnitVersionEntity != null) {
            companyUnitVersionEntity.setName(name);
            companyUnitVersionEntity.setAddress(address);
            companyUnitVersionEntity.setPrimaryIndustry(primaryIndustry);
            for (IndustryEntity industryEntity : secondaryIndustries) {
                companyUnitVersionEntity.addSecondaryIndustry(industryEntity);
            }
            companyUnitVersionEntity.setAdvertProtection(advertProtection);
            companyUnitVersionEntity.setStartDate(startDate);
            companyUnitVersionEntity.setEndDate(endDate);
            this.companyUnitRepository.save(companyUnitEntity);
        } else {
            if (printProcessing) {
                System.out.println("    no changes");
            }
        }
        return companyUnitEntity;
    }

    //--------------------------------------------------

    private Level1Container<CompanyUnitEntity> companyUnitCache;

    private Level1Container<CompanyUnitEntity> getCompanyUnitCache() {
        if (this.companyUnitCache == null) {
            this.companyUnitCache = new Level1Container<CompanyUnitEntity>();
            for (CompanyUnitEntity item : this.companyUnitRepository.findAll()) {
                this.companyUnitCache.put(item.getPNO(), item);
            }
        }
        return this.companyUnitCache;
    }

    private void putCompanyUnitCache(CompanyUnitEntity item) {
        if (this.companyUnitCache == null) {
            this.companyUnitCache = new Level1Container<CompanyUnitEntity>();
        }
        this.companyUnitCache.put(item.getPNO(), item);
    }

    public void resetCompanyUnitCache() {
        this.companyCache = null;
    }

    public CompanyUnitEntity getCompanyUnit(long pNummer) {
        CompanyUnitEntity companyUnitEntity = this.getCompanyUnitCache().get(pNummer);
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

    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private IndustryRepository industryRepository;

    public IndustryEntity setIndustry(int code, String name) {
        return this.setIndustry(code, name, false);
    }
    public IndustryEntity setIndustry(int code, String name, boolean noUpdate) {
        IndustryEntity industryEntity = this.getIndustryCache().get(code);
        if (industryEntity == null) {
            industryEntity = new IndustryEntity();
            industryEntity.setCode(code);
        }
        if (name != null && !name.isEmpty() && (industryEntity.getName() == null || (!noUpdate && !industryEntity.getName().equals(name)))) {
            industryEntity.setName(name);
            this.industryRepository.save(industryEntity);
            this.putIndustryCache(industryEntity);
        }
        return industryEntity;
    }

    public IndustryEntity getIndustryEntity(int code) {
        return this.industryRepository.getByCode(code);
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

}
