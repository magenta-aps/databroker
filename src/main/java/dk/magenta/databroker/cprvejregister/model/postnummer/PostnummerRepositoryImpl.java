package dk.magenta.databroker.cprvejregister.model.postnummer;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by lars on 09-12-14.
 */

interface PostnummerRepositoryCustom {
    public Collection<PostnummerEntity> search(String post);
}

public class PostnummerRepositoryImpl implements PostnummerRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<PostnummerEntity> search(String post) {

        StringList hql = new StringList();
        //StringList join = new StringList();
        //join.setPrefix("join ");
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Object[] params;

        hql.append("select post from PostnummerEntity as post");


        StringList where = new StringList();
        if (post != null) {
            params = RepositoryUtil.whereField(post, "post.nummer", "post.latestVersion.navn");
            where.append(params[0] + " " + params[1] + " :post");
            parameters.put("post", params[2]);
        }

        //if (join.size()>0) {
        //    hql.append(join.join(" "));
        //}
        if (where.size()>0) {
            hql.append("where ");
            hql.append(where.join(" and "));
        }

        hql.append("order by postnr.nummer");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        for (String key : parameters.keySet()) {
            q.setParameter(key, parameters.get(key));
            System.out.println(key + " = " + parameters.get(key));
        }
        return q.getResultList();
    }
}
