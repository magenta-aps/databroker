package dk.magenta.databroker.cvr.model.deltager.status;

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


interface StatusRepositoryCustom extends EntityRepositoryCustom<StatusEntity, String> {
    public StatusEntity getByUuid(String uuid);
    public StatusEntity getByName(String name, Session session);
}

public class StatusRepositoryImpl extends EntityRepositoryImplementation<StatusEntity, String> implements StatusRepositoryCustom {

    private Logger log = Logger.getLogger(StatusRepositoryImpl.class);

    public StatusEntity getByUuid(String uuid) {
        ConditionList conditions = new ConditionList();
        conditions.addCondition(StatusEntity.uuidCondition(uuid));
        final String key = this.getRandomKey();
        final String hql = "select distinct "+StatusEntity.databaseKey+" from StatusEntity "+StatusEntity.databaseKey+" where " + conditions.getWhere(key);
        List<StatusEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }

    public StatusEntity getByName(String name, Session session) {
        ConditionList conditions = new ConditionList();
        conditions.addCondition(StatusEntity.nameCondition(name));
        final String key = this.getRandomKey();
        final String hql = "select distinct " + StatusEntity.databaseKey + " from StatusEntity " + StatusEntity.databaseKey + " where " + conditions.getWhere(key);
        List<StatusEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }


    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<StatusEntity> search(SearchParameters parameters) {
        return null;
    }

    @Override
    public HashSet<String> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + StatusEntity.databaseKey + ".name from StatusEntity as " + StatusEntity.databaseKey);
        return new HashSet<String>(q.getResultList());
    }


    @Override
    public StatusEntity getByDescriptor(String descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public StatusEntity getByDescriptor(String descriptor, Session session) {
        return null;
    }

    @Override
    public void addKnownDescriptor(String descriptor, boolean dbLoad) {
        super.addKnownDescriptor(descriptor, dbLoad);
    }


    @Override
    public long count(Session session) {
        return (Long) session.createQuery("select count(*) from StatusEntity").uniqueResult();
    }

    @Override
    public List<StatusEntity> findAll(Session session) {
        return session.createQuery("select entity from StatusEntity entity").list();
    }

}
