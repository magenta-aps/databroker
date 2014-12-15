package dk.magenta.databroker.cprvejregister.model.adresse;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.Condition;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lars on 09-12-14.
 */

interface AdresseRepositoryCustom {
    public Collection<AdresseEntity> search(String kommune, String vej, String post, String husnr, String etage, String doer, GlobalCondition globalCondition);
}

public class AdresseRepositoryImpl implements AdresseRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<AdresseEntity> search(String kommune, String vej, String post, String husnr, String etage, String doer, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ArrayList<Condition> conditions = new ArrayList<Condition>();

        hql.append("select adresse from AdresseEntity as adresse");

        join.setPrefix("join ");
        join.append("adresse.husnummer hus");
        join.append("hus.adgangspunkt punkt");
        join.append("punkt.latestVersion punktversion");

        if (kommune != null || vej != null) {
            join.append("hus.navngivenVej vej");
            join.append("vej.latestVersion vejversion");
            join.append("vejversion.kommunedeleAfNavngivenVej delvej");
            if (kommune != null) {
                join.append("delvej.kommune kommune");
                conditions.add(RepositoryUtil.whereField(kommune, "kommune.kommuneKode", "kommune.latestVersion.navn"));
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

        if (etage != null || doer != null) {
            join.append("adresse.latestVersion version");
            if (etage != null) {
                conditions.add(RepositoryUtil.whereField(post, null, "version.etageBetegnelse"));
            }
            if (doer != null) {
                conditions.add(RepositoryUtil.whereField(post, null, "version.doerBetegnelse"));
            }
        }
        if (globalCondition != null) {
            conditions.addAll(globalCondition.whereField("kommune"));
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

        hql.append("order by hus.husnummerbetegnelse");

        System.out.println(hql.join(" "));
        Query q = this.entityManager.createQuery(hql.join(" "));
        Condition.addParameters(conditions, q);
        return q.getResultList();
    }
}
