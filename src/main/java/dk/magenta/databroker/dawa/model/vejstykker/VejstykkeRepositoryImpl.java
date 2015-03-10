package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;

import java.util.Collection;

/**
 * Created by lars on 19-12-14.
 */
interface VejstykkeRepositoryCustom {
    public Collection<VejstykkeEntity> search(SearchParameters parameters);
    public VejstykkeEntity getByDesc(int descriptor);
    public void clear();
}

public class VejstykkeRepositoryImpl extends RepositoryImplementation<VejstykkeEntity> implements VejstykkeRepositoryCustom {

    public VejstykkeEntity getByDesc(int descriptor) {
        StringList hql = new StringList();
        hql.append("select distinct "+VejstykkeEntity.databaseKey+" from VejstykkeEntity as "+VejstykkeEntity.databaseKey);
        ConditionList conditions = new ConditionList();
        conditions.addCondition(VejstykkeEntity.descriptorCondition(descriptor));
        hql.append("where");
        hql.append(conditions.getWhere());
        Collection<VejstykkeEntity> vejstykkeEntities = this.query(hql, conditions, GlobalCondition.singleCondition);
        return vejstykkeEntities.size() > 0 ? vejstykkeEntities.iterator().next() : null;
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
}
