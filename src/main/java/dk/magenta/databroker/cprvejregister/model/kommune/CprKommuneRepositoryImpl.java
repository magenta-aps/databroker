package dk.magenta.databroker.cprvejregister.model.kommune;

import dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers.StringList;
import dk.magenta.databroker.cprvejregister.model.Condition;
import dk.magenta.databroker.cprvejregister.model.GlobalCondition;
import dk.magenta.databroker.cprvejregister.model.RepositoryUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lars on 09-12-14.
 */

interface CprKommuneRepositoryCustom {
    public Collection<CprKommuneEntity> search(String kommune, GlobalCondition globalCondition);
}

public class CprKommuneRepositoryImpl implements CprKommuneRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    // Run a search where each input field may be a code or a name, and may contain leading and/or trailing wildcards
    // subject to a global condition (e.g. only extract entries with a version newer than a certain date)
    @Override
    public Collection<CprKommuneEntity> search(String kommune, GlobalCondition globalCondition) {
        StringList hql = new StringList();
        StringList join = new StringList();
        ArrayList<Condition> conditions = new ArrayList<Condition>();

        hql.append("select kommune from KommuneEntity as kommune");
        join.setPrefix("join ");

        if (kommune != null) {
            // Obtain a Condition representing the search parameter. e.g. "[where] kommune.kommunekode like %kommune%"
            conditions.add(RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn"));
        }
        if (globalCondition != null) {
            // Add any further restrictions from the global condition
            conditions.addAll(globalCondition.whereField("kommune"));
        }

        // our conditions list should now be complete

        for (Condition c : conditions) {
            if (c.hasRequiredJoin()) {
                // If any of our conditions have required joins (because their "where" clauses seeks in not-yet-included tables), join them in
                join.append(c.getRequiredJoin());
            }
        }

        // our join list should now be complete

        if (join.size()>0) {
            // Join all our JOIN definitions into one string (e.g. "join tableA.refB tableB join tableB.refC tableC")
            hql.append(join.join(" "));
        }
        if (conditions.size() > 0) {
            // Join all our WHERE definitions into one string (e.g. "where tableB.xyz = 42 and tableC.foo like %bar")
            hql.append(Condition.concatWhere(conditions));
        }
        // Append order clause
        hql.append("order by kommune.kommunekode");

        System.out.println(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));
        // Put all conditions' parameters into the query
        Condition.addParameters(conditions, q);
        // Run the query and return the results
        return q.getResultList();
    }
}
