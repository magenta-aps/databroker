package dk.magenta.databroker.cprvejregister.model.husnummer;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
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

        StringBuilder hql = new StringBuilder();
        StringList join = new StringList();
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        hql.append("select hus from HusnummerEntity as hus ");

        join.setPrefix("join ");
        join.append("hus.adgangspunkt punkt ");
        join.append("punkt.latestVersion punktversion ");

        StringList where = new StringList();
        if (kommune != null) {
            if (onlyDigits.matcher(kommune).matches()) {
                where.append("kommune.kommunekode = " + kommune);
            } else {
                where.append("kommune.latestVersion.navn like :kommuneNavn");
                parameters.put("kommuneNavn", "%"+kommune+"%");
            }
        }
        if (vej != null) {
            if (onlyDigits.matcher(vej).matches()) {
                where.append("delvej.vejKode = " + vej);
            } else {
                where.append("vejversion.vejnavn like :vejNavn");
                parameters.put("vejNavn", "%"+vej+"%");
            }
        }
        if (kommune != null || vej != null) {
            join.append("hus.navngivenVej vej ");
            join.append("vej.latestVersion vejversion ");
            join.append("vejversion.kommunedeleAfNavngivenVej delvej ");
            if (kommune != null) {
                join.append("delvej.kommune kommune ");
            }
        }

        if (post != null) {
            join.append("punktversion.liggerIPostnummer post ");
            if (onlyDigits.matcher(post).matches()) {
                where.append("post.nummer = " + post);
            } else {
                where.append("post.latestVersion.navn like :postNavn");
                parameters.put("postNavn", "%"+post+"%");
            }
        }
        if (husnr != null) {
            where.append("hus.husnummerbetegnelse like :husNr");
            parameters.put("husNr", "%"+husnr+"%");
        }

        hql.append(join.join());
        hql.append("where ");
        hql.append(where.join(" and "));

        hql.append(" order by hus.husnummerbetegnelse");

        Query q = this.entityManager.createQuery(hql.toString());
        for (String key : parameters.keySet()) {
            q.setParameter(key, parameters.get(key));
        }
        return q.getResultList();
    }
}
