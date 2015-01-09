package dk.magenta.databroker.dawa.model.enhedsadresser;

import dk.magenta.databroker.dawa.model.adgangsadresse.AdgangsAdresseEntity;
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

interface EnhedsAdresseRepositoryCustom {
    public Collection<EnhedsAdresseEntity> search(String land, String[] post, String[] kommune, String[] vej, String[] husnr, String[] etage, String[] sidedoer, GlobalCondition globalCondition);
}

public class EnhedsAdresseRepositoryImpl implements EnhedsAdresseRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<EnhedsAdresseEntity> search(String land, String[] post, String[] kommune, String[] vej, String[] husnr, String[] etage, String[] sidedoer, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct adresse from EnhedsAdresseEntity as adresse");
        join.setPrefix("join ");

        if (land != null || kommune != null || vej != null || post != null || husnr != null) {
            join.append("adresse.latestVersion as adresseVersion");
            join.append("adresseVersion.adgangsadresse as adgang");
            join.append("adgang.vejstykke as vejstykke");

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
                join.append("adgang.latestVersion.postnummer as post");
                conditions.addCondition(RepositoryUtil.whereField(post, "post.latestVersion.nr", "post.latestVersion.navn"));
            }
            if (husnr != null && husnr.length > 0) {
                conditions.addCondition(RepositoryUtil.whereField(husnr, null, "adgang.husnr"));
            }
        }

        if (etage != null && etage.length > 0) {
            conditions.addCondition(RepositoryUtil.whereField(etage, null, "adresseVersion.etage"));
        }
        if (sidedoer != null && sidedoer.length > 0) {
            conditions.addCondition(RepositoryUtil.whereField(sidedoer, null, "adresseVersion.doer"));
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
            hql.append(join.join(" \n"));
        }
        if (conditions.size() > 0) {
            hql.append("where");
            hql.append(conditions.getWhere());
        }

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
