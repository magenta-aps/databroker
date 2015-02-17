package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.dawa.model.RepositoryImplementation;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.dawa.model.lokalitet.LokalitetEntity;
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

interface AdgangsAdresseRepositoryCustom {
    public Collection<AdgangsAdresseEntity> search(SearchParameters parameters, boolean printQuery);
}

public class AdgangsAdresseRepositoryImpl extends RepositoryImplementation<AdgangsAdresseEntity> implements AdgangsAdresseRepositoryCustom {

    @Override
    public Collection<AdgangsAdresseEntity> search(SearchParameters parameters, boolean printQuery) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct "+AdgangsAdresseEntity.databaseKey+" from AdgangsAdresseEntity as "+AdgangsAdresseEntity.databaseKey);
        join.setPrefix("join ");

        if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.POST, Key.VEJ, Key.LOKALITET)) {

            join.append(AdgangsAdresseEntity.joinVej());

            conditions.addCondition(VejstykkeEntity.vejCondition(parameters));
            if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
                join.append(VejstykkeEntity.joinKommune());
                conditions.addCondition(KommuneEntity.landCondition(parameters));
                conditions.addCondition(KommuneEntity.kommuneCondition(parameters));

            }
            if (parameters.has(Key.POST)) {
                join.append(VejstykkeEntity.joinPost());
                conditions.addCondition(PostNummerEntity.postCondition(parameters));
            }
            if (parameters.has(Key.LOKALITET)) {
                join.append(VejstykkeEntity.joinLokalitet());
                conditions.addCondition(LokalitetEntity.lokalitetCondition(parameters));
            }
        }

        conditions.addCondition(AdgangsAdresseEntity.husnrCondition(parameters));
        conditions.addCondition(AdgangsAdresseEntity.bnrCondition(parameters));
        
        if (parameters.hasGlobalCondition()) {
            conditions.addCondition(parameters.getGlobalCondition().whereField("adresse"));
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

        hql.append("order by "+AdgangsAdresseEntity.databaseKey+".husnr");

        return this.query(hql, conditions, parameters.getGlobalCondition(), printQuery);
    }
}
