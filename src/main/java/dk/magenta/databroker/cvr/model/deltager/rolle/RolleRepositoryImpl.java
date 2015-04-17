package dk.magenta.databroker.cvr.model.deltager.rolle;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 02-02-15.
 */


interface RolleRepositoryCustom extends EntityRepositoryCustom<RolleEntity, String> {
    public RolleEntity getByUuid(String uuid);
    public RolleEntity getByName(String name, Session session);
}

public class RolleRepositoryImpl extends EntityRepositoryImplementation<RolleEntity, String> implements RolleRepositoryCustom {

    private Logger log = Logger.getLogger(RolleRepositoryImpl.class);

    public RolleEntity getByUuid(String uuid) {
        final String key = this.getRandomKey();
        ConditionList conditions = new ConditionList();
        conditions.addCondition(RolleEntity.uuidCondition(uuid));
        String hql = "select distinct " + RolleEntity.databaseKey + " from RolleEntity " + RolleEntity.databaseKey + " where " + conditions.getWhere(key);
        List<RolleEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }

    public RolleEntity getByName(String name, Session session) {
        log.info("getByName(" + name + ")");
        final String key = this.getRandomKey();
        ConditionList conditions = new ConditionList();
        conditions.addCondition(RolleEntity.nameCondition(name));
        String hql = "select distinct " + RolleEntity.databaseKey + " from RolleEntity " + RolleEntity.databaseKey + " where " + conditions.getWhere(key);
        List<RolleEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }


    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<RolleEntity> search(SearchParameters parameters) {
        return null;
    }

    @Override
    public HashSet<String> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + RolleEntity.databaseKey + ".name from RolleEntity as " + RolleEntity.databaseKey);
        return new HashSet<String>(q.getResultList());
    }


    @Override
    public RolleEntity getByDescriptor(String descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public RolleEntity getByDescriptor(String descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(RolleEntity.descriptorCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct " + RolleEntity.databaseKey + " from RolleEntity " + RolleEntity.databaseKey + " where " + conditions.getWhere(key);
        List<RolleEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }

    @Override
    public void addKnownDescriptor(String descriptor, boolean dbLoad) {
        super.addKnownDescriptor(descriptor, dbLoad);
    }


    @Override
    public long count(Session session) {
        return (Long) session.createQuery("select count(*) from RolleEntity").uniqueResult();
    }

    @Override
    public List<RolleEntity> findAll(Session session) {
        return session.createQuery("select entity from RolleEntity entity").list();
    }

}
