package dk.magenta.databroker.cvr.model.deltager.type;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.cvr.model.deltager.DeltagerEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;
import org.springframework.transaction.support.TransactionCallback;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lars on 02-02-15.
 */


interface TypeRepositoryCustom {
    public TypeEntity getByUuid(String uuid);
    public TypeEntity getByName(String name);
}

public class TypeRepositoryImpl extends RepositoryImplementation<TypeEntity> implements TypeRepositoryCustom {

    private Logger log = Logger.getLogger(TypeRepositoryImpl.class);

    public TypeEntity getByUuid(String uuid) {
        StringList hql = new StringList();
        hql.append("select distinct "+TypeEntity.databaseKey+" from TypeEntity "+TypeEntity.databaseKey+" where ");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(TypeEntity.uuidCondition(uuid));
        hql.append(conditions.getWhere());
        List<TypeEntity> items = this.query(hql, conditions, GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }

    public TypeEntity getByName(String name) {
        log.info("getByName("+name+")");
        StringList hql = new StringList();
        hql.append("select distinct "+TypeEntity.databaseKey+" from TypeEntity "+TypeEntity.databaseKey+" where ");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(TypeEntity.nameCondition(name));
        hql.append(conditions.getWhere());
        List<TypeEntity> items = this.query(hql, conditions, GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }


}
