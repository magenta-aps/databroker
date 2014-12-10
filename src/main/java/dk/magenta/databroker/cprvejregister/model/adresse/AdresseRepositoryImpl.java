package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
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

interface AdresseRepositoryCustom {
    public Collection<AdresseEntity> search(String kommune, String vej, String post, String husnr, String etage, String doer);
}

public class AdresseRepositoryImpl implements AdresseRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<AdresseEntity> search(String kommune, String vej, String post, String husnr, String etage, String doer) {
        Pattern onlyDigits = Pattern.compile("^\\d+$");

        StringList hql = new StringList();
        StringList join = new StringList();
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        hql.append("select adresse from AdresseEntity as adresse");

        join.setPrefix("join ");
        join.append("adresse.husnummer hus");
        join.append("hus.adgangspunkt punkt");
        join.append("punkt.latestVersion punktversion");

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
            join.append("hus.navngivenVej vej");
            join.append("vej.latestVersion vejversion");
            join.append("vejversion.kommunedeleAfNavngivenVej delvej");
            if (kommune != null) {
                join.append("delvej.kommune kommune");
            }
        }

        if (post != null) {
            join.append("punktversion.liggerIPostnummer post");
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
        if (etage != null || doer != null) {
            join.append("adresse.latestVersion version");
            if (etage != null) {
                where.append("version.etageBetegnelse like :etage");
                parameters.put("etage", "%" + etage + "%");
            }
            if (doer != null) {
                where.append("version.doerBetegnelse like :doer");
                parameters.put("doer", "%" + doer + "%");
            }
        }

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        if (where.size()>0) {
            hql.append("where ");
            hql.append(where.join(" and "));
        }

        hql.append("order by hus.husnummerbetegnelse");

        Query q = this.entityManager.createQuery(hql.join(" "));
        for (String key : parameters.keySet()) {
            q.setParameter(key, parameters.get(key));
        }
        return q.getResultList();
    }
}
