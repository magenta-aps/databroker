package dk.magenta.databroker.cprvejregister.model.navngivenvej;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by lars on 09-12-14.
 */

interface NavngivenVejRepositoryCustom {
    public Collection<NavngivenVejEntity> search(String kommune, String vej);
}

public class NavngivenVejRepositoryImpl implements NavngivenVejRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<NavngivenVejEntity> search(String kommune, String vej) {
        Pattern onlyDigits = Pattern.compile("^\\d+$");

        StringList hql = new StringList();
        StringList join = new StringList();
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Object[] params;

        hql.append("select vej from NavngivenVejEntity as vej");

        join.setPrefix("join ");
        join.append("vej.latestVersion vejversion");
        join.append("vejversion.kommunedeleAfNavngivenVej delvej");
        if (kommune != null) {
            join.append("delvej.kommune kommune");
        }

        StringList where = new StringList();
        if (kommune != null) {
            params = RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn");
            where.append(params[0] + " " + params[1] + " :kommune");
            parameters.put("kommune", params[2]);
            /*if (onlyDigits.matcher(kommune).matches()) {
                where.append("kommune.kommunekode = " + kommune);
            } else {
                where.append("kommune.latestVersion.navn like :kommuneNavn");
                parameters.put("kommuneNavn", "%"+kommune+"%");
            }*/
        }
        if (vej != null) {
            params = RepositoryUtil.whereField(vej, "delvej.vejkode", "vejversion.vejnavn");
            where.append(params[0] + " " + params[1] + " :vej");
            parameters.put("vej", params[2]);
            /*if (onlyDigits.matcher(vej).matches()) {
                where.append("delvej.vejKode = " + vej);
            } else {
                where.append("vejversion.vejnavn like :vejNavn");
                parameters.put("vejNavn", "%"+vej+"%");
            }*/
        }

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        if (where.size()>0) {
            hql.append("where ");
            hql.append(where.join(" and "));
        }
        hql.append("order by vejversion.vejnavn");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        for (String key : parameters.keySet()) {
            System.out.println(key+" = "+parameters.get(key));
            q.setParameter(key, parameters.get(key));
        }
        return q.getResultList();
    }
}
