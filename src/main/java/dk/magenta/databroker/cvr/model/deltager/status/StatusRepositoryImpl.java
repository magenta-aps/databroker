package dk.magenta.databroker.cvr.model.deltager.status;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by lars on 02-02-15.
 */


interface StatusRepositoryCustom {
    public StatusEntity getByUuid(String uuid);
    public StatusEntity getByName(String name);
}

public class StatusRepositoryImpl extends RepositoryImplementation<StatusEntity> implements StatusRepositoryCustom {

    private Logger log = Logger.getLogger(StatusRepositoryImpl.class);

    public StatusEntity getByUuid(String uuid) {
        StringList hql = new StringList();
        hql.append("select distinct "+StatusEntity.databaseKey+" from StatusEntity "+StatusEntity.databaseKey+" where ");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(StatusEntity.uuidCondition(uuid));
        hql.append(conditions.getWhere());
        List<StatusEntity> items = this.query(hql, conditions, GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }

    public StatusEntity getByName(String name) {
        log.info("getByName(" + name + ")");
        StringList hql = new StringList();
        hql.append("select distinct "+StatusEntity.databaseKey+" from StatusEntity "+StatusEntity.databaseKey+" where ");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(StatusEntity.nameCondition(name));
        hql.append(conditions.getWhere());
        List<StatusEntity> items = this.query(hql, conditions, GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }


}
