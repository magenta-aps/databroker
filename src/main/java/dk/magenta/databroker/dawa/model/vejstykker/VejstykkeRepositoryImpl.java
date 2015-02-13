package dk.magenta.databroker.dawa.model.vejstykker;

import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.dawa.model.SearchParameters.Key;
import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 19-12-14.
 */
interface VejstykkeRepositoryCustom {
    public Collection<VejstykkeEntity> search(SearchParameters parameters, boolean quiet);
}

public class VejstykkeRepositoryImpl implements VejstykkeRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<VejstykkeEntity> search(SearchParameters parameters, boolean printQuery) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct vej from VejstykkeEntity as vej");

        join.setPrefix("join ");
        if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
            join.append("vej.kommune kommune");
            if (parameters.has(Key.LAND)) {
                conditions.addCondition(RepositoryUtil.whereFieldLand(parameters.get(Key.LAND)));
            }
            if (parameters.has(Key.KOMMUNE)) {
                conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.KOMMUNE), "kommune.kode", "kommune.navn"));
            }
        }
        if (parameters.has(Key.VEJ)) {
            conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.VEJ), "vej.kode", "vej.latestVersion.vejnavn"));
        }
        if (parameters.has(Key.LOKALITET)) {
            join.append("vej.latestVersion.lokaliteter lokalitet");
            conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.LOKALITET), null, "lokalitet.navn"));
        }
        if (parameters.has(Key.POST)) {
            join.append("vej.latestVersion.postnumre post");
            conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.POST), "post.latestVersion.nr", "post.latestVersion.navn"));
        }
        if (parameters.hasGlobalCondition()) {
            conditions.addCondition(parameters.getGlobalCondition().whereField("vej"));
        }

        // our conditions list should now be complete

        if (conditions.hasRequiredJoin()) {
            System.out.println("conditions.getRequiredJoin(): "+conditions.getRequiredJoin());
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
        hql.append("order by vej.kode");

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
