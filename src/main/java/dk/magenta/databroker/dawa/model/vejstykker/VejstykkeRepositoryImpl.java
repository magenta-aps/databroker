package dk.magenta.databroker.dawa.model.vejstykker;

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
 * Created by lars on 19-12-14.
 */
interface VejstykkeRepositoryCustom {
    public Collection<VejstykkeEntity> search(String land, String[] kommune, String[] vej, String[] lokalitet, GlobalCondition globalCondition);
}

public class VejstykkeRepositoryImpl implements VejstykkeRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Collection<VejstykkeEntity> search(String land, String[] kommune, String[] vej, String[] lokalitet, GlobalCondition globalCondition) {

        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct vej from VejstykkeEntity as vej");

        join.setPrefix("join ");
        if (kommune != null || land != null) {
            join.append("vej.kommune kommune");
            if (land != null) {
                conditions.addCondition(RepositoryUtil.whereFieldLand(land));
            }
            if (kommune != null) {
                conditions.addCondition(RepositoryUtil.whereField(kommune, "kommune.kode", "kommune.navn"));
            }
        }
        if (vej != null) {
            conditions.addCondition(RepositoryUtil.whereField(vej, "vej.kode", "vej.latestVersion.vejnavn"));
        }
        if (lokalitet != null && lokalitet.length > 0) {
            join.append("vej.latestVersion.lokaliteter lokalitet");
            conditions.addCondition(RepositoryUtil.whereField(lokalitet, null, "lokalitet.navn"));
        }
        if (globalCondition != null) {
            conditions.addCondition(globalCondition.whereField("vej"));
        }

        // our conditions list should now be complete

        if (conditions.hasRequiredJoin()) {
            System.out.println("conditions.getRequiredJoin(): "+conditions.getRequiredJoin());
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
        hql.append("order by vej.kode");

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
