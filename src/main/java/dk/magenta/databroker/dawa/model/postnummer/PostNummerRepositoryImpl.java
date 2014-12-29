package dk.magenta.databroker.dawa.model.postnummer;

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

interface PostNummerRepositoryCustom {
    public Collection<PostNummerEntity> search(String land, String[] post, String[] kommune, GlobalCondition globalCondition);
}

public class PostNummerRepositoryImpl implements PostNummerRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<PostNummerEntity> search(String land, String[] post, String[] kommune, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select post from PostNummerEntity as post");
        join.setPrefix("join ");


        if (land != null) {
            conditions.addCondition(RepositoryUtil.whereFieldLand(land));
        }

        if (post != null) {
            conditions.addCondition(RepositoryUtil.whereField(post, "post.latestVersion.nr", "post.latestVersion.navn"));
        }

        if (kommune != null) {
            join.append("post.latestVersion.kommuner kommune");
            conditions.addCondition(RepositoryUtil.whereField(kommune, "kommune.kode", "kommune.navn"));
        }

        if (globalCondition != null) {
            conditions.addCondition(globalCondition.whereField("post"));
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