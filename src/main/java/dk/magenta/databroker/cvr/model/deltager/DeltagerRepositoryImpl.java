package dk.magenta.databroker.cvr.model.deltager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by lars on 02-02-15.
 */


interface DeltagerRepositoryCustom {
}

public class DeltagerRepositoryImpl implements DeltagerRepositoryCustom {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

}
