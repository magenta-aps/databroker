package dk.magenta.databroker.cvr.model.form;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.cvr.model.industry.IndustryEntity;
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

interface CompanyFormRepositoryCustom extends EntityRepositoryCustom<CompanyFormEntity, Integer> {
    public CompanyFormEntity getByCode(int code);
}

public class CompanyFormRepositoryImpl extends EntityRepositoryImplementation<CompanyFormEntity, Integer> implements CompanyFormRepositoryCustom {

    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<CompanyFormEntity> search(SearchParameters parameters) {
        return null;
    }


    @Override
    public CompanyFormEntity getByDescriptor(Integer descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public CompanyFormEntity getByDescriptor(Integer descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(CompanyFormEntity.descriptorCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct "+CompanyFormEntity.databaseKey+" from CompanyFormEntity as "+CompanyFormEntity.databaseKey+" where "+conditions.getWhere(key);
        Collection<CompanyFormEntity> companyFormEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return companyFormEntities.size() > 0 ? companyFormEntities.iterator().next() : null;
    }

    @Override
    public HashSet<Integer> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + IndustryEntity.databaseKey + ".code from IndustryEntity as " + IndustryEntity.databaseKey);
        return new HashSet<Integer>(q.getResultList());
    }

    @Override
    public CompanyFormEntity getByCode(int code) {
        return this.getByDescriptor(code);
    }

    @Override
    public void addKnownDescriptor(Integer descriptor, boolean dbLoad) {
        super.addKnownDescriptor(descriptor, dbLoad);
    }

    @Override
    public long count(Session session) {
        return (Long) session.createQuery("select count(*) from CompanyFormEntity").uniqueResult();
    }

    @Override
    public List<CompanyFormEntity> findAll(Session session) {
        return session.createQuery("select entity from CompanyFormEntity entity").list();
    }
}
