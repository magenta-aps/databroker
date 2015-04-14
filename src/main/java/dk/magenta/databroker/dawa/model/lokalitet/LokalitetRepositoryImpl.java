package dk.magenta.databroker.dawa.model.lokalitet;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.core.model.EntityRepositoryCustom;
import dk.magenta.databroker.core.model.EntityRepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
import dk.magenta.databroker.dawa.model.postnummer.PostNummerEntity;
import dk.magenta.databroker.dawa.model.temaer.KommuneEntity;
import dk.magenta.databroker.dawa.model.vejstykker.VejstykkeEntity;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.TransactionCallback;
import dk.magenta.databroker.util.objectcontainers.StringList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 09-12-14.
 */

interface LokalitetRepositoryCustom extends EntityRepositoryCustom<LokalitetEntity, Long> {
    public LokalitetEntity getByKommunekodeAndLokalitetsnavn(int kommuneKode, String lokalitetsnavn);
}

public class LokalitetRepositoryImpl extends EntityRepositoryImplementation<LokalitetEntity, Long> implements LokalitetRepositoryCustom {

    @Override
    public List<TransactionCallback> getBulkwireCallbacks() {
        return null;
    }

    @Override
    public Collection<LokalitetEntity> search(SearchParameters parameters) {

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

        return this.query(hql, conditions, parameters.getGlobalCondition());
    }

    public LokalitetEntity getByKommunekodeAndLokalitetsnavn(int kommuneKode, String lokalitetsnavn) {
        ConditionList conditions = new ConditionList();
        conditions.addCondition(LokalitetEntity.lokalitetCondition(lokalitetsnavn));
        StringList hql = new StringList();
        hql.append("select distinct " + LokalitetEntity.databaseKey + " from LokalitetEntity as " + LokalitetEntity.databaseKey);
        //StringList join = new StringList();
        //join.append(LokalitetEntity.joinVej());
        //join.append(VejstykkeEntity.joinKommune());
        conditions.addCondition(KommuneEntity.kommuneCondition(kommuneKode));
        hql.append("where");
        hql.append(conditions.getWhere());
        Collection<LokalitetEntity> lokalitetEntities = this.query(hql, conditions, GlobalCondition.singleCondition);
        hql = null;
        return lokalitetEntities.size() > 0 ? lokalitetEntities.iterator().next() : null;
    }

    @Override
    public HashSet<Long> getKnownDescriptors() {
        return new HashSet<Long>();
    }


    @Override
    public LokalitetEntity getByDescriptor(Long descriptor) {
        return this.getByDescriptor(descriptor, null);
    }

    @Override
    public LokalitetEntity getByDescriptor(Long descriptor, Session session) {
        return null;
    }
}
