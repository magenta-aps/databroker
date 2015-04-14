package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.objectcontainers.Pair;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.Query;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 19-12-14.
 */
interface KommuneRepositoryCustom extends EntityRepositoryCustom<KommuneEntity, Integer> {
    public KommuneEntity getByKode(int kommunekode);
}

public class KommuneRepositoryImpl extends EntityRepositoryImplementation<KommuneEntity, Integer> implements KommuneRepositoryCustom {

    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    // Run a search where each input field may be a code or a name, and may contain leading and/or trailing wildcards
    // subject to a global condition (e.g. only extract entries with a version newer than a certain date)
    @Override
    // String land, String[] kommune, String[] postnr, String[] lokalitet, String[] vej
    public Collection<KommuneEntity> search(SearchParameters parameters) {
        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct "+KommuneEntity.databaseKey+" from KommuneEntity as "+KommuneEntity.databaseKey);
        join.setPrefix("join ");

        conditions.addCondition(KommuneEntity.landCondition(parameters));

        conditions.addCondition(KommuneEntity.kommuneCondition(parameters));
        if (parameters.has(Key.LOKALITET)) {
            join.append(KommuneEntity.joinLokalitet());
            conditions.addCondition(LokalitetEntity.lokalitetCondition(parameters));
        }
        if (parameters.has(Key.POST)) {
            Pair<String[],Condition> post = KommuneEntity.joinPost();
            join.append(post.getLeft());
            conditions.addCondition(post.getRight());
            conditions.addCondition(PostNummerEntity.postCondition(parameters));
        }
        if (parameters.has(Key.VEJ)) {
            join.append(KommuneEntity.joinVej());
            conditions.addCondition(VejstykkeEntity.vejCondition(parameters));
        }
        if (parameters.hasGlobalCondition()) {
            // Add any further restrictions from the global condition
            conditions.addCondition(parameters.getGlobalCondition().whereField("kommune"));
        }

        // our conditions list should now be complete

        if (conditions.hasRequiredJoin()) {
            join.append(conditions.getRequiredJoin());
        }

        // our join list should now be complete

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        join = null;
        if (conditions.size() > 0) {
            hql.append("where");
            hql.append(conditions.getWhere());
        }
        // Append order clause
        hql.append("order by "+KommuneEntity.databaseKey+".kode");

        return this.query(hql, conditions, parameters.getGlobalCondition());
    }

    @Override
    public HashSet<Integer> getKnownDescriptors() {
        Query q = this.entityManager.createQuery("select " + KommuneEntity.databaseKey + ".kode from KommuneEntity as " + KommuneEntity.databaseKey);
        return new HashSet<Integer>(q.getResultList());
    }

    @Override
    public KommuneEntity getByDescriptor(Integer descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public KommuneEntity getByDescriptor(Integer descriptor, Session session) {
        if (!this.hasKnownDescriptor(descriptor, true)) {
            //System.out.println("noooooo");
            return null;
        }
        //System.out.println("yay");
        ConditionList conditions = new ConditionList();
        conditions.addCondition(KommuneEntity.kommuneCondition(descriptor));
        final String key = this.getRandomKey();
        final String hql = "select distinct " + KommuneEntity.databaseKey + " from KommuneEntity as " + KommuneEntity.databaseKey + " where " + conditions.getWhere(key);
        Collection<KommuneEntity> kommuneEntities = this.query(hql, conditions.getParameters(key), GlobalCondition.singleCondition, session);
        //System.out.println("Found "+kommuneEntities.size()+" candidates for descriptor "+descriptor);
        if (kommuneEntities != null && kommuneEntities.size() > 0) {
            return kommuneEntities.iterator().next();
        }
        return null;
    }

    @Override
    public KommuneEntity getByKode(int kommunekode) {
        return this.getByDescriptor(kommunekode);
    }

}