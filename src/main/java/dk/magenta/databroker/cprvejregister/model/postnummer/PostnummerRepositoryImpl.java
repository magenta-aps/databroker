package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.Condition;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by lars on 09-12-14.
 */

interface PostnummerRepositoryCustom {
    public Collection<PostnummerEntity> search(String post);
    public Collection<PostnummerEntity> search(String post, GlobalCondition globalCondition);
}

public class PostnummerRepositoryImpl implements PostnummerRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<PostnummerEntity> search(String post) {
        return this.search(post, null);
    }
    @Override
    public Collection<PostnummerEntity> search(String post, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ArrayList<Condition> conditions = new ArrayList<Condition>();

        hql.append("select post from PostnummerEntity as post");
        join.setPrefix("join ");

        if (post != null) {
            conditions.add(RepositoryUtil.whereField(post, "post.nummer", "post.latestVersion.navn"));
        }
        if (globalCondition != null) {
            conditions.addAll(globalCondition.whereField("post"));
        }

        // our conditions list should now be complete

        for (Condition c : conditions) {
            if (c.hasRequiredJoin()) {
                join.append(c.getRequiredJoin());
            }
        }

        // our join list should now be complete

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        if (conditions.size() > 0) {
            hql.append(Condition.concatWhere(conditions));
        }

        hql.append("order by post.nummer");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        Condition.addParameters(conditions, q);
        return q.getResultList();
    }
}
