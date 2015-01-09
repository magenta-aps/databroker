package dk.magenta.databroker.dawa.model.lokalitet;

import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.register.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 09-12-14.
 */

interface LokalitetRepositoryCustom {
    public Collection<LokalitetEntity> search(String land, String[] post, String[] kommune, String[] vej, String[] lokalitet, GlobalCondition globalCondition);
}

public class LokalitetRepositoryImpl implements LokalitetRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<LokalitetEntity> search(String land, String[] post, String[] kommune, String[] vej, String[] lokalitet, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct lokalitet from LokalitetEntity as lokalitet");
        join.setPrefix("join ");


        if (land != null) {
            conditions.addCondition(RepositoryUtil.whereFieldLand(land));
        }

        if ((vej != null && vej.length>0) || (kommune != null && kommune.length>0) || (post != null && post.length>0)) {
            join.append("lokalitet.vejstykkeVersioner vejVersion");
            join.append("vejVersion.entity as vej");

            if (vej != null) {
                conditions.addCondition(RepositoryUtil.whereField(vej, "vej.kode", "vejVersion.vejnavn"));
            }

            if (post != null) {
                join.append("vejversion.postnumre post");
                conditions.addCondition(RepositoryUtil.whereField(post, "post.latestVersion.nr", "post.latestVersion.navn"));
            }

            if (kommune != null) {
                join.append("vej.kommune kommune");
                conditions.addCondition(RepositoryUtil.whereField(kommune, "kommune.kode", "kommune.navn"));
            }
        }

        if (lokalitet != null && lokalitet.length > 0) {
            conditions.addCondition(RepositoryUtil.whereField(lokalitet, null, "lokalitet.navn"));
        }

        /*if (globalCondition != null) {
            conditions.addCondition(globalCondition.whereField("vej"));
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

        hql.append("order by lokalitet.navn");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        Map<String, Object> parameters = conditions.getParameters();
        for (String key : parameters.keySet()) {
            System.out.println(key+" = "+parameters.get(key));
            q.setParameter(key, parameters.get(key));
        }
        return q.getResultList();
    }
}
