package dk.magenta.databroker.cvr.model.deltager.type;

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


interface TypeRepositoryCustom extends EntityRepositoryCustom<TypeEntity, String> {
    public TypeEntity getByUuid(String uuid);
    public TypeEntity getByName(String name);
    public TypeEntity getByName(String name, Session session);
}

public class TypeRepositoryImpl extends EntityRepositoryImplementation<TypeEntity, String> implements TypeRepositoryCustom {

    private Logger log = Logger.getLogger(TypeRepositoryImpl.class);

    public TypeEntity getByUuid(String uuid) {
        ConditionList conditions = new ConditionList();
        conditions.addCondition(TypeEntity.uuidCondition(uuid));
        final String key = this.getRandomKey();
        final String hql = "select distinct " + TypeEntity.databaseKey + " from TypeEntity " + TypeEntity.databaseKey + " where " + conditions.getWhere(key);
        List<TypeEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }

    public TypeEntity getByName(String name) {
        log.info("getByName("+name+")");
        return this.getByDescriptor(name);
    }
    public TypeEntity getByName(String name, Session session) {
        log.info("getByName("+name+")");
        return this.getByDescriptor(name, session);
    }


    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<TypeEntity> search(SearchParameters parameters) {
        return null;
    }

    @Override
    public HashSet<String> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + TypeEntity.databaseKey + ".name from TypeEntity as " + TypeEntity.databaseKey);
        return new HashSet<String>(q.getResultList());
    }


    @Override
    public TypeEntity getByDescriptor(String descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public TypeEntity getByDescriptor(String descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(TypeEntity.nameCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct " + TypeEntity.databaseKey + " from TypeEntity " + TypeEntity.databaseKey + " where " + conditions.getWhere(key);
        List<TypeEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }


    @Override
    public void addKnownDescriptor(String descriptor, boolean dbLoad) {
        super.addKnownDescriptor(descriptor, dbLoad);
    }


    @Override
    public long count(Session session) {
        return (Long) session.createQuery("select count(*) from TypeEntity").uniqueResult();
    }

    @Override
    public List<TypeEntity> findAll(Session session) {
        return session.createQuery("select entity from TypeEntity entity").list();
    }

}
