package dk.magenta.databroker.core;

import org.hibernate.*;

import java.sql.SQLException;

/**
 * Created by lars on 13-04-15.
 */
public class Session {
    private org.hibernate.Session statefulSession;
    private org.hibernate.StatelessSession statelessSession;

    public Session(org.hibernate.Session statefulSession) {
        this.statefulSession = statefulSession;
    }

    public Session(org.hibernate.StatelessSession statelessSession) {
        this.statelessSession = statelessSession;
        /*try {
            this.statelessSession.connection().setTransactionIsolation(1);
            System.out.println(this.statelessSession.connection().getTransactionIsolation());
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public void save(Object item, boolean alreadyExists) {
        if (this.statefulSession != null) {
            this.statefulSession.save(item);
        }
        if (this.statelessSession != null) {
            if (alreadyExists) {
                this.statelessSession.update(item);
            } else {
                this.statelessSession.insert(item);
            }
        }
    }

    public org.hibernate.Transaction beginTransaction() {
        if (this.statefulSession != null) {
            return this.statefulSession.beginTransaction();
        } else if (this.statelessSession != null) {
            return this.statelessSession.beginTransaction();
        }
        return null;
    }


    public void close() {
        if (this.statefulSession != null) {
            this.statefulSession.close();
        } else if (this.statelessSession != null) {
            this.statelessSession.close();
        }
    }

    public Query createQuery(String queryString) {
        if (this.statefulSession != null) {
            return this.statefulSession.createQuery(queryString);
        } else if (this.statelessSession != null) {
            return this.statelessSession.createQuery(queryString);
        }
        return null;
    }
}
