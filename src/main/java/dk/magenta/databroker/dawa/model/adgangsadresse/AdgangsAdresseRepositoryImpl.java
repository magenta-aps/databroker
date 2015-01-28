package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
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

public class AdgangsAdresseRepositoryImpl implements AdgangsAdresseRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<AdgangsAdresseEntity> search(SearchParameters parameters, boolean printQuery) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct adresse from AdgangsAdresseEntity as "+AdgangsAdresseEntity.databaseKey);
        join.setPrefix("join ");

        if (parameters.hasAny(Key.LAND, Key.KOMMUNE, Key.POST, Key.VEJ)) {

            join.append(AdgangsAdresseEntity.joinVej());

            conditions.addCondition(VejstykkeEntity.vejCondition(parameters));
            if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
                join.append(VejstykkeEntity.joinKommune());
                conditions.addCondition(KommuneEntity.landCondition(parameters));
                conditions.addCondition(KommuneEntity.kommuneCondition(parameters));

            }
            if (parameters.has(Key.POST)) {
                join.append("vejstykke.latestVersion.postnumre as post");
                conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.POST), "post.latestVersion.nr", "post.latestVersion.navn"));
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

        if (printQuery) {
            System.out.println(hql.join(" \n"));
        }
        Query q = this.entityManager.createQuery(hql.join(" "));
        q.setMaxResults(1000);
        Map<String, Object> queryParameters = conditions.getParameters();
        for (String key : queryParameters.keySet()) {
            if (printQuery) {
                System.out.println(key + " = " + queryParameters.get(key));
            }
            q.setParameter(key, queryParameters.get(key));
        }
        return q.getResultList();

    }
}
