package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.ConditionList;
import dk.magenta.databroker.cprvejregister.model.SingleCondition;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 09-12-14.
 */

interface PostnummerRepositoryCustom {
    public Collection<PostnummerEntity> search(String[] post, GlobalCondition globalCondition);
}

public class PostnummerRepositoryImpl implements PostnummerRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<PostnummerEntity> search(String[] post, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select post from PostnummerEntity as post");
        join.setPrefix("join ");

        if (post != null) {
            conditions.addCondition(RepositoryUtil.whereField(post, "post.nummer", "post.latestVersion.navn"));
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

        hql.append("order by post.nummer");

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
