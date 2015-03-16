package dk.magenta.databroker.cvr.model.companyunit;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 02-02-15.
 */


interface CompanyUnitRepositoryCustom {
    public void bulkWireReferences();
    public void clear();
    public CompanyUnitEntity getByPno(long pno);
    public List<Long> getUnitNumbers();
}

public class CompanyUnitRepositoryImpl extends RepositoryImplementation<CompanyUnitEntity> implements CompanyUnitRepositoryCustom {

    private Logger log = Logger.getLogger(CompanyUnitRepositoryImpl.class);

    public void bulkWireReferences() {
        double time;
        this.log.info("Updating references between units and companies");
        time = Util.getTime();
        this.entityManager.createNativeQuery("update cvr_companyunit_version unitversion " +
                "join cvr_company company on unitversion.cvr_nummer=company.cvr_nummer " +
                "set unitversion.company_version_id=company.latest_version_id " +
                "where unitversion.company_version_id is NULL").executeUpdate();
        this.log.info("References updated in " + (Util.getTime() - time) + " ms");

        this.log.info("Updating references between units and addresses");
        time = Util.getTime();
        this.entityManager.createNativeQuery("update cvr_companyunit_version unitversion " +
                "join dawa_enhedsadresse address on unitversion.postal_address_descriptor=address.descriptor " +
                "set unitversion.postal_address_enheds_adresse=address.id " +
                "where unitversion.postal_address_enheds_adresse is NULL").executeUpdate();
        this.entityManager.createNativeQuery("update cvr_companyunit_version unitversion " +
                "join dawa_enhedsadresse address on unitversion.location_address_descriptor=address.descriptor " +
                "set unitversion.location_address_enheds_adresse=address.id " +
                "where unitversion.location_address_enheds_adresse is NULL").executeUpdate();
        this.log.info("References updated in "+(Util.getTime() - time)+" ms");
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


    public List<Long> getUnitNumbers() {
        Query q = this.entityManager.createQuery("select " + CompanyUnitEntity.databaseKey + ".pno from CompanyUnitEntity as " + CompanyUnitEntity.databaseKey);
        return q.getResultList();
    }

}
