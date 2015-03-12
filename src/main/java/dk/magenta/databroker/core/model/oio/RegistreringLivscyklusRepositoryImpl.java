package dk.magenta.databroker.core.model.oio;

import dk.magenta.databroker.core.model.RepositoryImplementation;

import javax.persistence.Query;
import java.util.List;

interface RegistreringLivscyklusRepositoryCustom {
    public RegistreringLivscyklusEntity getByNavn(String navn);
    public void clear();
}

public class RegistreringLivscyklusRepositoryImpl extends RepositoryImplementation<RegistreringLivscyklusEntity> implements RegistreringLivscyklusRepositoryCustom {

    public RegistreringLivscyklusEntity getByNavn(String navn) {
        Query q = this.entityManager.createQuery("select livscyklus from RegistreringLivscyklusEntity livscyklus where livscyklus.navn = :navn");
        q.setParameter("navn", navn);
        q.setMaxResults(1);
        List<RegistreringLivscyklusEntity> results = q.getResultList();
        return results != null && !results.isEmpty() ? results.iterator().next() : null;
    }

    public void clear() {
        this.entityManager.clear();
    }
}
