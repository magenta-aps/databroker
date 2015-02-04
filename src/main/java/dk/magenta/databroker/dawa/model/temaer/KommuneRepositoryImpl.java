package dk.magenta.databroker.dawa.model.temaer;

import dk.magenta.databroker.dawa.model.RepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.Pair;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 19-12-14.
 */
interface KommuneRepositoryCustom {
    public Collection<KommuneEntity> search(SearchParameters parameters, boolean printQuery);
}

public class KommuneRepositoryImpl extends RepositoryImplementation<KommuneEntity> implements KommuneRepositoryCustom {

    // Run a search where each input field may be a code or a name, and may contain leading and/or trailing wildcards
    // subject to a global condition (e.g. only extract entries with a version newer than a certain date)
    @Override
    // String land, String[] kommune, String[] postnr, String[] lokalitet, String[] vej
    public Collection<KommuneEntity> search(SearchParameters parameters, boolean printQuery) {
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
        if (conditions.size() > 0) {
            hql.append("where");
            hql.append(conditions.getWhere());
        }
        // Append order clause
        hql.append("order by "+KommuneEntity.databaseKey+".kode");

        return this.query(hql, conditions, parameters.getGlobalCondition(), printQuery);
    }
}