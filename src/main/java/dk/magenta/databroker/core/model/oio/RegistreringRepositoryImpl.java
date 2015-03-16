package dk.magenta.databroker.core.model.oio;

import dk.magenta.databroker.core.model.RepositoryImplementation;
import org.hibernate.Session;

interface RegistreringRepositoryCustom {
    public void clear();
}

public class RegistreringRepositoryImpl extends RepositoryImplementation<RegistreringEntity> implements RegistreringRepositoryCustom {
}
