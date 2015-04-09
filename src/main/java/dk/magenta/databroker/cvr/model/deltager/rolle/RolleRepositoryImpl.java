package dk.magenta.databroker.cvr.model.deltager.rolle;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 02-02-15.
 */


interface RolleRepositoryCustom {
    public RolleEntity getByUuid(String uuid);
    public RolleEntity getByName(String name);
}

public class RolleRepositoryImpl extends RepositoryImplementation<RolleEntity> implements RolleRepositoryCustom {

    private Logger log = Logger.getLogger(RolleRepositoryImpl.class);

    public RolleEntity getByUuid(String uuid) {
        final String key = "id_"+ UUID.randomUUID().toString().replace("-","");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(RolleEntity.uuidCondition(uuid));
        String hql = "select distinct " + RolleEntity.databaseKey + " from RolleEntity " + RolleEntity.databaseKey + " where " + conditions.getWhere(key);
        List<RolleEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }

    public RolleEntity getByName(String name) {
        log.info("getByName(" + name + ")");
        final String key = "id_"+ UUID.randomUUID().toString().replace("-","");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(RolleEntity.nameCondition(name));
        String hql = "select distinct " + RolleEntity.databaseKey + " from RolleEntity " + RolleEntity.databaseKey + " where " + conditions.getWhere(key);
        List<RolleEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }


}
