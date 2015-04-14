package dk.magenta.databroker.core.model;

import dk.magenta.databroker.core.Session;
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

    @PersistenceContext(type = PersistenceContextType.EXTENDED, name="statelessEntityManagerFactoryName")
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }


/*
    public Session getSession() {
        if (this.entityManager != null) {
            return (Session) this.entityManager.getDelegate();
        }
        return null;
    }*/

    public void clear() {
        /*Session session = this.getSession();
        if (session != null) {
            //session.flush();
            session.clear();
        }*/
        if (this.entityManager != null) {
//            this.entityManager.flush();
//            this.entityManager.clear();
        }
    }

    protected Logger log = Logger.getLogger(this.getClass());

    protected List<T> query(StringList hql, ConditionList conditions, GlobalCondition globalCondition) {
        this.log.trace(hql.join(" \n"));
        String queryString = hql.join(" ");
        hql = null;
        Query q = this.entityManager.createQuery(queryString);

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
        Map<String, Object> queryParameters = null;
        if (conditions != null) {
            queryParameters = conditions.getParameters();
            for (String key : queryParameters.keySet()) {
                Object value = queryParameters.get(key);
                this.log.trace(key + " = " + value);
                if (value.getClass() == Long.class) {
                    q.setParameter(key, (Long) value);
                } else if (value.getClass() == Integer.class) {
                    q.setParameter(key, (Integer) value);
                } else {
                    q.setParameter(key, value);
                }
            }
        }
        // Run the query and return the results
        try {
            return (List<T>) q.getResultList();
        } catch (Exception e) {
            System.out.println(queryString);
            if (queryParameters != null) {
                for (String key : queryParameters.keySet()) {
                    Object value = queryParameters.get(key);
                    System.out.println(key + " = " + value);
                }
            }
            e.printStackTrace();
        }
        return null;
    }

    protected List<T> query(String queryString, Map<String, Object> queryParameters, GlobalCondition globalCondition) {
            this.log.trace(queryString);
        Query q = this.entityManager.createQuery(queryString);
        try {
            return this.query(q, queryParameters, globalCondition);
        } catch (Exception e) {
            this.printQueryData(queryString, queryParameters);
            e.printStackTrace();
        }
        return null;
    }

    protected List<T> query(String queryString, Session session) {
        return this.query(queryString, null, null, session);
    }
    protected List<T> query(String queryString, Map<String, Object> queryParameters, Session session) {
        return this.query(queryString, queryParameters, null, session);
    }
    protected List<T> query(String queryString, Map<String, Object> queryParameters, GlobalCondition globalCondition, Session session) {
            //this.printQueryData(queryString, queryParameters);
        if (session == null) {
            return this.query(queryString, queryParameters, globalCondition);
        }
        org.hibernate.Query q = session.createQuery(queryString);
        try {

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
            if (queryParameters != null) {
                for (String key : queryParameters.keySet()) {
                    Object value = queryParameters.get(key);
                    this.log.trace(key + " = " + value);
                    if (value.getClass() == Long.class) {
                        q.setParameter(key, (Long) value);
                    } else if (value.getClass() == Integer.class) {
                        q.setParameter(key, (Integer) value);
                    } else {
                        q.setParameter(key, value);
                    }
                }
            }
            // Run the query and return the results
            List<T> results = (List<T>) q.list();
            //System.out.println("Found "+results.size()+" items");
            return results;
        } catch (Exception e) {
            this.printQueryData(queryString, queryParameters);
            e.printStackTrace();
        }
        return null;
    }

    private void printQueryData(String queryString, Map<String, Object> queryParameters) {
        System.out.println(queryString);
        if (queryParameters != null) {
            for (String key : queryParameters.keySet()) {
                Object value = queryParameters.get(key);
                System.out.println(key + " = " + value);
            }
        }
    }

    private List<T> query(Query q, Map<String, Object> queryParameters, GlobalCondition globalCondition) {

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
        if (queryParameters != null) {
            for (String key : queryParameters.keySet()) {
                Object value = queryParameters.get(key);
                this.log.trace(key + " = " + value);
                if (value.getClass() == Long.class) {
                    q.setParameter(key, (Long) value);
                } else if (value.getClass() == Integer.class) {
                    q.setParameter(key, (Integer) value);
                } else {
                    q.setParameter(key, value);
                }
            }
        }
        // Run the query and return the results
        return (List<T>) q.getResultList();
    }

    protected void runNativeQuery(String query) {
        this.log.info(query);
        this.entityManager.createNativeQuery(query).executeUpdate();
    }




    // public void saveInSession(T item, Session session) {
    // For bulk saving, chuck it in here
    // }

    // public T getFromSession(something) {
    // When bulk saving, check db here
    // }
}
