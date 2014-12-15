package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.Condition;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;

import javax.persistence.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by lars on 09-12-14.
 */

interface HusnummerRepositoryCustom {
    public Collection<HusnummerEntity> search(String kommune, String vej, String post, String husnr);
}

public class HusnummerRepositoryImpl implements HusnummerRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<HusnummerEntity> search(String kommune, String vej, String post, String husnr) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ArrayList<Condition> conditions = new ArrayList<Condition>();

        hql.append("select hus from HusnummerEntity as hus");

        join.setPrefix("join ");
        join.append("hus.adgangspunkt punkt");
        join.append("punkt.latestVersion punktversion");

        StringList where = new StringList();
        if (kommune != null || vej != null) {
            join.append("hus.navngivenVej vej");
            join.append("vej.latestVersion vejversion");
            join.append("vejversion.kommunedeleAfNavngivenVej delvej");
            if (kommune != null) {
                join.append("delvej.kommune kommune");
                conditions.add(RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn"));
            }
            if (vej != null) {
                conditions.add(RepositoryUtil.whereField(vej, "delvej.vejKode", "vejversion.vejnavn"));
            }
        }

        if (post != null) {
            join.append("punktversion.liggerIPostnummer post");
            conditions.add(RepositoryUtil.whereField(post, "post.nummer", "post.latestVersion.navn"));
        }
        if (husnr != null) {
            conditions.add(RepositoryUtil.whereField(husnr, null, "hus.husnummerbetegnelse"));
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
        if (where.size()>0) {
            hql.append(Condition.concatWhere(conditions));
        }

        hql.append("order by hus.husnummerbetegnelse");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        Condition.addParameters(conditions, q);
        return q.getResultList();
    }
}
