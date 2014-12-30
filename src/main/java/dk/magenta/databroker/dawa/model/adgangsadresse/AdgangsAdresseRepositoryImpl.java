package dk.magenta.databroker.dawa.model.adgangsadresse;

import dk.magenta.databroker.register.RepositoryUtil;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.register.objectcontainers.StringList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Map;

/**
 * Created by lars on 09-12-14.
 */

interface AdgangsAdresseRepositoryCustom {
    public Collection<AdgangsAdresseEntity> search(String land, String[] kommune, String[] post, String[] vej, String[] husnr, GlobalCondition globalCondition);
}

public class AdgangsAdresseRepositoryImpl implements AdgangsAdresseRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<AdgangsAdresseEntity> search(String land, String[] kommune, String[] post, String[] vej, String[] husnr, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select adresse from AdgangsAdresseEntity as adresse");
        join.setPrefix("join ");



        if (land != null || kommune != null || vej != null || post != null) {
            join.append("adresse.latestVersion as adresseVersion");
            join.append("adresseVersion.vejstykke as vejstykke");

            if (vej != null && vej.length > 0) {
                conditions.addCondition(RepositoryUtil.whereField(vej, "vejstykke.kode", "vejstykke.latestVersion.vejnavn"));
            }
            if (land != null || kommune != null && kommune.length > 0) {
                if (land != null) {
                    conditions.addCondition(RepositoryUtil.whereFieldLand(land));
                }
                join.append("vejstykke.kommune as kommune");
                conditions.addCondition(RepositoryUtil.whereField(kommune, "kommune.kode", "kommune.navn"));
            }
            if (post != null && post.length > 0) {
                join.append("vejstykke.latestVersion.postnummer as post");
                conditions.addCondition(RepositoryUtil.whereField(post, "post.latestVersion.nr", "post.latestVersion.navn"));
            }
        }

        if (husnr != null && husnr.length > 0) {
            conditions.addCondition(RepositoryUtil.whereField(husnr, null, "adresse.latestVersion.husnr"));
        }

        if (globalCondition != null) {
            conditions.addCondition(globalCondition.whereField("adresse"));
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

        hql.append("order by adresse.latestVersion.husnr");

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
