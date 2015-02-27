package dk.magenta.databroker.core.model;

import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.GlobalCondition;
import dk.magenta.databroker.util.objectcontainers.StringList;
import org.apache.log4j.Logger;

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
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }

    protected Logger log = Logger.getLogger(this.getClass());

    protected List<T> query(StringList hql, ConditionList conditions, GlobalCondition globalCondition) {
        this.log.trace(hql.join(" \n"));
        Query q = this.entityManager.createQuery(hql.join(" "));

        int offset;
        int limit;
        if (globalCondition != null) {
            offset = globalCondition.getOffset();
            limit = globalCondition.getLimit();
        } else {
            offset = 0;
            limit = 1000;
        }

        q.setFirstResult(offset);
        q.setMaxResults(limit);

        // Put all conditions' parameters into the query
        Map<String, Object> queryParameters = conditions.getParameters();
        for (String key : queryParameters.keySet()) {
            this.log.trace(key + " = " + queryParameters.get(key));
            q.setParameter(key, queryParameters.get(key));
        }
        // Run the query and return the results
        return (List<T>) q.getResultList();
    }
}
