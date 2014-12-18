package dk.magenta.databroker.core.model.oio;

import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.model.DataProviderEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;

interface RegistreringRepositoryCustom {
    public RegistreringEntity createNew(DataProviderEntity sourceDataProvider, String note);

    public RegistreringEntity createNew(DataProviderEntity sourceDataProvider);

    public RegistreringEntity importNew(
            Timestamp registreringstidspunk, String aktoerUUID, String note, RegistreringLivscyklusStatus status
    );

    public RegistreringEntity createUpdate(DataProviderEntity sourceDataProvider, String note);
    public RegistreringEntity createUpdate(DataProviderEntity sourceDataProvider);
}

public class RegistreringRepositoryImpl implements RegistreringRepositoryCustom {
    @Autowired
    RegistreringLivscyklusRepository lsRepo;
    @Autowired
    RegistreringRepository regRepo;

    private RegistreringEntity create(
            DataProviderEntity sourceDataProvider, String note, RegistreringLivscyklusStatus status
    ) {
        RegistreringEntity result = new RegistreringEntity(
                new Timestamp(System.currentTimeMillis()),
                sourceDataProvider.getUuid(),
                note
        );
        result.setLivscyklus(lsRepo.findOrCreateByStatus(status));
        regRepo.save(result);
        return result;
    }

    @Override
    public RegistreringEntity createNew(DataProviderEntity sourceDataProvider, String note) {
        return this.create(sourceDataProvider, note, RegistreringLivscyklusStatus.OPRETTET);
    }

    @Override
    public RegistreringEntity createUpdate(DataProviderEntity sourceDataProvider, String note) {
        return this.create(sourceDataProvider, note, RegistreringLivscyklusStatus.RETTET);
    }

    @Override
    public RegistreringEntity createNew(DataProviderEntity sourceDataProvider) {
        String note = "Created by dataprovider "+sourceDataProvider.getClass().getName();
        return this.createNew(sourceDataProvider, note);
    }
    @Override
    public RegistreringEntity createUpdate(DataProviderEntity sourceDataProvider) {
        String note = "Updated by dataprovider "+sourceDataProvider.getClass().getName();
        return this.createUpdate(sourceDataProvider, note);
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
