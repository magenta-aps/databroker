package dk.magenta.databroker.dawa.model;

import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created by lars on 03-02-15.
 */
public abstract class RepositoryImplementation<T> {

    protected EntityManager entityManager;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void clear() {
        if (this.entityManager != null) {
            //System.out.println("clearing " + this.entityManager);
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }


    protected List<T> query(StringList hql, ConditionList conditions, GlobalCondition globalCondition) {
        return this.query(hql, conditions, globalCondition, false);
    }
    protected List<T> query(StringList hql, ConditionList conditions, GlobalCondition globalCondition, boolean printQuery) {
        if (printQuery) {
            System.out.println(hql.join(" \n"));
        }
        Query q = this.entityManager.createQuery(hql.join(" "));

        int offset = 0;
        int limit = 1000;
        if (globalCondition != null) {
            offset = globalCondition.getOffset();
            limit = globalCondition.getLimit();
        }

        q.setFirstResult(offset);
        q.setMaxResults(limit);

        // Put all conditions' parameters into the query
        Map<String, Object> queryParameters = conditions.getParameters();
        for (String key : queryParameters.keySet()) {
            if (printQuery) {
                System.out.println(key + " = " + queryParameters.get(key));
            }
            q.setParameter(key, queryParameters.get(key));
        }
        // Run the query and return the results
        return (List<T>) q.getResultList();
    }
}
