package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;

import java.util.Collection;

/**
 * Created by lars on 02-02-15.
 */


interface CompanyUnitRepositoryCustom {
    public void bulkWireReferences();
    public void clear();
    public CompanyUnitEntity getByPno(long pno);
}

public class CompanyUnitRepositoryImpl extends RepositoryImplementation<CompanyUnitEntity> implements CompanyUnitRepositoryCustom {

    private Logger log = Logger.getLogger(CompanyUnitRepositoryImpl.class);

    public void bulkWireReferences() {
        double time;
        this.log.info("Updating references between units and companies");
        time = Util.getTime();
        this.entityManager.createNativeQuery("update cvr_companyunit unit join cvr_company company on unit.cvr_nummer=company.cvr_nummer set unit.company_id=company.id where unit.company_id is NULL").executeUpdate();
        this.log.info("References updated in " + (Util.getTime() - time) + " ms");

        this.log.info("Updating references between units and addresses");
        time = Util.getTime();
        this.entityManager.createNativeQuery("update cvr_companyunit_version unit join dawa_enhedsadresse address on unit.address_descriptor=address.descriptor set unit.address_id=address.id where unit.address_id is NULL").executeUpdate();
        this.log.info("References updated in "+(Util.getTime() - time)+" ms");
    }

    public void clear() {
        if (this.entityManager != null) {
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }


    public CompanyUnitEntity getByPno(long pno) {
        StringList hql = new StringList();
        hql.append("select distinct "+CompanyUnitEntity.databaseKey+" from CompanyUnitEntity as "+CompanyUnitEntity.databaseKey);
        ConditionList conditions = new ConditionList();
        conditions.addCondition(CompanyUnitEntity.pnoCondition(pno));
        hql.append("where");
        hql.append(conditions.getWhere());
        Collection<CompanyUnitEntity> companyUnitEntities = this.query(hql, conditions, GlobalCondition.singleCondition);
        return companyUnitEntities.size() > 0 ? companyUnitEntities.iterator().next() : null;
    }

}
