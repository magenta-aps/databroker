package dk.magenta.databroker.core.model.oio;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

interface RegistreringRepositoryCustom {
    public RegistreringEntity createNew(
            Timestamp fra, String aktoerUUID, String note, RegistreringLivscyklusStatus status
    );
}

public class RegistreringRepositoryImpl implements RegistreringRepositoryCustom {
    @Autowired
    RegistreringLivscyklusRepository lsRepo;

    @Override
    public RegistreringEntity createNew(
            Timestamp fra, String aktoerUUID, String note, RegistreringLivscyklusStatus status
    ) {
        RegistreringEntity result = new RegistreringEntity(fra, aktoerUUID, note);
        result.setLivscyklus(lsRepo.findOrCreateByStatus(status));
        return result;
    }
}
