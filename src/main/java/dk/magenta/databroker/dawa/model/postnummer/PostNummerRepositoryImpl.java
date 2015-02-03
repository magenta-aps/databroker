package dk.magenta.databroker.dawa.model.postnummer;

import dk.magenta.databroker.dawa.model.RepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.util.objectcontainers.Pair;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 09-12-14.
 */

interface PostNummerRepositoryCustom {
    public Collection<PostNummerEntity> search(SearchParameters parameters, boolean printQuery);
}

public class PostNummerRepositoryImpl extends RepositoryImplementation<PostNummerEntity> implements PostNummerRepositoryCustom {

    @Override
    public Collection<PostNummerEntity> search(SearchParameters parameters, boolean printQuery) {

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

        return this.query(hql, conditions, parameters.getGlobalCondition(), printQuery);
    }
}
