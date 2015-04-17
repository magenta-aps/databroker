package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.Query;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 19-12-14.
 */
interface VejstykkeRepositoryCustom extends EntityRepositoryCustom<VejstykkeEntity, Integer> {
}

public class VejstykkeRepositoryImpl extends EntityRepositoryImplementation<VejstykkeEntity, Integer> implements VejstykkeRepositoryCustom {


    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<VejstykkeEntity> search(SearchParameters parameters) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct "+VejstykkeEntity.databaseKey+" from VejstykkeEntity as "+VejstykkeEntity.databaseKey);

        join.setPrefix("join ");
        if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
            join.append(VejstykkeEntity.joinKommune());
            if (parameters.has(Key.LAND)) {
                conditions.addCondition(KommuneEntity.landCondition(parameters));
            }
            if (parameters.has(Key.KOMMUNE)) {
                conditions.addCondition(KommuneEntity.kommuneCondition(parameters));
            }
        }
        if (parameters.has(Key.VEJ)) {
            conditions.addCondition(VejstykkeEntity.vejCondition(parameters));
        }
        if (parameters.has(Key.LOKALITET)) {
            join.append(VejstykkeEntity.joinLokalitet());
            conditions.addCondition(LokalitetEntity.lokalitetCondition(parameters));
        }
        if (parameters.has(Key.POST)) {
            join.append(VejstykkeEntity.joinPost());
            conditions.addCondition(PostNummerEntity.postCondition(parameters));
        }
        if (parameters.hasGlobalCondition()) {
            conditions.addCondition(parameters.getGlobalCondition().whereField("vej"));
        }

        // our conditions list should now be complete

        if (conditions.hasRequiredJoin()) {
            join.append(conditions.getRequiredJoin());
        }

        // our join list should now be complete

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        if (conditions.size() > 0) {
            hql.append("where");
            hql.append(conditions.getWhere());
        }
        hql.append("order by "+VejstykkeEntity.databaseKey+".kode");

        return this.query(hql, conditions, parameters.getGlobalCondition());
    }

    @Override
    public HashSet<Integer> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + VejstykkeEntity.databaseKey + ".descriptor from VejstykkeEntity as " + VejstykkeEntity.databaseKey);
        return new HashSet<Integer>(q.getResultList());
    }


    @Override
    public VejstykkeEntity getByDescriptor(Integer descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public VejstykkeEntity getByDescriptor(Integer descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(VejstykkeEntity.descriptorCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct " + VejstykkeEntity.databaseKey + " from VejstykkeEntity as " + VejstykkeEntity.databaseKey + " where " + conditions.getWhere(key);
        Collection<VejstykkeEntity> vejstykkeEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        if (vejstykkeEntities != null && vejstykkeEntities.size() > 0) {
            VejstykkeEntity vejstykkeEntity = vejstykkeEntities.iterator().next();
            return vejstykkeEntity;
        }
        return null;
    }

    @Override
    public void addKnownDescriptor(Integer descriptor, boolean dbLoad) {
        super.addKnownDescriptor(descriptor, dbLoad);
    }


    @Override
    public long count(Session session) {
        return (Long) session.createQuery("select count(*) from VejstykkeEntity").uniqueResult();
    }

    @Override
    public List<VejstykkeEntity> findAll(Session session) {
        return session.createQuery("select entity from VejstykkeEntity entity").list();
    }
}
