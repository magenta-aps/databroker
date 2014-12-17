package dk.magenta.databroker.cprvejregister.model.adresse;

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

interface AdresseRepositoryCustom {
    public Collection<AdresseEntity> search(String land, String[] kommune, String[] vej, String[] post, String[] husnr, String[] etage, String[] doer, GlobalCondition globalCondition);
}

public class AdresseRepositoryImpl implements AdresseRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<AdresseEntity> search(String land, String[] kommune, String[] vej, String[] post, String[] husnr, String[] etage, String[] doer, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select adresse from AdresseEntity as adresse");

        join.setPrefix("join ");
        join.append("adresse.husnummer hus");
        join.append("hus.adgangspunkt punkt");
        join.append("punkt.latestVersion punktversion");

        if (kommune != null || vej != null) {
            join.append("hus.navngivenVej vej");
            join.append("vej.latestVersion vejversion");
            join.append("vejversion.kommunedeleAfNavngivenVej delvej");
            if (kommune != null || land != null) {
                join.append("delvej.kommune kommune");
                if (land != null) {
                    conditions.addCondition(RepositoryUtil.whereFieldLand(land));
                }
                if (kommune != null) {
                    conditions.addCondition(RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn"));
                }
            }
            if (vej != null) {
                conditions.addCondition(RepositoryUtil.whereField(vej, "delvej.vejKode", "vejversion.vejnavn"));
            }
        }

        if (post != null) {
            join.append("punktversion.liggerIPostnummer post");
            conditions.addCondition(RepositoryUtil.whereField(post, "post.nummer", "post.latestVersion.navn"));
        }

        if (husnr != null) {
            conditions.addCondition(RepositoryUtil.whereField(husnr, null, "hus.husnummerbetegnelse"));
        }

        if (etage != null || doer != null) {
            join.append("adresse.latestVersion version");
            if (etage != null) {
                conditions.addCondition(RepositoryUtil.whereField(post, null, "version.etageBetegnelse"));
            }
            if (doer != null) {
                conditions.addCondition(RepositoryUtil.whereField(post, null, "version.doerBetegnelse"));
            }
        }
        if (globalCondition != null) {
            conditions.addCondition(globalCondition.whereField("kommune"));
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

        hql.append("order by hus.husnummerbetegnelse");

        System.out.println(hql.join(" "));
        Query q = this.entityManager.createQuery(hql.join(" "));
        Map<String, Object> parameters = conditions.getParameters();
        for (String key : parameters.keySet()) {
            System.out.println(key+" = "+parameters.get(key));
            q.setParameter(key, parameters.get(key));
        }
        return q.getResultList();
    }
}
