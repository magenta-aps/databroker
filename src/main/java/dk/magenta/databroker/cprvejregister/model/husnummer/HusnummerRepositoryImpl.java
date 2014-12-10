package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;

import javax.persistence.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        Pattern onlyDigits = Pattern.compile("^\\d+$");

        StringList hql = new StringList();
        StringList join = new StringList();
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Object[] params;

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
                params = RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn");
                where.append(params[0] + " " + params[1] + " :kommune");
                parameters.put("kommune", params[2]);
            }
            if (vej != null) {
                params = RepositoryUtil.whereField(vej, "delvej.vejKode", "vejversion.vejnavn");
                where.append(params[0] + " " + params[1] + " :vej");
                parameters.put("vej", params[2]);
            }
        }

        if (post != null) {
            join.append("punktversion.liggerIPostnummer post");
            params = RepositoryUtil.whereField(post, "post.nummer", "post.latestVersion.navn");
            where.append(params[0] + " " + params[1] + " :post");
            parameters.put("post", params[2]);
        }
        if (husnr != null) {
            params = RepositoryUtil.whereField(husnr, null, "hus.husnummerbetegnelse");
            where.append(params[0] + " " + params[1] + " :husnr");
            parameters.put("husnr", params[2]);
        }

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        if (where.size()>0) {
            hql.append("where ");
            hql.append(where.join(" and "));
        }

        hql.append("order by hus.husnummerbetegnelse");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        for (String key : parameters.keySet()) {
            q.setParameter(key, parameters.get(key));
            System.out.println(key + " = " + parameters.get(key));
        }
        return q.getResultList();
    }
}
