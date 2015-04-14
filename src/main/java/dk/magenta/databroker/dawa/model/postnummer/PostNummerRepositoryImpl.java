package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.objectcontainers.Pair;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 09-12-14.
 */

interface PostNummerRepositoryCustom extends EntityRepositoryCustom<PostNummerEntity, Integer> {
    public PostNummerEntity getByNr(int nr);
}

public class PostNummerRepositoryImpl extends EntityRepositoryImplementation<PostNummerEntity, Integer> implements PostNummerRepositoryCustom {


    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<PostNummerEntity> search(SearchParameters parameters) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct "+PostNummerEntity.databaseKey+" from PostNummerEntity as "+PostNummerEntity.databaseKey);
        join.setPrefix("join ");

        if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
            join.append(PostNummerEntity.joinKommune());
            conditions.addCondition(KommuneEntity.landCondition(parameters));
            conditions.addCondition(KommuneEntity.kommuneCondition(parameters));
        }

        conditions.addCondition(PostNummerEntity.postCondition(parameters));

        if (parameters.has(Key.VEJ)) {
            Pair<String[],Condition> post = PostNummerEntity.joinVej();
            join.append(post.getLeft());
            conditions.addCondition(post.getRight());
            conditions.addCondition(VejstykkeEntity.vejCondition(parameters));
        }

        if (parameters.hasGlobalCondition()) {
            conditions.addCondition(parameters.getGlobalCondition().whereField(PostNummerEntity.databaseKey));
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

        hql.append("order by "+PostNummerEntity.databaseKey+".latestVersion.nr");

        return this.query(hql, conditions, parameters.getGlobalCondition());
    }

    @Override
    public HashSet<Integer> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + PostNummerEntity.databaseKey + ".nr from PostNummerEntity as " + PostNummerEntity.databaseKey);
        return new HashSet<Integer>(q.getResultList());
    }


    @Override
    public PostNummerEntity getByDescriptor(Integer descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public PostNummerEntity getByDescriptor(Integer descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            return null;
        }
        ConditionList conditions = new ConditionList();
        conditions.addCondition(PostNummerEntity.descriptorCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct " + PostNummerEntity.databaseKey + " from PostNummerEntity as " + PostNummerEntity.databaseKey + " where " + conditions.getWhere(key);
        Collection<PostNummerEntity> postEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        if (postEntities != null && postEntities.size() > 0) {
            return postEntities.iterator().next();
        }
        return null;
    }

    @Override
    public PostNummerEntity getByNr(int nr) {
        return this.getByDescriptor(nr);
    }
}
