package dk.magenta.databroker.dawa.model.postnummer;

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
 * Created by lars on 09-12-14.
 */

interface PostNummerRepositoryCustom {
    public Collection<PostNummerEntity> search(SearchParameters parameters, boolean printQuery);
}

public class PostNummerRepositoryImpl implements PostNummerRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<PostNummerEntity> search(SearchParameters parameters, boolean printQuery) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct post from PostNummerEntity as post");
        join.setPrefix("join ");


        if (parameters.hasAny(Key.LAND, Key.KOMMUNE)) {
            join.append("post.latestVersion.kommuner kommune");
            if (parameters.has(Key.LAND)) {
                conditions.addCondition(RepositoryUtil.whereFieldLand(parameters.get(Key.LAND)));
            }
            if (parameters.has(Key.KOMMUNE)) {
                conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.KOMMUNE), "kommune.kode", "kommune.navn"));
            }
        }

        if (parameters.has(Key.POST)) {
            conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.POST), "post.latestVersion.nr", "post.latestVersion.navn"));
        }

        if (parameters.has(Key.VEJ)) {
            join.append("post.vejstykkeVersioner vejVersion");
            join.append("vejVersion.entity vej");
            conditions.addCondition(RepositoryUtil.whereVersionLatest("vejVersion"));
            conditions.addCondition(RepositoryUtil.whereField(parameters.get(Key.VEJ), "vej.kode", "vejVersion.vejnavn"));
        }



        if (parameters.hasGlobalCondition()) {
            conditions.addCondition(parameters.getGlobalCondition().whereField("post"));
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

        hql.append("order by post.latestVersion.nr");

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
