package dk.magenta.databroker.dawa.model.temaer;

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
interface KommuneRepositoryCustom {
    public Collection<KommuneEntity> search(String land, String[] kommune, String[] postnr, String[] lokalitet, String[] vej, GlobalCondition globalCondition);
}

public class KommuneRepositoryImpl implements KommuneRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    // Run a search where each input field may be a code or a name, and may contain leading and/or trailing wildcards
    // subject to a global condition (e.g. only extract entries with a version newer than a certain date)
    @Override
    public Collection<KommuneEntity> search(String land, String[] kommune, String[] postnr, String[] lokalitet, String[] vej, GlobalCondition globalCondition) {
        StringList hql = new StringList();
        StringList join = new StringList();
        ConditionList conditions = new ConditionList(ConditionList.Operator.AND);

        hql.append("select distinct kommune from KommuneEntity as kommune");
        join.setPrefix("join ");

        if (land != null) {
            conditions.addCondition(RepositoryUtil.whereFieldLand(land));
        }

        if (kommune != null) {
            // Obtain a Condition representing the search parameter. e.g. "[where] kommune.kommunekode like %kommune%"
            conditions.addCondition(RepositoryUtil.whereField(kommune, "kommune.kode", "kommune.navn"));
        }
        if (lokalitet != null && lokalitet.length > 0) {
            join.append("kommune.lokaliteter lokalitet");
            conditions.addCondition(RepositoryUtil.whereField(lokalitet, null, "lokalitet.navn"));
        }
        if (postnr != null) {
            join.append("kommune.postnumre postVersion");
            conditions.addCondition(RepositoryUtil.whereVersionLatest("postVersion"));
            conditions.addCondition(RepositoryUtil.whereField(postnr, "postVersion.nr", "postVersion.navn"));
        }
        if (vej != null) {
            join.append("kommune.vejstykker vej");
            conditions.addCondition(RepositoryUtil.whereField(vej, "vej.kode", "vej.latestVersion.vejnavn"));
        }
        /*if (globalCondition != null) {
            // Add any further restrictions from the global condition
            conditions.addCondition(globalCondition.whereField("kommune"));
        }*/

        // our conditions list should now be complete

        if (conditions.hasRequiredJoin()) {
            join.append(conditions.getRequiredJoin());
        }

        // our join list should now be complete

        if (join.size()>0) {
            hql.append(join.join(" "));
        }
        if (conditions.size() > 0) {
            System.out.println(conditions.size());
            hql.append("where");
            hql.append(conditions.getWhere());
        }
        // Append order clause
        hql.append("order by kommune.kode");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        q.setMaxResults(1000);
        // Put all conditions' parameters into the query
        Map<String, Object> parameters = conditions.getParameters();
        for (String key : parameters.keySet()) {
            System.out.println(key+" = "+parameters.get(key));
            q.setParameter(key, parameters.get(key));
        }
        // Run the query and return the results
        return q.getResultList();
    }
}