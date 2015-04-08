package dk.magenta.databroker.core.model.oio;

import dk.magenta.databroker.core.model.RepositoryImplementation;

interface RegistreringRepositoryCustom {
    public void clear();
}

public class RegistreringRepositoryImpl extends RepositoryImplementation<RegistreringEntity> implements RegistreringRepositoryCustom {
}
