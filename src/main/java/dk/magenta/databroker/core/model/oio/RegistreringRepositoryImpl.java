package dk.magenta.databroker.core.model.oio;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

interface RegistreringRepositoryCustom {
    public RegistreringEntity createNew(
            DataProvider sourceDataProvider, String note
    );

    public RegistreringEntity importNew(
            Timestamp registreringstidspunk, String aktoerUUID, String note, RegistreringLivscyklusStatus status
    );

    public RegistreringEntity createUpdate(DataProvider sourceDataProvider, String note);
}

public class RegistreringRepositoryImpl implements RegistreringRepositoryCustom {
    @Autowired
    RegistreringLivscyklusRepository lsRepo;
    @Autowired
    RegistreringRepository regRepo;

    private RegistreringEntity create(
            DataProvider sourceDataProvider, String note, RegistreringLivscyklusStatus status
    ) {
        RegistreringEntity result = new RegistreringEntity(
                new Timestamp(System.currentTimeMillis()),
                sourceDataProvider.getDataProviderEntity().getUuid(),
                note
        );
        result.setLivscyklus(lsRepo.findOrCreateByStatus(status));
        regRepo.save(result);
        return result;
    }

    @Override
    public RegistreringEntity createNew(DataProvider sourceDataProvider, String note) {
        return this.create(sourceDataProvider, note, RegistreringLivscyklusStatus.OPRETTET);
    }

    @Override
    public RegistreringEntity createUpdate(DataProvider sourceDataProvider, String note) {
        return this.create(sourceDataProvider, note, RegistreringLivscyklusStatus.RETTET);
    }

    @Override
    public RegistreringEntity importNew(
            Timestamp registreringstidspunk, String aktoerUUID, String note, RegistreringLivscyklusStatus status
    ) {
        RegistreringEntity result = new RegistreringEntity(registreringstidspunk, aktoerUUID, note);
        result.setLivscyklus(lsRepo.findOrCreateByStatus(status));
        return result;
    }
}
