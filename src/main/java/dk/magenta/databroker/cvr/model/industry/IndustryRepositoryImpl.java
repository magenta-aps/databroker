package dk.magenta.databroker.cvr.model.industry;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;

import javax.persistence.Query;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


/**
 * Created by lars on 13-03-15.
 */

interface IndustryRepositoryCustom extends EntityRepositoryCustom<IndustryEntity, Integer> {
    public IndustryEntity getByCode(int code);
}

public class IndustryRepositoryImpl extends EntityRepositoryImplementation<IndustryEntity, Integer> implements IndustryRepositoryCustom {

    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<IndustryEntity> search(SearchParameters parameters) {
        return null;
    }


    @Override
    public IndustryEntity getByDescriptor(Integer descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public IndustryEntity getByDescriptor(Integer descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(IndustryEntity.descriptorCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct "+IndustryEntity.databaseKey+" from IndustryEntity as "+IndustryEntity.databaseKey+" where "+conditions.getWhere(key);
        Collection<IndustryEntity> industryEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return industryEntities.size() > 0 ? industryEntities.iterator().next() : null;
    }

    @Override
    public HashSet<Integer> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + IndustryEntity.databaseKey + ".code from IndustryEntity as " + IndustryEntity.databaseKey);
        return new HashSet<Integer>(q.getResultList());
    }

    @Override
    public IndustryEntity getByCode(int code) {
        return this.getByDescriptor(code);
    }

    @Override
    public void addKnownDescriptor(Integer descriptor, boolean dbLoad) {
        super.addKnownDescriptor(descriptor, dbLoad);
    }

    @Override
    public long count(Session session) {
        return (Long) session.createQuery("select count(*) from IndustryEntity").uniqueResult();
    }

    @Override
    public List<IndustryEntity> findAll(Session session) {
        return session.createQuery("select entity from IndustryEntity entity").list();
    }
}
