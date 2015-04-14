package dk.magenta.databroker.cvr.model.deltager;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.*;

/**
 * Created by lars on 02-02-15.
 */


interface DeltagerRepositoryCustom extends EntityRepositoryCustom<DeltagerEntity, Long> {
    public void detach(DeltagerEntity deltagerEntity);
    public void detach(DeltagerVersionEntity deltagerVersionEntity);
}

public class DeltagerRepositoryImpl extends EntityRepositoryImplementation<DeltagerEntity, Long> implements DeltagerRepositoryCustom {

    private Logger log = Logger.getLogger(DeltagerRepositoryImpl.class);

    public List<TransactionCallback> getBulkwireCallbacks() {
        ArrayList<TransactionCallback> transactionCallbacks = new ArrayList<TransactionCallback>();
/*
        transactionCallbacks.add(new TransactionCallback() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                DeltagerRepositoryImpl repositoryImplementation = DeltagerRepositoryImpl.this;
                repositoryImplementation.log.info("Updating references between members and companies");
                double time = Util.getTime();
                repositoryImplementation.runNativeQuery("update cvr_deltager deltager " +
                        "inner join cvr_company company on deltager.cvr_nummer=company.cvr_nummer " +
                        "set deltager.company_id=company.id " +
                        "where deltager.company_id is NULL");
                repositoryImplementation.log.info("References updated in "+(Util.getTime()-time)+" ms");
                return null;
            }
        });*/
        return transactionCallbacks;
    }

    @Override
    public Collection<DeltagerEntity> search(SearchParameters parameters) {
        return null;
    }

    @Override
    public HashSet<Long> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + DeltagerEntity.databaseKey + ".deltagerNummer from DeltagerEntity as " + DeltagerEntity.databaseKey);
        return new HashSet<Long>(q.getResultList());
    }


    @Override
    public DeltagerEntity getByDescriptor(Long descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public DeltagerEntity getByDescriptor(Long descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        final String key = "id_"+ UUID.randomUUID().toString().replace("-","");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(DeltagerEntity.descriptorCondition(descriptor));
        final String hql = "select " + DeltagerEntity.databaseKey + " from DeltagerEntity " + DeltagerEntity.databaseKey + " where " + conditions.getWhere(key);
        List<DeltagerEntity> items = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        return items != null && !items.isEmpty() ? items.iterator().next() : null;
    }


    public void detach(DeltagerEntity deltagerEntity) {
        this.entityManager.detach(deltagerEntity);
    }
    public void detach(DeltagerVersionEntity deltagerVersionEntity) {
        this.entityManager.detach(deltagerVersionEntity);
    }



}
