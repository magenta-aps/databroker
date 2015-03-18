package dk.magenta.databroker.cvr.model.deltager;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.Util;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by lars on 02-02-15.
 */


interface DeltagerRepositoryCustom {
    public void bulkWireReferences();
    public void clear();
    public DeltagerEntity getByDeltagerNummer(long deltagernummer);
    public List<Long> getDeltagerNumbers();
}

public class DeltagerRepositoryImpl extends RepositoryImplementation<DeltagerEntity> implements DeltagerRepositoryCustom {

    private Logger log = Logger.getLogger(DeltagerRepositoryImpl.class);

    public void bulkWireReferences() {
        /*this.log.info("Updating references between members and companies");
        double time = Util.getTime();
        this.runNativeQuery("update cvr_deltager deltager " +
                "inner join cvr_company company on deltager.cvr_nummer=company.cvr_nummer " +
                "set deltager.company_id=company.id " +
                "where deltager.company_id is NULL");
        this.log.info("References updated in "+(Util.getTime()-time)+" ms");*/
    }

    public DeltagerEntity getByDeltagerNummer(long deltagernummer) {
        StringList hql = new StringList();
        hql.append("select "+DeltagerEntity.databaseKey+" from DeltagerEntity "+DeltagerEntity.databaseKey+" where ");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(DeltagerEntity.nummerCondition(deltagernummer));
        hql.append(conditions.getWhere());
        List<DeltagerEntity> items = this.query(hql, conditions, GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }

    public List<Long> getDeltagerNumbers() {
        Query q = this.entityManager.createQuery("select " + DeltagerEntity.databaseKey + ".deltagerNummer from DeltagerEntity as " + DeltagerEntity.databaseKey);
        return q.getResultList();
    }

}
