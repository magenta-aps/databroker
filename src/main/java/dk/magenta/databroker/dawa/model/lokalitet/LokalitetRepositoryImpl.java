package dk.magenta.databroker.dawa.model.lokalitet;

import dk.magenta.databroker.dawa.model.RepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.util.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 09-12-14.
 */

interface LokalitetRepositoryCustom {
    public Collection<LokalitetEntity> search(SearchParameters parameters, boolean printQuery);
}

public class LokalitetRepositoryImpl extends RepositoryImplementation<LokalitetEntity> implements LokalitetRepositoryCustom {

    @Override
    public Collection<LokalitetEntity> search(SearchParameters parameters, boolean printQuery) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct "+LokalitetEntity.databaseKey+" from LokalitetEntity as "+LokalitetEntity.databaseKey);
        join.setPrefix("join ");

        if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.POST, Key.VEJ)) {
            join.append(LokalitetEntity.joinVej());

            conditions.addCondition(VejstykkeEntity.vejCondition(parameters));

            if (parameters.has(Key.POST)) {
                join.append(VejstykkeEntity.joinPost());
                conditions.addCondition(PostNummerEntity.postCondition(parameters));
            }

            if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
                join.append(VejstykkeEntity.joinKommune());
                conditions.addCondition(KommuneEntity.kommuneCondition(parameters));
                conditions.addCondition(KommuneEntity.landCondition(parameters));
            }
        }

        conditions.addCondition(LokalitetEntity.lokalitetCondition(parameters));

        /*if (parameters.hasGlobalCondition()) {
            conditions.addCondition(parameters.getGlobalCondition().whereField("vej"));
        }*/

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

        hql.append("order by "+LokalitetEntity.databaseKey+".navn");

        return this.query(hql, conditions, parameters.getGlobalCondition(), printQuery);
    }
}
